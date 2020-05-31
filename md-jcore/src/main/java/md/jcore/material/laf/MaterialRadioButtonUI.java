package md.jcore.material.laf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import md.jcore.awt.AdvancedGraphics;

public class MaterialRadioButtonUI extends BasicRadioButtonUI {

	public static MaterialRadioButtonUI createUI(final JComponent component) {
		return new MaterialRadioButtonUI();
	}

	@Override
	public synchronized void paint(final Graphics g, final JComponent c) {
		JRadioButton radioButton = (JRadioButton) c;

		Graphics2D g2d = (Graphics2D) g;
		AdvancedGraphics gadv = new AdvancedGraphics(g2d);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D.Float markOuter = new Ellipse2D.Float(8f, 8f, 20f, 20f);
		Ellipse2D.Float markInner = new Ellipse2D.Float(10f, 10f, 16f, 16f);
		Ellipse2D.Float selection = new Ellipse2D.Float(13f, 13f, 10f, 10f);
		Color background = c.getParent().getBackground();
		gadv.fillRectangle(background, new Rectangle(0, 0, c.getWidth(), c.getHeight()));

		if (radioButton.isSelected()) {
			Color color = UIManager
					.getColor(radioButton.isEnabled() ? "RadioButton.selectedMark" : "RadioButton.disabledMark");
			gadv.fillEllipse(color, markOuter);
			gadv.fillEllipse(background, markInner);
			gadv.fillEllipse(color, selection);
		} else {
			Color color = UIManager
					.getColor(radioButton.isEnabled() ? "RadioButton.unselectedMark" : "RadioButton.disabledMark");
			gadv.fillEllipse(color, markOuter);
			gadv.fillEllipse(background, markInner);
		}

		if (radioButton.isFocusOwner() && radioButton.isEnabled()) {
			Color focus = new Color(UIManager
					.getColor(radioButton.isSelected() ? "RadioButton.selectedMark" : "RadioButton.unselectedMark")
					.getRGB() + 0x20000000, true);
			gadv.fillEllipse(focus, new Ellipse2D.Float(0f, 0f, 36f, 36f));
		}

		Color textColor = UIManager.getColor(radioButton.isEnabled() ? "Label.foreground" : "Label.disabledForeground");
		gadv.drawText(textColor, UIManager.getFont("Label.font"), radioButton.getText(), new Point(36, 18), AdvancedGraphics.TEXT_ALIGN_LEFT,
				AdvancedGraphics.TEXT_ALIGN_CENTER);
	}

	@Override
	public Dimension getMinimumSize(final JComponent c) {
		return getPreferredSize(c);
	}

	@Override
	public Dimension getPreferredSize(final JComponent c) {
		JRadioButton radioButton = (JRadioButton) c;
		FontMetrics fontMetrics = c.getFontMetrics(UIManager.getFont("Label.font"));
		String text = radioButton.getText();
		int textWidth = (text == null ? 0 : fontMetrics.stringWidth(text));
		return new Dimension(textWidth + 54, 36);
	}
}
