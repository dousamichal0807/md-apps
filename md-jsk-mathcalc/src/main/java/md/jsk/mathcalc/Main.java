package md.jsk.mathcalc;

import javafx.application.Application;
import javafx.stage.Stage;
import md.jsk.mathcalc.activities.MainActivity;

/**
 * Class with entry point for MathCalc application.
 *
 * @see #main(String[])
 * @author Michal Douša
 */
public final class Main extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(final Stage primaryStage) {
        Main.primaryStage = primaryStage;

        primaryStage.setTitle("MD MathCalc");
        primaryStage.show();

        primaryStage.setScene(MainActivity.getInstance());
    }

    /**
     * Entry point for MD jSK MathCalc application.
     */
    public static void main(String[] args) {
        Main.launch(args);
    }
}
