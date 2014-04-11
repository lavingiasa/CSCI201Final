package Map;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;

import Cars.Car;
import Cars.CarDot;
import Freeways.Interstate10;
import Freeways.Interstate101;
import Freeways.Interstate105;
import Freeways.Interstate405;
import Freeways.Ramp;
import Freeways.RampDot;
import JSON.JSONsParser;


@SuppressWarnings("serial")
public class MapGUI extends JFrame implements JMapViewerEventListener
{	

	private JMapViewerTree treeMap = null;

	//private JLabel zoomLabel=null;
	private JLabel zoomValue=null;

	//private JLabel mperpLabelName=null;
	private JLabel mperpLabelValue = null;

	private Vector<Car> cars = new Vector<Car>();
	//private Vector<String> ramps = new Vector<String>();
	public static MapGUI currentMap;
	public Interstate10 I10 = new Interstate10();
	public Interstate101 I101 = new Interstate101();
	public Interstate105 I105 = new Interstate105();
	public Interstate405 I405 = new Interstate405();

	JSONsParser parser = null;

	
	
	public static void main (String [] args)
	{
		MapGUI map = new MapGUI();
		currentMap = map;
		map.addFreewayPoints();
		
		
		map.parser = new JSONsParser(map.cars);
		map.parser.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		map.setTheCurrentXandYs();

		//map.addTheOnOffRamps();
		for(int i = 0; i < map.cars.size(); i++)
		{
			map.cars.get(i).start();
		}
		
		
	}
	private void moveCars() 
	{
		/*
		for(int i = 0; i < cars.size(); i++)
		{
			long currentTime = System.currentTimeMillis();
			drawCars();
		}*/
	}
	/*
	private void addTheOnOffRamps() 
	{
		for(int i = 0; i < cars.size(); i++)
		{
			pullJSONUsingName(cars.get(i).getRamp());
			parseJSONUsingPulledJSON();
		}
		

	}*/
	
	private void setTheCurrentXandYs() 
	{
		for (int i = 0; i < cars.size(); i++) 
		{
			Integer freewayNumber = cars.get(i).getFreewayNumber();
			switch (freewayNumber) {
			case 10:
				drawCarOnThe10(cars.get(i));
				cars.get(i).setWaypoints(I10.waypoints);
				cars.get(i).setRamps(I10.ramps);
				break;
			case 101:
				drawCarOnThe101(cars.get(i));
				cars.get(i).setWaypoints(I101.waypoints);
				cars.get(i).setRamps(I101.ramps);
				break;
			case 105:
				drawCarOnThe105(cars.get(i));
				cars.get(i).setWaypoints(I105.waypoints);
				cars.get(i).setRamps(I105.ramps);
				break;
			case 405:
				drawCarOnThe405(cars.get(i));
				cars.get(i).setWaypoints(I405.waypoints);
				cars.get(i).setRamps(I405.ramps);
				break;
			default:
				break;
			}
		}
	}
	
	//making this one work first
	private void drawCarOnThe405(Car car) 
	{
		if(car.getxLocation() == 0 || car.getyLocation() == 0)
		{
			int rampNumber = car.getRampNumber();
			Ramp ramp = I405.ramps.get(rampNumber);
			double xLocation = ramp.getxLocation();
			double yLocation = ramp.getyLocation();
			car.setxLocation(xLocation);
			car.setyLocation(yLocation);
		}
		car.setMarker(drawTheCarOnTheMap(car.getSpeed(), car.getxLocation(), car.getyLocation()));
		
	}

	public synchronized CarDot drawTheCarOnTheMap(double speed, double xLocation, double yLocation) 
	{
		CarDot currentDot = new CarDot(speed, Color.BLACK, xLocation, yLocation);
		map().addMapMarker(currentDot);		
		return currentDot;
	}
	
	public static synchronized void refreshTheMap()
	{
		currentMap.repaint();
	}

	private void drawCarOnThe105(Car car) 
	{
		// TODO Auto-generated method stub
		
	}

	private void drawCarOnThe101(Car car) 
	{
		// TODO Auto-generated method stub
		
	}
	
	private void drawCarOnThe10(Car car) 
	{
		// TODO Auto-generated method stub
		
	}

	private void addFreewayPoints() 
	{
		I10.addFreewayPoints();
		Ramp.setCurrentID(0);
		I101.addFreewayPoints();
		Ramp.setCurrentID(0);
		I105.addFreewayPoints();
		Ramp.setCurrentID(0);
		I405.addFreewayPoints();
		Ramp.setCurrentID(0);
		//testTheFreewayPoints();
		
	}

