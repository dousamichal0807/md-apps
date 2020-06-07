package md.jsk.mathcalc.activities;

import javafx.fxml.FXML;
import md.jsk.mathcalc.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class MainActivityController {
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public MainActivityController() {
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void openSettings() {
        //Main.primaryStage().setScene(SettingsActivity.getInstance());
    }
}
