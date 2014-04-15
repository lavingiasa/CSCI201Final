package Freeways;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Freeway 
{
	/*
	public static Vector<Ramp> rampsOnThe10;
	public static Vector<Ramp> rampsOnThe101;
	public static Vector<Ramp> rampsOnThe105;
	public static Vector<Ramp> rampsOnThe405;

	public static Vector<Waypoint> waypointsOnThe10;
	public static Vector<Waypoint> waypointsOnThe101;
	public static Vector<Waypoint> waypointsOnThe105;
	public static Vector<Waypoint> waypointsOnThe405;
	*/
	int num;
	
	public Freeway( int number )
//	public Interstate10()
	{
		/*
		if(rampsOnThe10 == null)
		{
			rampsOnThe10 = new Vector<Ramp>();
		}
		
		if(rampsOnThe101 == null)
		{
			rampsOnThe101 = new Vector<Ramp>();
		}
		
		if(rampsOnThe105 == null)
		{
			rampsOnThe105 = new Vector<Ramp>();
		}
		
		if(rampsOnThe405 == null)
		{
			rampsOnThe405 = new Vector<Ramp>();
		}
		
		if(waypointsOnThe10 == null)
		{
			waypointsOnThe10 = new Vector<Waypoint>();
		}
		
		if(waypointsOnThe101 == null)
		{
			waypointsOnThe101 = new Vector<Waypoint>();
		}
		
		if(waypointsOnThe105 == null)
		{
			waypointsOnThe105 = new Vector<Waypoint>();
		}
		
		if(waypointsOnThe405== null)
		{
			waypointsOnThe405 = new Vector<Waypoint>();
		}

		*/
		num = number;
	}
	/*
	public void addFreewayPoints()
	{
		try {
			FileReader fr = new FileReader("waypoints/" + num + "WayPoints.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			String headerText = br.readLine();
			System.out.println("Adding points on " + headerText);         // Prints out name of highway
			while ((line = br.readLine()) != null) {
				final String[] coordinatesArray = line.split(",");
				final double x = Double.parseDouble(coordinatesArray[0]);
				final double y = Double.parseDouble(coordinatesArray[1]);
				switch (num) 
				{
				case 10:
					waypointsOnThe10.add(new Waypoint(x, y));
					break;
				case 101:
					waypointsOnThe101.add(new Waypoint(x, y));
					break;
				case 105:
					waypointsOnThe105.add(new Waypoint(x, y));
					break;
				case 405:
					waypointsOnThe405.add(new Waypoint(x, y));
					break;
				default:
					break;
				}
			}
			
			br.close();
			//Adding ramps
			FileReader fr2 = new FileReader("ramps/" + num + "Ramps.txt");
			
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
					switch (num) 
					{
					case 10:
						rampsOnThe10.add(new Ramp(rampX,rampY,orientation,name));
						break;
					case 101:
						rampsOnThe101.add(new Ramp(rampX,rampY,orientation,name));
						break;
					case 105:
						rampsOnThe105.add(new Ramp(rampX,rampY,orientation,name));
						break;
					case 405:
						rampsOnThe405.add(new Ramp(rampX,rampY,orientation,name));
						break;
					default:
						break;
					}
				}
			}
			br2.close();
			Ramp.setCurrentID(0);
			
		} catch (NumberFormatException nfe) { nfe.printStackTrace(); } 
		catch (FileNotFoundException e) { e.printStackTrace(); }  
		catch (IOException e) { e.printStackTrace();}
	}
	*/
	
}
