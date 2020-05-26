package md.jcore.resources;

import java.io.FileNotFoundException;
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
    public static InputStream loadToStream(String resourceName) {
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
    public static URL loadResourceURL(String resourceName) {
        URL url = CoreResources.class.getResource(resourceName);
        if (url == null)
            throw new RuntimeException(new FileNotFoundException("jCore resource not found: " + resourceName));
        return url;
    }
}
