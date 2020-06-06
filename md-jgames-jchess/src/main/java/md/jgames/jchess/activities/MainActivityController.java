package md.jgames.jchess.activities;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import md.jgames.jchess.App;
import md.jgames.jchess.resources.AppResources;

import java.net.URL;
import java.util.ResourceBundle;

public class MainActivityController implements Initializable {

    public MainActivityController() {
    }

    @FXML
    private ImageView logoImageView;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        logoImageView.setImage(new Image(AppResources.loadResource("jchess-logo-256px.png")));
    }

    @FXML
    private void newGame() {
        App.primaryStage().setScene(NewGameActivity.getInstance());
    }
}
