package md.jsk.mathcalc.components;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public final class CardioidProgressBar extends Pane {

    /**
     * Represents that progress bar is in inderminate state. Pass this value to
     * {@link #setValue(double)}, if you want to have progress bar been in this
     * state.
     */
    public static final double INDETERMINATE = -1D;

    private final PBAnimationTimer animation;
    private double animationProgress;
    private long animationStartingTime;
    private double value;

    /**
     * Returns the value of the progress bar as {@code double} in range from 0 to 1
     * (inclusive), or {@link #INDETERMINATE}, if the progress bar is in
     * indeterminate state.
     *
     * @return the value of the progress bar
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the progress bar. Possible values are from range from 0 to
     * 1 inclusive, or {@link #INDETERMINATE} if the progress bar should be in
     * indeterminate state.
     * @param value value to be set
     */
    public void setValue(final double value) {
        if (value != INDETERMINATE && (value < 0 || value > 1))
            throw new IllegalArgumentException("Illegal progress bar value: " + value);
        this.value = value;
    }

    /**
     * Creates new progress bar with initial value set to zero.
     */
    public CardioidProgressBar() {
        this(0.0D);
    }

    /**
     * Creates new progress bar using given progress bar value.
     *
     * @param value the value of the new progress bar
     * @throws IllegalArgumentException if given progress bar value is invalid
     *
     * @see #getValue()
     * @see #setValue(double)
     * @see #refresh()
     */
    public CardioidProgressBar(double value) {
        setValue(value);
        setMinSize(100, 100);

        animation = new PBAnimationTimer(this);
        animationProgress = 0.0;
        animationStartingTime = System.nanoTime();

        Bindings.selectBoolean(this.sceneProperty(), "window", "showing").addListener((observable, wasShown, isShown) -> {
            if (isShown) {
                animation.start();
                if (!wasShown)
                    animationStartingTime = System.nanoTime();
            } else {
                animation.stop();
                animationProgress = 0.0;
            }
        });
    }

    /**
     * Refreshes the progress bar.
     */
    public synchronized strictfp void refresh() {
        double d = Math.min(this.getWidth(), this.getHeight()) * .9;
        double r = d / 2.0;
        double centerX = getWidth() / 2.0;
        double centerY = getHeight() / 2.0;
        boolean reversed = animationProgress % 2 >= 1 && value == INDETERMINATE;
        double progress = value == INDETERMINATE ? (reversed ? 1 - animationProgress % 1.0 : animationProgress % 1.0) : value;
        int lineCount = (int)Math.round(1.6 * Math.pow(d, 0.8));
        int shownLinesCount = (int)Math.ceil(lineCount * progress);
        double unitAngle = 2.0 * Math.PI / (double) lineCount;
        double phase = (animationProgress - 1.0) * Math.PI / 4.0;
        double phaseDeg = (animationProgress - 1.0) * 45.0;
        double arcStart = value == INDETERMINATE ? (- phaseDeg - 90.0 + (reversed ? 360 : -360) * progress) : 90.0;
        double arcLength = (reversed ? -360 : 360) * (value == INDETERMINATE ? progress : -value);

        Arc arc = new Arc();
        arc.setCenterX(centerX);
        arc.setCenterY(centerY);
        arc.setRadiusX(r);
        arc.setRadiusY(r);
        arc.setStartAngle(arcStart);
        arc.setLength(arcLength);
        arc.setStroke(Color.BLACK);
        arc.setStrokeWidth(2.0);
        arc.setFill(Color.TRANSPARENT);

        Ellipse circle = new Ellipse();
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadiusX(r);
        circle.setRadiusY(r);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(.5);
        circle.setFill(Color.TRANSPARENT);

        Line[] lines = new Line[shownLinesCount];

        for (int i = 0; i < shownLinesCount; i++) {
            double angle = i * unitAngle * (reversed ? -1.0 : 1.0);
            Line line = new Line();
            line.setStartX(centerX - r * Math.sin(angle + phase));
            line.setStartY(centerY + r * Math.cos(angle + phase));
            line.setEndX(centerX - r * Math.sin(2.0 * angle + phase));
            line.setEndY(centerY + r * Math.cos(2.0 * angle + phase));
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(.1);
            lines[i] = line;
        }

        this.getChildren().clear();

        for (Line line : lines)
            this.getChildren().add(line);

        this.getChildren().add(circle);
        this.getChildren().add(arc);
    }

    @Override
    public void setMinSize(final double minWidth, final double minHeight) {
        double w = Math.max(minWidth, 100);
        double h = Math.max(minHeight, 100);
        super.setMinSize(w, h);
    }

    private static final class PBAnimationTimer extends AnimationTimer {

        // The progress bar
        private final CardioidProgressBar progressBar;

        // ctor
        PBAnimationTimer(final CardioidProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public strictfp void handle(long time) {
            progressBar.animationProgress = ((time - progressBar.animationStartingTime) / 1000000000.0) % 8.0;
            progressBar.refresh();
        }
    }
}
