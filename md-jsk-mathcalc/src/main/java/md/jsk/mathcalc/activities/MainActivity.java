package md.jsk.mathcalc.activities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import md.jcore.resources.CoreResources;
import md.jsk.mathcalc.resources.Resources;

import java.io.IOException;

/**
 * Main activity for MD jSK MathCalc
 *
 * @author Michal Douša
 * @see SettingsActivity
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

    private MainActivity() throws IOException {
        // Load FXML file
        super(FXMLLoader.load(ActivityLoader.loadActivityFXML("main")), 800, 600);

        // Load CSS stylesheets
        this.getStylesheets().add(CoreResources.loadResourceURL("material-light.css").toExternalForm());
        this.getStylesheets().add(Resources.loadResourceURL("material-theme.css").toExternalForm());
    }
}
