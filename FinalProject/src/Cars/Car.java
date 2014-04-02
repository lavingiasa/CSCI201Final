package Cars;

public class Car 
{
	private int id;
	private double speed;
	private String direction;
	private String ramp;
	private String freeway;
	
	public Car(int id, double speed, String direction, String ramp, String freeway)
	{
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		this.freeway = freeway;
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

}
