package md.jcore.material.components;

import md.jcore.material.MaterialConstants;
import md.jcore.material.MaterialIcon;

import javax.swing.JButton;

/**
 * Represents the Material button. More about buttons in Material Design on
 * Material Design website: https://material.io/components/buttons/
 * 
 * @author Michal Douša
 *
 */
public class MaterialButton extends JButton {
	private static final long serialVersionUID = 1L;

	private int elevation;
	private boolean isPrimary;
	private MaterialIcon materialIcon;

	/**
	 * Gets the Material button elevation. More information:
	 * https://material.io/components/buttons/.
	 * 
	 * @return Material button elevation
	 * @see #setElevation(int)
	 */
	public int getElevation() {
		return elevation;
	}

	/**
	 * Sets the Material button elevation. Permitted values are
	 * {@link MaterialConstants#ELEVATION_FLAT},
	 * {@link MaterialConstants#ELEVATION_OUTLINED} and
	 * {@link MaterialConstants#ELEVATION_ELEVATED} only. More information:
	 * https://material.io/components/buttons/.
	 * 
	 * @param elevation Material button elevation
	 * @throws IllegalArgumentException if the passed value is not permitted as
	 *                                  described above.
	 * @see #getElevation()
	 */
	public void setElevation(int elevation) {
		if (elevation != MaterialConstants.ELEVATION_FLAT && elevation != MaterialConstants.ELEVATION_OUTLINED
				&& elevation != MaterialConstants.ELEVATION_ELEVATED)
			throw new IllegalArgumentException("Invalid elevation argument passed");
		this.elevation = elevation;
	}

	/**
	 * Gets if the Material button does primary action. More information:
	 * https://material.io/components/buttons/.
	 * 
	 * @return if the Material button does primary action
	 * @see #setIsPrimary(boolean)
	 */
	public boolean isPrimary() {
		return isPrimary;
	}

	/**
	 * Sets if the Material button does primary action. More information:
	 * https://material.io/components/buttons/.
	 * 
	 * @param primary {@code boolean} value if the Material button does primary
	 *                action
	 * @see #isPrimary()
	 */
	public void setIsPrimary(boolean primary) {
		this.isPrimary = primary;
	}

	/**
	 * Gets the icon used for this button.
	 * 
	 * @return used {@link MaterialIcon}
	 */
	public MaterialIcon getMaterialIcon() {
		return materialIcon;
	}

	/**
	 * Sets the icon for the button. If you don't want use the icon, {@code null}
	 * can be also passed.
	 * 
	 * @param icon {@link MaterialIcon} to be used on this button
	 */
	public void setMaterialIcon(MaterialIcon icon) {
		this.materialIcon = icon;
	}

	public MaterialButton(MaterialIcon icon, String text, boolean primary, int elevation) {
		this.setMaterialIcon(icon);
		this.setText(text);
		this.setIsPrimary(primary);
		this.setElevation(elevation);
	}

	public MaterialButton(MaterialIcon icon, boolean primary, int elevation) {
		this(icon, null, primary, elevation);
	}

	public MaterialButton(String text, boolean primary, int elevation) {
		this(null, text, primary, elevation);
	}

	public MaterialButton(boolean primary, int elevation) {
		this(null, null, primary, elevation);
	}

	public MaterialButton(String text) {
		this(text, false, MaterialConstants.ELEVATION_OUTLINED);
	}

	public MaterialButton(MaterialIcon icon) {
		this(icon, false, MaterialConstants.ELEVATION_OUTLINED);
	}

	public MaterialButton() {
		this(null, null, false, MaterialConstants.ELEVATION_OUTLINED);
	}
}
