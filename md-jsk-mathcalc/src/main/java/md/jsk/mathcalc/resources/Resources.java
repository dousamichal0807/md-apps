package md.jsk.mathcalc.resources;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.net.URL;

public final class Resources {

    // Do not create any instances
    private Resources() {
    }

    /**
     * Material Design's primary color of the MathCalc application.
     * @see <a href="https://material.io/design/color/the-color-system.html" target="_top">Material Design Color System</a>
     */
    public static final Color COLOR_PRIMARY = new Color(0xc0ca33);

    /**
     * Material Design's secondary color of the MathCalc application.
     * @see <a href="https://material.io/design/color/the-color-system.html" target="_top">Material Design Color System</a>
     */
    public static final Color COLOR_SECONDARY = new Color(0xc51162);

    public static URL loadResourceURL(final String resourceName) {
        URL url = Resources.class.getResource(resourceName);
        if (url == null)
            throw new RuntimeException(new FileNotFoundException("Resource not found: " + resourceName));
        return url;
    }
}
