package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Cars.Car;
import Freeways.Freeway;

public class ExecuteCommands {
	public static void main(String[] args) {		
		try {
			ExecuteCommands demo = new ExecuteCommands();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}		
	}

	public static void addFreeway(int freewayID, int numCars, int averageSpeed)  {		
		Connection connection = null;

		try {			
			connection = CreateConnection.getConnection();
			Statement insertStatement = connection.createStatement();
            insertStatement.executeUpdate("insert into FinalProject.Freeways " + "values("+freewayID+", "+numCars+", "+averageSpeed+")");

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
	
	public static void updateFreeway(int freewayID, int numCars, int averageSpeed)  {		
		Connection connection = null;

		try {			
			connection = CreateConnection.getConnection();
			Statement insertStatement = connection.createStatement();
            insertStatement.executeUpdate("UPDATE FinalProject.Freeways "
            		+ "SET numCars='"+numCars+"', averageSpeed='"+averageSpeed+"' "
            		+ "WHERE Name='"+freewayID+"';");

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
	
	public static Freeway getFreewayInfo(int freewayID)  {		
		ResultSet rs = null;
		Connection connection = null;
		java.sql.Statement statement = null; 
		
		Freeway freeway = null;
		String query = "SELECT * FROM FinalProject.Freeways WHERE carID=" + freewayID;
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
		return freeway;
	}
}
