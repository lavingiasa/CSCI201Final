package Freeways;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Interstate10 extends Freeway 
{
	public static ArrayList<Ramp> ramps;
	public static ArrayList<Waypoint> waypoints;
	
	public Interstate10()
	{
		ramps = new ArrayList<Ramp>();
		waypoints = new ArrayList<Waypoint>();
	}
	
	public void addFreewayPoints()
	{
		try {
			FileReader fr = new FileReader("waypoints/10WayPoints.txt");
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
			//Adding ramps
			FileReader fr2 = new FileReader("ramps/10Ramps.txt");
			
			BufferedReader br2 = new BufferedReader(fr2);
			br2.readLine();
			String orientation = "";
			System.out.println("Adding ramps on " + headerText);
			while((line = br2.readLine()) != null){
				if(line.equals("North") || line.equals("South") || line.equals("East") || line.equals("West")){
					orientation = line;
					System.out.println("Setting orientation " + orientation);
				}
				else{
					final double rampX = Double.parseDouble(line.substring(0,line.indexOf(',')));
					final double rampY = Double.parseDouble(line.substring(line.indexOf(',') + 1, line.indexOf('|')));
					final String name = line.substring(line.indexOf('|') + 1);
					ramps.add(new Ramp(rampX,rampY,orientation,name));
				}
			}
			br2.close();
			
		} catch (NumberFormatException nfe) { nfe.printStackTrace(); } 
		catch (FileNotFoundException e) { e.printStackTrace(); }  
		catch (IOException e) { e.printStackTrace();}
	}
}
