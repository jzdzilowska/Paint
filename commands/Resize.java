package sketchy.commands;
import sketchy.shapes.SketchyShape;
import javafx.geometry.Point2D;

/** This class implements the Command interface and is responsible for handling the
 * shape's changes in size. An instance of it is created whenever
 * a mouse is released upon change of shape's dimensions in Sketchy class.
 * Since the constructor of this class is called after the change in shape's dimensions that are updated
 * continuously depending on user's mouse drag,
 * the initial position of shape's size (both width and height) are stored as instance variables in the Sketchy class
 * (later reassigned whenever a shape is pressed on again) and are passed into this class's
 * constructor as parameters. The new one are instantiated in the constructor (the second user's mouse
 * is released) and are also stored as an instance variables, so that the action can be
 * later both undone and redone as the methods have continuous access to these values.
 * Both setWidth and setHeight methods are called identically (without taking into consideration
 * what type of shape has been resized) as shapes specify their methods of calculating, or setting the
 * dimensions for the objects they wrap on their own.
 * Additionally, to make up for the difference in location (as upon resize, the location of
 * a rectangle changes as its calculated based on its top-left corner - upon undoing, it
 * becomes the rectangle's previous center), shape's center
 * (to allow for generic coding - both for ellipses and rectangles) is stored similarily to
 * their dimensions and later used in both methods.
 */
public class Resize implements Command {
    private double firstWidth;
    private double firstHeight;
    private double secondWidth;
    private double secondHeight;
    private Point2D firstLocation;
    private Point2D secondLocation;
    private SketchyShape isSelected;

    public Resize(double firstWidth, double firstHeight, Point2D firstLocation, SketchyShape isSelected) {
        this.isSelected = isSelected;
        this.firstWidth = firstWidth;
        this.firstHeight = firstHeight;
        this.firstLocation = firstLocation;
        this.secondLocation = this.isSelected.getCenter();
        this.secondWidth = this.isSelected.getWidth();
        this.secondHeight = this.isSelected.getHeight();
    }

    /** Undo method below sets the width and height of a recently transformed shape to values
     * from before the change, that have been initially passed in as parameters and later stored
     * as instance variables. The center is also changed - although it doesn't change anything for ellipses,
     * it makes up for the difference in location of rectangles.
     */
    @Override
    public void undo() {
        this.isSelected.setWidth(this.firstWidth);
        this.isSelected.setHeight(this.firstHeight);
        this.isSelected.setCenter(this.firstLocation);
    }

    /** Redo method below sets the width and height of a recently un-transformed shape to values
     * from after the change, that have been firstly instantiated in this class's constructor.
     * Similarly to the undo method, the center is also changed.
     */
    @Override
    public void redo() {
        this.isSelected.setWidth(this.secondWidth);
        this.isSelected.setHeight(this.secondHeight);
        this.isSelected.setCenter(this.secondLocation);
    }
}


