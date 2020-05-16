package md.jcore.material;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.border.Border;

public class MaterialCardBorder implements Border {

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int w = width - 1, h = height - 1;
		Path2D.Float shadowPath = new Path2D.Float();
		shadowPath.moveTo(x + 10, y + 6);
		shadowPath.lineTo(x + w - 10, y + 6);
		shadowPath.quadTo(x + w - 7, y + 6, x + w - 7, y + 10);
		shadowPath.lineTo(x + w - 7, y + h - 10);
		shadowPath.quadTo(x + w - 7, y + h - 8, x + w - 10, y + h - 8);
		shadowPath.lineTo(x + 10, y + h - 8);
		shadowPath.quadTo(x + 6, y + h - 8, x + 6, y + h - 11);
		shadowPath.lineTo(x + 6, y + 10);
		shadowPath.quadTo(x + 6, y + 6, x + 10, y + 6);
		shadowPath.closePath();
		
		Path2D.Float boxPath = new Path2D.Float();
		boxPath.moveTo(x + 8, y + 4);
		boxPath.lineTo(x + w - 8, y + 4);
		boxPath.quadTo(x + w - 4, y + 4, x + w - 4, y + 8);
		boxPath.lineTo(x + w - 4, y + h - 10);
		boxPath.quadTo(x + w - 4, y + h - 6, x + w - 8, y + h - 6);
		boxPath.lineTo(x + 8, y + h - 6);
		boxPath.quadTo(x + 4, y + h - 6, x + 4, y + h - 10);
		boxPath.lineTo(x + 4, y + 8);
		boxPath.quadTo(x + 4, y + 4, x + 8, y + 4);
		boxPath.closePath();

		Color background = c.getBackground();
		Color shadow = new Color(0, 0, 0, 20);

		g2d.setColor(c.getParent().getBackground());
		g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
		g2d.setColor(shadow);
		for(int i = 2; i <= 8; i += 2) {
			g2d.setStroke(new BasicStroke(i));
			g2d.draw(shadowPath);
		}
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(background);
		g2d.fill(boxPath);
		//g2d.setColor(Color.red);
		//g2d.draw(shadowPath);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(16, 4, 12, 4);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}
}
