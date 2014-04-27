package Map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;

import Cars.Car;
import Cars.CarDot;
import Database.ExecuteCommands;
import Freeways.Interstate10;
import Freeways.Interstate101;
import Freeways.Interstate105;
import Freeways.Interstate405;
import Freeways.Ramp;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import JSON.JSONsParser;

@SuppressWarnings({ "static-access", "serial" })
public class MapGUI extends JFrame implements JMapViewerEventListener
{	

	private JMapViewerTree treeMap = null;

	//private JLabel zoomLabel=null;
	private JLabel zoomValue=null;

	//private JLabel mperpLabelName=null;
	private JLabel mperpLabelValue = null;

	private Vector<Car> cars = new Vector<Car>();
	//private Vector<String> ramps = new Vector<String>();
		
	public boolean allCarsAdded = false;
	
	public static MapGUI currentMap;
	public Interstate10 I10 = new Interstate10();
	public Interstate101 I101 = new Interstate101();
	public Interstate105 I105 = new Interstate105();
	public Interstate405 I405 = new Interstate405();
	/*public Freeway I10 = new Freeway( 10 );
	public Freeway I101 = new Freeway( 101 );
	public Freeway I105 = new Freeway( 105 );
	public Freeway I405 = new Freeway( 405 );*/
	
	public Ramp startRamp;
	public Ramp endRamp;
	public Ramp lastRamp;
	public boolean preferShortestTime = true;
	public boolean showDirections = false;
	
	private double routeDistance;
	
	
	private final String MAPQUEST_API_KEY = "Fmjtd%7Cluur2qu829%2Cal%3Do5-9aaldu";
	
	private JPanel directionsPanel;
	private JPanel startPanel;
	private JPanel endPanel;
	private JPanel lastPanel;
	
		
	private boolean hasThreeDestinations = false;
	
	
	JSONsParser parser = null;

	
	
