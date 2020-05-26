package md.jsk.mathcalc.tests;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import md.jsk.mathcalc.components.CardioidProgressBar;

public class CardioidProgressBarTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CardioidProgressBar progressBar1 = new CardioidProgressBar(CardioidProgressBar.INDETERMINATE);

        HBox progressBars = new HBox();
        progressBars.setAlignment(Pos.CENTER);
        progressBars.getChildren().addAll(progressBar1);

        Slider sizeSlider = new Slider();
        sizeSlider.setMin(100);
        sizeSlider.setMax(400);
        sizeSlider.setBlockIncrement(1);
        sizeSlider.setMajorTickUnit(50);
        sizeSlider.setMinorTickCount(5);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.valueProperty().addListener(observable -> {
            double size = sizeSlider.getValue();
            progressBar1.setMinSize(size, size);
        });

        Slider valueSlider = new Slider();
        valueSlider.setMin(-1);
        valueSlider.setMax(1);
        valueSlider.setBlockIncrement(.001);
        valueSlider.setMajorTickUnit(.1);
        valueSlider.setMinorTickCount(10);
        valueSlider.setShowTickMarks(true);
        valueSlider.setShowTickLabels(true);
        valueSlider.valueProperty().addListener(observable -> {
            double newValue = valueSlider.getValue();
            if (newValue == CardioidProgressBar.INDETERMINATE || (newValue >= 0 && newValue <= 1))
                progressBar1.setValue(newValue);
        });

        VBox sliders = new VBox();
        sliders.setAlignment(Pos.CENTER);
        sliders.getChildren().addAll(sizeSlider, valueSlider);

        BorderPane root = new BorderPane();
        root.setCenter(progressBars);
        root.setBottom(sliders);

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle(CardioidProgressBar.class.getName() + " Test Case");
        primaryStage.show();
    }
}
