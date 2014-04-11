package Cars;

import java.util.Vector;

import Freeways.Interstate10;
import Freeways.Interstate101;
import Freeways.Interstate105;
import Freeways.Interstate405;
import Freeways.Ramp;
import Freeways.Waypoint;
import Map.MapGUI;
import MathFunctions.MathEquations;

public class Car extends Thread
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
	private int wayPointNumber;
	private double currentTime;
	private CarDot marker;
	private Ramp currentRamp;
	private Waypoint currentWaypoint;
	private Vector <Ramp> ramps;
	private Vector <Waypoint> waypoints;
	private boolean setRampHasBeenDone = false;
	
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
		this.currentWaypoint = null;
		this.wayPointNumber = 0;
		this.currentRamp = null;
		//setRampNumber();
	}

	private void setRampTesting() 
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

	@Override
	public void run() 
	{
		//getNextWayPoint
		//findHowMuchTime
		//ifCurrentTime < HowMuchTime then move
		if(!setRampHasBeenDone)
		{
			setRampTesting();
			setRampHasBeenDone = true;
		}

		while(true)
		{
			Ramp nextRamp = null;
			//get closest point to current ramp
			currentRamp = ramps.get(rampNumber);
			nextRamp = ramps.get(rampNumber + 1);
			
			Waypoint nextWaypoint = getNextWaypoint();
			double timeItShouldBeThereBy = getTimeItShouldBeThereBy(nextWaypoint);
			
			/*
			double currentTimeParam = System.currentTimeMillis();
			double deltaTime = currentTime - currentTimeParam;
			double KMPerHour = MathEquations.milesInKM(speed);

			double deltaX = Math.abs(xLocation - nextWaypoint.getxLocation());
			double deltaY = Math.abs(yLocation - nextWaypoint.getyLocation());
			double angleInRad = Math.atan2(deltaY, deltaX);
			double deltaWhatIWantToMove = KMPerHour * 0.00000027777778 * deltaTime;
			double newX = deltaWhatIWantToMove * Math.cos(angleInRad);
			double newY = deltaWhatIWantToMove * Math.sin(angleInRad);
			
			xLocation += newX;
			yLocation += newY;
			*/
			while(timeItShouldBeThereBy > System.currentTimeMillis())
			{
				System.out.println("ID: " + id + " Current: " +System.currentTimeMillis() + " By: " + timeItShouldBeThereBy );
			}
			
			marker.setLat(nextWaypoint.getxLocation());
			marker.setLon(nextWaypoint.getyLocation());
			
			
			
			//System.out.println("moving car: " + id);
			
			
			//currentTime = currentTimeParam;
			MapGUI.refreshTheMap();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private double getTimeItShouldBeThereBy(Waypoint nextWaypoint) 
	{
		double distanceItShouldBe = MathEquations.calculateDistance(xLocation, yLocation, nextWaypoint.getxLocation(), nextWaypoint.getyLocation());
		double KMPerHour = MathEquations.milesInKM(speed);

		double timeItShouldTakeInHours = distanceItShouldBe/KMPerHour;
		double timeItShouldTakeInMilliSeconds = timeItShouldTakeInHours * 3600000; 
		
		return timeItShouldTakeInMilliSeconds + System.currentTimeMillis();
	}

	private Waypoint getNextWaypoint() 
	{
		if(currentWaypoint == null)
		{
			
			double minimumDifference = 10000;
			int nextIndex = -1;

			for(int j = 0; j < waypoints.size(); j++)
			{
				double deltaX = Math.abs(currentRamp.getxLocation() - waypoints.get(j).getxLocation());
				double deltaY = Math.abs(currentRamp.getyLocation() - waypoints.get(j).getyLocation());
				double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

				if(distance < minimumDifference)
				{
					minimumDifference = distance;
					nextIndex = j;
				}
			}
			
			currentWaypoint = waypoints.get(nextIndex);
			wayPointNumber = nextIndex;
			return currentWaypoint;
		}else{
			currentWaypoint = waypoints.get(wayPointNumber+1);
			return currentWaypoint;
		}
	}

	public void setMarker(CarDot currentCarDot)
	{
		marker = currentCarDot;
	}

	public Vector<Ramp> getRamps() {
		return ramps;
	}

	public void setRamps(Vector<Ramp> ramps) {
		this.ramps = ramps;
	}

	public Vector<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(Vector<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}
	
	
	
}
