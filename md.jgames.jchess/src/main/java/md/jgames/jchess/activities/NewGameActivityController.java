package md.jgames.jchess.activities;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class NewGameActivityController implements Initializable {


    @FXML
    private GridPane mainContent;
    @FXML
    private BorderPane positionSelectPane;
    @FXML
    private BorderPane playerSetupSelectPane;
    @FXML
    private HBox bottomPane;

    /**
     * Used by {@code javafx.fxml} module.
     */
    public NewGameActivityController() {
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        // Main Content
        ColumnConstraints cc = new ColumnConstraints();
        RowConstraints rc = new RowConstraints();
        cc.setPercentWidth(50);
        rc.setPercentHeight(100);
        mainContent.getColumnConstraints().addAll(cc, cc);
        mainContent.getRowConstraints().addAll(rc);
        GridPane.setConstraints(positionSelectPane, 0, 0);
        GridPane.setConstraints(playerSetupSelectPane, 1, 0);

        // Bottom Pane
        Pane separating = new Pane();
        HBox.setHgrow(separating, Priority.ALWAYS);
        bottomPane.getChildren().add(1, separating);
    }
}
