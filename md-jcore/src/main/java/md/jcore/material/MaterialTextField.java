package md.jcore.material;

import javax.swing.JTextField;

public class MaterialTextField extends JTextField {
	
	private static final long serialVersionUID = 0x0100L;
	
	private String labelText;

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(final String labelText) {
		if (labelText == null) {
			this.labelText = null;
		} else {
			String trimmed = labelText.trim();
			this.labelText = trimmed.isEmpty() ? null : trimmed;
		}
	}
}
