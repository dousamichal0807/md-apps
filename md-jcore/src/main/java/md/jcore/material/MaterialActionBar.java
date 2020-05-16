package md.jcore.material;

import java.awt.Color;
import javax.swing.JToolBar;

public class MaterialActionBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	public Color getBackground() {
		return this.getParent().getBackground();
	}
}
