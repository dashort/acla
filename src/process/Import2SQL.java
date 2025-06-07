package process;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility class for importing account spreadsheets into SQL Server.
 */
public class Import2SQL {
    private static final int BATCH_SIZE = 20;

    public static void main(String account) {
        String connectionUrl = getConnectionUrl(account);
        if (connectionUrl == null) {
            System.err.println("Invalid account: " + account);
            return;
        }

        String filePath = "Z:/" + account + "/" + account + ".xlsx";
        String sql = getInsertQuery(account);
        if (sql == null) {
            System.err.println("No SQL defined for account: " + account);
            return;
        }

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             FileInputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            connection.setAutoCommit(false);
            Sheet sheet = workbook.getSheetAt(0);
            processSheet(connection, sheet, sql, account);
            connection.commit();
            System.out.println("Data imported successfully for account: " + account);
        } catch (IOException | SQLException e) {
            System.err.println("Error processing account: " + account);
            e.printStackTrace();
        }
    }

    /**
     * Builds a connection URL using environment variables. Required variables:
     * DB_HOST, DB_USER, DB_PASSWORD, DB_NAME_537 and DB_NAME_CUSTOMERS.
     */
    private static String getConnectionUrl(String account) {
        String host = System.getenv("DB_HOST");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        if (host == null || user == null || password == null) {
            System.err.println("Database environment variables not set");
            return null;
        }

        String db;
        switch (account) {
            case "537" -> db = System.getenv("DB_NAME_537");
            case "903", "218", "115" -> db = System.getenv("DB_NAME_CUSTOMERS");
            default -> {
                return null;
            }


        }
        if (db == null) {
            System.err.println("Database name environment variable missing for account " + account);
            return null;
        }


        return String.format(
            "jdbc:sqlserver://%s;database=%s;user=%s;password=%s;loginTimeout=30;",
            host, db, user, password);
    }

    private static String getInsertQuery(String account) {
        return switch (account) {
            case "537" ->
                "INSERT INTO Patients (LOCATION, NAME, PHYSICIAN, PCP, FIN) VALUES (?, ?, ?, ?, ?)";
            case "903" ->
                "INSERT INTO OnCall_903 (CONTACT, ROLE) VALUES (?, ?)";
            case "218" ->
                "INSERT INTO Advantage_218 (client_name, order_cert, order_specialty, " +
                "client_address, client_city, client_telephone_number, temp_first_name, " +
                "temp_last_name, temp_certification, temp_telephone_number, shift_start, shift_end) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            case "115" ->
                "INSERT INTO Properties_115 (Property_Name, Address, City, First, First_Cell, " +
                "First_Email, Second, Second_Cell, Second_Email, Third, Third_Cell, Third_Email, " +
                "Fourth, Fourth_Cell, Fourth_Email, Fifth, Fifth_Cell, Fifth_Email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            default -> null;
        };
    }

    private static void processSheet(Connection connection, Sheet sheet, String sql, String account) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Iterator<Row> rowIterator = sheet.iterator();
            skipHeaderRows(rowIterator);

            int rowCount = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                mapRowToStatement(row, statement, account);
                statement.addBatch();

                if (++rowCount % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch();
        }
    }

    private static void skipHeaderRows(Iterator<Row> rowIterator) {
        for (int i = 0; i < 3 && rowIterator.hasNext(); i++) {
            rowIterator.next();
        }
    }

    private static void mapRowToStatement(Row row, PreparedStatement statement, String account) throws SQLException {
        DataFormatter formatter = new DataFormatter();


        for (Cell cell : row) {
            int index = cell.getColumnIndex();
            String value = getCellValue(cell, formatter);
            switch (account) {
                case "537" -> mapRow537(statement, index, value);
                case "903" -> mapRow903(statement, index, value);
                case "218" -> mapRow218(statement, index, cell);
                case "115" -> mapRow115(statement, index, value);


                default -> {
                }
            }
        }
    }

    private static String getCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> formatter.formatCellValue(cell);
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> formatter.formatCellValue(cell);
            default -> "";
        };
    }

    private static void mapRow537(PreparedStatement statement, int columnIndex, String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            value = "Unknown";
        }
        switch (columnIndex) {
            case 0 -> statement.setString(1, value);
            case 1 -> statement.setString(2, value);
            case 2 -> statement.setString(3, value);
            case 3 -> statement.setString(4, value);
            case 4 -> statement.setString(5, value);
        }
    }

    private static void mapRow903(PreparedStatement statement, int columnIndex, String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            value = "Unknown";
        }
        switch (columnIndex) {
            case 0 -> statement.setString(1, value);
            case 1 -> statement.setString(2, value);
        }
    }

    private static void mapRow218(PreparedStatement statement, int columnIndex, Cell cell) throws SQLException {
        DataFormatter formatter = new DataFormatter();
        String value = getCellValue(cell, formatter);
        switch (columnIndex) {
            case 0 -> statement.setString(1, defaultIfEmpty(value));
            case 10, 11 -> {
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    Date date = cell.getDateCellValue();
                    statement.setTimestamp(columnIndex + 1, new Timestamp(date.getTime()));
                } else {
                    statement.setNull(columnIndex + 1, java.sql.Types.TIMESTAMP);
                }
            }
            default -> statement.setString(columnIndex + 1, defaultIfEmpty(value));
        }
    }

    private static String defaultIfEmpty(String value) {
        return (value == null || value.isEmpty()) ? "Unknown" : value;
    }

    private static void mapRow115(PreparedStatement statement, int columnIndex, String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            value = "Unknown";
        }
        statement.setString(columnIndex + 1, value);
    }
}

