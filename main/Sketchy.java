package sketchy.main;
import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Stack;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import sketchy.commands.*;
import sketchy.shapes.*;
import javafx.scene.paint.Color;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Math.*;

/**
 * This top-level logic class handles the overall
 * logic behind Sketchy. The class is contained by the PaneOrganizer class and
 * is associated with the main sketchPane so that it can call methods on it
 * (such as add instances of Rectangles, Ellipses, or CurvedLines to it).
 * In the constructor, instance variables are initialized (in particular mouse's location that's
 * updated continously, "starting point" necessary for handling the change in location
 * of shapes, as well as initial parameters of shapes before any action is performed on them, so
 * that any interaction with the canvas can be later undone - or redone - by the user),
 * and association is set up with the main sketchPane - all methods are explained in-detail below :)
 */

public class Sketchy {
    private Stage stage;
    private Pane sketchPane;
    private Point2D mouseLocation;
    private Point2D translateStartingPoint;
    private CurvedLine line;
    private SketchyRectangle sketchyRectangle;
    private SketchyEllipse sketchyEllipse;
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Savable> elements;
    private SketchyShape isSelected;
    private Point2D firstLocation;
    private double firstAngle;
    private double firstWidth;
    private double firstHeight;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public Sketchy(Pane pane, Stage stage) {
        this.stage = stage;
        this.sketchPane = pane;
        this.shapes = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /** Method below utilizing a switch statement is called upon mouse press and calls appropriate
     * methods depending on which radio button has been chosen
     * by the user. Firstly, it initializes an instance variable of mouseLocation, so
     * that translate, resize, and rotate methods can be called on the selected shape properly.
     * It also stores mouse's location as a local variable, so that method within the scope
     * of this method can be called independently of changes in the value of mouseLoc instance variable.
     * In case of the Select Shape option, it selects a shape calling an appropriate method,
     * and later calculates the startingPoint that's a base for shape translation (to which
     * the new location of a mouse is later compared). After that, it reassigns
     * all instance variables to the current values of the selected shape, so that they can
     * be later passed in into the constructors of classes implementing the Command interface.
     * If the Draw with Pen option is selected, it creates a new instance of a
     * CurvedLine class, as well as an instance of the
     * DrawLine class implementing the command interface, after which it calls the
     * performAction method so that it can be later undone and redone.
     * If either Draw Ellipse or Draw Rectangle options are selected, it firstly deselects
     * any shapes that have been previously selected, creates an instance of a chosen shape
     * (size of which is initially set to 0,0), selects them (both logically and graphically)
     * and performs an action by pushing it onto the undoStack and clearing the redoStack.
     */
    public void onMousePressed(MouseEvent e, Options option, Color color) {
        Point2D point = new Point2D(e.getX(), e.getY());
        this.mouseLocation = new Point2D(e.getX(), e.getY());
        if (option != null) {
            switch (option) {
                case SELECT_SHAPE:
                    this.select(point);
                    if (this.isSelected != null) {
                        this.translateStartingPoint = new Point2D(this.isSelected.getCenter().getX() - point.getX(),
                                this.isSelected.getCenter().getY() - point.getY());
                        this.firstLocation = this.isSelected.getCenter();
                        this.firstAngle = this.isSelected.getAngle();
                        this.firstWidth = this.isSelected.getWidth();
                        this.firstHeight = this.isSelected.getHeight();
                    }
                    break;
                case DRAW_WITH_PEN:
                    this.deselect();
                    this.line = new CurvedLine(point, this.sketchPane, color, this.elements);
                    Command drawLine = new DrawLine(this.line, this.sketchPane, this.elements);
                    this.performAction(drawLine);
                    break;
                case DRAW_RECTANGLE:
                    this.deselect();
                    this.sketchyRectangle = new SketchyRectangle(this.sketchPane, point, color, this.shapes, this.elements);
                    this.sketchyRectangle.select();
                    this.isSelected = this.sketchyRectangle;
                    Command createRectangle = new CreateShape(this.sketchyRectangle, this.sketchPane, this.shapes, this.elements);
                    this.performAction(createRectangle);
                    break;
                case DRAW_ELLIPSE:
                    this.deselect();
                    this.sketchyEllipse = new SketchyEllipse(this.sketchPane, point, color, this.shapes, this.elements);
                    this.sketchyEllipse.select();
                    this.isSelected = this.sketchyEllipse;
                    Command createEllipse = new CreateShape(this.sketchyEllipse, this.sketchPane, this.shapes, this.elements);
                    this.performAction(createEllipse);
                    break;
            }
        }
    }

    /** Method below utilizing a switch statement is called upon mouse drag and calls appropriate
     * methods depending on which radio button has been chosen
     * by the user. In case of the Select Shape option, it resizes the shape if the
     * shift button is pressed, rotates it if control is pressed (or does both
     * simultaneously if both of these buttons are pressed at the same time), and if
     * neither of them are pressed, translates the selected shape to a location
     * specified by mouse's current position that's stored as a variable. If the Draw with
     * Pen option is selected it adds the current point of mouse's location to the line; if
     * Draw Rectangle or Draw Ellipse, it resizes those shapes. It also reassigns the
     * instance variable of Mouse's location that's firstly instantiated in the
     * onMousePressed method, so that the angle of shape's rotation can be
     * properly calculated.
     */
    public void onMouseDragged(MouseEvent e, Options option) {
        Point2D point = new Point2D(e.getX(), e.getY());
        if (option != null) {
            switch (option) {
                case SELECT_SHAPE:
                    if (this.isSelected != null) {
                        if (e.isShiftDown()) {
                            this.resize(this.isSelected, point);
                        }
                        if (e.isControlDown()) {
                            this.rotate(point);
                        }
                        if (!(e.isControlDown()) && (!(e.isShiftDown()))) {
                            this.translate(point);
                        }
                    }
                    break;
                case DRAW_WITH_PEN:
                    this.line.addPoint(point);
                    break;
                case DRAW_RECTANGLE:
                    this.resize(this.sketchyRectangle, point);
                    break;
                case DRAW_ELLIPSE:
                    this.resize(this.sketchyEllipse, point);
                    break;
            }
        }
        this.mouseLocation = new Point2D(e.getX(), e.getY());
    }

    /** Method below is called upon mouse release and calls appropriate
     * methods depending on whether the "select shape" radio button has been chosen
     * by the user. It checks whether the shape's new location, angle, or size are the same as the initial ones,
     * and if not, instantiates an instance of either the Translate, Rotate, or Resize classes
     * respectively, passing in the initial values (from before the change - the instance variables are reassigned
     * upon mouse press) as parameters (in case there was no change, no
     * command should be pushed to the undo stack).
     */
    public void onMouseReleased(Options option) {
        if (option != null) {
            if (option == Options.SELECT_SHAPE) {
                    if (this.isSelected != null) {
                        if (!(this.firstLocation.equals(this.isSelected.getCenter()))) {
                            Command translate = new Translate(this.firstLocation, this.isSelected);
                            this.performAction(translate);
                        }
                        if (this.firstAngle != this.isSelected.getAngle()) {
                            Command rotate = new Rotate(this.firstAngle, this.isSelected);
                            this.performAction(rotate);
                        }
                        if ((this.firstHeight != this.isSelected.getHeight())
                                && (this.firstWidth != this.isSelected.getWidth())) {
                            Command resize = new Resize(this.firstWidth, this.firstHeight, this.firstLocation,
                                    this.isSelected);
                            this.performAction(resize);
                        }
                    }
            }
        }
    }

    /** Method below is responsible for selecting a shape that's being clicked on by the user depending
     * on the mouse point. It firstly deselects the shape that has been previously selected (as long
     * as it was, which is checked in the deselect method), and later loops through all the shapes visible
     * on the pane from the top to the bottom, checking whether any of them contain the rotated point
     * (location of which is based on whether the shape on which the contains methods is being called has
     * been rotated or not), and if yes, calling their select method which informs the user visually of
     * selection (sets the stroke) as well as reassigns the variable of the isSelected shape to one that
     * has just been selected.
     * */
    public void select(Point2D point) {
        this.deselect();
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (this.shapes.get(i).contains(this.rotatePoint(point, this.shapes.get(i).getCenter(), this.shapes.get(i).getAngle()))) {
                this.shapes.get(i).select();
                this.isSelected = this.shapes.get(i);
                break;
            }
        }
    }

