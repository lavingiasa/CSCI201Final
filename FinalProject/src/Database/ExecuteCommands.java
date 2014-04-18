package Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Cars.Car;

public class ExecuteCommands {
	public static void main(String[] args) {		
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("Enter the EmployeeID:");
//		
//		int employeeId;
//		try {
//			employeeId = Integer.parseInt(br.readLine());
//			ExecuteCommands demo = new ExecuteCommands();
//			
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		}		
	}

	public static void addCar(int carID, int carSpeed, String carDirection, int freewayNumber, int wayPointNumber)  {		
		Connection connection = null;

		try {			
			connection = CreateConnection.getConnection();
			Statement insertStatement = connection.createStatement();
            insertStatement.executeUpdate("insert into Cars " + "values("+carID+", "+carSpeed+", "+carDirection+", "+freewayNumber+","+wayPointNumber+")");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Car getCar(int carID)  {		
		ResultSet rs = null;
		Connection connection = null;
		java.sql.Statement statement = null; 
		
		Car car = null;
		String query = "SELECT * FROM Cars WHERE carID=" + carID;
		try {			
			connection = CreateConnection.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			
			if (rs.next()) {
//				employee = new Employee();
//				employee.setEmpId(rs.getInt("emp_id"));
//				employee.setEmpName(rs.getString("emp_name"));
//				employee.setDob(rs.getDate("dob"));
//				employee.setSalary(rs.getDouble("salary"));
//				employee.setDeptId((rs.getInt("dept_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return car;
	}
}
