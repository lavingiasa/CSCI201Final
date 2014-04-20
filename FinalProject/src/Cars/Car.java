package Cars;

import java.util.Vector;

import Freeways.Interstate10;
import Freeways.Interstate101;
import Freeways.Interstate105;
import Freeways.Interstate405;
import Freeways.Ramp;
import Freeways.Waypoint;
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
	private CarDot marker;
	private Ramp currentRamp;
	private Waypoint currentWaypoint;
	private Vector <Ramp> ramps;
	private Vector <Waypoint> waypoints;
	
	public Car(int id, double speed, String direction, String ramp, String freeway, long time)
	{
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		this.freeway = freeway;
		this.freewayNumber = Integer.parseInt(freeway);
		this.xLocation = 0;
		this.yLocation = 0;
		this.rampNumber = 0;
		this.currentWaypoint = null;
		this.wayPointNumber = 0;
		this.currentRamp = null;
		try {
			sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setRamps();
		setRamp();
	}

	private void setRamp() 
	{
		
		for(int i = 0; i < ramps.size(); i++)
		{
			if(ramps.get(i).getName().equals(ramp))
			{
				rampNumber = i;
				//this.xLocation = ramps.get(rampNumber).getxLocation();
				//this.yLocation = ramps.get(rampNumber).getyLocation();
				//System.out.println(ramp + ": " + rampNumber);
				return;
			}
		}
		System.out.println(ramp);
		//System.out.println(ramp + ": " + rampNumber);

	}
	
	private void setRamps() 
	{
		
		switch (freewayNumber) {
		case 10:
			ramps = Interstate10.ramps;
			break;
		case 101:
			ramps = Interstate101.ramps;
			break;
		case 105:
			ramps = Interstate105.ramps;
			break;
		case 405:
			ramps = Interstate405.ramps;
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
		/*
		 * if(!setRampHasBeenDone) { setRampTesting(); setRampHasBeenDone =
		 * true; }
		 */
		try {
			sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(true)
		{
			//Ramp nextRamp = null;
			//get closest point to current ramp
			currentRamp = ramps.get(rampNumber);
			//nextRamp = ramps.get(rampNumber + 1);
			
			Waypoint nextWaypoint = getNextWaypoint();
			long timeItShouldBeThereBy = getTimeItShouldBeThereBy(nextWaypoint);
			//System.out.println("TimeShould " + timeItShouldBeThereBy + "current" +  System.currentTimeMillis());

			
			while(timeItShouldBeThereBy > System.currentTimeMillis())
			{
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			marker.setLat(nextWaypoint.getxLocation());
			marker.setLon(nextWaypoint.getyLocation());
			setxLocation(nextWaypoint.getxLocation());
			setyLocation(nextWaypoint.getyLocation());
			currentWaypoint = nextWaypoint;
			//currentTime = currentTimeParam;
			//MapGUI.refreshTheMap();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private long getTimeItShouldBeThereBy(Waypoint nextWaypoint) 
	{
		double distanceItShouldBe = MathEquations.calculateDistance(xLocation, yLocation, nextWaypoint.getxLocation(), nextWaypoint.getyLocation());
		if(distanceItShouldBe == 0)
		{
			distanceItShouldBe = .1;
		}
		double KMPerHour = MathEquations.milesInKM(speed);

		double timeItShouldTakeInHours = (distanceItShouldBe/KMPerHour);
		long timeItShouldTakeInMilliSeconds = (long) (timeItShouldTakeInHours * 3600000); 
		long totalTime = System.currentTimeMillis() + timeItShouldTakeInMilliSeconds;

		return totalTime;
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
			
			if(nextIndex == -1)
			{
				nextIndex = 0;
			}
			currentWaypoint = waypoints.get(nextIndex);
			//System.out.println(xLocation +" "+currentWaypoint.getxLocation());
			//System.out.println(yLocation +" "+currentWaypoint.getyLocation());

			wayPointNumber = nextIndex;
			return currentWaypoint;
		}else{			
			if(wayPointNumber < waypoints.size()-1 && wayPointNumber > 0)
			{
				if(direction.equals("North") || direction.equals("East"))
				{
					currentWaypoint = waypoints.get(wayPointNumber+1);
					wayPointNumber ++;
				}else{
					currentWaypoint = waypoints.get(wayPointNumber-1);
					wayPointNumber --;
				}
			}else{
				currentWaypoint = waypoints.get(wayPointNumber);
				marker.setVisible(false);
			}
			
			
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