    /** Method responsible for deselecting the currently selected shape by both informing the user
     * of performed deselection visually (calling the selected shape's deselect method that sets its stroke's width
     * to zero), and logically (by setting the isSelected instance variable to null).
     * */
    public void deselect() {
        if (this.isSelected != null) {
            this.isSelected.deselect();
            this.isSelected = null;
        }
    }

    /** Method handling the deletion of a currently selected shape that's called whenever the user interacts with
     * the delete button. It instantiates an instance of the Delete class implementing the Command
     * interface so that the action can be undone, as well as performs the action (pushes it to
     * the undoStack and clears the redoStack). It later removes that shape by calling its remove
     * method that deletes it from the main pane's list of children, and removes it both from
     * the arraylist of shapes and of elements.
     * */
    public void delete() {
        if (this.isSelected != null) {
            Command delete = new Delete(this.isSelected, this.sketchPane, this.shapes, this.elements);
            this.performAction(delete);
            this.isSelected.remove();
            this.shapes.remove(this.isSelected);
            this.elements.remove(this.isSelected);
        }
    }

    /** Method below is responsible for raising the shape - both logically and graphically. It firstly
     * stores the initial indexes of the selected shape in the pane and in the array of elements. Later, it checks
     * whether raising a shape is possible (if the shape is on top of all the other ones, of if it's
     * covered by any other element of sketchy canvas), and if yes, raises it in the list of Pane's
     * elements and the elements arraylist by one (as they include both shapes and lines),
     * and calculates the index to which it should be moved in the arraylist of shapes by calling
     * the getRaiseIndex method. Finally, it instantiates an instance of the Raise class implementing
     * the Command interface passing these newly calculated values as parameters, so that the action
     * can be later undone (or redone).
     */
    public void raise() {
        if (this.isSelected != null) {
            int paneIndex = this.isSelected.getIndex();
            int elementsArrayIndex = this.elements.indexOf(this.isSelected);
            int shapesArrayIndex = this.shapes.indexOf(this.isSelected);
            if (paneIndex == (this.sketchPane.getChildren().size() - 1)) {
            } else if (paneIndex < (this.sketchPane.getChildren().size() - 1)) {
                this.isSelected.remove();
                this.isSelected.addToPane(paneIndex + 1);
                int newShapesIndex = this.getRaiseIndex();
                this.shapes.remove(this.isSelected);
                this.shapes.add(newShapesIndex, this.isSelected);
                this.elements.remove(this.isSelected);
                this.elements.add(elementsArrayIndex + 1, this.isSelected);
                Command raise = new Raise(paneIndex, shapesArrayIndex, elementsArrayIndex, this.shapes, this.elements, this.sketchPane, this.isSelected);
                this.performAction(raise);
            }
        }
    }

