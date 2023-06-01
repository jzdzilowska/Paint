package sketchy.commands;
import javafx.scene.layout.Pane;
import sketchy.shapes.Savable;
import sketchy.shapes.SketchyShape;
import java.util.ArrayList;

/** This class implementing the Command interface is responsible undoing and redoing
 * SketchyShape's raising of layers. An instance of it is created whenever
 * the raise method is called in the Sketchy class. Since the constructor of this class is called
 * after the raising of shape's layer, it takes in the old values stored in the method
 * of the Sketchy class as parameters, and stores the new value of shape's index in the shape's
 * arraylist (which is the only one in which changes vary) as instance variable after having
 * instantiated it in the constructor to allow for the preservation of layers (both logically and graphically).
 * */
public class Raise implements Command {
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Savable> elements;
    private Pane sketchPane;
    private SketchyShape isSelected;
    private int paneIndexBefore;
    private int elementsIndexBefore;
    private int shapesIndexBefore;
    private int shapesIndexAfter;


    public Raise(int paneIndexBefore, int shapesIndexBefore, int elementsIndexBefore, ArrayList<SketchyShape> shapes, ArrayList<Savable> elements, Pane sketchPane, SketchyShape isSelected) {
        this.shapes = shapes;
        this.elements = elements;
        this.sketchPane = sketchPane;
        this.isSelected = isSelected;
        this.paneIndexBefore = paneIndexBefore;
        this.shapesIndexBefore = shapesIndexBefore;
        this.elementsIndexBefore = elementsIndexBefore;
        this.shapesIndexAfter = this.shapes.indexOf(this.isSelected);
    }

    /** Undo method below can be called on an instance of any class implementing the command interface depending
     * on their position in the undoStack. In case of raising of shape's layer, undoing an action is based on
     * lowering it - firstly removing it from the pane, later adding it once again to the pane
     * with an index that has been originally passed into the class's constructor,
     * and then recreating this action with both the shapes and
     * elements arraylists.
     */
    @Override
    public void undo() {
        this.isSelected.remove();
        this.isSelected.addToPane(this.paneIndexBefore);
        this.shapes.remove(this.isSelected);
        this.elements.remove(this.isSelected);
        this.shapes.add(this.shapesIndexBefore, this.isSelected);
        this.elements.add(this.elementsIndexBefore, this.isSelected);
        }

    /** The redo method below can be called on an instance of any class implementing the command interface depending
     * on their position in the redoStack. In case of raising of shape's layer, redoing an action is based on
     * raising it again by restoring its parameters from when the Raise class was initially instantiated (by using
     * the values one greater than those passed into this class's constructor for the pane and
     * an arraylist of elements, and, for the shapes arraylist in which changes of index can vary,
     * newly calculated ones firstly instantiated in
     * the constructor.
     */
    @Override
    public void redo() {
        this.isSelected.remove();
        this.isSelected.addToPane(this.paneIndexBefore +1);
        this.shapes.remove(this.isSelected);
        this.elements.remove(this.isSelected);
        this.shapes.add(this.shapesIndexAfter, this.isSelected);
        this.elements.add(this.elementsIndexBefore +1, this.isSelected);
        }
    }