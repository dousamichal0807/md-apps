package md.jcore.material;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import md.jcore.Utilities;
import md.jcore.material.laf.MaterialLookAndFeel;

public class MaterialUtilities {
	static final EmptyBorder PADDING_TEXT = new EmptyBorder(0, 16, 0, 16);

	public static void registerFonts() {
		Utilities.loadFont("Roboto-Thin");
		Utilities.loadFont("Roboto-ThinItalic");
		
		Utilities.loadFont("Roboto-Light");
		Utilities.loadFont("Roboto-LightItalic");
		
		Utilities.loadFont("Roboto-Regular");
		Utilities.loadFont("Roboto-Italic");
		
		Utilities.loadFont("Roboto-Medium");
		Utilities.loadFont("Roboto-MediumItalic");
		
		Utilities.loadFont("Roboto-Bold");
		Utilities.loadFont("Roboto-BoldItalic");
	}

	/**
	 * Creates a heading as a {@link javax.swing.JLabel JLabel}.
	 * 
	 * @param level Main header: 0, heading in content: 1, subheader in content: 2
	 *              and so on
	 * @return the appropriate {@link javax.swing.JLabel JLabel}.
	 */
	public static JLabel createHeadingTextLabel(int level) {
		JLabel label = new JLabel();
		label.setBorder(MaterialUtilities.PADDING_TEXT);
	
		switch (level) {
		case 3:
			label.setFont(new Font("Roboto", Font.BOLD, (int) (label.getFont().getSize() * 1.2 + 0.5)));
			break;
		}
	
		return label;
	}

	public static void initialize(int theme, Color primaryColor, Color secondaryColor) {
		try {
			MaterialLookAndFeel laf = new MaterialLookAndFeel(theme, primaryColor, secondaryColor);
			UIManager.setLookAndFeel(laf);
		} catch (UnsupportedLookAndFeelException ignored) {
			// should never happen
		}
	}
}
