package sketchy.shapes;
import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

/** Wrapper class of Java's rectangle - implements the SketchyShape interface, so that commands can be called
 * on it as it's stored in an arraylist of SketchyShapes in the sketchy
 * class. In the constructor, a point of initial mouse point is passed in, sketchyPane (so that the ellipse
 * can "add itself" to the pane, limiting the repetitiveness
 * of methods required to be called from the top-level class), color chosen through the color picker,
 * and arraylists of shapes (rectangles or ellipses) and elements (all visual elements that can be added by the user).
 * Initial width and height is set to 0 and is later resized, depending on the location of the mouse upon a drag.
 */
public class SketchyRectangle implements SketchyShape {
    private Rectangle currRectangle;
    private Pane sketchPane;
    private Point2D clickPoint;
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Savable> elements;


    public SketchyRectangle(Pane sketchyPane, Point2D point, Color color, ArrayList<SketchyShape> shapes, ArrayList<Savable> elements) {
        this.sketchPane = sketchyPane;
        this.shapes = shapes;
        this.elements = elements;
        this.clickPoint = point;

        this.currRectangle = new Rectangle(this.clickPoint.getX(), this.clickPoint.getY(), 0, 0);
        this.currRectangle.setFill(color);
        this.shapes.add(this);
        this.elements.add(this);
        this.sketchPane.getChildren().add(this.currRectangle);
    }

    /** Method below checks whether the rectangle contains a specified point passed in as a parameter
     * (depending on the MouseEvent);
     * useful for Sketchy's Select method, where it's able to detect a shape in the pane depending on whether
     * it contains a point of user's click, or not, and if yes, reassign the isSelected instance variable
     * to the shape that's been chosen.
     */
    @Override
    public boolean contains(Point2D point) {
        if (this.currRectangle.contains(point.getX(), point.getY())) {
            return true;
        } return false;
    }

    /** As previously, method below is called whenever a user selects a shape - it doesn't change anything
     * logically (a particular shape gets the "isSelected" instance variable through Sketchy's Select method),
     * but does so visually - a 2px-width stroke is set to a pink color, so that it's clear to the
     * user what shape they selected.
     */
    @Override
    public void select() {
        this.currRectangle.setStroke(Color.LIGHTPINK);
        this.currRectangle.setStrokeWidth(2);
    }

    /** Changes the visual aspects of a shape (sets its stroke to 0) so that it informs the user about the
     * shape not being selected anymore
     */
    @Override
    public void deselect() {
        this.currRectangle.setStrokeWidth(0);
    }

    /** Remove method that deletes a Java's rectangle that's contained in the pane.
     * It can be called from the Sketchy class on any SketchyShape interface
     * that's currently selected by the user, and, upon a button click,
     * it's deleted graphically (removed from the Pane) through the method below.
     * Logical removal an instance of a SketchyRectangle (from arraylists to which it belongs) is done through the
     * Sketchy's delete method (I preferred to not prompt a class to "delete itself" through one of
     * its methods).
     * */
    @Override
    public void remove() {
        this.sketchPane.getChildren().remove(this.currRectangle);
    }

    /** Methods below adds Java's rectangle node to the main sketchPane to limit the exposure
     * of the wrapped object to other classes. Since certain methods
     * (especially those related to lowering/raising object on a pane)
     * require passing in object's index as a parameter and others
     * don't, two methods are specified - one taking in the index as a parameter, and the other
     * one not.
     */
    @Override
    public void addToPane() {
        this.sketchPane.getChildren().add(this.currRectangle);
    }

    @Override
    public void addToPane(int index) {
        this.sketchPane.getChildren().add(index, this.currRectangle);
    }

    /** Setters and getters below either change, or store the value of the color of a rectangle.
     * They're useful for the "undo" and "redo" methods in the ChangeColor
     * command class, where both the initial and final colors are stored as instance variables.
     */
    @Override
    public void setColor(Color color) {
        this.currRectangle.setFill(color);
    }

