package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnection {
	//static reference to itself
	private static CreateConnection instance = new CreateConnection();
	public static final String URL = "jdbc:mysql://localhost/JDBCDB";             // Change this part to connect to different servers
	public static final String USER = "root";
	public static final String PASSWORD = "";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";          // this driver is included in the FinalProject directory
	
	//private constructor
	private CreateConnection() {
		try {
			//Step 2: Load MySQL Java driver
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Connection createConnection() {

		Connection connection = null;
		try {
			//Step 3: Establish Java MySQL connection
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to Connect to Database.");
		}
		return connection;
	}	
	
	public static Connection getConnection() {
		return instance.createConnection();
	}
}