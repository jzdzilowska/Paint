package sketchy.commands;

/** This is the command interface implemented by all command classes that can call methods on Sketchy's shapes
 * (ellipses and rectangles). Although each command class is responsible for a different action
 * (lowering/raising shapes, etc.), they all share the capability to be redone or undone, and
 * should be therefore added to a stack of commands to preserve the order of being called - thus,
 * to allow for generic coding, they all implement the interface which f.e. allows sketchy to call
 * a universal method on the recently performed command without the need to specify its actual type.
 * */
public interface Command {
    void undo() ;
    void redo() ;
}
