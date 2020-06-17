package mdlib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class containing some utility methods for JavaFX framework.
 *
 * @author Michal Douša
 * @see ColorUtilities
 * @see ClasspathUtilities
 */
public final class JFXUtilities {

    // Do not create any instances
    private JFXUtilities() {
    }

    /**
     * Loads {@link Image} from given {@link InputStream}.
     *
     * @param inputStream {@link InputStream} to be the image loaded from
     * @return loaded JavaFX {@link Image}
     *
     * @throws NullPointerException if input stream is {@code null}
     */
    public static Image loadImage(final InputStream inputStream) {
        return new Image(inputStream);
    }

    /**
     * Loads {@link Image} from given {@link URL}.
     *
     * @param url {@link URL} to be the image loaded from
     * @return loaded JavaFX {@link Image}
     *
     * @throws IOException if I/O exception occurs
     */
    public static Image loadImage(final URL url) throws IOException {
        return loadImage(url.openStream());
    }

    /**
     * Creates an {@link AnimationTimer} from a {@link Runnable} object.
     *
     * @param runnable the {@link Runnable} object
     * @return the created {@link AnimationTimer}
     *
     * @throws NullPointerException if {@code null} is passed instead of instance that implements {@link Runnable}
     *                              interface
     */
    public static AnimationTimer animationTimer(final Runnable runnable) {
        Objects.requireNonNull(runnable, "Cannot pass null as a java.lang.Runnable object");
        return new AnimationTimer() {
            @Override
            public void handle(final long now) {
                runnable.run();
            }
        };
    }

    /**
     * Creates an {@link AnimationTimer} from {@link Consumer} that can consume {@code long} integers. That {@link
     * Consumer} will consume system time in nanoseconds, same as {@link AnimationTimer#handle(long)} method does
     * through its parameter.
     *
     * @param nanoTimeConsumer a {@link Consumer} of system time in nanoseconds
     * @return the created {@link AnimationTimer}
     *
     * @throws NullPointerException if {@code null} is passed instead of instance implementing {@link Consumer}{@code <?
     *                              super Long>}
     */
    public static AnimationTimer animationTimer(final Consumer<? super Long> nanoTimeConsumer) {
        Objects.requireNonNull(nanoTimeConsumer, "Cannot pass null as a Consumer<? super Long> instance");
        return new AnimationTimer() {
            @Override
            public void handle(final long now) {
                nanoTimeConsumer.accept(now);
            }
        };
    }

    /**
     * Creates an {@link EventHandler} from given {@link Runnable} object.
     *
     * @param runnable the {@link Runnable} object to create an {@link EventHandler} from
     * @param <E>      type of the {@link Event} to be handled by {@link EventHandler}
     * @return {@link EventHandler} created from {@link Runnable} object
     *
     * @throws NullPointerException if {@code null} is passed instead of {@link Runnable} object
     */
    public static <E extends Event> EventHandler<E> eventHandler(final Runnable runnable) {
        Objects.requireNonNull(runnable, "Cannot pass null as a java.lang.Runnable object");
        return event -> runnable.run();
    }

    /**
     * Creates an {@link EventHandler} from given {@link Consumer} consuming given {@code <E>} {@link Event} type
     * instance.
     *
     * @param eventConsumer compatible {@link Consumer}
     * @param <E>           type of event that {@link Consumer} should consume
     * @return {@link EventHandler} from given {@link Consumer}
     *
     * @throws NullPointerException if {@code null} is passed instead of valid {@link Consumer} object
     */
    public static <E extends Event> EventHandler<E> eventHandler(final Consumer<? super E> eventConsumer) {
        Objects.requireNonNull(eventConsumer, "Event consumer cannot be null");
        return eventConsumer::accept;
    }

    /**
     * Draws an image on specified {@link javafx.scene.canvas.Canvas Canvas} specified by its {@link GraphicsContext}
     * instance, bounded by specified {@link Rectangle2D}.
     *
     * @param ctx   the {@link javafx.scene.canvas.Canvas Canvas}'s {@link GraphicsContext} instance
     * @param image image to be drawn
     * @param rect  rectangle within the image will be drawn
     * @throws NullPointerException if at least one given argument is {@code null}
     */
    public static void canvasDrawImage(final GraphicsContext ctx, final Image image, final Rectangle2D rect) {
        Objects.requireNonNull(ctx, "GraphicsContext cannot be null");
        Objects.requireNonNull(image, "Cannot draw image set to null");
        Objects.requireNonNull(rect, "Specified rectangle cannot be null");
        ctx.drawImage(image, rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
    }
}
