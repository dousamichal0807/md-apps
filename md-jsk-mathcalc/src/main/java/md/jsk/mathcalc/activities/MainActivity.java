package md.jsk.mathcalc.activities;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import md.jcore.resources.CoreResources;
import md.jsk.mathcalc.Main;
import md.jsk.mathcalc.resources.Resources;

public class MainActivity extends Scene {

    private static MainActivity instance;

    public static MainActivity getInstance() {
        if (instance == null)
            instance = new MainActivity();
        return instance;
    }

    private final BorderPane root;

    private final VBox mainMenu;
    private final Label title;
    private final Button startButton;

    private final HBox bottomMenu;
    private final Button settingsButton;
    private final Button helpButton;

    private MainActivity() {
        super(new BorderPane(), 800, 600);

        title = new Label("MathCalc");
        title.getStyleClass().add("h1");

        startButton = new Button("Start");
        //startButton.setDisable(true);
        startButton.getStyleClass().add("button-flat-primary");

        mainMenu = new VBox();
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.getChildren().addAll(title, startButton);

        settingsButton = new Button("Settings");
        settingsButton.setOnAction(event -> Main.primaryStage().setScene(SettingsActivity.getInstance()));

        helpButton = new Button("Help");

        bottomMenu = new HBox();
        bottomMenu.getChildren().addAll(settingsButton, helpButton);
        bottomMenu.setPadding(new Insets(32, 32, 32, 32));

        root = (BorderPane) getRoot();
        root.setCenter(mainMenu);
        root.setBottom(bottomMenu);

        this.getStylesheets().add(CoreResources.loadResourceURL("material-light.css").toExternalForm());
        this.getStylesheets().add(Resources.loadResourceURL("material-theme.css").toExternalForm());
    }
}
