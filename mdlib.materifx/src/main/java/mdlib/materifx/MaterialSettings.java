package mdlib.materifx;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import mdlib.materifx.resources.MateriJResources;
import mdlib.utils.ColorUtilities;
import mdlib.utils.FilteredAtomicReference;

import java.util.Objects;

/**
 * This class contains utilities for JavaFX applications that helps to make this
 * implementation of Google's Material Design working.
 *
 * @author Michal Douša
 */
public final class MaterialSettings {

    // Do not create any instances
    private MaterialSettings() {
    }

    /**
     * Constant for setting the dark theme.
     */
    public static final byte THEME_DARK = 1;

    /**
     * Constant for setting the light theme.
     */
    public static final byte THEME_LIGHT = 0;

    private static final FilteredAtomicReference<Color> primaryColor = new FilteredAtomicReference<>(Objects::nonNull, Color.INDIGO);
    private static final FilteredAtomicReference<Color> primaryVariantColor = new FilteredAtomicReference<>(Objects::nonNull, Color.WHITE);
    private static final FilteredAtomicReference<Color> secondaryColor = new FilteredAtomicReference<>(Objects::nonNull, Color.DARKCYAN);
    private static final FilteredAtomicReference<Color> secondaryVariantColor = new FilteredAtomicReference<>(Objects::nonNull, Color.WHITE);
    private static final FilteredAtomicReference<Byte> theme = new FilteredAtomicReference<>(value -> value != null && (value == THEME_LIGHT || value == THEME_DARK), THEME_LIGHT);

    /**
     * Returns reference to primary color of the application based on this
     * implementation of Google's Material Design. Primary color cannot be set to
     * {@code null}, because {@code null} does not pass through filter.
     *
     * @return reference to primary color
     * @see FilteredAtomicReference
     */
    public static FilteredAtomicReference<Color> primaryColor() {
        return primaryColor;
    }

    /**
     * Returns reference to color of text being on background set to primary color.
     * This <em>on-primary</em> color cannot be set to {@code null}, because
     *
     * @return reference to on-primary color
     * @code null} does not pass through filter.
     * @see FilteredAtomicReference
     */
    public static FilteredAtomicReference<Color> primaryVariantColor() {
        return primaryVariantColor;
    }

    /**
     * Returns reference to secondary color of the application based on this
     * implementation of Google's Material Design. Primary color cannot be set to
     * {@code null}, because {@code null} does not pass through filter.
     *
     * @return reference to secondary color
     */
    public static FilteredAtomicReference<Color> secondaryColor() {
        return secondaryColor;
    }

    /**
     * Returns reference to color of text being on background set to secondary
     * color. This <em>on-secondary</em> color cannot be set to {@code null},
     * because {@code null} does not pass through filter.
     *
     * @return reference to on-secondary color
     */
    public static FilteredAtomicReference<Color> secondaryVariantColor() {
        return secondaryVariantColor;
    }

    /**
     * Use this property to set if you want to use light or dark theme
     */
    public static FilteredAtomicReference<Byte> theme() {
        return theme;
    }

    /**
     * Initializes the application. Loads all required fonts.
     */
    public static void launchInit() {
        // Load Roboto font
        MateriJResources.loadFont("Roboto-Regular.ttf");
        MateriJResources.loadFont("Roboto-Italic.ttf");
        MateriJResources.loadFont("Roboto-Medium.ttf");
        MateriJResources.loadFont("Roboto-MediumItalic.ttf");
        MateriJResources.loadFont("Roboto-Bold.ttf");
        MateriJResources.loadFont("Roboto-BoldItalic.ttf");
    }

    /**
     * Initializes given {@link Scene} as an activity used in Android apps.
     *
     * @param activity the {@link Scene} instance to initialize
     * @throws NullPointerException if given {@link Scene} is {@code null}
     */
    public static void activityPostInit(final Scene activity) {

        // Core Material CSS
        activity.getStylesheets().add(MateriJResources.getResourceURL("material.css").toExternalForm());

        // Light or dark theme CSS
        activity.getStylesheets().add(MateriJResources.getResourceURL(theme().get() == THEME_LIGHT ?
                "material-light.css" : "material-dark.css").toExternalForm());

        // MATERIAL COLOR THEMING

        // Calculate on-primary and on-secondary
        Color onPrimary = primaryColor().get().getBrightness() > .5 ? Color.WHITE : Color.BLACK;
        Color onSecondary = secondaryColor().get().getBrightness() > .5 ? Color.WHITE : Color.BLACK;

        // Calculate

        // Build inline CSS style for root element
        StringBuilder inlineStyleBuilder = new StringBuilder();
        inlineStyleBuilder.append(activity.getRoot().getStyle());
        inlineStyleBuilder.append("; -mdc-primary: ");
        inlineStyleBuilder.append(ColorUtilities.colorToHexRGB(primaryColor().get()));
        inlineStyleBuilder.append("; -mdc-onprimary: ");
        inlineStyleBuilder.append(ColorUtilities.colorToHexRGB(onPrimary));
        inlineStyleBuilder.append("; -mdc-secondary: ");
        inlineStyleBuilder.append(ColorUtilities.colorToHexRGB(secondaryColor().get()));
        inlineStyleBuilder.append("; -mdc-onsecondary: ");
        inlineStyleBuilder.append(ColorUtilities.colorToHexRGB(onSecondary));
        activity.getRoot().setStyle(inlineStyleBuilder.toString());
    }
}
