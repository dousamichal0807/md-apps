package md.jsk.mathcalc.activities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import md.jcore.resources.CoreResources;
import md.jsk.mathcalc.resources.Resources;

import java.io.IOException;

public class MainActivity extends Scene {

    private static MainActivity instance;

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
        super(FXMLLoader.load(ActivityLoader.loadActivity("main")), 800, 600);

        this.getStylesheets().add(CoreResources.loadResourceURL("material-light.css").toExternalForm());
        this.getStylesheets().add(Resources.loadResourceURL("material-theme.css").toExternalForm());
    }
}
