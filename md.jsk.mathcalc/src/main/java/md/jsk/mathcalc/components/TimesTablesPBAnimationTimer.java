package md.jsk.mathcalc.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import mdlib.mdfx.components.MDFXProgressBar;

import static mdlib.mdfx.components.MDFXProgressBar.INDETERMINATE;

public final class TimesTablesPBAnimationTimer implements MDFXProgressBar.Animator {

    private int pointCount;
    private int multiplier;

    public TimesTablesPBAnimationTimer() {
        this(200, 51);
    }

    public TimesTablesPBAnimationTimer(final int pointCount, final int multiplier) {
        setPointCount(pointCount);
        setMultiplier(multiplier);
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(final int pointCount) {
        this.pointCount = Math.max(pointCount, 20);
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(final int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public strictfp void refresh(final long nanoTime, final MDFXProgressBar progressBar) {
        double timeSecs = nanoTime / 1000000000.0;
        double animationProgress = (progressBar.getValue() == INDETERMINATE) ?
                (timeSecs % 8.0) : ((timeSecs % 32.0) / 4.0);

        double pbValue = progressBar.getValue();
        double d = Math.min(progressBar.getWidth(), progressBar.getHeight()) * .9;
        double r = d / 2.0;
        double centerX = progressBar.getWidth() / 2.0;
        double centerY = progressBar.getHeight() / 2.0;
        boolean reversed = animationProgress % 2 >= 1 && pbValue == INDETERMINATE;
        double progress = pbValue == INDETERMINATE ? (reversed ? 1 - animationProgress % 1.0 : animationProgress % 1.0) : pbValue;
        double shownLinesCount = pointCount * progress;
        double lineStrokeWidth = .05 * Math.exp(d / 300);
        double unitAngle = 2.0 * Math.PI / (double) pointCount;
        double phase = (animationProgress - 1.0) * Math.PI / 4.0;
        double phaseDeg = (animationProgress - 1.0) * 45.0;
        double arcStart = progressBar.getValue() == INDETERMINATE ? (-phaseDeg - 90.0 + (reversed ? 360 : -360) * progress) : 90.0;
        double arcLength = (reversed ? -360 : 360) * (pbValue == INDETERMINATE ? progress : -pbValue);

        Arc arc = new Arc();
        arc.setCenterX(centerX);
        arc.setCenterY(centerY);
        arc.setRadiusX(r);
        arc.setRadiusY(r);
        arc.setStartAngle(arcStart);
        arc.setLength(arcLength);
        arc.setStroke(Color.BLACK);
        arc.setStrokeWidth(20.0 * lineStrokeWidth);
        arc.setFill(Color.TRANSPARENT);

        Ellipse circle = new Ellipse();
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadiusX(r);
        circle.setRadiusY(r);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(lineStrokeWidth);
        circle.setFill(Color.TRANSPARENT);

        Line[] lines = new Line[(int)Math.ceil(shownLinesCount)];

        for (int i = 0; i < lines.length; i++) {
            double angle = i * unitAngle * (reversed ? -1.0 : 1.0);
            Line line = new Line();
            line.setStartX(centerX - r * Math.sin(angle + phase));
            line.setStartY(centerY + r * Math.cos(angle + phase));
            line.setEndX(centerX - r * Math.sin(multiplier * angle + phase));
            line.setEndY(centerY + r * Math.cos(multiplier * angle + phase));
            line.setStroke(Color.BLACK);
            line.setStrokeWidth((((i == (lines.length - 1)) && ((shownLinesCount % 1.0) != 0.0)) ?
                    (shownLinesCount % 1.0) : 1.0) * lineStrokeWidth);
            lines[i] = line;
        }

        progressBar.getChildren().clear();

        for (Line line : lines)
            progressBar.getChildren().add(line);

        progressBar.getChildren().add(circle);
        progressBar.getChildren().add(arc);
    }
}