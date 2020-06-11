package mdlib.materifx.resources;

import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public final class MateriJResources {

    // Do not create any instances
    private MateriJResources() {
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
    public static URL getResourceURL(final String resourceName) {
        URL url = MateriJResources.class.getResource(resourceName);
        if (url == null)
            throw new RuntimeException(new FileNotFoundException("MD MateriJ resource not found: " + resourceName));
        return url;
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
        InputStream stream = MateriJResources.class.getResourceAsStream(resourceName);
        if (stream == null)
            throw new RuntimeException(new FileNotFoundException("jCore resource not found: " + resourceName));
        return stream;
    }

    public static void loadFont(final String resourceName) {
        Font.loadFont(getResourceURL(resourceName).toExternalForm(), 10);
    }
}
