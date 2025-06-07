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
		
	
