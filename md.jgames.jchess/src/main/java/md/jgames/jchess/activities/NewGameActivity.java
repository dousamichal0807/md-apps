package md.jgames.jchess.activities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import mdlib.materifx.MaterialSettings;

import java.io.IOException;

public class NewGameActivity extends Scene {

    private static NewGameActivity instance;

    public static NewGameActivity getInstance() {
        try {
            if (instance == null)
                instance = new NewGameActivity();
            return instance;
        } catch (IOException exc) {
            // Should never happen
            throw new RuntimeException(exc);
        }
    }

    private NewGameActivity() throws IOException {
        super(FXMLLoader.load(NewGameActivity.class.getResource("activity_newgame.fxml")), 800, 600);
        MaterialSettings.activityPostInit(this);
    }
}
