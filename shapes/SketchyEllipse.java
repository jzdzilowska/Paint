package sketchy.shapes;
import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import java.util.ArrayList;

/** Wrapper class of Java's ellipse - it implements the SketchyShape interface, so that commands (resizing,
 * translating, etc.) can be called on it as it's stored in an arraylist of SketchyShapes in the top-level sketchy
 * class. In the constructor, a point of initial mouse point is passed in (that's the initial location - thus, the
 * center - of the shape), sketchyPane (so that the ellipse can "add itself" to the pane, limiting the repetitiveness
 * of methods required to be called from the top-level class), color chosen through the color picker,
 * and arraylists of shapes (rectangles or ellipses) and elements (all visual elements that can be added by the user).
 * Initial radius is set to 0 and is later resized, depending on the location of the mouse upon a drag.
 */
public class SketchyEllipse implements SketchyShape {
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Savable> elements;
    private Ellipse currEllipse;
    private Pane sketchPane;
    private Point2D point;

    public SketchyEllipse(Pane sketchyPane, Point2D point, Color color, ArrayList<SketchyShape> shapes, ArrayList<Savable> elements) {
        this.sketchPane = sketchyPane;
        this.shapes = shapes;
        this.elements = elements;
        this.point = point;

        this.currEllipse = new Ellipse(this.point.getX(), this.point.getY(), 0, 0);
        this.currEllipse.setFill(color);
        this.shapes.add(this);
        this.elements.add(this);
        this.sketchPane.getChildren().add(this.currEllipse);
    }

    /** Method below checks whether the ellipse contains a specified point passed in as a parameter;
     * useful for Sketchy's Select method, where it's able to detect a shape in the pane depending on whether
     * it contains a point of user's click, or not, and if yes, reassign the isSelected instance variable
     * to the shape that's been chosen.
     */
    @Override
    public boolean contains(Point2D point) {
        if (this.currEllipse.contains(point.getX(), point.getY())) {
            return true;
        } return false;
    }

    /** As previously, method below is called whenever a user selects a shape - it doesn't change anything
     * logically (a particular shape gets the "isSelected" instance variable through Sketchy's Select method),
     * but does so visually - a 2px-width stroke is set to a pretty pink color, so that it's clear to the
     * user what shape they selected.
     */
    @Override
    public void select() {
        this.currEllipse.setStroke(Color.LIGHTPINK);
        this.currEllipse.setStrokeWidth(2);
    }

    /** Informs the user about the shape not being selected by setting the width of the stroke to 0 - similarly
     * to the previous method, it doesn't change anything logically, but does so visually.
     */
    @Override
    public void deselect() {
        this.currEllipse.setStrokeWidth(0);
    }

    /** Remove method that deletes a Java's node that's being wrapped by a class.
     * It can be called polymorphically from the Sketchy class on any shape implementing the SketchyShape interface
     * that's currently selected by the user, and, upon a button click,
     * it's deleted graphically (removed from the Pane) through the method below.
     * Logical removal an instance of a SketchyEllipse (from arraylists) is done through the
     * Sketchy's delete method (I preferred to not prompt a class to "delete itself" through one of
     * its methods).
     */
    @Override
    public void remove() {
        this.sketchPane.getChildren().remove(this.currEllipse);
    }


    /** Methods below adds Java's ellipse node to the main sketchPane to limit the exposure
     * of the wrapped object to other classes. Since in some instances passing in
     * object's index as a parameter is required, and in others
     * not, two methods are specified - one taking in the index as a parameter, and the other
     * one not.
     */
    @Override
    public void addToPane() {
        this.sketchPane.getChildren().add(this.currEllipse);
    }

    @Override
    public void addToPane(int index) {
        this.sketchPane.getChildren().add(index, this.currEllipse);
    }

    /** Setters and getters below either change, or store the value of the color of an ellipse.
     * They're useful especially for the "undo" and "redo" methods in the ChangeColor
     * command class, where both the initial and final colors are stored as instance variables.
     */
    @Override
    public void setColor(Color color) {
        this.currEllipse.setFill(color);
    }

    @Override
    public Color getColor() {
        return (Color) this.currEllipse.getFill();
    }

    /** Methods below, similarly to setters and getters related to shape's color above, are responsible for
     * preserving or changing the values of shape's width and height. Useful for calling methods of
     * the Resize command class (undo/redo), as well as loading a shape, or
     * resizing it from the top-level Sketchy class both when initially creating, or changing a shape.
     */
    @Override
    public void setWidth(double radiusX) {
        this.currEllipse.setRadiusX(radiusX);
    }

    @Override
    public void setHeight(double radiusY) {
        this.currEllipse.setRadiusY(radiusY);
    }

    @Override
    public double getWidth() {
        return this.currEllipse.getRadiusX();
    }
    @Override
    public double getHeight() {
        return this.currEllipse.getRadiusY();
    }

    /** Methods below access and return ellipse's center - useful for translating the
     * SketchyShape or rotating it, by comparing the angle between current mouse point (at the moment of
     * drag) and shape's center.
     */
    @Override
    public void setCenter(Point2D center){
        this.currEllipse.setCenterX(center.getX());
        this.currEllipse.setCenterY(center.getY());
    }

    @Override
    public Point2D getCenter() {
        Point2D center = new Point2D(this.currEllipse.getCenterX(), this.currEllipse.getCenterY());
        return center;
    }

    /** Methods below set or return the value of shape's angle - used for setting the angle through the Sketchy
     * class' rotate method taking in a current mouse point as a parameter, the undo/redo methods of
     * the Rotate command class, or loading the object.
     */
    @Override
    public void setAngle(double angle) {
        this.currEllipse.setRotate(angle);
    }

    @Override
    public double getAngle() {
        return this.currEllipse.getRotate();
    }

    /** Method below returns the wrapped ellipse's index in the pane's list of children - useful
     * particularly for the lower/raise command classes
     */
    @Override
    public int getIndex() {
        return this.sketchPane.getChildren().indexOf(this.currEllipse);
    }

    /** Save method - a CS15FileIO is passed into the constructor, so that whenever the sketchy class
     * iterates through an arraylist of saveables, a particular element can "add itself" and its
     * parameters to the file.
     * After stating its type, it lists its center, radius, angle of rotation and values of its color casted
     * onto an integer (as it's initially a double which doesn't correspond to Java's paint value) so that
     * the shape can be recreated upon loading.
     */
    @Override
    public void save(CS15FileIO io) {
        io.writeString("Ellipse");
        io.writeDouble(this.getCenter().getX());
        io.writeDouble(this.getCenter().getY());
        io.writeDouble(this.currEllipse.getRadiusX());
        io.writeDouble(this.currEllipse.getRadiusY());
        io.writeDouble(this.getAngle());
        io.writeDouble(this.getColor().getRed());
        io.writeDouble(this.getColor().getGreen());
        io.writeDouble(this.getColor().getBlue());
    }
}
