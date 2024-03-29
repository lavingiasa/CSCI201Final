package MathFunctions;
public class MathEquations 
{
	public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
  //http://stackoverflow.com/questions/27928/how-do-i-calculate-distance-between-two-latitude-longitude-points
	public static int calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {

	    double latDistance = Math.toRadians(userLat - venueLat);
	    double lngDistance = Math.toRadians(userLng - venueLng);

	    double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
	                    (Math.cos(Math.toRadians(userLat))) *
	                    (Math.cos(Math.toRadians(venueLat))) *
	                    (Math.sin(lngDistance / 2)) *
	                    (Math.sin(lngDistance / 2));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    //return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
	    return (int) (AVERAGE_RADIUS_OF_EARTH * c);

	}
	
	public static double milesInKM(double speed)
	{
		return speed * 1.609344f;
	}
}
