<<<<<<< HEAD
package process;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Import2SQL {

    private static final int BATCH_SIZE = 20;

    public static void main(String account) {
        String connectionUrl = getConnectionUrl(account);
        if (connectionUrl == null) {
            System.out.println("Invalid account: " + account);
            return;
        }

        String filePath = "Z:\\" + account + "\\" + account + ".xlsx";
        String sql = getInsertQuery(account);
        if (sql == null) {
            System.out.println("No SQL defined for account: " + account);
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

    private static String getConnectionUrl(String account) {
        switch (account) {
            case "537":
                return "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com;database=537_2414_1;user=user2414;password=N6sgWPUTcp6J;loginTimeout=30;";
            case "903":
            case "218":
            case "115":
                return "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com;database=Customers_2414_1;user=user2414;password=N6sgWPUTcp6J;loginTimeout=30;";
            default:
                return null;
        }
    }

    private static String getInsertQuery(String account) {
        switch (account) {
            case "537":
                return "INSERT INTO Patients (LOCATION, NAME, PHYSICIAN, PCP, FIN) VALUES (?, ?, ?, ?, ?)";
            case "903":
                return "INSERT INTO OnCall_903 (CONTACT, ROLE) VALUES (?, ?)";
            case "218":
                return "INSERT INTO Advantage_218 (client_name, order_cert, order_specialty, client_address, client_city, client_telephone_number, temp_first_name, temp_last_name, temp_certification, temp_telephone_number, shift_start, shift_end) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            case "115":
                return "INSERT INTO Properties_115 (Property_Name, Address, City, First, First_Cell, First_Email, Second, Second_Cell, Second_Email, Third, Third_Cell, Third_Email, Fourth, Fourth_Cell, Fourth_Email, Fifth, Fifth_Cell, Fifth_Email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            default:
                return null;
        }
    }

    private static void processSheet(Connection connection, Sheet sheet, String sql, String account) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Iterator<Row> rowIterator = sheet.iterator();
            skipHeaderRows(rowIterator, account);

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

    private static void skipHeaderRows(Iterator<Row> rowIterator, String account) {
        // Skip header rows; can be customized per account if needed
        for (int i = 0; i < 3 && rowIterator.hasNext(); i++) {
            rowIterator.next();
        }
    }

    private static void mapRowToStatement(Row row, PreparedStatement statement, String account) throws SQLException {
        DataFormatter formatter = new DataFormatter();

        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex();
            String cellValue = getCellValue(cell, formatter); // Handles null/empty cells.

            switch (account) {
                case "537":
                    mapRow537(statement, columnIndex, cellValue);
                    break;
                case "903":
                    mapRow903(statement, columnIndex, cellValue);
                    break;
                case "218":
                    mapRow218(statement, columnIndex, cell);
                    break;
                case "115":
                    mapRow115(statement, columnIndex, cellValue);
                    break;
                default:
                    break;
            }
        }
    }

    private static String getCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return ""; // Default value for null cells.
        }

        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim(); // Remove unnecessary whitespace.
            case NUMERIC:
                return formatter.formatCellValue(cell); // Format numeric values as strings.
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return formatter.formatCellValue(cell); // Evaluate formula and format result.
            case BLANK:
                return ""; // Handle blank cells gracefully.
            default:
                return ""; // Default for unexpected types.
        }
    }

    private static void mapRow537(PreparedStatement statement, int columnIndex, String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            value = "Unknown"; // Default value for missing data.
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
            value = "Unknown"; // Default value for missing data.
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
            case 0 -> statement.setString(1, value);
            case 10, 11 -> {
                Date date = cell.getDateCellValue();
                statement.setTimestamp(columnIndex + 1, new Timestamp(date.getTime()));
            }
            default -> statement.setString(columnIndex + 1, value);
        }
    }

    private static void mapRow115(PreparedStatement statement, int columnIndex, String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            value = "Unknown"; // Default value for missing data.
        }
        statement.setString(columnIndex + 1, value);
    }
}
=======
package process;

