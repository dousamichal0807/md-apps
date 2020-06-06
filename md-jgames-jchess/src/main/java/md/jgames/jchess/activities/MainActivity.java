package md.jgames.jchess.activities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import md.jgames.jchess.resources.AppResources;
import mdlib.materij.MaterialSettings;

import java.io.IOException;

/**
 * App activity for MD jSK MathCalc
 *
 * @author Michal Douša
 */
public final class MainActivity extends Scene {

    private static MainActivity instance;

    /**
     * Returns the only instance of {@link MainActivity} class.
     * @return the {@link MainActivity} instance
     */
    public static MainActivity getInstance() {
        if (instance == null) try {
            instance = new MainActivity();
        } catch (IOException exc) {
            // Should never happen
            throw new RuntimeException(exc);
        }
        return instance;
    }

    // Private constructor for material activity
    private MainActivity() throws IOException {
        // Load FXML file
        super(FXMLLoader.load(AppResources.loadActivityFXML("main")), 800, 600);
        MaterialSettings.activityPostInit(this);
    }
}
