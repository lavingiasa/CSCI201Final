package Freeways;

public class Ramp 
{
	double xLocation;
	double yLocation;
	String orientation; //NSEW, dictates direction of off ramp
	String name;
	static int currentID = 0;
	int id;
	
	public Ramp(double x, double y, String orient, String n)
	{
		xLocation = x;
		yLocation = y;
		orientation = orient;
		name = n;
		id = currentID;
		currentID ++;
	}
}
