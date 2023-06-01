package sketchy.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * It's time for Sketchy! This is the App class to get things started.
 *
 * Your job is to fill in the start method!
 *
 * Class comments here...
 *
 * This App class allows Sketchy to appear by initializing the PaneOrganizer which
 * holds all the GUI elements of the application. This class also instantiates a Scene set, associates
 * it with the BorderPane root, adds the scene to the stage, and thus lets it appear.
 * */

public class App extends Application {

  @Override
  public void start(Stage stage) {
    PaneOrganizer organizer = new PaneOrganizer(stage);
    stage.setScene(new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
    stage.setTitle("sketchy!");
    stage.show();
  }

  public static void main(String[] argv) {
    launch(argv);
  }
}
