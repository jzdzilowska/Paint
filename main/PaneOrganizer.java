package sketchy.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is a class responsible for setting up appropriate panes of Sketchy's interface
 * (the creation of GUI's elements - buttons - is delegated to the Control class). It contains the root pane
 * and its children (sketchPane that's playing the role of a canvas, leftPane that contains DrawingOptionsPane,
 * ColorPane, ShapeOptionsPane and OperationsPane corresponding to appropriate button types), an instance of the
 * Sketchy class associated with the sketchyPane, and an instance of Control class handling the creation of
 * buttons and user's interaction with them.
 */
public class PaneOrganizer {
    private BorderPane root;
    private Control control;

    /**
     * Constructor below sets up a root pane, and calls other method
     * responsible for setting up additional panes contained (graphically) in it.
     * Methods for panes to be added into the root node are called from
     * within the methods creating them - this way, these nodes can be stored
     * as local variables. The constructor also creates an instance of the Sketchy class
     * (handling the "canvas" itself) as well as an instance of the Control class
     * (overseeing the buttons and MouseEvents responding to user's input).
     */
    public PaneOrganizer(Stage stage) {
        this.root = new BorderPane();
        Pane sketchPane = new Pane();

        Sketchy sketchy = new Sketchy(sketchPane, stage);
        this.control = new Control(this, sketchy, sketchPane);

        this.root.setCenter(sketchPane);
        this.createLeftPane();
    }

    /**
     * Helper method below creates a leftPane while specifying its background, alignment, spacing, and padding.
     * It later simultaneously creates additional panes that correspond to the button types they include
     * and adds them to it.
     * Because the leftPane - and all additional panes - are added to the root pane (or their
     * parent panes) through the method by which they're instantiated,
     * they're all stored as a local variables.
     */
    public void createLeftPane() {
        VBox leftPane = new VBox();
        leftPane.setStyle(Constants.LEFT_PANE_COLOR);
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setSpacing(30);
        leftPane.setPadding(new Insets(0,30,0,30));

        leftPane.getChildren().addAll(this.createDrawingOptionsPane(), this.createColorPane(),
                this.createShapeActionsPane(), this.createOperationsPane());

        leftPane.setFocusTraversable(true);
        this.root.setLeft(leftPane);
    }

    /**
     * Helper method below creates a drawingOptionsPane that contains radio buttons, but calls a method
     * of the control class handling the GUI to actually instantiate them and add them to itself
     * (this way, the association is nicely simplified :))
     */
    public VBox createDrawingOptionsPane() {
        VBox drawingOptionsPane = new VBox();
        this.createLabel(drawingOptionsPane, "Drawing Options");
        drawingOptionsPane.setAlignment(Pos.CENTER);
        this.control.createDrawingOptions(drawingOptionsPane);

        return drawingOptionsPane;
    }

    /**
     * Similarly to the previous method, helper method below creates a pane that contains an instance of
     * Java's color picker, but delegates the creation of it to the control class - as with the
     * buttons, this way, the picker's value can be stored within the control class and passed in without
     * any redundant association to the methods requiring information about the chosen color.
     */
    public VBox createColorPane() {
        VBox colorPane = new VBox();
        this.createLabel(colorPane, "Set the Color!");
        colorPane.setAlignment(Pos.CENTER);
        this.control.createColor(colorPane);

        return colorPane;
    }

    /**
     * Helper method below creates a pane containing buttons regarding shape actions (lowering, raising, filling them
     * with appropriate color, deleting). It calls an appropriate method of the control class to actually
     * instantiate the buttons and let them respond to user's input.
     */
    public VBox createShapeActionsPane() {
        VBox shapeActionsPane = new VBox();
        this.createLabel(shapeActionsPane, "Shape Actions");
        shapeActionsPane.setAlignment(Pos.CENTER);
        this.control.createShapeActions(shapeActionsPane);

        return shapeActionsPane;
    }

    /**
     * Helper method below creates a pane containing buttons responsible for redoing, undoing the recently performed
     * action(s), or saving and loading. It calls an appropriate method of the control class to actually
     * instantiate the corresponding buttons and let them handle user's input.
     */
    public VBox createOperationsPane() {
        VBox operationsPane = new VBox();
        this.createLabel(operationsPane, "Operations");
        operationsPane.setAlignment(Pos.CENTER);
        this.control.createOperationsPane(operationsPane);

        return operationsPane;
    }

    /**
     * Method below creates a label of a specified string and adds
     * it to a pane, values both of which are passed in as parameters.
     */
    public void createLabel(Pane pane, String s) {
        Label label = new Label(s);
        pane.getChildren().add(label);
    }

    /**
     * Accessor method below returns the root pane, so that it can be passed into the Scene's constructor
     * (accessed from within the App class).
     */
    public BorderPane getRoot() {
        return this.root;
    }
}