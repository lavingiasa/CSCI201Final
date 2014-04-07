package Freeways;

public class Waypoint 
{
	double xLocation;
	double yLocation;
	static int currentID = 0;
	int id;
	
	public Waypoint(double x, double y)
	{
		xLocation = x;
		yLocation = y;
		id = currentID;
		currentID ++;
	}
}
