package md.jcore.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class contains advanced shorthand methods for drawing an AWT or Swing
 * component.
 * 
 * @author Michal Douša
 */
public final class AdvancedGraphics {

	public static final byte TEXT_ALIGN_LEFT = 0;

	public static final byte TEXT_ALIGN_RIGHT = 2;

	public static final byte TEXT_ALIGN_TOP = 0;

	public static final byte TEXT_ALIGN_BOTTOM = 2;

	public static final byte TEXT_ALIGN_CENTER = 1;

	private final Graphics2D baseGraphics;

	/**
	 * Returns the {@link java.awt.Graphics2D Graphics2D} object which
	 * {@code AdvancedGraphics} object is using.
	 * 
	 * @return the base {@link java.awt.Graphics2D Graphics2D} object
	 */
	public Graphics2D getBaseGraphics() {
		return baseGraphics;
	}

	/**
	 * Constructs an {@code AdvancedGraphics} object using given
	 * {@link java.awt.Graphics2D}. The only constructor of this class.
	 * 
	 * @param graphics {@link java.awt.Graphics2D} to be used
	 * 
	 * @throws NullPointerException if {@code null} is passed instead of
	 *                              {@link java.awt.Graphics2D} object
	 * 
	 * @see #getBaseGraphics()
	 */
	public AdvancedGraphics(final Graphics2D graphics) {
		if (graphics == null)
			throw new NullPointerException("Passed java.awt.Graphics2D object cannot be null");
		this.baseGraphics = graphics;
	}

	/**
	 * Draws given ellipse with specified color and stroke.
	 * 
	 * @param paint   {@link java.awt.Paint} to be used as stroke paint
	 * @param stroke  {@link java.awt.Stroke} to be used as stroke
	 * @param ellipse {@link java.awt.geom.Ellipse2D} to be drawn
	 * 
	 * @throws NullPointerException if {@code null} is passed instead of
	 *                              {@link java.awt.geom.Ellipse2D} object
	 */
	public void drawEllipse(final Paint paint, final Stroke stroke, final Ellipse2D ellipse) {
		if (paint != null)
			baseGraphics.setPaint(paint);
		if (stroke != null)
			baseGraphics.setStroke(stroke);

		baseGraphics.drawOval((int) Math.round(ellipse.getX()), (int) Math.round(ellipse.getY()),
				(int) Math.round(ellipse.getWidth()), (int) Math.round(ellipse.getHeight()));
	}

	/**
	 * Draws a line with specified {@link Stroke} and {@link Paint}.
	 * 
	 * @param paint  {@link Paint} to be used
	 * @param stroke {@link Stroke} to be used
	 * @param line   {@link Line2D} to be drawn
	 */
	public void drawLine(final Paint paint, final Stroke stroke, final Line2D line) {
		if (paint != null)
			baseGraphics.setPaint(paint);
		if (stroke != null)
			baseGraphics.setStroke(stroke);
		
		int x1 = (int) (line.getX1() + .5);
		int y1 = (int) (line.getY1() + .5);
		int x2 = (int) (line.getX2() + .5);
		int y2 = (int) (line.getY2() + .5);

		baseGraphics.drawLine(x1, y1, x2, y2);
	}

	/**
	 * Draws given rectangle with specified color and stroke.
	 * 
	 * @param paint  {@link java.awt.Paint} to be used as stroke paint
	 * @param stroke {@link java.awt.Stroke} to be used as stroke
	 * @param rect   {@link java.awt.geom.Rectangle2D} to be drawn
	 * 
	 * @throws NullPointerException if {@code null} is passed instead of
	 *                              {@link java.awt.geom.Rectangle2D} object
	 */
	public void drawRectangle(final Paint paint, final Stroke stroke, final Rectangle2D rect) {
		if (paint != null)
			baseGraphics.setPaint(paint);
		if (stroke != null)
			baseGraphics.setStroke(stroke);

		baseGraphics.drawRect((int) Math.round(rect.getX()), (int) Math.round(rect.getY()),
				(int) Math.round(rect.getWidth()), (int) Math.round(rect.getHeight()));
	}

	/**
	 * Draws given rectangle with specified color and stroke.
	 * 
	 * @param paint  {@link java.awt.Paint} to be used as stroke paint
	 * @param stroke {@link java.awt.Stroke} to be used as stroke
	 * @param shape  {@link java.awt.Shape} to be drawn
	 * 
	 * @throws NullPointerException if {@code null} is passed instead of
	 *                              {@link java.awt.Shape} object
	 */
	public void drawShape(final Paint paint, final Stroke stroke, final Shape shape) {
		if (paint != null)
			baseGraphics.setPaint(paint);
		if (stroke != null)
			baseGraphics.setStroke(stroke);

		baseGraphics.draw(shape);
	}

