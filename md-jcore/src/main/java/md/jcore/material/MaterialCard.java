package md.jcore.material;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

import md.jcore.material.laf.MaterialLookAndFeel;

public class MaterialCard extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public MaterialCard() {
		this.border = new MaterialCardBorder();
		super.setBorder(border);
	}

	// Border -----------------------------------------------------------------
	
	private MaterialCardBorder border;
	
	public void setBorder(Border border) {
		// do nothing
	}
	
	// Color ------------------------------------------------------------------
	
	public Color getBackground() {
		try {
			switch (((MaterialLookAndFeel) UIManager.getLookAndFeel()).getTheme()) {
			case MaterialConstants.THEME_LIGHT:
				return new Color(255, 255, 255);
			case MaterialConstants.THEME_DARK:
				return new Color(32, 32, 32);
			default:
				return super.getBackground();
			}
		} catch(ClassCastException exc) {
			throw new IllegalStateException("Material Look and Feel not applied");
		}
	}
}
