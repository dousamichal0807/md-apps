package md.jcore.material.laf;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ToolBarUI;

public class MaterialToolBarUI extends ToolBarUI {

	public static MaterialToolBarUI createUI(JComponent component) {
		return new MaterialToolBarUI();
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		g.setColor(c.getParent().getBackground());
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		for(int i = 0; i < c.getComponentCount(); i++) {
			c.getComponent(i).repaint();
		}
	}
}
