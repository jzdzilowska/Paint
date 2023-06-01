package sketchy.shapes;

import cs15.fnl.sketchySupport.CS15FileIO;

/** Savable interface implemented by all visual elements that can be added by the user to the sketchy pane
 * (lines, rectangles, ellipses) - particularly helpful for the save method, where sketchy iterates through all the
 * "savable" elements (polymorphism!) and calls the save methods from within their
 * classes so that they can add themselves to the file (write down their properties upon saving).
 */
public interface Savable {
    void save(CS15FileIO io);
}
