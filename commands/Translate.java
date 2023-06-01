package sketchy.commands;
import javafx.geometry.Point2D;
import sketchy.shapes.SketchyShape;

/** This command implementing the Command interface is responsible for handling the
 * shape's changes in location. An instance of it (of declared command type) is created whenever
 * a mouse is released upon change of location in Sketchy class. Since the constructor of this class is called
 * after the change in shape's location that's updated continuously depending on user's mouse drag,
 * the initial position of shape's center is stored as an instance variable in the Sketchy class
 * (later reassigned whenever a shape is pressed on again) and passed into this class's
 * constructor as a parameter.
 * The new one is instantiated in the constructor (the second user's mouse is released)
 * and is also later stored as an instance variable, so that the action can be later both undone and redone.
 * In methods below, center is used as a parameter of location to account for generic coding,
 * and make the code more readable.
 */
public class Translate implements Command {
    private Point2D firstLocation;
    private Point2D secondLocation;
    private SketchyShape isSelected;
    public Translate(Point2D firstLocation, SketchyShape isSelected) {
        this.firstLocation = firstLocation;
        this.isSelected = isSelected;
        this.secondLocation = this.isSelected.getCenter();
        }

    /** Undo method below can be called on instance of any class implementing the command interface
     * depending on it's position in the undoStack. To undo the action, shape's center is set to
     * the one from before the change (one that was passed in as a parameter).
     */
    @Override
    public void undo(){
        this.isSelected.setCenter(firstLocation);
        }

    /** Redo method below can be called on instance of any class implementing the command interface
     * depending on it's position in the redoStack. To redo the action, shape's center is set to
     * the one from after the change (one instantiated in the constructor).
     */
    @Override
    public void redo() {
        this.isSelected.setCenter(secondLocation);
        }
    }