    /** Method below is responsible for lowering the shape - both logically and graphically. Similarly
     * to the previous one, it firstly stores the initial indexes of the selected shape in the
     * pane and in the array of elements. Later, it checks whether lowering a shape is possible
     * (if the shape at the bottom of the pane, or above other elements), and if yes, lowers it in the list of Pane's
     * elements and the elements arraylist by one (as they include both shapes and lines),
     * and calculates the index to which it should be moved in the arraylist of shapes by calling
     * the getLowerIndex method. Finally, it instantiates an instance of the Lower class implementing
     * the Command interface passing these newly calculated values as parameters, so that the action
     * can be later undone (or redone).
     */
    public void lower() {
        if (this.isSelected != null) {
            int paneIndex = this.isSelected.getIndex();
            int elementsArrayIndex = this.elements.indexOf(this.isSelected);
            int shapesArrayIndex = this.shapes.indexOf(this.isSelected);
            if (paneIndex == 0) {
            } else if (paneIndex > 0) {
                this.isSelected.remove();
                this.isSelected.addToPane(paneIndex - 1);
                int newShapesIndex = this.getLowerIndex();
                this.shapes.remove(this.isSelected);
                this.shapes.add(newShapesIndex, this.isSelected);
                this.elements.remove(this.isSelected);
                this.elements.add(elementsArrayIndex - 1, this.isSelected);
                Command lower = new Lower(paneIndex, shapesArrayIndex, elementsArrayIndex, this.shapes, this.elements, this.sketchPane, this.isSelected);
                this.performAction(lower);
            }
        }
    }

