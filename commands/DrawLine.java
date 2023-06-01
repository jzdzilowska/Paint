package sketchy.commands;
import javafx.scene.layout.Pane;
import sketchy.shapes.CurvedLine;
import sketchy.shapes.Savable;

import java.util.ArrayList;

/** DrawLine class implements the Command interface and is instantiated in the Sketchy class upon user's
 * mouse press whenever a line is being created by the user ("Draw with Pen" radio button is selected).
 * Since it's declared as a Command type and thus can be added to both the undo- and redo- stacks,
 * it allows for the undo and redo methods to be called on it. It takes in a recently created line,
 * the main sketchPane, and an arraylist of all "savable" elements as parameters to set up an association.
 */
public class DrawLine implements Command{
    private CurvedLine line;
    private Pane pane;
    private ArrayList<Savable> elements;

    public DrawLine(CurvedLine line, Pane pane, ArrayList<Savable> elements){
        this.line = line;
        this.pane = pane;
        this.elements = elements;
    }

    /** To undo having created a line, the Java's polyline is removed graphically from the pane
     * and logically from the arraylist of all "savable" elements.
     */
    @Override
    public void undo() {
        this.line.removeFromPane();
        this.elements.remove(this.line);
    }

    /**To redo having created a line, Java's polyline is added once again to the pane and
     * to the elements arraylist. Since the
     * undo method can only be called whenever there are no other objects on top of the line (changing
     * the layers puts these raised elements "on top" of the stack), the redo method can only be called
     * under the same conditions - thus, there's no need to take into account any form of layering.
     */
    @Override
    public void redo() {
        this.line.addToPane();
        this.elements.add(this.line);
    }
}