	/**
	 * Draws specified single-line text with specified color, font, alignment and
	 * reference point to be aligned from.
	 * 
	 * @param color           color to be text drawn with
	 * @param font            font of the text
	 * @param text            the particular text to be drawn
	 * @param point           the reference point
	 * @param horizontalAlign horizontal alignment of the text (possible values:
	 *                        {@link #TEXT_ALIGN_LEFT}, {@link #TEXT_ALIGN_CENTER},
	 *                        {@link #TEXT_ALIGN_RIGHT})
	 * @param verticalAlign   vertical alignment of the text (possible values:
	 *                        {@link #TEXT_ALIGN_TOP}, {@link #TEXT_ALIGN_CENTER},
	 *                        {@link #TEXT_ALIGN_BOTTOM})
	 * 
	 * @throws IllegalArgumentException if multi-line text and/or illegal alignment
	 *                                  value is passed
	 */
	public void drawText(final Color color, final Font font, final String text, final Point2D point,
			final byte horizontalAlign, final byte verticalAlign) {
		if (color != null)
			baseGraphics.setColor(color);
		if (font != null)
			baseGraphics.setFont(font);
		if (text.matches(".*[\r\n].*"))
			throw new IllegalArgumentException("Multi-line text");
		if (horizontalAlign < 0 || horizontalAlign > 2 || verticalAlign < 0 || verticalAlign > 2)
			throw new IllegalArgumentException("Illegal value for alignment of the text");

		// AWT - Font Metriccs
		FontMetrics fontMetrics = baseGraphics.getFontMetrics();
		int lineHeight = fontMetrics.getHeight();
		int lineWidth = fontMetrics.stringWidth(text);
		int lineAscent = fontMetrics.getAscent();

		// AWT - base point
		Point2D.Double basePoint = new Point2D.Double(point.getX(), point.getY());
		basePoint.x -= lineWidth * horizontalAlign * .5;
		basePoint.y += lineAscent - lineHeight * verticalAlign * .5;

		// AWT - draw
		baseGraphics.drawString(text, (float) basePoint.x, (float) basePoint.y);
	}

	public void drawText(final Color color, final Font font, final String text, final Point2D point) {
		drawText(color, font, text, point, TEXT_ALIGN_LEFT, TEXT_ALIGN_TOP);
	}

	/**
	 * Fills given {@link java.awt.geom.Ellipse2D} with given
	 * {@link java.awt.Paint}.
	 * 
	 * @param paint   {@link java.awt.Paint} to be used as filament
	 * @param ellipse {@link java.awt.geom.Ellipse2D} to be filled
	 * 
	 * @throws NullPointerException if {@code null} is passed instead of
	 *                              {@link java.awt.geom.Ellipse2D} object
	 */
	public void fillEllipse(final Paint paint, final Ellipse2D ellipse) {
		if (paint != null)
			baseGraphics.setPaint(paint);

		baseGraphics.fillOval((int) Math.round(ellipse.getX()), (int) Math.round(ellipse.getY()),
				(int) Math.round(ellipse.getWidth()), (int) Math.round(ellipse.getHeight()));
	}

	/**
	 * Fills given {@link java.awt.geom.Rectangle2D} with given
	 * {@link java.awt.Paint}.
	 * 
	 * @param paint {@link java.awt.Paint} to be used as filament
	 * @param rect  {@link java.awt.geom.Rectangle2D} to be filled
	 * 
	 * @throws NullPointerException if {@code null} is passed instead of
	 *                              {@link java.awt.geom.Rectangle2D} object
	 */
	public void fillRectangle(final Paint paint, final Rectangle2D rect) {
		if (paint != null)
			baseGraphics.setPaint(paint);

		baseGraphics.fillRect((int) Math.round(rect.getX()), (int) Math.round(rect.getY()),
				(int) Math.round(rect.getWidth()), (int) Math.round(rect.getHeight()));
	}

	/**
	 * Fills given {@link java.awt.Shape} with given {@link java.awt.Paint}.
	 * 
	 * @param paint {@link java.awt.Paint} to be used as filament
	 * @param shape {@link java.awt.Shape} to be filled
	 * 
	 * @throws NullPointerException if {@code null} is passed instead of // *
	 *                              {@link java.awt.Shape} object
	 */
	public void fillShape(final Paint paint, final Shape shape) {
		if (paint != null)
			baseGraphics.setPaint(paint);

		baseGraphics.fill(shape);
	}

	public void paintImage(final Image img, final Rectangle2D rect) {
		baseGraphics.setPaint(null);
		// XY coordinates
		int x = (int) (Math.round(rect.getX()));
		int y = (int) (Math.round(rect.getY()));
		// Size (width, height)
		int w = (int) (Math.round(rect.getWidth()));
		int h = (int) (Math.round(rect.getHeight()));
		// Drawing
		baseGraphics.drawImage(img, x, y, w, h, null);
	}

	public void paintImage(final Image image, final Point2D position, final Dimension2D size) {
		baseGraphics.setPaint(null);
		// XY coordinates
		int x = (int) (Math.round(position.getX()));
		int y = (int) (Math.round(position.getY()));
		// Size (width, height)
		int w = (int) (Math.round(size.getWidth()));
		int h = (int) (Math.round(size.getHeight()));
		// Drawing
		baseGraphics.drawImage(image, x, y, w, h, null);
	}

}
