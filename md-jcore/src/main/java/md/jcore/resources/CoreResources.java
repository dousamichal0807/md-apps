package md.jcore.resources;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class CoreResources {

    // Do not create any instances
    private CoreResources() {
    }

    /**
     * Returns given resource in {@link InputStream}.
     *
     * @param resourceName resource name
     * @return {@link InputStream} of given resource, if resource exists
     * @throws NullPointerException if {@code null} is given as {@code resourceName}
     * @throws RuntimeException     caused by {@link FileNotFoundException} if resource
     *                              does not exist
     */
    public static InputStream loadToStream(final String resourceName) {
        InputStream stream = CoreResources.class.getResourceAsStream(resourceName);
        if (stream == null)
            throw new RuntimeException(new FileNotFoundException("jCore resource not found: " + resourceName));
        return stream;
    }

    /**
     * Returns {@link URL} of given resource.
     *
     * @param resourceName resource name
     * @return {@link URL} of given resource, if resource exists
     * @throws NullPointerException if {@code null} is given as {@code resourceName}
     * @throws RuntimeException     caused by {@link FileNotFoundException} if resource
     *                              does not exist
     */
    public static URL loadResourceURL(final String resourceName) {
        URL url = CoreResources.class.getResource(resourceName);
        if (url == null)
            throw new RuntimeException(new FileNotFoundException("jCore resource not found: " + resourceName));
        return url;
    }

    public static void loadFont(final String resourceName) {
        try {
            Font[] fonts = Font.createFonts(loadToStream(resourceName));
            for (Font font : fonts)
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (IOException | FontFormatException exc) {
            throw new RuntimeException(exc);
        }
    }
}
