package md.jsk.mathcalc.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public final class CardioidProgressBar extends Canvas {

    private double value;

    /**
     * Returns the current value of this progress bar in range from 0 to 1 (inclusive)
     * @return the current value of the progress bar
     */
    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        if (value < 0 || value > 1)
            throw new IllegalArgumentException("Value not in permitted range from 0 to 1");
        this.value = value;
        this.repaint();
    }

    public CardioidProgressBar() {
        value = .0;
    }

    public CardioidProgressBar(double value) {
        setValue(value);
    }

    public void repaint() {
        double d = Math.min(this.getWidth(), this.getHeight()) * .9;
        double r = d / 2.0;
        double ox = (getWidth() - d) / 2.0;
        double oy = (getHeight() - d) / 2.0;
        double cx = getWidth() / 2.0;
        double cy = getHeight() / 2.0;
        int p = (int)Math.round(d);
        int ps = (int)Math.ceil(p * value);
        double a = 2.0 * Math.PI / (double)p;
        double[][] points = new double[ps][4];

        for (int i = 0; i < ps; i++) {
            double[] rec = points[i];
            rec[0] = cx - r * Math.sin(i * a);
            rec[1] = cy + r * Math.cos(i * a);
            rec[2] = cx - r * Math.sin(2.0 * i * a);
            rec[3] = cy + r * Math.cos(2.0 * i * a);
        }

        GraphicsContext ctx = this.getGraphicsContext2D();
        ctx.setFill(Color.WHITE);
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(.1);

        ctx.fillRect(0, 0, getWidth(), getHeight());

        for (double[] rec : points)
            ctx.strokeLine(rec[0], rec[1], rec[2], rec[3]);
        ctx.strokeArc(ox, oy, d, d, .0, 360.0, ArcType.OPEN);

        ctx.setLineWidth(2);
        ctx.strokeArc(ox, oy, d, d, 90.0, - 360.0 * value, ArcType.OPEN);
    }
}
