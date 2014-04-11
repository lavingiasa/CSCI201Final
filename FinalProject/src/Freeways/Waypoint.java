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

	// Accessors
	public double getxLocation() {
		return xLocation;
	}

	public void setxLocation(double xLocation) {
		this.xLocation = xLocation;
	}

	public double getyLocation() {
		return yLocation;
	}

	public void setyLocation(double yLocation) {
		this.yLocation = yLocation;
	}

	public static int getCurrentID() {
		return currentID;
	}

	public static void setCurrentID(int currentID) {
		Waypoint.currentID = currentID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
