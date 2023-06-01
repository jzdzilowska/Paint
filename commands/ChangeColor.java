package sketchy.commands;
import javafx.scene.paint.Color;
import sketchy.shapes.SketchyShape;

/** This command implementing the Command interface is responsible for handling the
 * shape's changes in color. An instance of it (of declared command type) is created whenever
 * a changeColor method is called in the Sketchy class. Since the constructor of this class is called
 * before the change in shape's fill, the initial color is stored as an instance variable after being instantiated in
 * the constructor. The new one is passed in into this class's constructor as a parameter
 * corresponding to the value passed into Sketchy's method called from the control class,
 * and is also later stored as an instance variable, so that the action can be later both undone and redone.
 */
public class ChangeColor implements Command {
    private SketchyShape shape;
    private Color initialColor;
    private Color newColor;

    public ChangeColor(SketchyShape shape, Color color) {
        this.shape = shape;
        this.initialColor = shape.getColor();
        this.newColor = color;
    }

    /** Undo method below can be called polymorphically on instance of any class implementing the command interface
     * depending on it's position in the undoStack. To undo the action, shape's color is set to
     * the one from before the change (one that's instantiated in the constructor, as the ChangeColor
     * command class is created before the change).
     */
    @Override
    public void undo() {
        this.shape.setColor(this.initialColor);
    }

    /** Redo method below can be called polymorphically on instance of any class implementing the command interface
     * depending on it's position in the redoStack. To redo the action, shape's color is set to
     * the one from after the change (one passed into the sketchy changeColor method's parameter - and thus, to
     * changeColor command class's constructor).
     */
    @Override
    public void redo() {
        this.shape.setColor(this.newColor);
    }
}