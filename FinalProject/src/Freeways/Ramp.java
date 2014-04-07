package Freeways;

public class Ramp 
{
	double xLocation;
	double yLocation;
	static int currentID = 0;
	int id;
	
	public Ramp(double x, double y)
	{
		xLocation = x;
		yLocation = y;
		id = currentID;
		currentID ++;
	}
}
