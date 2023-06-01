package sketchy.main;

/** Class of enums corresponding to radio buttons. Depending on the value of an instance variable in the
 * top-level Sketchy class that can be changed based on user's input (button click) from the control
 * class, an appropriate method prompted by the switch statement in Sketchy class is called. Because of unnecessarily
 * complicated association that would be required if I were to create enums corresponding to all buttons (the order
 * of pane creation would have to be changed, or it would require a very tedious process of adding pane's additional
 * elements manually), I've decided to use just four of them that prompt the Sketchy's onMouse(...) methods
 * to respond accordingly to users interaction with Sketchy's canvas, depending on which one
 * is currently selected. It also helps keep the Sketchy class - in my opinion - more concise,
 * as the number of lines of code is greatly limited (no need for additional unnecessary switch statements in the
 * top-level class!) than if there were enums for all sketchy actions.
 */
public enum Options {
    SELECT_SHAPE,
    DRAW_WITH_PEN,
    DRAW_RECTANGLE,
    DRAW_ELLIPSE,
}
