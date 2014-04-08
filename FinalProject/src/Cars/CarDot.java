package Cars;

import java.awt.Color;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
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

}
