package md.jsk.mathcalc.activities;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import md.jcore.resources.CoreResources;
import md.jsk.mathcalc.resources.Resources;


public class SettingsActivity extends Scene {

    private static SettingsActivity instance;

    public static SettingsActivity getInstance() {
        if (instance == null)
            instance = new SettingsActivity();
        return instance;
    }

    private final BorderPane root;

    private final HBox bottomPane;
    private final Button saveButton;
    private final Button discardButton;

    private SettingsActivity() {
        super(new BorderPane(), 800, 600);

        discardButton = new Button("Discard");
        discardButton.getStyleClass().add("button-flat");

        saveButton = new Button("Save");
        saveButton.getStyleClass().add("button-flat-primary");

        bottomPane = new HBox();
        bottomPane.setAlignment(Pos.CENTER_RIGHT);
        bottomPane.setPadding(new Insets(32));
        bottomPane.getChildren().addAll(discardButton, saveButton);

        root = (BorderPane) getRoot();
        root.setBottom(bottomPane);

        this.getStylesheets().add(CoreResources.loadResourceURL("material-light.css").toExternalForm());
        this.getStylesheets().add(Resources.loadResourceURL("material-theme.css").toExternalForm());
    }
}