    /** Method below returns the index of the shapes ArrayList to which a particular shape
     * that's selected should be moved upon raising, while taking into consideration their layering
     * among both other shapes, and lines. If index of a shape in the arraylist of shapes upon raising
     * doesn't match its index on a pane upon raising (if there's a line above the selected
     * shape that's supposed to be raised), it changes only the shape's index in the Pane's list of children,
     * rather than also in the arraylist of shapes, which doesn't take these lines into account.
     */
    public int getRaiseIndex() {
        int currShapeIndex = this.shapes.indexOf(this.isSelected);
        int nextShapeIndex = currShapeIndex + 1;
        int moveToIndex = currShapeIndex;

        if (nextShapeIndex < this.shapes.size()) {
            if (nextShapeIndex - currShapeIndex == 1) {
                moveToIndex += 1;
            }
        }
        return moveToIndex;
    }

    /** Method below returns the index of the shapes arraylist to which a particular shape
     * that's selected should be moved upon lowering, while taking into consideration their layering
     * among both other shapes, and lines. If index of a shape in the arraylist of shapes upon lowering
     * doesn't match its index on a pane upon lowering (if there's a line under the selected
     * shape that's supposed to be lowered), it changes index only in the Pane's list of children,
     * rather than also in the arraylist of shapes, which doesn't take these lines into account.
     */
    public int getLowerIndex() {
        int currShapeIndex = this.shapes.indexOf(this.isSelected);
        int prevShapeIndex = currShapeIndex - 1;
        int moveToIndex = currShapeIndex;

        if (prevShapeIndex >= 0) {
            if (currShapeIndex - prevShapeIndex == 1) {
                moveToIndex -= 1;
            }
        }
        return moveToIndex;
    }

    /** Translate method called whenever a mouse is dragged and a shape is selected,
     * but both the control and shift buttons aren't pressed - to account for both ellipses and
     * rectangles, rather than calculating their new locations based on their centers and top-left corners
     * respectively, it calculates and sets their centers by adding the location of a point passed
     * in as a parameter (location of a mouse upon drag) to the tarting point
     * (an instance variable reassigned whenever a mouse is pressed and a shape
     * is selected, that's the distance between shape's center and point of press).
     */
    public void translate(Point2D point) {
        if (this.isSelected != null) {
            Point2D newCenter = new Point2D(this.translateStartingPoint.getX() + point.getX(),
                                              this.translateStartingPoint.getY() + point.getY());
            this.isSelected.setCenter(newCenter);
        }
    }

    /** Method responsible for rotating a currently selected shape - sets its angle to one calculated through
     * the getAngle method. Takes in current point (mouse's current location) as a parameter, so that the
     * angle between mouse's previous location and the new one stored as an instance variable and
     * continuously updated can be calculated.
     */
    private void rotate(Point2D currentPoint) {
        this.isSelected.setAngle(this.isSelected.getAngle() + (-1) * this.getAngle(currentPoint, this.mouseLocation));
    }

    /** Accessor method responsible for returning the angle between two points passed in as parameters. Used
     * for shape's rotation, where the angle of the shape that's continuously being updates is the angle between
     * the point of press, and the current drag.
     */
    private double getAngle(Point2D curr, Point2D prev) {
        double centerX = this.isSelected.getCenter().getX();
        double centerY = this.isSelected.getCenter().getY();
        double angle = Math.atan2((prev.getY() - centerY), (prev.getX() - centerX))
                - Math.atan2((curr.getY() - centerY), (curr.getX() - centerX));
        return Math.toDegrees(angle);
    }

