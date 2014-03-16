package Map;

import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import Map.MapPanel;

public class MapGUI extends JApplet 
{	
	public MapGUI()
	{
		MapPanel.Gui gui = new MapPanel.Gui();
        JMenuBar menuBar = gui.createMenuBar();
        setJMenuBar(menuBar);
        getContentPane().add(gui);
		//this.setSize(800, 800);
		//getContentPane().setSize(800, 800);
	}
	
}
