package md.jsk.mathcalc.components;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public final class CardioidIndeterminateProgressBar extends Canvas {
    private final Animation animation;
    private double animationProgress;
    private long animationStartingTime;

    @Override
    public double minWidth(final double height) {
        return 20.0;
    }

    @Override
    public double minHeight(final double width) {
        return 20.0;
    }

    public CardioidIndeterminateProgressBar() {
        animation = new Animation(this);
        animationProgress = 0.0;
        animationStartingTime = System.nanoTime();

        Bindings.selectBoolean(this.sceneProperty(), "window", "showing").addListener((observable, wasShown, isShown) -> {
            if (isShown) {
                animation.start();
                if (!wasShown)
                    animationStartingTime = System.nanoTime();
            } else
                animation.stop();
        });
    }

    public synchronized strictfp void repaint() {
        double d = Math.min(this.getWidth(), this.getHeight()) * .9;
        double r = d / 2.0;
        double offsetX = (getWidth() - d) / 2.0;
        double offsetY = (getHeight() - d) / 2.0;
        double centerX = getWidth() / 2.0;
        double centerY = getHeight() / 2.0;
        boolean reversed = animationProgress % 2 >= 1;
        double progress = reversed ? 1 - animationProgress % 1.0 : animationProgress % 1.0;
        int p = (int)Math.round(6 * d);
        int ps = (int)Math.ceil(p * progress);
        double unitAngle = 2.0 * Math.PI / (double) p;
        double phase = (animationProgress - 1.0) * Math.PI / 4.0;
        double phaseDeg = (animationProgress - 1.0) * 45.0;
        double arcstart = - phaseDeg - 90.0 + (reversed ? 360 * progress : -360 * progress);
        double arcend = (reversed ? -360 * progress : 360 * progress);
        double[][] points = new double[ps][4];

        for (int i = 0; i < ps; i++) {
            double[] rec = points[i];
            double angle = i * unitAngle * (reversed ? -1.0 : 1.0);
            rec[0] = centerX - r * Math.sin(angle + phase);
            rec[1] = centerY + r * Math.cos(angle + phase);
            rec[2] = centerX - r * Math.sin(2.0 * angle + phase);
            rec[3] = centerY + r * Math.cos(2.0 * angle + phase);
        }

        GraphicsContext ctx = this.getGraphicsContext2D();
        ctx.setFill(Color.WHITE);
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(.01);

        ctx.fillRect(0, 0, getWidth(), getHeight());

        for (double[] rec : points)
            ctx.strokeLine(rec[0], rec[1], rec[2], rec[3]);
        ctx.strokeArc(offsetX, offsetY, d, d, .0, 360.0, ArcType.OPEN);

        ctx.setLineWidth(2);
        ctx.strokeArc(offsetX, offsetY, d, d, arcstart, arcend, ArcType.OPEN);
    }

    private static final class Animation extends AnimationTimer {

        // The progress bar
        private final CardioidIndeterminateProgressBar progressBar;

        // ctor
        Animation(final CardioidIndeterminateProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public strictfp void handle(long time) {
            progressBar.animationProgress = ((time - progressBar.animationStartingTime) / 1000000000.0) % 8.0;
            progressBar.repaint();
        }
    }
}
