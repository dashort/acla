package process;

import java.sql.*;


public class DB_Connection {
	
		public static void main(String[] args) {
				
			/*
			 * DB_Connection obj_DB_Connection=new DB_Connection(); 
			 * String connectionUrl =
			 * "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com:1433;" +
			 * "database=537_2414_1;" + "user=user2414;" + "password=N6sgWPUTcp6J;" +
			 * "encrypt=true;" + "trustServerCertificate=false;" + "loginTimeout=30;";
			 */
			
			DB_Connection conn=new DB_Connection();
			   String connectionUrl =
					    "jdbc:sqlserver://acla.cnbusmu0fj2t.us-east-1.rds.amazonaws.com;"
		                        + "database=537;"
		                        + "user=sa;"
		                        + "password=5020Glendale;"
		                        + "loginTimeout=30;";

				try (Connection connection = DriverManager.getConnection(connectionUrl);) {
					
					System.out.println("Connected With the database successfully");
		        	
		        	System.out.println(connection);
		            // Code here.
		        }
		        // Handle any errors that may have occurred.
		        catch (SQLException e) {
		            e.printStackTrace();
		        }
		 				
			}
		
		public Connection get_connection() {
		Connection connection=null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://dashort.sftp.wpengine.com:3306/wp_dashort","dashort", "gt9wkk6r1TPnkgrY");
		}catch (Exception e) {
			System.out.println(e);
		}
				
		return connection;
	}

		public static PreparedStatement prepareStatement(String sql) {
			// TODO Auto-generated method stub
			return null;
		}

		public void commit() {
			// TODO Auto-generated method stub
			
		}

		public void close() {
			// TODO Auto-generated method stub
			
		}
}
