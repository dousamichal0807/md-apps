package md.jgames.jchess.resources;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Class containing some constants and responsible for loading resources from
 * this package. Used by the jChess application.
 * 
 * @author Michal Douša
 */
public final class Resources {

	private Resources() {
	}

	/**
	 * Material primary color of jChess application.
	 */
	public static final Color COLOR_PRIMARY = new Color(0x795548);

	public static final Color COLOR_ONPRIMARY = Color.WHITE;

	/**
	 * Material secondary color of jChess application.
	 */
	public static final Color COLOR_SECONDARY = new Color(0x34515e);

	public static final Color COLOR_ONSECONDARY = Color.WHITE;

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
		InputStream stream = Resources.class.getResourceAsStream(resourceName);
		if (stream == null)
			throw new RuntimeException(
					new FileNotFoundException("jChess resource '" + resourceName + "' was not found."));
		return stream;
	}

	public static URL loadResourceURL(final String resourceName) {
		URL url = Resources.class.getResource(resourceName);
		if (url == null)
			throw new RuntimeException(new FileNotFoundException("Resource not found: " + resourceName));
		return url;
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

	/**
	 * Loads an image into {@link ImageIcon} instance. Uses
	 * {@link #loadImageResource(String)} method. That means this method behaves the
	 * same way as {@link #loadImageResource(String)} method.
	 * 
	 * @param resourceName name of the image file
	 * @return resource as {@link ImageIcon}
	 * 
	 * @throws RuntimeException like {@link #loadImageResource(String)} method
	 * 
	 * @see #loadImageResource(String)
	 */
	public static ImageIcon loadIconResource(final String resourceName) {
		return new ImageIcon(loadImageResource(resourceName));
	}

	/**
	 * Returns an {@link URL} of given activity FXML file, if exists.
	 *
	 * @param activityName the name of the requested activity
	 * @return {@link URL} of given activity FXML file
	 * @throws RuntimeException caused by {@link FileNotFoundException} if activity
	 *                          FXML file does not exist
	 */
	public static URL loadActivityFXML(final String activityName) {
		URL url = Resources.class.getResource("activity_" + activityName + ".fxml");
		if (url == null)
			throw new RuntimeException(new FileNotFoundException("Activity FXML file not found: \"" + activityName + "\""));
		return url;
	}
}
