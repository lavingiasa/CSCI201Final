package Cars;

import Freeways.Interstate10;
import Freeways.Interstate101;
import Freeways.Interstate105;
import Freeways.Interstate405;
import Freeways.Ramp;
import Freeways.Waypoint;
import MathFunctions.MathEquations;

public class Car 
{
	private int id;
	private double speed;
	private String direction;
	private String ramp;
	private String freeway;
	private int rampNumber;
	private int freewayNumber;
	private double xLocation;
	private double yLocation;
	private long currentTime;
	
	public Car(int id, double speed, String direction, String ramp, String freeway, long time)
	{
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		this.freeway = freeway;
		this.freewayNumber = Integer.parseInt(freeway);
		this.currentTime = time;
		this.xLocation = 0;
		this.yLocation = 0;
		setRampNumberTesting();
		//setRampNumber();
	}

	private void setRampNumberTesting() 
	{
		
		switch (freewayNumber) {
		case 10:
			rampNumber = 0;
			break;
		case 101:
			rampNumber = 0;
			break;
		case 105:
			rampNumber = 0;
			break;
		case 405:
			rampNumber = 0;
			break;
			
		default:
			break;
		}
		
	}
	
	private void setRampNumber() 
	{
		
		switch (freewayNumber) {
		case 10:
			rampNumber = 0;
			break;
		case 101:
			rampNumber = 0;
			break;
		case 105:
			rampNumber = 0;
			break;
		case 405:
			rampNumber = 0;
			break;
			
		default:
			break;
		}
		
	}

	public int getID()
	{
		return id;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public void setDirection(String direction)
	{
		this.direction = direction;
	}

	public void setRamp(String ramp)
	{
		this.ramp = ramp;
	}
	

	public String getRamp()
	{
		return ramp;
	}

	public void setFreeway(String freeway)
	{
		this.freeway = freeway;
	}

	public String getFreeway() 
	{
		return freeway;
	}

	public int getRampNumber() 
	{
		return rampNumber;
	}

	public int getFreewayNumber() {
		return freewayNumber;
	}

	public void setFreewayNumber(int freewayNumber)
	{
		this.freewayNumber = freewayNumber;
	}

	public void setRampNumber(int rampNumber) 
	{
		this.rampNumber = rampNumber;
	}
	
	public double getSpeed()
	{
		return speed;
	}

	public double getxLocation() 
	{
		return xLocation;
	}

	public void setxLocation(double xLocation) 
	{
		this.xLocation = xLocation;
	}

	public double getyLocation() 
	{
		return yLocation;
	}

	public void setyLocation(double yLocation) 
	{
		this.yLocation = yLocation;
	}

	public void updateLocation(long currentTimeParam) 
	{
		Ramp nextWaypoint = null;
		switch (freewayNumber) {
		case 10:
			//TODO fix this to do waypoints
			nextWaypoint = Interstate10.ramps.get(rampNumber + 1);
			break;
		case 101:
			nextWaypoint = Interstate101.ramps.get(rampNumber + 1);
			rampNumber = 0;
			break;
		case 105:
			nextWaypoint = Interstate105.ramps.get(rampNumber + 1);
			rampNumber = 0;
			break;
		case 405:
			nextWaypoint = Interstate405.ramps.get(rampNumber + 1);
			rampNumber = 0;
			break;
			
		default:
			break;
		}
		
		double KMPerHour = MathEquations.milesInKM(speed);
		double deltaTime = currentTime - currentTimeParam;
		double deltaX = Math.abs(xLocation - nextWaypoint.getxLocation());
		double deltaY = Math.abs(yLocation - nextWaypoint.getyLocation());
		double angleInRad = Math.atan2(deltaY, deltaX);
		double deltaWhatIWantToMove = KMPerHour * 0.00000027777778 * deltaTime; //TODO wrong units
		double newX = deltaWhatIWantToMove * Math.cos(angleInRad);
		double newY = deltaWhatIWantToMove * Math.sin(angleInRad);
		
		xLocation += newX;
		yLocation += newY;
		
		currentTime = currentTimeParam;
	}
	
	
	
	

}
