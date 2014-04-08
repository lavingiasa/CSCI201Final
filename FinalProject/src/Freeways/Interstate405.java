package Freeways;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Interstate405 extends Freeway 
{
	public ArrayList<Ramp> ramps;
	public ArrayList<Waypoint> waypoints;
	
	public Interstate405()
	{
		ramps = new ArrayList<Ramp>();
		waypoints = new ArrayList<Waypoint>();
	}
	
	public void addFreewayPoints()
	{
		try {
			FileReader fr = new FileReader("src/405WayPoints.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			String headerText = br.readLine();
			System.out.println("Adding points on " + headerText);         // Prints out name of highway
			while ((line = br.readLine()) != null) {
				final String[] coordinatesArray = line.split(",");
				final double x = Double.parseDouble(coordinatesArray[0]);
				final double y = Double.parseDouble(coordinatesArray[1]);
				waypoints.add(new Waypoint(x, y));
			}
			
			br.close();
		} catch (NumberFormatException nfe) { nfe.printStackTrace(); } 
		catch (FileNotFoundException e) { e.printStackTrace(); }  
		catch (IOException e) { e.printStackTrace();}
	}
}
