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
	private double currentTime;
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
		this.currentTime = time;
		this.xLocation = 0;
		this.yLocation = 0;
		this.currentWaypoint = null;
		this.currentRamp = null;
		setRampTesting();
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
		while(true)
		{
			Ramp nextRamp = null;
			//get closest point to current ramp
			Waypoint nextWaypoint = getNextWaypoint();
			
			switch (freewayNumber) {
			case 10:
				//TODO fix this to do waypoints
				currentRamp = Interstate10.ramps.get(rampNumber);
				nextRamp = Interstate10.ramps.get(rampNumber + 1);
				ramps = Interstate10.ramps;
				waypoints = Interstate10.waypoints;
				break;
			case 101:
				currentRamp = Interstate101.ramps.get(rampNumber);
				nextRamp = Interstate101.ramps.get(rampNumber + 1);
				ramps = Interstate101.ramps;
				waypoints = Interstate101.waypoints;
				//rampNumber = 0;
				break;
			case 105:
				currentRamp = Interstate105.ramps.get(rampNumber);
				nextRamp = Interstate105.ramps.get(rampNumber + 1);
				ramps = Interstate105.ramps;
				waypoints = Interstate105.waypoints;
				//rampNumber = 0;
				break;
			case 405:
				currentRamp = Interstate405.ramps.get(rampNumber);
				nextRamp = Interstate405.ramps.get(rampNumber + 1);
				ramps = Interstate405.ramps;
				waypoints = Interstate405.waypoints;
				//rampNumber = 0;
				break;
				
			default:
				break;
			}
			
			double currentTimeParam = System.currentTimeMillis();
			double KMPerHour = MathEquations.milesInKM(speed);
			double deltaTime = currentTime - currentTimeParam;
			double deltaX = Math.abs(xLocation - nextWaypoint.getxLocation());
			double deltaY = Math.abs(yLocation - nextWaypoint.getyLocation());
			double angleInRad = Math.atan2(deltaY, deltaX);
			double deltaWhatIWantToMove = KMPerHour * 0.00000027777778 * deltaTime;
			double newX = deltaWhatIWantToMove * Math.cos(angleInRad);
			double newY = deltaWhatIWantToMove * Math.sin(angleInRad);
			
			xLocation += newX;
			yLocation += newY;
			
			//System.out.println("moving car: " + id);
			marker.setLat(xLocation);
			marker.setLon(yLocation);
			
			currentTime = currentTimeParam;
			MapGUI.refreshTheMap();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		}else{
			
		}
		return null;
	}

	public void setMarker(CarDot currentCarDot)
	{
		marker = currentCarDot;
	}
	
}
