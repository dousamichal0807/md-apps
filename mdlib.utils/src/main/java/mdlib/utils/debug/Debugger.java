package mdlib.utils.debug;

public final class Debugger {

    // Do not create any instances
    private Debugger() {
    }

    /**
     * Logs an event occured in specified class as information.
     *
     * @param loggingClass which class is logging (a {@link Class} object)
     * @param info         any object, will be transformed into {@link String}
     */
    public static void info(final Class<?> loggingClass, final Object info) {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("[INFO: ");
        sbuilder.append(loggingClass.getName());
        sbuilder.append("] ");
        sbuilder.append(info);
        System.out.println(sbuilder);
    }

    /**
     * Logs an event occured in specified class as a warning.
     *
     * @param loggingClass which class is logging (a {@link Class} object)
     * @param info         any object, will be transformed into {@link String}
     */
    public static void warning(final Class<?> loggingClass, final Object info) {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("[WARNING: ");
        sbuilder.append(loggingClass.getName());
        sbuilder.append("]");
        sbuilder.append(info);
        System.err.println(sbuilder);
    }

    /**
     * Logs an event occured in specified class as an error.
     *
     * @param loggingClass which class is logging (a {@link Class} object)
     * @param info         any object, will be transformed into {@link String}
     */
    public static void error(final Class<?> loggingClass, final Object info) {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("[ERROR: ");
        sbuilder.append(loggingClass.getName());
        sbuilder.append("] ");
        sbuilder.append(info);
        System.err.println(sbuilder);
    }
}
