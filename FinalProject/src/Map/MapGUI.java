package Map;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;

import Cars.Car;
import JSON.JSONsParser;


public class MapGUI extends JFrame implements JMapViewerEventListener
{	
	private JMapViewerTree treeMap = null;

    //private JLabel zoomLabel=null;
    private JLabel zoomValue=null;

    //private JLabel mperpLabelName=null;
    private JLabel mperpLabelValue = null;
    
    private Vector<Car> cars = new Vector<Car>();
    private Vector<String> ramps = new Vector<String>();
    JSONsParser parser = null;

	
	public static void main (String [] args)
	{
		MapGUI map = new MapGUI();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		map.addTheOnOffRamps();
	}
	
	private void addTheOnOffRamps() 
	{
		for(int i = 0; i < cars.size(); i++)
		{
			pullJSONUsingName(cars.get(i).getRamp());
			parseJSONUsingPulledJSON();
		}
		
	}

	private void drawTheRampOnTheMap(Double xLocation, Double yLocation) 
	{
		map().addMapMarker(new MapMarkerDot(xLocation, yLocation));
		
	}

	private void parseJSONUsingPulledJSON() 
	{
		JSONParser parser = new JSONParser();
		try {
			Object obj=parser.parse(new FileReader("JSONs/currentRamp.json"));
			JSONArray array=(JSONArray)obj;
			System.out.println(array.get(0));
//			//JSONObject objectFromFile = (JSONObject) parser.parse(new FileReader("JSONs/currentRamp.json"));
//			//if(!arrayFromFile.isEmpty())
//			{
//				JSONObject ramp = (JSONObject) objectFromFile.get(0);
//				System.out.println(ramp);
//				//Double xLocation = Double.parseDouble((String) ramp.get("lat"));
//				//Double yLocation = Double.parseDouble((String) ramp.get("lon"));
//				//System.out.println(xLocation +" " +yLocation);
//				//drawTheRampOnTheMap(xLocation, yLocation);
//		//	}else{
//				System.out.println("I could not find the spot for a ramp :(");
//			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

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
		
		URLOfRamp = URLOfRamp.concat("&sensor=false");
		return URLOfRamp; 
		
	}

	public MapGUI()
	{
		super("Map Demo");
		setSize(400,400);
		
		parser = new JSONsParser(cars);
		parser.start();
		
		treeMap = new JMapViewerTree("Zones");
		map().addJMVListener(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    
	    map().setTileSource((TileSource) new MapQuestOsmTileSource());
	
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