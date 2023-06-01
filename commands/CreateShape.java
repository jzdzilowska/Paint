package sketchy.commands;
import javafx.scene.layout.Pane;
import sketchy.shapes.Savable;
import sketchy.shapes.SketchyShape;
import java.util.ArrayList;

/** CrateShape class implements the Command interface and is instantiated in the Sketchy class upon user's
 * mouse press whenever either an ellipse or a rectangle is being created by the user. Since it's declared
 * as a Command type and thus can be added to both the undo- and redo- stacks,
 * it allows for these (undo and redo) methods to be called on it. It takes in an arraylist of shapes,
 * as well as the shape that has been created and the main sketchPane as parameters to set up appropriate
 * association.
 */
public class CreateShape implements Command{
    private SketchyShape shape;
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Savable> elements;
    private Pane pane;

    public CreateShape(SketchyShape shape, Pane pane, ArrayList<SketchyShape> shapes, ArrayList<Savable> elements){
        this.shape = shape;
        this.shapes = shapes;
        this.pane = pane;
        this.elements = elements;
    }

    /** Undo method below can be called  on an instance of any class that implements the command interface
     * depending on its position in the undoStack. To undo creating a shape, recently created SketchyShape
     * is removed from the sketchPane by calling its remove method, and from the arraylist of shapes to
     * account for differences in layering. It's also removed from the elements arraylist - since undoing is
     * possible only on the recently performed action, however, there's no need to take into account any
     * form of layering as no other element can be "on top" of the shape.
     */
    @Override
    public void undo() {
        this.shape.remove();
        this.shapes.remove(this.shape);
        this.elements.remove(this.shape);
    }

    /** Redo method below can also be called on an instance of any class that implements the command interface
     * depending on its position in the redoStack. To redo creating a shape, recently deleted SketchyShape
     * is added to the sketchPane and later to the arraylists of Sketchy's shapes and elements (similarly as in the
     * undo method, redo is possible only on the recently performed action - and since shapes can be crated only
     * on top of other ones, there's no need to take layering into account (and store the shape's index in the
     * elements arraylist).
     */
    @Override
    public void redo() {
        this.shape.addToPane();
        this.shapes.add(this.shape);
        this.elements.add(this.shape);
    }
}