	private void testTheFreewayPoints() 
	{
		for(int i = 0; i < I101.waypoints.size(); i++)
		{
			drawTheRampOnTheMap(I101.waypoints.get(i).getxLocation(), I101.waypoints.get(i).getyLocation());
		}
		
		for(int i = 0; i < I405.waypoints.size(); i++)
		{
			drawTheRampOnTheMap(I405.waypoints.get(i).getxLocation(), I405.waypoints.get(i).getyLocation());
		}
		
		for(int i = 0; i < I10.waypoints.size(); i++)
		{
			drawTheRampOnTheMap(I10.waypoints.get(i).getxLocation(), I10.waypoints.get(i).getyLocation());
		}

	    for(int i = 0; i < I105.waypoints.size(); i++)
	    {
	      drawTheRampOnTheMap(I105.waypoints.get(i).getxLocation(), I105.waypoints.get(i).getyLocation());
	    }
		
	}
	//http://svn.openstreetmap.org/applications/viewer/jmapviewer/src/org/openstreetmap/gui/jmapviewer/
	private void drawTheRampOnTheMap(Double xLocation, Double yLocation) 
	{
		map().addMapMarker(new RampDot(xLocation, yLocation));

	}
	
	/*
	private void parseJSONUsingPulledJSON() 
	{
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("JSONs/currentRamp.json"));
			String status = (String) jsonObject.get("status");
			if(status.equals("OK"))
			{
				JSONArray resultsArray = (JSONArray) jsonObject.get("results");
				JSONObject results = (JSONObject) resultsArray.get(0);
				String formattedAddress = (String) results.get("formatted_address");
				System.out.println(formattedAddress);
				JSONObject geometry = (JSONObject) results.get("geometry");
				JSONObject location = (JSONObject) geometry.get("location");
				Double xLocation = (Double) location.get("lat");
				Double yLocation = (Double) location.get("lng"); 
				drawTheRampOnTheMap(xLocation, yLocation);
			}else{
				System.out.println("I could not find the spot for a ramp :(");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}*/
	/*
	private void pullJSONUsingName(String rampName) 
	{
		URL website;
		String nameOfRampURL = getURLOfTheRamp(rampName);
		try {
			website = new URL(nameOfRampURL);
			//"http://nominatim.openstreetmap.org/search/Robertson%20boulevard,%20Culver?format=json&polygon=0&addressdetails=0");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(
					"JSONs/currentRamp.json");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	//http://maps.googleapis.com/maps/api/geocode/json?address=market+and+4th,+san+francisco&sensor=false
	private String getURLOfTheRamp(String rampName) 
	{
		String URLOfRamp = "http://maps.googleapis.com/maps/api/geocode/json?address=";
		String fixedRampName = rampName.replace('?', ' ');
		System.out.println(fixedRampName);
		ramps.add(fixedRampName);
		String[] arrayOfTheRampNameSplitByName = fixedRampName.split(" ");
		for(int i = 0; i < arrayOfTheRampNameSplitByName.length; i++)
		{
			if(i == 0)
			{
				URLOfRamp = URLOfRamp.concat(arrayOfTheRampNameSplitByName[i]);
			}else{
				URLOfRamp = URLOfRamp.concat("+" + arrayOfTheRampNameSplitByName[i]);
			}
		}

		URLOfRamp = URLOfRamp.concat("+los+angeles&sensor=false");
		return URLOfRamp; 

	}
	*/

	public MapGUI()
	{
		super("Map Demo");
		setSize(400,400);

		

		treeMap = new JMapViewerTree("Zones");
		map().addJMVListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		map().setTileSource((TileSource) new MapQuestOsmTileSource());
		map().setDisplayPositionByLatLon(34.1073594,-118.2007454, 11); 

		add(treeMap);

		map().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					map().getAttribution().handleAttribution(e.getPoint(), true);
				}
			}
		});

		map().addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				boolean cursorHand = map().getAttribution().handleAttributionCursor(p);
				if (cursorHand) {
					map().setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					map().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		setVisible(true);
	}

	private JMapViewer map() 
	{
		return treeMap.getViewer();
	}

	@Override
	public void processCommand(JMVCommandEvent command) {
		if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
				command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
			updateZoomParameters();
		}
	}

	private void updateZoomParameters() {
		if (mperpLabelValue!=null)
			mperpLabelValue.setText(String.format("%s",map().getMeterPerPixel()));
		if (zoomValue!=null)
			zoomValue.setText(String.format("%s", map().getZoom()));
	}

}
