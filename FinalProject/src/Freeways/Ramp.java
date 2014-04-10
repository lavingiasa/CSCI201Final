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

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static int getCurrentID() {
		return currentID;
	}

	public static void setCurrentID(int currentID) {
		Ramp.currentID = currentID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
