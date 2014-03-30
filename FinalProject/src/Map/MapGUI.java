package Map;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;


public class MapGUI extends JFrame implements JMapViewerEventListener
{	
	private JMapViewerTree treeMap = null;

    //private JLabel zoomLabel=null;
    private JLabel zoomValue=null;

    //private JLabel mperpLabelName=null;
    private JLabel mperpLabelValue = null;

	
	public static void main (String [] args)
	{
		new MapGUI();
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