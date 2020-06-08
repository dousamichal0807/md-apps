package md.jsk.mathcalc;

import javafx.application.Application;
import javafx.stage.Stage;
import md.jsk.mathcalc.activities.MainActivity;

/**
 * Class with entry point for MathCalc application.
 *
 * @author Michal Douša
 * @see #main(String[])
 */
public final class App extends Application {

    private static Stage primaryStage;

    /**
     * Returns the primary stage of the application.
     *
     * @return primary stage of the application
     */
    public static Stage primaryStage() {
        return primaryStage;
    }

    /**
     * Entry point for MD jSK MathCalc application.
     */
    public static void main(final String[] args) {
        App.launch(args);
    }

    /**
     * Constructor to create JavaFX {@link Application} instance.
     */
    public App() {
    }

    @Override
    public void start(final Stage primaryStage) {

        // Check if the application had not run yet
        if (App.primaryStage != null)
            throw new IllegalStateException("This application can be run only once");

        // Set the primary stage of the application
        App.primaryStage = primaryStage;

        // Configure primary stage and show it
        primaryStage.setTitle("MD MathCalc");
        primaryStage.setScene(MainActivity.getInstance());
        primaryStage.show();
    }
}
