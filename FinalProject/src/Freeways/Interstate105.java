package Freeways;

import java.util.ArrayList;

public class Interstate105 extends Freeway 
{
	static ArrayList<Ramp> ramps;
	static ArrayList<Waypoint> waypoints;
	
	static
	{
		ramps.add(new Ramp(20, 20));
		
		waypoints.add(new Waypoint(10, 10));	
		
	}
}
