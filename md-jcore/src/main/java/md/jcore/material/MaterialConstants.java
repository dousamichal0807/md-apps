package md.jcore.material;

import md.jcore.material.components.MaterialButton;
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

}
