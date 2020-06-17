package md.jgames.jchess;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import md.jgames.jchess.activities.MainActivity;
import md.jgames.jchess.components.ChessboardView;
import md.jgames.jchess.components.ChessboardViewConfiguration;
import md.jgames.jchess.resources.AppResources;
import mdlib.materifx.MaterialSettings;
import mdlib.utils.JFXUtilities;

/**
 * MD jChess as JavaFX {@link Application} entry point.
 *
 * @author Michal Douša
 */
public final class App extends Application {

    private static Stage primaryStage;

    /**
     * Returns the primary {@link Stage} used in this application.
     *
     * @return the primary stage
     */
    public static Stage primaryStage() {
        return primaryStage;
    }

    /**
     * Empty constructor used by JavaFX framework.
     */
    public App() {
    }

    /**
     * The {@code main()} method of MD jChess application.
     *
     * @param args arguments passed to the {@code main()} method
     */
    public static void main(final String[] args) {
        MaterialSettings.launchInit();

        // For testing purposes, only
        // Will be changed by loading user settings
        boolean lightTheme = false;
        if (lightTheme) {
            MaterialSettings.primaryColor().set(Color.valueOf("#5d4037"));
            MaterialSettings.secondaryColor().set(Color.valueOf("#006064"));
            MaterialSettings.theme().set(MaterialSettings.THEME_LIGHT);
        } else {
            MaterialSettings.primaryColor().set(Color.valueOf("#a1887f"));
            MaterialSettings.secondaryColor().set(Color.valueOf("#4dd0e1"));
            MaterialSettings.theme().set(MaterialSettings.THEME_DARK);
        }

        // Load images for ChessboardView
        Image chessboardImage = JFXUtilities.loadImage(AppResources.loadResource("default-chessboard.png"));
        ChessboardViewConfiguration.chessboardImage().set(chessboardImage);

        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {

        // Check if the application has not run yet
        if (App.primaryStage != null)
            throw new IllegalStateException("This application can be run only once");

        // Set the primary stage of the application
        App.primaryStage = primaryStage;

        // Configure primary stage and show it
        primaryStage.setTitle("MD jChess");
        primaryStage.setScene(MainActivity.getInstance());
        primaryStage.show();
    }
}
