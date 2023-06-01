package sketchy.commands;
import sketchy.shapes.SketchyShape;

/** This class implementing the Command interface is responsible for handling the
 * shape's changes in rotation. An instance of it (of declared command type) is created whenever
 * a mouse is released upon change in rotation in the Sketchy class. Since the constructor of this class is called
 * after the change in shape's angle that's updated continuously depending on user's mouse drag,
 * the initial value of shape's angle is stored as an instance variable in the Sketchy class
 * (later reassigned whenever a shape is pressed on again) and passed into this class's
 * constructor as a parameter.
 * The new one is instantiated in the constructor (the second user's mouse is released)
 * and is stored as an instance variable, so that the action can be later both undone and redone.
 */
public class Rotate implements Command {
    private double firstAngle;
    private double secondAngle;
    private SketchyShape isSelected;

    public Rotate(double angle, SketchyShape isSelected) {
        this.isSelected = isSelected;
        this.firstAngle = angle;
        this.secondAngle = this.isSelected.getAngle();
    }

    /** Undo method below can be called on instance of any class implementing the command interface
     * depending on it's position in the undoStack. To undo the action, shape's angle is set to
     * the one from before the change (one that was passed in as a parameter).
     */
    @Override
    public void undo() {
        this.isSelected.setAngle(this.firstAngle);
    }

    /** Redo method below can be called on instance of any class implementing the command interface
     * depending on it's position in the redoStack. To redo the action, shape's rotation is set to
     * the one from after the change (one instantiated in the constructor).
     */
    @Override
    public void redo() {
        this.isSelected.setAngle(this.secondAngle);
    }
}


