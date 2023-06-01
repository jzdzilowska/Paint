package sketchy.shapes;

import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import java.util.ArrayList;

/**
 * This class contained by the top-level sketchy class is a wrapper for Java's polyline. It implements the
 * saveable interfaces, so that it can be "exported"
 * (saved), as the save method iterates through an arraylist of saveables.
 * The class contains a constructor in which the line's appearance is set up - firstly, an initial point
 * (where the mouse was firstly pressed) is passed in as a "starting points" of the line, and upon the drag
 * of the mouse, additional points are continuously added whenever user interacts with the pane. The color
 * of the line depends on a color chosen through the color picker, as that value is also passed in as
 * a parameter into the line's constructor. The line is later added to the "elements" arraylist
 * (an arraylist of all elements visible on the sketchy pane), and to the pane - this way, the amount
 * of methods required to be called from the sketchy class is limited.
 */
public class CurvedLine implements Savable {
    private Polyline line;
    private Point2D point;
    private Pane pane;
    private ArrayList<Savable> elements;
    private Color color;

    public CurvedLine(Point2D point, Pane pane, Color color, ArrayList<Savable> elements) {
        this.point = point;
        this.pane = pane;
        this.elements = elements;
        this.color = color;

        this.line = new Polyline(point.getX(), point.getY());
        this.line.setStroke(color);
        this.pane.getChildren().add(this.line);
        this.elements.add(this);
    }

    /** Method deleting Java's Polyline (node added to a pane) from the main sketchPane */
    public void removeFromPane() {
        this.pane.getChildren().remove(this.line);
    }

    /** Method adding Java's Polyline (node that can be added to a pane) to the main sketchPane */
    public void addToPane() {
        this.pane.getChildren().add(this.line);
    }

    /** Method continuously adding points to the line (thus, to the pane), whenever a mouse is dragged.
     * Line is updated both logically and graphically */
    public void addPoint(Point2D point) {
        this.line.getPoints().addAll(point.getX(), point.getY());
    }

    /** Save method - a CS15FileIO is passed into its constructor, so that whenever the sketchy class
     * iterates through an arraylist of saveables, a particular element can "add itself" and its
     * parameters to the file.
     * After stating its type (CurvedLine), it lists the RGB values of its color, and later returns the
     * number of points (including the starting one) that the polyline has. Because of that, in the load
     * method, the loop can iterate through all these points (knowing their number without throwing an
     * "invalid token" error) and add them to a newly instantiated polyline
     * that's a recreation of the old one. Method below later loops through all these line's points
     * and adds them to the file.
     */
    @Override
    public void save(CS15FileIO io){
        io.writeString("CurvedLine");
        io.writeDouble(this.color.getRed());
        io.writeDouble(this.color.getGreen());
        io.writeDouble(this.color.getBlue());
        io.writeInt(this.line.getPoints().size());
        for (double points: this.line.getPoints()){
            io.writeDouble(points);
        }
    }
}
