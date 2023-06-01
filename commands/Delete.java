package sketchy.commands;
import javafx.scene.layout.Pane;
import sketchy.shapes.Savable;
import sketchy.shapes.SketchyShape;
import java.util.ArrayList;

/** This command implementing the Command interface is responsible for handling the undo/redo aspect of
 * shape's deletion. An instance of it (of declared command type) is created whenever
 * a delete method is called in the Sketchy class. Since the constructor of this class is called
 * before the deletion of a shape, all of its initial parameters
 * are stored as instance variables after being instantiated in the constructor to allow for
 * shape's recreation and the preservation of layers (both logically and graphically).
 */
public class Delete implements Command{
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Savable> elements;
    private int shapesArrayIndex;
    private int elementsArrayIndex;
    private int paneIndex;
    private Pane pane;
    private SketchyShape shape;

    public Delete(SketchyShape shape, Pane pane, ArrayList<SketchyShape> shapes, ArrayList<Savable> elements){
        this.shapes = shapes;
        this.elements = elements;
        this.pane = pane;
        this.shape = shape;
        this.shapesArrayIndex = this.shapes.indexOf(this.shape);
        this.elementsArrayIndex = this.elements.indexOf(this.shape);
        this.paneIndex = this.shape.getIndex();
    }

    /** Undo method below can be called on an instance of any class implementing the command interface depending
     * on their position in the undoStack. In case of shape deletion, undoing an action is based on
     * firstly adding it to the pane (visually), and then restoring its logical layering in the
     * arraylist of shapes and elements. To prevent any additional association from being set up,
     * I've decided to use a getter method for an object wrapped by a particular class, as only a node
     * can be added to Java's pane.
     */
    @Override
    public void undo() {
        this.shape.addToPane(this.paneIndex);
        this.shapes.add(this.shapesArrayIndex, this.shape);
        this.elements.add(this.elementsArrayIndex, this.shape);
    }

    /** To redo having deleted a shape, it's once again removed from both the pane, and from the
     * arraylists to which it belonged.
     */
    @Override
    public void redo() {
        this.shape.remove();
        this.shapes.remove(this.shape);
        this.elements.remove(this.shape);
    }
}
