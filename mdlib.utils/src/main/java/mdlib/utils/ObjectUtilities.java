package mdlib.utils;

import java.util.Objects;

public final class ObjectUtilities {

    // Do not create any instances
    private ObjectUtilities() {
    }

    /**
     * The opposite of {@link Objects#requireNonNull(Object, String)}. Instead of message only the whole
     * exception object is needed.
     *
     * @param object object to be tested
     * @param exception the {@link RuntimeException} to be thrown
     */
    public static void requireNull(final Object object, final RuntimeException exception) {
        if (object != null)
            throw exception;
    }
}
