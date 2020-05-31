package md.jcore.material.laf;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSeparatorUI;

import md.jcore.awt.AdvancedGraphics;

public class MaterialSeparatorUI extends BasicSeparatorUI {
	
	public static MaterialSeparatorUI createUI(final JComponent c) {
		return new MaterialSeparatorUI();
	}

	@Override
	public void paint(final Graphics g, final JComponent c) {
		// Cast to javax.swing.JSeparator
		JSeparator separator = (JSeparator) c;
		
		// Graphics
		AdvancedGraphics gadv = new AdvancedGraphics((Graphics2D) g);
		
		// Center point
		float centerX = c.getWidth() / 2f;
		float centerY = c.getHeight() / 2f;
		
		// Create Line2D
		Line2D.Float line = new Line2D.Float(centerX, centerY, centerX, centerY);

		// Adjust Line2D
		switch (separator.getOrientation()) {
		// Horizontal separator
		case JSeparator.HORIZONTAL:
			line.x1 = 4f;
			line.x2 = c.getWidth() - 4f;
			break;

		// Vertical separator
		case JSeparator.VERTICAL:
			line.y1 = 4f;
			line.y2 = c.getHeight() - 4f;
			break;

		// What!?
		default:
			throw new IllegalArgumentException("Illegal javax.swing.JSeparator orientation");
		}
		
		gadv.drawLine(UIManager.getColor("Separator.foreground"), new BasicStroke(1), line);
	}

	@Override
	public Dimension getPreferredSize(final JComponent c) {
		return new Dimension(9, 9);
	}

	@Override
	public Dimension getMinimumSize(final JComponent c) {
		return new Dimension(9, 9);
	}

	@Override
	public Dimension getMaximumSize(final JComponent c) {
		// Cast to javax.swing.JSeparator
		JSeparator separator = (JSeparator) c;
		
		switch (separator.getOrientation()) {
		// Horizontal separator
		case JSeparator.HORIZONTAL:
			return new Dimension(Integer.MAX_VALUE, 9);

		// Vertical separator
		case JSeparator.VERTICAL:
			return new Dimension(9, Integer.MAX_VALUE);

		// What!?
		default:
			throw new IllegalArgumentException("Illegal javax.swing.JSeparator orientation");
		}
	}

}
