package md.jcore.material;

import javax.swing.JLabel;
import javax.swing.UIManager;

import md.jcore.material.laf.MaterialLookAndFeel;

public final class MaterialConstants {

	/**
	 * One of the possible values for the elevation: a flat component with no
	 * border.
	 * 
	 * @see MaterialButton
	 */
	public static final int ELEVATION_FLAT = 0;

	/**
	 * One of the possible values for the elevation: a flat component with thin
	 * border.
	 * 
	 * @see MaterialButton
	 */
	public static final int ELEVATION_OUTLINED = 1;

	/**
	 * One of the possible values for the elevation: a component with shadow.
	 * 
	 * @see MaterialButton
	 */
	public static final int ELEVATION_ELEVATED = 2;

	/**
	 * Constant for setting the light theme.
	 * 
	 * @see MaterialLookAndFeel
	 */
	public static final int THEME_LIGHT = 0;

	/**
	 * Constant for setting the dark theme.
	 * 
	 * @see MaterialLookAndFeel
	 */
	public static final int THEME_DARK = 1;

	public static JLabel createHeadingSecondaryTextLabel(int level) {
		JLabel label = new JLabel();
		label.setBorder(MaterialUtilities.PADDING_TEXT);
		label.setForeground(UIManager.getColor("Label.disabledForeground"));

		switch (level) {
		case 3:
			break;
		}

		return label;
	}

}
