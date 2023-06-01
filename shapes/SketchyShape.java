package sketchy.shapes;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/** SketchyShape interface implemented by Ellipses and Rectangles - allows the methods of command classes
 * (resize, reshape, lower, etc.) to be called on the shapes polymorphically
 * without having to state the node's type, as classes override
 * these methods accordingly to their own parameters. It also allows the classes to be stored in an
 * arraylist of SketchyShapes which is particularly useful for lowering/raising them visually
 * on the pane and logically in the arraylist depending on their indexes.
 */
public interface SketchyShape extends Savable {
    boolean contains(Point2D point);
    void remove();
    void addToPane();
    void addToPane(int index);
    void select();
    void deselect();
    void setColor(Color color);
    Color getColor();
    void setWidth(double x);
    void setHeight(double y);
    double getWidth();
    double getHeight();
    void setCenter(Point2D center);
    Point2D getCenter();
    void setAngle(double angle);
    double getAngle();
    int getIndex();
}
