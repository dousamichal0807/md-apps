package md.jcore.material.laf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;

import md.jcore.awt.AdvancedGraphics;
import md.jcore.material.components.MaterialButton;
import md.jcore.material.MaterialConstants;
import md.jcore.material.MaterialIcon;

/**
 * Button UI implementation for the Material Look and Feel
 * 
 * @author Michal Douša
 * @see MaterialLookAndFeel
 */
public class MaterialButtonUI extends BasicButtonUI {

	/**
	 * Creates {@link MaterialButtonUI} for specified {@link JButton}.
	 * 
	 * @param c the button to create UI on
	 * @return new {@link MaterialButtonUI} instance
	 */
	public static MaterialButtonUI createUI(final JComponent c) {
		return new MaterialButtonUI();
	}

	@Override
	public void paint(final Graphics g, final JComponent c) {
		Graphics2D g2d = (Graphics2D) g;
		AdvancedGraphics gadv = new AdvancedGraphics(g2d);
		JButton button = (JButton) c;
		Font font = UIManager.getFont("Button.font");
		FontMetrics fontMetrics = g2d.getFontMetrics(font);
		String text = getButtonText(button);
		BufferedImage icon = getButtonIcon(button);
		Path2D.Float path = new Path2D.Float();
		boolean primary = false;
		int elevation = MaterialConstants.ELEVATION_FLAT;
		int textWidth = text == null ? 0 : fontMetrics.stringWidth(text);
		int textHeight = fontMetrics.getHeight();
		int textYOffset = fontMetrics.getAscent();
		Point buttonScrPos = button.getLocationOnScreen();
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
		Rectangle buttonBounds = new Rectangle(buttonScrPos.x, buttonScrPos.y, button.getWidth(), button.getHeight());

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(font);

		if (button instanceof MaterialButton && !(button.getParent() instanceof JToolBar)) {
			MaterialButton mButton = (MaterialButton) button;
			primary = mButton.isPrimary();
			elevation = mButton.getElevation();
		}
		if (button.getParent() instanceof JToolBar) {
			path.moveTo(0, 4);
			path.quadTo(0, 0, 4, 0);
			path.lineTo(button.getWidth() - 4, 0);
			path.quadTo(button.getWidth(), 0, button.getWidth(), 4);
			path.lineTo(button.getWidth(), button.getHeight() - 4);
			path.quadTo(button.getWidth(), button.getHeight(), button.getWidth() - 4, button.getHeight());
			path.lineTo(4, button.getHeight());
			path.quadTo(0, button.getHeight(), 0, button.getHeight() - 4);
			path.closePath();
		} else {
			path.moveTo(button.getWidth() - 7, 4);
			path.quadTo(button.getWidth() - 4, 4, button.getWidth() - 4, 7);
			path.lineTo(button.getWidth() - 4, button.getHeight() - 7);
			path.quadTo(button.getWidth() - 4, button.getHeight() - 4, button.getWidth() - 7, button.getHeight() - 4);
			path.lineTo(7, button.getHeight() - 4);
			path.quadTo(4, button.getHeight() - 4, 4, button.getHeight() - 7);
			path.lineTo(4, 7);
			path.quadTo(4, 4, 7, 4);
			path.closePath();
		}

		gadv.fillRectangle(button.getParent().getBackground(), new Rectangle(0, 0, c.getWidth(), c.getHeight()));

		if (elevation == MaterialConstants.ELEVATION_ELEVATED) {
			g2d.setColor(new Color(0, 0, 0, 7));
			for (int i = 1; i <= 6; i++) {
				g2d.setStroke(new BasicStroke(i));
				g2d.draw(path);
			}
			g2d.setStroke(new BasicStroke(1));
		}
		if (primary) {
			g2d.setColor(UIManager.getColor("Button.primaryBackground"));
			g2d.fill(path);

		} else {
			if (elevation == MaterialConstants.ELEVATION_ELEVATED) {
				g2d.setColor(UIManager.getColor("Card.background"));
				g2d.fill(path);
			} else if (elevation == MaterialConstants.ELEVATION_OUTLINED) {
				g2d.setColor(UIManager.getColor("Separator.foreground"));
				g2d.draw(path);
			}
			if (buttonBounds.contains(mousePosition) && button.isEnabled()) {
				Color baseColor = button.getParent() instanceof JToolBar ? button.getParent().getForeground()
						: UIManager.getColor("Button.foreground");
				g2d.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 32));
				g2d.fill(path);
			}
		}
		if (!button.isEnabled()) g2d.setColor(UIManager.getColor("Button.disabledForeground"));
		else if (primary) g2d.setColor(UIManager.getColor("Button.primaryForeground"));
		else if (button.getParent() instanceof JToolBar) g2d.setColor(button.getParent().getForeground());
		else g2d.setColor(UIManager.getColor("Button.foreground"));
		if (text != null && icon == null) g2d.drawString(text,
				(button.getParent() instanceof JToolBar ? (button.getHeight() - textHeight) / 2
						: (button.getWidth() - textWidth) / 2),
				textYOffset + (button.getHeight() - textHeight) / 2);
	}

	private static BufferedImage getButtonIcon(final JButton button) {
		if (!(button instanceof MaterialButton)) // Not a Material button
			return null;
		MaterialButton mb = (MaterialButton) button;
		MaterialIcon mi = mb.getMaterialIcon();
		if (mi == null)
			return null;
		return mi.getAWTImage(1f,
				UIManager.getColor(mb.isPrimary() ? "Button.primaryForeground" : "Button.foreground"));
	}

	private static String getButtonText(final JButton button) {
		return (button.getParent() instanceof JToolBar || button.getText() == null) ? button.getText()
				: button.getText().toUpperCase((Locale) UIManager.get("Locale"));
	}

	@Override
	public Dimension getMinimumSize(final JComponent c) {
		return getPreferredSize(c);
	}

	@Override
	public Dimension getPreferredSize(final JComponent c) {
		JButton button = (JButton) c;
		Font font = UIManager.getFont("Button.font");
		FontMetrics fontMetrics = button.getFontMetrics(font);
		String text = getButtonText(button);
		BufferedImage icon = getButtonIcon(button);
		int w = text == null ? 0 : fontMetrics.stringWidth(text), h = fontMetrics.getHeight();
		return new Dimension(w + (c.getParent() instanceof JToolBar ? 16 : 48) + (icon == null ? 0 : 28),
				h + (c.getParent() instanceof JToolBar ? 16 : 24));
	}

	@Override
	public Dimension getMaximumSize(final JComponent c) {
		return getPreferredSize(c);
	}
}