	public static void main (String [] args)
	{
		MapGUI map = new MapGUI();
		currentMap = map;
		map.addFreewayPoints();
		
		
		map.parser = new JSONsParser(map.cars);
		map.parser.setPriority(Thread.MAX_PRIORITY);
		map.parser.start();
		
		//openTheLoadingPane(100000);
		/*
		try {
			Thread.sleep(120000);	// 120 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		
		
		//System.out.println("Number of Cars: " + map.cars.size());
		/*map.setTheCurrentXandYs();
		
		//map.addTheOnOffRamps();
		for(int i = 0; i < map.cars.size(); i++)
		{
			map.cars.get(i).start();
		}*/
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() 
			  {
				  if(!MapGUI.currentMap.allCarsAdded && MapGUI.currentMap.cars.size() == 1000)
				  {
					  JOptionPane.showMessageDialog(null, "All cars have been loaded!");
					  MapGUI.currentMap.allCarsAdded = true;
				  }
				  currentMap.repaint();
			  }
			}, 1000, 1000);
		
		
	}
	private static void openTheLoadingPane(int timeToLoadFor)
	{
		final JOptionPane optionPane = new JOptionPane("Loading", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

		final JDialog dialog = new JDialog();
		dialog.setTitle("Loading");
		dialog.setModal(true);

		dialog.setContentPane(optionPane);

		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();

		//create timer to dispose of dialog after 5 seconds
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				dialog.dispose();
			}
		}, timeToLoadFor);

		dialog.setVisible(true);		
	}
	
	
	public void setTheCurrentXandYs(Car car) 
	{
			Integer freewayNumber = car.getFreewayNumber();
			switch (freewayNumber) {
			case 10:
				car.setWaypoints(I10.waypoints);
				car.setRamps(I10.ramps);
				drawCarOnThe10(car);
				break;
			case 101:
				car.setWaypoints(I101.waypoints);
				car.setRamps(I101.ramps);
				drawCarOnThe101(car);
				break;
			case 105:
				car.setWaypoints(I105.waypoints);
				car.setRamps(I105.ramps);
				drawCarOnThe105(car);
				break;
			case 405:
				car.setWaypoints(I405.waypoints);
				car.setRamps(I405.ramps);
				drawCarOnThe405(car);
				break;
			default:
				break;
			
		}
	}
	
	//making this one work first
	private void drawCarOnThe405(Car car) 
	{
		if(car.getxLocation() == 0 || car.getyLocation() == 0)
		{
			Ramp ramp;
			
			ramp = I405.ramps.get(car.getRampNumber());

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
		//currentMap.repaint();
	}

	private void drawCarOnThe105(Car car) 
	{
		if(car.getxLocation() == 0 || car.getyLocation() == 0)
		{
			int rampNumber = car.getRampNumber();
			Ramp ramp = I105.ramps.get(rampNumber);
			double xLocation = ramp.getxLocation();
			double yLocation = ramp.getyLocation();
			car.setxLocation(xLocation);
			car.setyLocation(yLocation);
		}
		car.setMarker(drawTheCarOnTheMap(car.getSpeed(), car.getxLocation(), car.getyLocation()));		
	}

	private void drawCarOnThe101(Car car)
	{
		if(car.getxLocation() == 0 || car.getyLocation() == 0)
		{
			int rampNumber = car.getRampNumber();
			Ramp ramp = I101.ramps.get(rampNumber);
			double xLocation = ramp.getxLocation();
			double yLocation = ramp.getyLocation();
			car.setxLocation(xLocation);
			car.setyLocation(yLocation);
		}
		car.setMarker(drawTheCarOnTheMap(car.getSpeed(), car.getxLocation(), car.getyLocation()));		
	}
	
	private void drawCarOnThe10(Car car) 
	{
		if(car.getxLocation() == 0 || car.getyLocation() == 0)
		{
			int rampNumber = car.getRampNumber();
			Ramp ramp = I10.ramps.get(rampNumber);
			double xLocation = ramp.getxLocation();
			double yLocation = ramp.getyLocation();
			car.setxLocation(xLocation);
			car.setyLocation(yLocation);
		}
		car.setMarker(drawTheCarOnTheMap(car.getSpeed(), car.getxLocation(), car.getyLocation()));		
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

	
	//http://svn.openstreetmap.org/applications/viewer/jmapviewer/src/org/openstreetmap/gui/jmapviewer/
	
	
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
	
	public void getDirections() {
		String priority = "fastest";

		String url = "http://open.mapquestapi.com/directions/v2/route?key=" + MAPQUEST_API_KEY + "&callback=renderAdvancedNarrative&outFormat=xml&routeType=" + priority + "&timeType=1&enhancedNarrative=false&shapeFormat=raw&generalize=0&locale=en_US&unit=m&from="
					+ startRamp.getxLocation() + "," + startRamp.getyLocation() + "&to=" + endRamp.getxLocation() + "," + endRamp.getyLocation();

		pullDirectionData(url);
		parseDirectionData();
		
		if (hasThreeDestinations) {
			url = "http://open.mapquestapi.com/directions/v2/route?key=" + MAPQUEST_API_KEY + "&callback=renderAdvancedNarrative&outFormat=xml&routeType=" + priority + "&timeType=1&enhancedNarrative=false&shapeFormat=raw&generalize=0&locale=en_US&unit=m&from="
					+ endRamp.getxLocation() + "," + endRamp.getyLocation() + "&to=" + lastRamp.getxLocation() + "," + lastRamp.getyLocation();

			pullDirectionData(url);
			parseDirectionData();
			map().removeAllMapPolygons();
		}
		
		showDirections = true;
		
		double averageSpeed = 0;
		
		String freeway1 = "";
		String freeway2 = "";
		String freeway3 = "";

		//freeway 1
		if (I10.ramps.contains(startRamp)) {
			freeway1 = "10";
		}
		
		else if (I101.ramps.contains(startRamp)) {
			freeway1 = "101";
		}
		
		else if (I105.ramps.contains(startRamp)) {
			freeway1 = "105";
		}
		
//		else if (I110.ramps.contains(startRamp)) {
//			freeway1 = "110";
//		}
		
		else if (I405.ramps.contains(startRamp)) {
			freeway1 = "405";
		}
		
		//freeway 2
		if (I10.ramps.contains(endRamp)) {
			freeway2 = "10";
		}
		
		else if (I101.ramps.contains(endRamp)) {
			freeway2 = "101";
		}
		
		else if (I105.ramps.contains(endRamp)) {
			freeway2 = "105";
		}
		
//		else if (I110.ramps.contains(endRamp)) {
//			freeway2 = "110";
//		}
		
		else if (I405.ramps.contains(endRamp)) {
			freeway2 = "405";
		}
		
		if (hasThreeDestinations) {
			if (I10.ramps.contains(lastRamp)) {
				freeway3 = "10";
			}
			
			else if (I101.ramps.contains(lastRamp)) {
				freeway3 = "101";
			}
			
			else if (I105.ramps.contains(lastRamp)) {
				freeway3 = "105";
			}
			
//			else if (I110.ramps.contains(lastRamp)) {
//				freeway3 = "110";
//			}
			
			else if (I405.ramps.contains(lastRamp)) {
				freeway3 = "405";
			}
		}
		
		int numCars = 0;
		
		for (int i = 0; i < cars.size(); i++) {
			//System.out.println(cars.get(i).getFreeway());
			if (hasThreeDestinations) {
				if (cars.get(i).getFreeway().equals(freeway1) || cars.get(i).getFreeway().equals(freeway2) || cars.get(i).getFreeway().equals(freeway3)) {
					numCars += 1;
					averageSpeed += cars.get(i).getSpeed();
					//System.out.println(cars.get(i).getSpeed());
				}
			}
			else {
				if (cars.get(i).getFreeway().equals(freeway1) || cars.get(i).getFreeway().equals(freeway2)) {
					numCars += 1;
					averageSpeed += cars.get(i).getSpeed();
					//System.out.println(cars.get(i).getSpeed());
				}
			}
		}
		
		averageSpeed = averageSpeed/((double) numCars);
		//System.out.println("Average Speed: " + averageSpeed);
		
		double projectedTime = routeDistance/averageSpeed;
		
		JOptionPane.showMessageDialog(currentMap, "Route Distance: " + routeDistance + " miles \n" + "Projected Time: " + projectedTime + " minutes");
	}

	@SuppressWarnings("unchecked")
	private void parseDirectionData() 
	{
		File XMLFile;
		Document XMLTree;
		SAXReader reader;
		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		
		XMLTree = null;
		reader = new SAXReader();
		try {
			XMLFile = new File("JSONs/directionsData.xml");
			XMLTree = reader.read(XMLFile);//new FileInputStream("JSONs/directionsData.xml"));
			List<? extends Node> shapeNodes = new ArrayList<Node>(); 
			//shapeNodes = XMLTree.selectNodes("//response");
			shapeNodes = XMLTree.selectNodes("//response/route/shape/shapePoints/latLng");
			List<? extends Node> distanceNodes = XMLTree.selectNodes("//response/route/distance");
			for(int i = 0; i < shapeNodes.size(); i++)
			{
				double latitude = Double.valueOf(shapeNodes.get(i).selectSingleNode("lat").getText());
				double longitude = Double.valueOf(shapeNodes.get(i).selectSingleNode("lng").getText());
//				System.out.println(latitude + "," + longitude);
				coordinateList.add(new Coordinate(latitude, longitude));
			}
			
			MapPolyLine route = new MapPolyLine(coordinateList);
	
			map().addMapPolygon(route);
			map().setMapPolygonsVisible(true);

			for (int i = 0; i < distanceNodes.size(); i++) {
//				System.out.println("distance: " + distanceNodes.get(i).getText().toString());
				routeDistance += Double.parseDouble(distanceNodes.get(i).getText().toString());
			}

		} catch (DocumentException e) {
			System.out.println("Your XML File is invalid");
			return;
		}
	}
	
	@SuppressWarnings("resource")
	private void pullDirectionData(String url) {
		URL website;
		try {
			website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(
					"JSONs/directionsData.xml");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private Car searchClick(Coordinate c){
		for(int i = 0; i < cars.size(); i++){
			if(c.getLat() > cars.get(i).getxLocation()-.001 && c.getLat() < cars.get(i).getxLocation()+.001){
				if(c.getLon() > cars.get(i).getyLocation()-.001 && c.getLon() < cars.get(i).getyLocation()+.001){
					return cars.get(i);
				}
			}
		}
		return null;
	}
	
	public MapGUI()
	{
		super("Map Demo");
		setSize(400,400);

		JMenuBar jmb = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu graph = new JMenu("Graph");
		JMenu directions = new JMenu("Directions");
		
		//File menu items
		JMenuItem exportToCSV = new JMenuItem("Export to CSV");
		exportToCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				exportDataFromDatabaseToCSV();
				
			}
		});
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setVisible(false);
				dispose();
			}
		});
		
		file.add(exportToCSV);
		file.add(exit);
		
		//Graph menu items
		JMenuItem openGraph = new JMenuItem("Open Graph");
		graph.add( openGraph );
		openGraph.addActionListener( new ActionListener () {
			public void actionPerformed( ActionEvent ae ) {
			
				// Create Bar Chart with data
				 DefaultCategoryDataset datasetOfNumberOfCars = new DefaultCategoryDataset();
				 DefaultCategoryDataset datasetOfAverageSpeed = new DefaultCategoryDataset();
				 
				 
				 Vector<Car> I10Cars = new Vector<Car>();
				 Vector<Car> I101Cars = new Vector<Car>();
				 Vector<Car> I105Cars = new Vector<Car>();
				 Vector<Car> I405Cars = new Vector<Car>();
				 
				 for (int i = 0; i < cars.size(); i++) {
					 if (cars.get(i).getFreewayNumber() == 10) {
						 I10Cars.add(cars.get(i));
					 }
					 
					 if (cars.get(i).getFreewayNumber() == 101) {
						 I101Cars.add(cars.get(i));
					 }
					 
					 if (cars.get(i).getFreewayNumber() == 105) {
						 I105Cars.add(cars.get(i));
					 }
					 
					 if (cars.get(i).getFreewayNumber() == 405) {
						 I405Cars.add(cars.get(i));
					 }
				 }
				 
				
				 
				 datasetOfNumberOfCars.addValue(I10Cars.size(), "# Cars", "I 10" );
				 datasetOfNumberOfCars.addValue(I101Cars.size(), "# Cars", "I 101");
				 datasetOfNumberOfCars.addValue(I105Cars.size(), "# Cars", "I 105");
				 datasetOfNumberOfCars.addValue(I405Cars.size(), "# Cars", "I 405");

				 
				 
				 datasetOfNumberOfCars.addValue(null, "# Cars", "I 10 ");
				 datasetOfNumberOfCars.addValue(null, "# Cars", "I 101 ");
				 datasetOfNumberOfCars.addValue(null, "# Cars", "I 105 ");
				 datasetOfNumberOfCars.addValue(null, "# Cars", "I 405 ");
				 
				 datasetOfAverageSpeed.addValue( null, "Avg. Speed", "I 10" );
				 datasetOfAverageSpeed.addValue( null, "Avg. Speed", "I 101" );
				 datasetOfAverageSpeed.addValue( null, "Avg. Speed", "I 105" );
				 datasetOfAverageSpeed.addValue( null, "Avg. Speed", "I 405" );
				 
				 double I10averageSpeed = 0;
				 for (int i = 0; i < I10Cars.size(); i++) {
					 I10averageSpeed += I10Cars.get(i).getSpeed();
				 }
				 I10averageSpeed = I10averageSpeed/((double) I10Cars.size());
				 
				 datasetOfAverageSpeed.addValue(I10averageSpeed, "Avg. Speed", "I 10 " );				 
				 ExecuteCommands.addFreeway(10, I10Cars.size(), I10averageSpeed);                     // Add 10 Freeway to databse
				 
				 datasetOfAverageSpeed.addValue(I10averageSpeed, "Avg. Speed", "I 10 " );
				 
				 double I101averageSpeed = 0;
				 for (int i = 0; i < I101Cars.size(); i++) {
					 I101averageSpeed += I101Cars.get(i).getSpeed();
				 }
				 I101averageSpeed = I101averageSpeed/((double) I101Cars.size());
				 
				 datasetOfAverageSpeed.addValue(I101averageSpeed, "Avg. Speed", "I 101 ");
				 ExecuteCommands.addFreeway(101, I101Cars.size(), I101averageSpeed);                   // Add 101 Freeway to database

				 datasetOfAverageSpeed.addValue(I101averageSpeed, "Avg. Speed", "I 101 ");
				 
				 double I105averageSpeed = 0;
				 for (int i = 0; i < I105Cars.size(); i++) {
					 I105averageSpeed += I105Cars.get(i).getSpeed();
				 }
				 I105averageSpeed = I105averageSpeed/((double) I105Cars.size());
				 
				 datasetOfAverageSpeed.addValue(I105averageSpeed, "Avg. Speed", "I 105 ");
				 I105averageSpeed = I105averageSpeed/((double) I105Cars.size());

				 
				 ExecuteCommands.addFreeway(105, I105Cars.size(), I105averageSpeed);                 // Add 105 Freeway to database


				 datasetOfAverageSpeed.addValue(I105averageSpeed, "Avg. Speed", "I 105 ");
				 
				 double I405averageSpeed = 0;
				 for (int i = 0; i < I405Cars.size(); i++) {
					 I405averageSpeed += I405Cars.get(i).getSpeed();
				 }
				 I405averageSpeed = I405averageSpeed/((double) I405Cars.size());
				 
				 datasetOfAverageSpeed.addValue(I405averageSpeed, "Avg. Speed", "I 405 ");
				 I405averageSpeed = I405averageSpeed/((double) I405Cars.size());

				 ExecuteCommands.addFreeway(405, I405Cars.size(), I405averageSpeed);                 // Add 405 Freeway to database


				 datasetOfAverageSpeed.addValue(I405averageSpeed, "Avg. Speed", "I 405 ");
				 
				 JFreeChart chart = ChartFactory.createBarChart( "Freeway Data", "Interstate", "# Cars", datasetOfNumberOfCars, PlotOrientation.VERTICAL, true, true, false);
				// set the background color for the chart...

		        // get a reference to the plot for further customisation...
		        CategoryPlot plot = (CategoryPlot)chart.getPlot();
		        // set the range axis to display integers only...
		        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
		        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		        BarRenderer renderer = (BarRenderer)plot.getRenderer();
		        renderer.setDrawBarOutline(true);
		        renderer.setMaximumBarWidth(.05);
		        // set up gradient paints for series...
		        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 64));
		        renderer.setSeriesPaint(0, gp0);

		        CategoryAxis domainAxis = plot.getDomainAxis();
		        domainAxis.setCategoryMargin(0.1f);
		        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI
		        // / 6.0));
		        
	            final NumberAxis axis2 = new NumberAxis("Avg. Speed");
	            plot.setDataset(1, datasetOfAverageSpeed);
	            plot.mapDatasetToRangeAxis(1, 1);
	            plot.setRangeAxis(1, axis2);
	            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
	            final BarRenderer renderer2 = new BarRenderer();
	            renderer2.setDrawBarOutline(true);
	            renderer2.setMaximumBarWidth(.05);
	            plot.setRenderer(1, renderer2);
	        
				 
				 try {
					 ChartUtilities.saveChartAsJPEG(new File( "src/chart.png" ), chart, 500, 300);
				 } catch (IOException e) { System.err.println("Problem occurred creating chart.");
				 }
				 
				 // Displays chart as image on a JFrame
				 
				 JFrame chartFrame = new JFrame( "Freeway Data" );
				 chartFrame.setSize( new Dimension( 882, 330 ) );
				 chartFrame.setResizable( false );
				 chartFrame.add(new JLabel(new ImageIcon("src/chart.png")), BorderLayout.WEST);
				 
				 
				 DefaultTableModel tableModel = new DefaultTableModel();
				 
				 tableModel.addColumn("");
				 tableModel.addColumn("");
				 tableModel.addColumn("");
				 tableModel.addColumn("");
				 tableModel.addColumn("");
				 
				 Vector<String> columnNames = new Vector<String>();
				 columnNames.add("");
				 columnNames.add("I10");
				 columnNames.add("I101");
				 columnNames.add("I105");
				 columnNames.add("I405");
				 
				 tableModel.addRow(columnNames);
				 
				 DecimalFormat df = new DecimalFormat("0.00");
				 Vector<String> averageSpeedVector = new Vector<String>();
				 averageSpeedVector.add("Avg. Speed");
				 averageSpeedVector.add(df.format(I10averageSpeed));
				 averageSpeedVector.add(df.format(I101averageSpeed));
				 averageSpeedVector.add(df.format(I105averageSpeed));
				 averageSpeedVector.add(df.format(I405averageSpeed));

				 tableModel.addRow(averageSpeedVector);
				 
				 Vector<String> numCarsVector = new Vector<String>();
				 numCarsVector.add("# of Cars");
				 numCarsVector.add(Integer.toString(I10Cars.size()));
				 numCarsVector.add(Integer.toString(I101Cars.size()));
				 numCarsVector.add(Integer.toString(I105Cars.size()));
				 numCarsVector.add(Integer.toString(I405Cars.size()));
				 
				 tableModel.addRow(numCarsVector);
				 tableModel.setNumRows(3);
				 JTable jTable = new JTable(tableModel);
				 
				 chartFrame.add(jTable, BorderLayout.EAST);
				 chartFrame.setVisible( true );
				
			}
		});
		
		
		//Directions menu items
		JMenuItem clearDirections = new JMenuItem("Clear Directions");
		clearDirections.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				map().removeAllMapPolygons();
				showDirections = false;
				routeDistance = 0;
			}
		});
		
		JMenuItem getDirections = new JMenuItem("Get Directions");
		getDirections.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			private JOptionPane directionsDialog;

			public void actionPerformed(ActionEvent ae) {
				Vector<String> rampList = new Vector<String>();
				Vector<String> optionList = new Vector<String>();
				optionList.add("Shortest Time");
				optionList.add("Shortest Distance");
				
				for (int i = 0; i < I10.ramps.size(); i ++) {
					rampList.add(I10.ramps.get(i).getName() + "-- I-10");
				}
				
				for (int i = 0; i < I101.ramps.size(); i ++) {
					rampList.add(I101.ramps.get(i).getName() + "-- I-101");
				}
				
				for (int i = 0; i < I105.ramps.size(); i ++) {
					rampList.add(I105.ramps.get(i).getName() + "-- I-105");
				}
				
//				for (int i = 0; i < I110.ramps.size(); i ++) {
//					rampList.add(I110.ramps.get(i) + "-- I-110");
//				}
				
				for (int i = 0; i < I405.ramps.size(); i ++) {
					rampList.add(I405.ramps.get(i).getName() + "-- I-405");
				}
				
				directionsPanel = new JPanel();
				directionsPanel.setLayout(new BoxLayout(directionsPanel, BoxLayout.PAGE_AXIS));
				
				JComboBox startList = new JComboBox(rampList);
				JComboBox endList = new JComboBox(rampList);
				JComboBox lastList = new JComboBox(rampList);
				
				startPanel = new JPanel(new FlowLayout());
				startPanel.add(new JLabel("Starting Ramp:"));
				startPanel.add(startList);
				
				endPanel = new JPanel(new FlowLayout());
				endPanel.add(new JLabel("Ending Ramp:"));
				endPanel.add(endList);
				
				lastPanel = new JPanel(new FlowLayout());
				lastPanel.add(new JLabel("Ending Ramp:"));
				lastPanel.add(lastList);
				
				directionsDialog = new JOptionPane();
				directionsPanel.add(startPanel);
				directionsPanel.add(endPanel);
				
				hasThreeDestinations = false;
				
				final JButton addButton = new JButton("Add Stop");
				addButton.setAlignmentX(CENTER_ALIGNMENT);
				addButton.addActionListener(new ActionListener() {
					@SuppressWarnings("unused")
					private int response;

					public void actionPerformed(ActionEvent ae) {
						hasThreeDestinations = true;
						directionsPanel.remove(addButton);
						directionsPanel.add(lastPanel);
						endPanel.remove(0);
						endPanel.add(new JLabel("Middle Ramp:"), 0);
						SwingUtilities.getWindowAncestor(directionsPanel).dispose();
						response = JOptionPane.showConfirmDialog(null, directionsPanel, "Select Locations", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					}
				});
				
				directionsPanel.add(addButton);
				
//				JPanel buttonPanel = new JPanel(new FlowLayout());
//				
//				JButton okButton = new JButton("OK");
//				JButton cancelButton = new JButton("Cancel");
//				
//				buttonPanel.add(okButton);
//				buttonPanel.add(cancelButton);
//				
//				directionsPanel.add(buttonPanel);
				
				int response = JOptionPane.showConfirmDialog(null, directionsPanel, "Select Locations", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (response != -1 && response != 2) {
					JLabel errorLabel = new JLabel("Please select separate starting and ending points.");
					errorLabel.setAlignmentX(CENTER_ALIGNMENT);
					directionsPanel.add(errorLabel);
					while(response != -1 && response != 2 && startList.getSelectedItem().toString().equals(endList.getSelectedItem().toString())) {
						response = JOptionPane.showConfirmDialog(null, directionsPanel, "Select Locations", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					}
				}
				
				String[] startListSelection = startList.getSelectedItem().toString().split("--");
				
				if (startListSelection[startListSelection.length - 1].equals(" I-10")) {
					//System.out.println("Start ramp is on I-10");
					for (int i = 0; i < I10.ramps.size(); i++) {
						if (I10.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I10.ramps.get(i);
							//System.out.println("Start ramp is " + I10.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (startListSelection[startListSelection.length - 1].equals(" I-101")) {
//					System.out.println("Start ramp is on I-101");
					for (int i = 0; i < I101.ramps.size(); i++) {
						if (I101.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I101.ramps.get(i);
							//System.out.println("Start ramp is " + I101.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (startListSelection[startListSelection.length - 1].equals(" I-105")) {
					//System.out.println("Start ramp is on I-105");
					for (int i = 0; i < I105.ramps.size(); i++) {
						if (I105.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I105.ramps.get(i);
							//System.out.println("Start ramp is " + I105.ramps.get(i).getName());
							break;
						}
					}
				}
				
//				else if (startListSelection[startListSelection.length - 1].equals(" I-110")) {
//					System.out.println("Start ramp is on I-110");
//					for (int i = 0; i < I110.ramps.size(); i++) {
//						if (I110.ramps.get(i).getName().equals(startListSelection[0])) {
//							startRamp = I110.ramps.get(i);
//							System.out.println("Start ramp is " + I110.ramps.get(i).getName());
//							break;
//						}
//					}
//				}
				
				else if (startListSelection[startListSelection.length - 1].equals(" I-405")) {
					//System.out.println("Start ramp is on I-405");
					for (int i = 0; i < I405.ramps.size(); i++) {
						if (I405.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I405.ramps.get(i);
							//System.out.println("Start ramp is " + I405.ramps.get(i).getName());
							break;
						}
					}
				}
				
				String[] endListSelection = endList.getSelectedItem().toString().split("--");
				
				if (endListSelection[endListSelection.length - 1].equals(" I-10")) {
					//System.out.println("End ramp is on I-10");
					for (int i = 0; i < I10.ramps.size(); i++) {
						if (I10.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I10.ramps.get(i);
							//System.out.println("End ramp is " + I10.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (endListSelection[endListSelection.length - 1].equals(" I-101")) {
					//System.out.println("End ramp is on I-101");
					for (int i = 0; i < I101.ramps.size(); i++) {
						if (I101.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I101.ramps.get(i);
							//System.out.println("End ramp is " + I101.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (endListSelection[endListSelection.length - 1].equals(" I-105")) {
					//System.out.println("End ramp is on I-105");
					for (int i = 0; i < I105.ramps.size(); i++) {
						if (I105.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I105.ramps.get(i);
							//System.out.println("End ramp is " + I105.ramps.get(i).getName());
							break;
						}
					}
				}
				
//				else if (startListSelection[endListSelection.length - 1].equals(" I-110")) {
//					System.out.println("End ramp is on I-110");
//					for (int i = 0; i < I110.ramps.size(); i++) {
//						if (I110.ramps.get(i).getName().equals(endListSelection[0])) {
//							endRamp = I110.ramps.get(i);
//							System.out.println("End ramp is " + I110.ramps.get(i).getName());
//							break;
//						}
//					}
//				}
				
				else if (endListSelection[endListSelection.length - 1].equals(" I-405")) {
					//System.out.println("End ramp is on I-405");
					for (int i = 0; i < I405.ramps.size(); i++) {
						if (I405.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I405.ramps.get(i);
							//System.out.println("End ramp is " + I405.ramps.get(i).getName());
							break;
						}
					}
				}
				
				if (hasThreeDestinations) {
				
					String[] lastListSelection = lastList.getSelectedItem().toString().split("--");
					
					if (lastListSelection[lastListSelection.length - 1].equals(" I-10")) {
						//System.out.println("Last ramp is on I-10");
						for (int i = 0; i < I10.ramps.size(); i++) {
							if (I10.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I10.ramps.get(i);
								//System.out.println("Last ramp is " + I10.ramps.get(i).getName());
								break;
							}
						}
					}
					
					else if (lastListSelection[lastListSelection.length - 1].equals(" I-101")) {
						//System.out.println("Last ramp is on I-101");
						for (int i = 0; i < I101.ramps.size(); i++) {
							if (I101.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I101.ramps.get(i);
								//System.out.println("Last ramp is " + I101.ramps.get(i).getName());
								break;
							}
						}
					}
					
					else if (lastListSelection[lastListSelection.length - 1].equals(" I-105")) {
//						System.out.println("Last ramp is on I-105");
						for (int i = 0; i < I105.ramps.size(); i++) {
							if (I105.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I105.ramps.get(i);
//								System.out.println("Last ramp is " + I105.ramps.get(i).getName());
								break;
							}
						}
					}
					
	//				else if (lastListSelection[lastListSelection.length - 1].equals(" I-110")) {
	//					System.out.println("Last ramp is on I-110");
	//					for (int i = 0; i < I110.ramps.size(); i++) {
	//						if (I110.ramps.get(i).getName().equals(lastListSelection[0])) {
	//							lastRamp = I110.ramps.get(i);
	//							System.out.println("Last ramp is " + I110.ramps.get(i).getName());
	//							break;
	//						}
	//					}
	//				}
					
					else if (lastListSelection[lastListSelection.length - 1].equals(" I-405")) {
						//System.out.println("Last ramp is on I-405");
						for (int i = 0; i < I405.ramps.size(); i++) {
							if (I405.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I405.ramps.get(i);
								//System.out.println("Last ramp is " + I405.ramps.get(i).getName());
								break;
							}
						}
					}
				}
			
				getDirections();
			}
		});
		
		directions.add(getDirections);
		directions.add(clearDirections);
	
		jmb.add(file);
		jmb.add(graph);
		jmb.add(directions);
		
		setJMenuBar(jmb);
		

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
				if(e.getButton() == MouseEvent.BUTTON1) {
					//map().getAttribution().handleAttribution(e.getPoint(), true);
					Car c = searchClick(map().getPosition(e.getPoint()));
					if(c != null){
						String carInfo = "ID: " + c.getID();
						carInfo += "\nSpeed: " + c.getSpeed();
						carInfo += "\nFreeway: " + c.getFreeway();
						carInfo += "\nRamp: " + c.getRamp();
						carInfo += "\nLatitude: " + c.getxLocation();
						carInfo += "\nLongitude: " + c.getyLocation();
						JOptionPane.showMessageDialog(MapGUI.this,carInfo,"Selected Car",JOptionPane.PLAIN_MESSAGE);
					}
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

	public void exportDataFromDatabaseToCSV() {
		ExecuteCommands.convertToCSV();
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
	
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		
	}

	private class MapPolyLine extends MapPolygonImpl {
		//By Aqua http://stackoverflow.com/users/1048330/aqua
		//From http://stackoverflow.com/questions/20252592/draw-polyline-in-jmapviewer
		 public MapPolyLine(List<? extends ICoordinate> points) {
	            super(null, null, points);
	        }

	        @Override
	        public void paint(Graphics g, List<Point> points) {
	            Graphics2D g2d = (Graphics2D) g.create();
	            g2d.setColor(getColor());
	            g2d.setStroke(getStroke());
	            Path2D path = buildPath(points);
	            g2d.draw(path);
	            g2d.dispose();
	        }

	        private Path2D buildPath(List<Point> points) {
	            Path2D path = new Path2D.Double();
	            if (points != null && points.size() > 0) {
	                Point firstPoint = points.get(0);
	                path.moveTo(firstPoint.getX(), firstPoint.getY());
	                for (Point p : points) {
	                    path.lineTo(p.getX(), p.getY());    
	                }
	            } 
	            return path;
	        }
	}

	public void clearAllDots()
	{
		map().removeAllMapMarkers();
		map().removeAllMapPolygons();
		map().removeAllMapRectangles();
		
	}
}

