package md.jsk.chemitables;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The entry point for MD jSK Chemitables application
 *
 * @author Michal Douša
 * @see #main(String[])
 */
public final class Main extends Application {

    private static Stage primaryStage;

    /**
     * Returns the primary stage of MD jSK Chemitables application
     *
     * @return primary stage of this application
     */
    public static Stage primaryStage() {
        return primaryStage;
    }

    /**
     * Empty public constructor used by JavaFX.
     */
    public Main() {
    }

    @Override
    public void start(final Stage primaryStage) {

        // Check if the application had not run yet
        if (Main.primaryStage != null)
            throw new IllegalStateException("This application can be run only once");

        // Set up primary stage and show it
        primaryStage.show();
    }

    /**
     * Entry point for MD jSK Chemitables application.
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