import java.awt.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Import2SQL {

	public static void main(String account)  {

//		String connectionUrl = "jdbc:sqlserver://acla.cnbusmu0fj2t.us-east-1.rds.amazonaws.com;" + "database=537;"
//				+ "user=sa;" + "password=5020Glendale;" + "loginTimeout=30;";

		if ("537".equals(account)) {

		String connectionUrl = "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com;" + "database=537_2414_1;" + "user=user2414;" + "password=N6sgWPUTcp6J;" + "loginTimeout=30;";
		int batchSize = 20;
		Connection connection = null;

			try {
			long start = System.currentTimeMillis();

//			String filepath = ("D:\\" + account + ".xlsx");
			String filepath = ("Z:\\" + account + "\\" + account + ".xlsx");
			FileInputStream inputStream = new FileInputStream(filepath);
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			Iterator<Row> rowIterator = firstSheet.iterator();
			connection = DriverManager.getConnection(connectionUrl);
			connection.setAutoCommit(false);

			String sql = "INSERT INTO Patients (LOCATION, NAME, PHYSICIAN, PCP, FIN) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			int count = 0;
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					switch (columnIndex) {
					case 0:
						String location = nextCell.getStringCellValue();
						statement.setString(1, location);
						break;
					case 1:
						String name = nextCell.getStringCellValue();
						statement.setString(2, name);
						break;
					case 2:
						String Physician = nextCell.getStringCellValue();
						statement.setString(3, Physician);
						break;
					case 3:
						String PCP = nextCell.getStringCellValue();
						statement.setString(4, PCP);
						break;
					case 4:
//						double FIN = nextCell.getNumericCellValue();
						String FIN = formatter.formatCellValue(nextCell);
						statement.setString(5, FIN);
//						String FIN = nextCell.getStringCellValue();
//						statement.setString(6, FIN);
						break;
					default:
						break;
					}
				}
				statement.addBatch();
				if (count % batchSize == 0) {
					statement.executeBatch();
				}
			}
			workbook.close();
			statement.executeBatch();
			
//			String sql2 = "UPDATE PATIENTS SET NAME = CONCAT(LASTNAME, ', ',FIRSTNAME)";
//			PreparedStatement statement2 = connection.prepareStatement(sql2);
//			statement2.execute();
			connection.commit();
			connection.close();			long end = System.currentTimeMillis();
			System.out.printf("Import done in %d ms\n", (end - start));

		} catch (IOException ex1) {
			System.out.println("Error reading file");
			ex1.printStackTrace();
		} catch (SQLException ex2) {
			System.out.println("Database error");
			ex2.printStackTrace();
		}}
			
		else if ("903".equals(account)) {
//			Component panelMenu = null;
//			JOptionPane.showMessageDialog(panelMenu, "903");
			String connectionUrl2 = "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com;" + "database=Customers_2414_1;" + "user=user2414;" + "password=N6sgWPUTcp6J;" + "loginTimeout=30;";
			
			
//			String connectionUrl2 = "jdbc:sqlserver://acla.cnbusmu0fj2t.us-east-1.rds.amazonaws.com;" + "user=sa;" + "password=5020Glendale;" + "loginTimeout=30;" + "database=Customers;";
			int batchSize = 20;
			Connection connection = null;

				try {
				long start = System.currentTimeMillis();
				
				
//				String filepath = ("D:\\" + account + ".xlsx");
				String filepath = ("Z:\\" + account + "\\" + account + ".xlsx");
				FileInputStream inputStream = new FileInputStream(filepath);
				Workbook workbook = new XSSFWorkbook(inputStream);
				Sheet firstSheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = firstSheet.iterator();
				connection = DriverManager.getConnection(connectionUrl2);
				connection.setAutoCommit(false);

				String sql = "INSERT INTO OnCall_903 (CONTACT, ROLE) VALUES (?, ?)";
				PreparedStatement statement = connection.prepareStatement(sql);

				int count = 0;
				rowIterator.next();
				

				while (rowIterator.hasNext()) {
					Row nextRow = rowIterator.next();
					Iterator<Cell> cellIterator = nextRow.cellIterator();
					while (cellIterator.hasNext()) {
						Cell nextCell = cellIterator.next();
						int columnIndex = nextCell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							String contact = nextCell.getStringCellValue();
							statement.setString(1, contact);
							break;
						case 1:
							String role = nextCell.getStringCellValue();
							statement.setString(2, role);
							break;
	
						}
					}
					statement.addBatch();
					if (count % batchSize == 0) {
						statement.executeBatch();
					}
				}
				workbook.close();
				statement.executeBatch();
				connection.commit();
				connection.close();
				long end = System.currentTimeMillis();
				System.out.printf("Import done in %d ms\n", (end - start));

			} catch (IOException ex1) {
				System.out.println("Error reading file");
				ex1.printStackTrace();
			} catch (SQLException ex2) {
				System.out.println("Database error");
				ex2.printStackTrace();
			}}
			
			
			
			
			}
			}
		
	
>>>>>>> bb500479f201c5b0b636cd9283c4f25d780a2c0f
