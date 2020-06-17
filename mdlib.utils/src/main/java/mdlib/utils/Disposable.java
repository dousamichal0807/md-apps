package mdlib.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface works as disposable object in C# programming language, e.g. C# {@code System.IDisposable} interface. Uses
 * Java's {@link Closeable} interface
 *
 * @author Michal Douša
 */
public interface Disposable extends Closeable {

    /**
     * Used to dispose an object. Call this method every time, when you stop using the object.
     */
    default void dispose() {
        try {
            close();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Returns {@code boolean} value, if the object is already disposed.
     *
     * @return if the object is already disposed
     */
    boolean isDisposed();

    /**
     * Checks if specified {@link Disposable} instance is not disposed. If it <em>is</em> disposed, {@link
     * AlreadyDisposedException} is thrown.
     *
     * @param obj object that is required to be not disposed
     * @throws AlreadyDisposedException if object is disposed, as intended.
     */
    static void requireNotDisposed(final Disposable obj) {
        if (obj.isDisposed())
            throw new AlreadyDisposedException(obj);
    }
}
