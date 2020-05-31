package md.jcore.material;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import md.jcore.Utilities;
import md.jcore.material.laf.MaterialLookAndFeel;
import md.jcore.resources.CoreResources;

public final class MaterialUtilities {

    private MaterialUtilities() {
    }

    private static final EmptyBorder PADDING_TEXT = new EmptyBorder(0, 16, 0, 16);

    /**
     * Registers all font used in Material Design to {@link java.awt.GraphicsEnvironment GraphicsEnvironment}
     */
    public static void registerFonts() {
        CoreResources.loadFont("Roboto-Thin");
        CoreResources.loadFont("Roboto-ThinItalic");

        CoreResources.loadFont("Roboto-Light");
        CoreResources.loadFont("Roboto-LightItalic");

        CoreResources.loadFont("Roboto-Regular");
        CoreResources.loadFont("Roboto-Italic");

        CoreResources.loadFont("Roboto-Medium");
        CoreResources.loadFont("Roboto-MediumItalic");

        CoreResources.loadFont("Roboto-Bold");
        CoreResources.loadFont("Roboto-BoldItalic");
    }

    /**
     * Creates a heading as a {@link javax.swing.JLabel JLabel}.
     *
     * @param level Main header: 0, heading in content: 1, subheader in content: 2
     *              and so on
     * @return the appropriate {@link javax.swing.JLabel JLabel}.
     */
    public static JLabel createHeadingTextLabel(final int level) {
        JLabel label = new JLabel();
        label.setBorder(MaterialUtilities.PADDING_TEXT);

        switch (level) {
            case 3:
                label.setFont(new Font("Roboto", Font.BOLD, (int) (label.getFont().getSize() * 1.2 + 0.5)));
                break;
        }

        return label;
    }

    public static void initializeLookAndFeel(final int theme, final Color primaryColor, final Color secondaryColor) {
        try {
            MaterialLookAndFeel laf = new MaterialLookAndFeel(theme, primaryColor, secondaryColor);
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException ignored) {
            // should never happen
        }
    }

    public static JLabel createHeadingSecondaryTextLabel(final int level) {
        JLabel label = new JLabel();
        label.setBorder(PADDING_TEXT);
        label.setForeground(UIManager.getColor("Label.disabledForeground"));

        switch (level) {
            case 3:
                break;
        }

        return label;
    }
}
