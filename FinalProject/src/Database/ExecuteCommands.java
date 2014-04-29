package Database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

	public static void addFreeway(int freewayID, int numCars, double averageSpeed)  {		
		Connection connection = null;
//		System.out.println("Adding Freeway "+ freewayID);
		try {			
			connection = CreateConnection.getConnection();
			Statement insertStatement = connection.createStatement();
            insertStatement.executeUpdate("INSERT INTO FinalProject.Freeways "
            		+ "VALUES("+freewayID+", "+numCars+", "+averageSpeed+");");

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
	
	public static void updateFreeway(int freewayID, int numCars, double averageSpeed)  {		
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

	public static void addCar(int carID, double speed, String direction, String ramp, String freeway)  {		
		Connection connection = null;
		//	System.out.println("Adding Car "+ carID);
		try {			
			connection = CreateConnection.getConnection();
			if(connection == null)
			{
				//TODO CREATE The DATABASE OR WHATEVER YOU NEED TO DO
			}
			Statement insertStatement = connection.createStatement();
			insertStatement.executeUpdate("INSERT INTO FinalProject.Cars (carID, speed, direction, freeway) "    
					+ "VALUES("+carID+", "+speed+", '"+direction+"','"+freeway+"');");

		} catch (SQLException e) {	e.printStackTrace();} 
		finally {
			if (connection != null) {
				try {	connection.close();	} 
				catch (SQLException e) {e.printStackTrace();	}
			}
		}
	} 
		
	public static void CSV() {

		FileWriter fw ;
		Connection connection = null;
		try {			
			connection = CreateConnection.getConnection();
			Statement queryStatement = connection.createStatement();
			ResultSet res = queryStatement.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = 'FinalProject' ");

			//Preparing List of table Names
			List <String> tableNameList = new ArrayList<String>();
			while(res.next())
			{
				tableNameList.add(res.getString(1));
			}

			//star iterating on each table to fetch its data and save in a .csv file
			for(String tableName:tableNameList)
			{
				int k=0;

				int j=1;

				System.out.println(tableName);

				List<String> columnsNameList  = new ArrayList<String>();

				//select all data from table
				res = queryStatement.executeQuery("select * from FinalProject."+tableName);

				//colunm count is necessay as the tables are dynamic and we need to figure out the numbers of columns
				int colunmCount = getColumnCount(res);

				try {
					fw = new FileWriter("CSCI201Final/FinalProject/CSVs/"+tableName+".csv");
					//this loop is used to add column names at the top of file , if you do not need it just comment this loop
					for(int i=1 ; i<= colunmCount ;i++)
					{
						fw.append(res.getMetaData().getColumnName(i));
						fw.append(",");

					}

					fw.append(System.getProperty("line.separator"));

					while(res.next())
					{
						for(int i=1;i<=colunmCount;i++)
						{

							//you can update it here by using the column type but i am fine with the data so just converting 
							//everything to string first and then saving
							if(res.getObject(i)!=null)
							{
								String data= res.getObject(i).toString();
								fw.append(data) ;
								fw.append(",");
							}
							else
							{
								String data= "null";
								fw.append(data) ;
								fw.append(",");
							}

						}
						//new line entered after each row
						fw.append(System.getProperty("line.separator"));
					}

					fw.flush();
					fw.close();

				} catch (IOException e) {	e.printStackTrace();		}

			}
			connection.close();
			System.out.println("Exporting CSV file");
		}
		catch(SQLException ex){
			System.err.println("SQLException information");
		}
	}

	//to get numbers of rows in a result set 
	public static int  getRowCount(ResultSet res) throws SQLException
	{
		res.last();
		int numberOfRows = res.getRow();
		res.beforeFirst();
		return numberOfRows;
	}

	//to get no of columns in result set

	public static int  getColumnCount(ResultSet res) throws SQLException
	{
		return res.getMetaData().getColumnCount();
	}

}
