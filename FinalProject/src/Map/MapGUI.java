package Map;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BoxLayout;
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
import javax.swing.SwingUtilities;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import Freeways.Freeway;
import Freeways.Interstate10;
import Freeways.Interstate101;
import Freeways.Interstate105;
import Freeways.Interstate110;
import Freeways.Interstate405;
import Freeways.Ramp;
import Freeways.RampDot;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

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
	
	private double[] routePoints;
	
	private final String MAPQUEST_API_KEY = "Fmjtd%7Cluur2qu829%2Cal%3Do5-9aaldu";
	
	private JPanel directionsPanel;
	private JPanel startPanel;
	private JPanel endPanel;
	private JPanel lastPanel;
	
	private int response;
	
	private JOptionPane directionsDialog;
	
	private boolean hasThreeDestinations = false;
	
	
	JSONsParser parser = null;

	
	
	public static void main (String [] args)
	{
		MapGUI map = new MapGUI();
		currentMap = map;
		map.addFreewayPoints();
		
		
		map.parser = new JSONsParser(map.cars);
		map.parser.start();
		
		try {
			Thread.sleep(5000);	// .5 seconds
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
				//drawCarOnThe10(cars.get(i));
				cars.get(i).setWaypoints(I10.waypoints);
				cars.get(i).setRamps(I10.ramps);
				break;
			case 101:
				//drawCarOnThe101(cars.get(i));
				cars.get(i).setWaypoints(I101.waypoints);
				cars.get(i).setRamps(I101.ramps);
				break;
			case 105:
				//drawCarOnThe105(cars.get(i));
				cars.get(i).setWaypoints(I105.waypoints);
				cars.get(i).setRamps(I105.ramps);
				break;
			case 405:
				cars.get(i).setWaypoints(I405.waypoints);
				cars.get(i).setRamps(I405.ramps);
				//drawCarOnThe405(cars.get(i));
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
			System.out.println(cars.get(i).getFreeway());
			if (hasThreeDestinations) {
				if (cars.get(i).getFreeway().equals(freeway1) || cars.get(i).getFreeway().equals(freeway2) || cars.get(i).getFreeway().equals(freeway3)) {
					numCars += 1;
					averageSpeed += cars.get(i).getSpeed();
					System.out.println(cars.get(i).getSpeed());
				}
			}
			else {
				if (cars.get(i).getFreeway().equals(freeway1) || cars.get(i).getFreeway().equals(freeway2)) {
					numCars += 1;
					averageSpeed += cars.get(i).getSpeed();
					System.out.println(cars.get(i).getSpeed());
				}
			}
		}
		
		averageSpeed = averageSpeed/((double) numCars);
		System.out.println("Average Speed: " + averageSpeed);
		
		double projectedTime = routeDistance/averageSpeed;
		
		JOptionPane.showMessageDialog(currentMap, "Route Distance: " + routeDistance + " miles \n" + "Projected Time: " + projectedTime + " minutes");
	}

	private void parseDirectionData() 
	{
		File XMLFile;
		Document XMLTree;
		SAXReader reader;
		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		List<ArrayList<Coordinate>> routeListList = new ArrayList<ArrayList<Coordinate>>();
		
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
				System.out.println(latitude + "," + longitude);
				coordinateList.add(new Coordinate(latitude, longitude));
			}
			
			MapPolyLine route = new MapPolyLine(coordinateList);
	
			map().addMapPolygon(route);
			map().setMapPolygonsVisible(true);

			for (int i = 0; i < distanceNodes.size(); i++) {
				System.out.println("distance: " + distanceNodes.get(i).getText().toString());
				routeDistance += Double.parseDouble(distanceNodes.get(i).getText().toString());
			}

		} catch (DocumentException e) {
			System.out.println("Your XML File is invalid");
			return;
		}
	}
	
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
		graph.add(openGraph);
		
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
					System.out.println("Start ramp is on I-10");
					for (int i = 0; i < I10.ramps.size(); i++) {
						if (I10.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I10.ramps.get(i);
							System.out.println("Start ramp is " + I10.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (startListSelection[startListSelection.length - 1].equals(" I-101")) {
					System.out.println("Start ramp is on I-101");
					for (int i = 0; i < I101.ramps.size(); i++) {
						if (I101.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I101.ramps.get(i);
							System.out.println("Start ramp is " + I101.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (startListSelection[startListSelection.length - 1].equals(" I-105")) {
					System.out.println("Start ramp is on I-105");
					for (int i = 0; i < I105.ramps.size(); i++) {
						if (I105.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I105.ramps.get(i);
							System.out.println("Start ramp is " + I105.ramps.get(i).getName());
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
					System.out.println("Start ramp is on I-405");
					for (int i = 0; i < I405.ramps.size(); i++) {
						if (I405.ramps.get(i).getName().equals(startListSelection[0])) {
							startRamp = I405.ramps.get(i);
							System.out.println("Start ramp is " + I405.ramps.get(i).getName());
							break;
						}
					}
				}
				
				String[] endListSelection = endList.getSelectedItem().toString().split("--");
				
				if (endListSelection[endListSelection.length - 1].equals(" I-10")) {
					System.out.println("End ramp is on I-10");
					for (int i = 0; i < I10.ramps.size(); i++) {
						if (I10.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I10.ramps.get(i);
							System.out.println("End ramp is " + I10.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (endListSelection[endListSelection.length - 1].equals(" I-101")) {
					System.out.println("End ramp is on I-101");
					for (int i = 0; i < I101.ramps.size(); i++) {
						if (I101.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I101.ramps.get(i);
							System.out.println("End ramp is " + I101.ramps.get(i).getName());
							break;
						}
					}
				}
				
				else if (endListSelection[endListSelection.length - 1].equals(" I-105")) {
					System.out.println("End ramp is on I-105");
					for (int i = 0; i < I105.ramps.size(); i++) {
						if (I105.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I105.ramps.get(i);
							System.out.println("End ramp is " + I105.ramps.get(i).getName());
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
					System.out.println("End ramp is on I-405");
					for (int i = 0; i < I405.ramps.size(); i++) {
						if (I405.ramps.get(i).getName().equals(endListSelection[0])) {
							endRamp = I405.ramps.get(i);
							System.out.println("End ramp is " + I405.ramps.get(i).getName());
							break;
						}
					}
				}
				
				if (hasThreeDestinations) {
				
					String[] lastListSelection = lastList.getSelectedItem().toString().split("--");
					
					if (lastListSelection[lastListSelection.length - 1].equals(" I-10")) {
						System.out.println("Last ramp is on I-10");
						for (int i = 0; i < I10.ramps.size(); i++) {
							if (I10.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I10.ramps.get(i);
								System.out.println("Last ramp is " + I10.ramps.get(i).getName());
								break;
							}
						}
					}
					
					else if (lastListSelection[lastListSelection.length - 1].equals(" I-101")) {
						System.out.println("Last ramp is on I-101");
						for (int i = 0; i < I101.ramps.size(); i++) {
							if (I101.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I101.ramps.get(i);
								System.out.println("Last ramp is " + I101.ramps.get(i).getName());
								break;
							}
						}
					}
					
					else if (lastListSelection[lastListSelection.length - 1].equals(" I-105")) {
						System.out.println("Last ramp is on I-105");
						for (int i = 0; i < I105.ramps.size(); i++) {
							if (I105.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I105.ramps.get(i);
								System.out.println("Last ramp is " + I105.ramps.get(i).getName());
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
						System.out.println("Last ramp is on I-405");
						for (int i = 0; i < I405.ramps.size(); i++) {
							if (I405.ramps.get(i).getName().equals(lastListSelection[0])) {
								lastRamp = I405.ramps.get(i);
								System.out.println("Last ramp is " + I405.ramps.get(i).getName());
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
}

