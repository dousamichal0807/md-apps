package md.jgames.jchess.jfx;

import javafx.application.Application;
import javafx.stage.Stage;
import md.jgames.jchess.jfx.activities.MainActivity;

public class Main extends Application {

    private static Stage primaryStage;

    public static Stage primaryStage() {
        return primaryStage;
    }

    public Main() {
    }

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {

        // Check if the application had not run yet
        if (Main.primaryStage != null)
            throw new IllegalStateException("This application can be run only once");

        // Set the primary stage of the application
        Main.primaryStage = primaryStage;

        // Configure primary stage and show it
        primaryStage.setTitle("MD jChess");
        primaryStage.setScene(MainActivity.getInstance());
        primaryStage.show();
    }
}
