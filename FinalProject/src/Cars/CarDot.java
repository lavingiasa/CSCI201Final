package Cars;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.Style;

public class CarDot extends MapMarkerCircle{

	public static final int DOT_RADIUS = 5;

    public CarDot(Coordinate coord) {
        this(null, null, coord);
    }
    public CarDot(String name, Coordinate coord) {
        this(null, name, coord);
    }
    public CarDot(Layer layer, Coordinate coord) {
        this(layer, null, coord);
    }
    public CarDot(Layer layer, String name, Coordinate coord) {
        this(layer, name, coord, getDefaultStyle());
    }
    public CarDot(Color color, double lat, double lon) {
        this(null, null, lat, lon);
        setColor(color);
    }
    public CarDot(double lat, double lon) {
        this(null, null, lat, lon);
    }
    public CarDot(Layer layer, double lat, double lon) {
        this(layer, null, lat, lon);
    }
    public CarDot(Layer layer, String name, double lat, double lon) {
        this(layer, name, new Coordinate(lat, lon), getDefaultStyle());
    }
    public CarDot(Layer layer, String name, Coordinate coord, Style style) 
    {
        super(layer, name, coord, DOT_RADIUS, STYLE.FIXED, style);
    }

    public static Style getDefaultStyle()
    {
        return new Style(Color.BLACK, Color.YELLOW, null, getDefaultFont());
    }
    
	@Override
	public void paint(Graphics g, Point position, int radio) 
	{
		//super.paint(arg0, arg1, arg2);
		int size_h = radio;
        int size = size_h * 2;

        if (g instanceof Graphics2D && getBackColor()!=null) {
            Graphics2D g2 = (Graphics2D) g;
            Composite oldComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            //g2.setPaint(getBackColor());
            g2.setPaint(Color.BLUE);
            g.fillOval(position.x - size_h, position.y - size_h, size, size);
            g2.setComposite(oldComposite);
        }
        g.setColor(getColor());
        g.drawOval(position.x - size_h, position.y - size_h, size, size);

        if(getLayer()==null||getLayer().isVisibleTexts()) paintText(g, position);
	}
    
    

}