    /** Method responsible for calculating the rotation of a point - particularly useful
     * for selecting a shape after it has been firstly rotated, as java's "contains" method
     * don't take the change in angle into account. The rotateAround point that's passed
     * in as a parameter is always a particular shape's center.
     */
    public Point2D rotatePoint(Point2D pointToRotate, Point2D rotateAround, double angle) {
        double sine = Math.sin(toRadians(angle));
        double cosine = Math.cos(toRadians(angle));
        Point2D point = new Point2D(pointToRotate.getX() - rotateAround.getX(), pointToRotate.getY() -
                rotateAround.getY());
        Point2D rotate = new Point2D(point.getX() * cosine + point.getY() * sine, -point.getX() * sine +
                point.getY() * cosine);
        Point2D correct = new Point2D(rotate.getX() + rotateAround.getX(), rotate.getY() + rotateAround.getY());

        return correct;
    }

    /** Method responsible for resizing the currently selected (or newly created) shape,
     * called when a mouse is dragged while the shift button being pressed while the "Select
     * Shape" radio button is chosen, or while the "Draw Rectangle" or "Draw Ellipse" options
     * are selected (action is then performed regardless of whether shift is pressed). It takes
     * in a selected shape and current point (location of a mouse) as parameters, and then
     * calculates the absolute value of a distance between shape's center and that mouse point
     * (rotated, if the angle of the shape isn't a default value) to account for both
     * ellipses and rectangles. If the initial center of shape
     * from before the resize method has been called isn't equal to shape's new center (if the shape
     * being resized is a rectangle - for ellipses, their centers are fixed points), it sets is center
     * to that initial value (so that the rectangle only resizes in two dimensions without falling off
     * the screen).
     */
    public void resize(SketchyShape shape, Point2D currPoint) {
        Point2D initialCenter = shape.getCenter();
        Point2D rotatedCurrPoint = this.rotatePoint(currPoint, initialCenter, shape.getAngle());

        double dx = rotatedCurrPoint.getX() - shape.getCenter().getX();
        double dy = rotatedCurrPoint.getY() - shape.getCenter().getY();

        shape.setWidth(abs(dx));
        shape.setHeight(abs(dy));

        if (!(initialCenter.equals(shape.getCenter()))) {
            shape.setCenter(initialCenter);
        }
    }

    /** Method responsible for changing the color of a shape, called whenever user interacts with the
     * "fill" button - it firstly checks whether a shape has been selected, and if yes, instantiates an
     * instance of the ChangeColor command class (so that the shape's initial color can be stored
     * and later recreated whenever undo method is called), passing in the new color chosen
     * through the color picker into its parameter. Since it's called from the command class upon a button
     * click, it firstly takes in that new color as parameter too. */
    public void changeColor(Color color) {
        if (this.isSelected != null) {
            Command command = new ChangeColor(this.isSelected, color);
            this.performAction(command);
            this.isSelected.setColor(color);
        }
    }

    /** Helper method for performing an action that can be handled by a class implementing the command
     * interface - to limit the number of lines of code, it takes in the command that has been instantiated,
     * pushes it to the undoStack, and clears the redo one.
     */
    public void performAction(Command command) {
        this.undoStack.push(command);
        this.redoStack.clear();
    }

    /** Helper method for undoing an action - firstly, it checks whether the undoStack isn't empty,
     * and later pops a command that has been recently performed while calling its undo method
     * (polymorphism! can be performed on all classes implementing the Command interface).
     * It later pushes that command to the redoStack so that it can be redone.
     */
    public void undo() {
        if (!this.undoStack.empty()) {
            Command command = this.undoStack.pop();
            command.undo();
            this.redoStack.push(command);
        }
    }

    /** Helper method for redoing an action - firstly, it checks whether the redoStack isn't empty,
     * and later pops a command that has been recently undone while calling its redo method.
     * It later pushes that command to the undoStack so that it can be undone.
     */
    public void redo() {
        if (!this.redoStack.empty()) {
            Command command = this.redoStack.pop();
            command.redo();
            this.undoStack.push(command);
        }
    }

