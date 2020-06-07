package mdlib.utils;

import java.util.Formatter;

public final class ColorUtilities {

    // Do not create any instances
    private ColorUtilities() {
    }

    public static String colorToHexRGB(final javafx.scene.paint.Color color) {
        StringBuilder sb = new StringBuilder(7);
        Formatter formatter = new Formatter(sb);
        sb.append('#');
        formatter.format("%02X%02X%02X%02X",
                (int)(255 * color.getRed()),
                (int)(255 * color.getGreen()),
                (int)(255 * color.getBlue()),
                (int)(255 * color.getOpacity()));
        return sb.toString();
    }

    public static String colorToHexRGB(final java.awt.Color color) {
        StringBuilder sb = new StringBuilder(7);
        Formatter formatter = new Formatter(sb);
        sb.append('#');
        formatter.format("%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        return sb.toString();
    }
}