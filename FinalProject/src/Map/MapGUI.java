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


public class MapGUI extends JFrame implements JMapViewerEventListener
{	
	private JMapViewerTree treeMap = null;

    //private JLabel zoomLabel=null;
    private JLabel zoomValue=null;

    //private JLabel mperpLabelName=null;
    private JLabel mperpLabelValue = null;

	
	public static void main (String [] args)
	{
		MapGUI map = new MapGUI();
		map.addTheOnOffRamps();
	}
	
	private void addTheOnOffRamps() 
	{
		pullJSONUsingName();
		Double xLocation = 0.0;
		Double yLocation = 0.0;
		parseJSONUsingPulledJSON(xLocation, yLocation);
		
	}

	private void drawTheRampOnTheMap(Double xLocation, Double yLocation) 
	{
		map().addMapMarker(new MapMarkerDot(xLocation, yLocation));
		
	}

	private void parseJSONUsingPulledJSON(Double xLocation, Double yLocation) 
	{
		JSONParser parser = new JSONParser();
		try {
			JSONArray arrayFromFile = (JSONArray) parser.parse(new FileReader("JSONs/currentRamp.json"));
			JSONObject ramp = (JSONObject) arrayFromFile.get(0);
			xLocation = Double.parseDouble((String) ramp.get("lat"));
			yLocation = Double.parseDouble((String) ramp.get("lon"));
			System.out.println(xLocation +" " +yLocation);
			drawTheRampOnTheMap(xLocation, yLocation);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		
	}

	private void pullJSONUsingName() 
	{
		URL website;
		try {
			website = new URL(
					"http://nominatim.openstreetmap.org/search/Robertson%20boulevard,%20Culver?format=json&polygon=0&addressdetails=0");
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
		
	}

	public MapGUI()
	{
		super("Map Demo");
		setSize(400,400);
		
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