    @Override
    public Color getColor() {
        return (Color) this.currRectangle.getFill();
    }

    /** Methods below, similarly to setters and getters related to shape's color above, are responsible for
     * preserving or changing the values of rectangle's width and height. Useful for calling methods of
     * the Resize command class (undo/redo), as well as loading a shape, or
     * resizing it from the top-level Sketchy class both when initially creating, or changing a shape.
     * In case of a rectangle, its width is multiplied by 2, as the method is universal for
     * both rectangles and ellipses ("width" of which is their radius); the width and height to
     * which the shape is set in Sketchy's resize method is the distance between shape's center and location of
     * a mouse - half of the rectangle's actual size.
     */
    @Override
    public void setWidth(double x) {
        this.currRectangle.setWidth(x*2);
    }

    public void setHeight(double y) {
        this.currRectangle.setHeight(y*2);
    }

    /** In case of rectangles, accessor method below return half the value of Java's rectangle's
     * actual size to correspond to the way of setting SketchyShape's size and make up for the difference
     * between ellipses and rectangles (polymorphism! :))
     */
    @Override
    public double getWidth() {
        return (0.5*this.currRectangle.getWidth());
    }

    public double getHeight() {
        return (0.5*this.currRectangle.getHeight());
    }

    /** Methods below access and return rectangle's center - useful for translating the
     * SketchyShape or rotating it, by comparing the angle between current mouse point (at the moment of
     * drag) and shape's center.
     * Since these methods are used polymorphically on all SketchyShapes (both ellipses and rectangles,
     * locations of which are being calculated differently), I've decided to use shape's center rather than
     * its location. Center of a rectangle is calculated by adding half of its width to its current location
     * (top-left corner). The width is divided by two to make up for the multiplication in setWidth,
     * setHeight methods.
     * @return
     */
    @Override
    public Point2D getCenter() {
        double x = this.currRectangle.getX() + (this.currRectangle.getWidth()*0.5);
        double y = this.currRectangle.getY() + (this.currRectangle.getHeight()*0.5);
        Point2D point = new Point2D(x, y);
        return point;
    }

    @Override
    public void setCenter(Point2D center){
        this.currRectangle.setX(center.getX() - (this.currRectangle.getWidth()*0.5));
        this.currRectangle.setY(center.getY() - (this.currRectangle.getHeight() *0.5));
    }

    /** Methods below set or return the value of shape's angle - used for setting the angle through the Sketchy
     * class' rotate method taking in a current mouse point as a parameter, the undo/redo methods of
     * the Rotate command class, or loading the object.
     */
    @Override
    public void setAngle(double angle) {
        this.currRectangle.setRotate(angle);
    }

    @Override
    public double getAngle() {
        return this.currRectangle.getRotate();
    }

    /** Method below returns the wrapped rectangle's index in the pane's list of children - useful
     * particularly for the lower/raise command classes
     */
    @Override
    public int getIndex() {
        return this.sketchPane.getChildren().indexOf(this.currRectangle);
    }

    /** Save method - a CS15FileIO is passed into the constructor, so that whenever the sketchy class
     * iterates through an arraylist of saveables, a particular element can "add itself" and its
     * parameters to the file. After stating its type, it lists its location, radius,
     * angle of rotation and values of its color so that
     * the shape can be recreated upon loading.
     */
    @Override
    public void save(CS15FileIO io) {
        io.writeString("Rectangle");
        io.writeDouble(this.currRectangle.getX());
        io.writeDouble(this.currRectangle.getY());
        io.writeDouble(this.getWidth());
        io.writeDouble(this.getHeight());
        io.writeDouble(this.getAngle());
        io.writeDouble(this.getColor().getRed());
        io.writeDouble(this.getColor().getGreen());
        io.writeDouble(this.getColor().getBlue());
    }
}
