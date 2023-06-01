package sketchy.main;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This is a class responsible for Sketchy's actual GUI - instantiating buttons and
 * making them respond to user's input by prompting them to call appropriate methods of the Sketchy class.
 * Structure of the panes to which they belong is handled exclusively by the PaneOrganizer class. The control
 * class is also responsible for overseeing the current value of the "option" instance variable that's changed
 * depending on user's interaction with the radio buttons, and calls a switch-statement-method of
 * the sketchy class.
 */
public class Control {
    private PaneOrganizer paneOrganizer;
    private ToggleGroup group;
    private Sketchy sketchy;
    private Pane sketchPane;
    private ColorPicker picker;
    private Options option;

    /** In the constructor, the association and handling of MouseEvents in relation to the main sketchPane
     * ("canvas") is set up. Methods responsible for establishing Sketchy's response to user's input
     * are called in here rather than from the PaneOrganizer class, as knowledge of the enum's and
     * picker's current value is required.
     */
    public Control (PaneOrganizer paneOrganizer, Sketchy sketchy, Pane sketchPane){
        this.paneOrganizer = paneOrganizer;
        this.sketchy = sketchy;
        this.sketchPane = sketchPane;
        this.sketchPane.setOnMousePressed((MouseEvent e) -> this.sketchy.onMousePressed(e, this.option, this.picker.getValue()));
        this.sketchPane.setOnMouseDragged((MouseEvent e) -> this.sketchy.onMouseDragged(e, this.option));
        this.sketchPane.setOnMouseReleased((MouseEvent e) -> this.sketchy.onMouseReleased(this.option));
    }

    /**
     * Method below instantiates necessary elements of the drawing options pane instantiated in the
     * PaneOrganizer class by calling helper method responsible for creating radio buttons. An enum corresponding
     * to a particular button is passed into a called helper method as a parameter -
     * this way, the instance variable is reassigned to a chosen option depending on which
     * button has been clicked, and thus allows for proper methods to be called through the switch statement
     * in the Sketchy class.
     */
    public void createDrawingOptions(Pane pane) {
        this.group = new ToggleGroup();
        this.createRadioButton(pane, "Select Shape", Options.SELECT_SHAPE);
        this.createRadioButton(pane, "Draw with Pen", Options.DRAW_WITH_PEN);
        this.createRadioButton(pane, "Draw Rectangle", Options.DRAW_RECTANGLE);
        this.createRadioButton(pane, "Draw Ellipse", Options.DRAW_ELLIPSE);
    }

    /**
     * Helper method below instantiates an instance of Java's color picker with the default color set to
     * black and adds it to a colorPane previously created in the PaneOrganizer.
     */
    public void createColor(Pane pane) {
        this.picker = new ColorPicker(Color.BLACK);
        pane.getChildren().add(this.picker);
    }

    /**
     * Methods below create respectively shapeActionPane's and operationPane's buttons and
     * handle their response to user's input by calling appropriate methods of the sketchy class.
     * To limit complicated association, no enums are used for these elements
     * (more clarification is included in the Options enums class!).
     */
    public void createShapeActions(Pane pane) {
        Button fill = this.createButton(pane, "Fill");
        fill.setOnMouseClicked((MouseEvent e) -> this.sketchy.changeColor(this.picker.getValue()));
        Button delete = this.createButton(pane, "Delete");
        delete.setOnMouseClicked((MouseEvent e) -> this.sketchy.delete());
        Button raise = this.createButton(pane, "Raise");
        raise.setOnMouseClicked((MouseEvent e) -> this.sketchy.raise());
        Button lower = this.createButton(pane, "Lower");
        lower.setOnMouseClicked((MouseEvent e) -> this.sketchy.lower());
    }

    public void createOperationsPane(Pane pane) {
        Button undo = this.createButton(pane, "Undo");
        undo.setOnMouseClicked((MouseEvent e) -> this.sketchy.undo());
        Button redo = this.createButton(pane, "Redo");
        redo.setOnMouseClicked((MouseEvent e) -> this.sketchy.redo());
        Button save = this.createButton(pane, "Save");
        save.setOnMouseClicked((MouseEvent e) -> this.sketchy.save());
        Button load = this.createButton(pane, "Load");
        load.setOnMouseClicked((MouseEvent e) -> this.sketchy.load());
    }

    /**
     * Method below creates a radio button of a specified string and adds
     * it to a pane, values both of which are passed in as parameters. It also
     * takes in the value of an enum as a parameter that corresponds to a button.
     * This way, depending on which button has been clicked on by the user, an instance variable
     * is reassigned, and an appropriate method of the Sketchy class is called
     * upon user's interaction with the sketchPane. Additionally, a toggle group is set uo,
     * so that only one option can be selected at a time.
     */
    public RadioButton createRadioButton(Pane pane, String s, Options option) {
        RadioButton button = new RadioButton(s);
        button.setToggleGroup(this.group);
        button.setOnMouseClicked((MouseEvent e) -> this.option = option);
        pane.getChildren().add(button);
        return button;
    }

    /** Method below creates a regular button (one that doesn't correspond to an enum value).
     */
    public Button createButton(Pane pane, String name) {
        Button button = new Button(name);
        pane.getChildren().add(button);
        return button;
    }
}
