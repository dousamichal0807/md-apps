package md.jgames.jchess.resources;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Class containing some constants and responsible for loading resources from
 * this package. Used by the jChess application.
 *
 * @author Michal Douša
 */
public final class AppResources {

    private AppResources() {
    }

    /**
     * Returns an {@link InputStream} of given resource. If requested resource does
     * not exist, this method throws {@link RuntimeException}. Its
     * {@link RuntimeException#getCause()} method returns a
     * {@link FileNotFoundException}.
     *
     * @param resourceName name of the resource file
     * @return resource as input stream
     *
     * @throws RuntimeException when required resource does not exist as desribed
     *                          above
     *
     * @see #loadImageResource(String)
     */
    public static InputStream loadResource(final String resourceName) {
        InputStream stream = AppResources.class.getResourceAsStream(resourceName);
        if (stream == null)
            throw new RuntimeException(
                    new FileNotFoundException("jChess resource '" + resourceName + "' was not found."));
        return stream;
    }

    /**
     * Loads an image resource into {@link BufferedImage} instance using
     * {@link ImageIO#read(InputStream)} method.
     *
     * @param resourceName name of the image file
     * @return resource as {@link BufferedImage}
     *
     * @throws RuntimeException when one of the situations desribed below occurs:
     *                          <ul>
     *                          <li>when required resource does not exist</li>
     *                          <li>when required resource is not an image or it is
     *                          not in supported format by
     *                          {@link ImageIO#read(InputStream)} method</li>
     *                          </ul>
     */
    public static BufferedImage loadImageResource(final String resourceName) {
        try {
            InputStream stream = loadResource(resourceName);
            return ImageIO.read(stream);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