    /** Save method below saves the current state of the sanvas. Whenever a user decided to save a file,
     * all the elements implementing the Savable elements are being looped through,
     * and their "save" methods are called so that they can add their parameters to the file.
     *
     */
    public void save() {
        CS15FileIO io = new CS15FileIO();
        String fileName = io.getFileName(TRUE, this.stage);
        if (fileName != null) {
            io.openWrite(fileName);
            for (Savable element : this.elements) {
                element.save(io);
            }
            io.closeWrite();
        }
    }


    /** Method below is called whenever a user decided to load a previously saved drawing based
     * on a button click. Depending on whether a file was selected, it clears the pane from currently
     * existing elements, the arraylist of shapes, and the arraylist of savable elements - this way,
     * whenever a user decides to load a file after having made changes in the main sketchyPane, save it, and
     * later load again, only the final version (from after the file was first loaded) will be visible
     * (without the elements that were on the pane before user loaded a file). After that, it loops through
     * the file that's open for reading checking whether there's any more data to be read, and
     * depending on what type of shape is now being recreated
     * (stated by the value of the string that precedes the parameters of Sketchy's elements in their
     * save methods), it loads its values that have been added to the file upon the call of "save" method
     * through a switch statement.
     * In case of the CurvedLine, it loops through all the points saved to a file (knowing their number,
     * as it's stated through the CurvedLine's save method) and adds them to a line firstly instantiated
     * with a starting point (one at the beginning of the point's list). If there's no more data to be read,
     * the loop is terminated.
     *
     */
    public void load() {
        CS15FileIO io = new CS15FileIO();
        String fileName = io.getFileName(FALSE, this.stage);
        if (fileName != null) {
            this.sketchPane.getChildren().clear();
            this.shapes.clear();
            this.elements.clear();
            io.openRead(fileName);
            while (io.hasMoreData()) {
                String s = io.readString();
                switch (s) {
                    case "Rectangle":
                        double locationX = io.readDouble();
                        double locationY = io.readDouble();
                        Point2D location = new Point2D(locationX, locationY);
                        double width = io.readDouble();
                        double height = io.readDouble();
                        double angle = io.readDouble();
                        double colorRed = io.readDouble();
                        double colorGreen = io.readDouble();
                        double colorBlue = io.readDouble();
                        SketchyRectangle rect = new SketchyRectangle(this.sketchPane, location, (new Color(colorRed, colorGreen, colorBlue, 1)), this.shapes, this.elements);
                        rect.setWidth(width);
                        rect.setHeight(height);
                        rect.setAngle(angle);
                        break;
                    case "Ellipse":
                        double centerX2 = io.readDouble();
                        double centerY2 = io.readDouble();
                        Point2D center2 = new Point2D(centerX2, centerY2);
                        double width2 = io.readDouble();
                        double height2 = io.readDouble();
                        double angle2 = io.readDouble();
                        double colorRed2 = io.readDouble();
                        double colorGreen2 = io.readDouble();
                        double colorBlue2 = io.readDouble();
                        SketchyEllipse ellipse = new SketchyEllipse(this.sketchPane, center2, (new Color(colorRed2, colorGreen2, colorBlue2, 1)), this.shapes, this.elements);
                        ellipse.setWidth(width2);
                        ellipse.setHeight(height2);
                        ellipse.setAngle(angle2);
                        break;
                    case "CurvedLine":
                        double colorRed3 = io.readDouble();
                        double colorGreen3 = io.readDouble();
                        double colorBlue3 = io.readDouble();
                        int numberOfPoints = io.readInt();
                        double startX = io.readDouble();
                        double startY = io.readDouble();
                        Point2D startingPoint = new Point2D(startX, startY);
                        CurvedLine line = new CurvedLine(startingPoint, this.sketchPane, (new Color(colorRed3, colorGreen3, colorBlue3, 1)), this.elements);
                        for (int i = 0; i < (numberOfPoints-2)/2; i++) {
                            double point1X = io.readDouble();
                            double point1Y = io.readDouble();
                            Point2D newPoint = new Point2D(point1X, point1Y);
                            line.addPoint(newPoint);
                        }
                        break;
                }
            } io.closeRead();
        }
    }
}

