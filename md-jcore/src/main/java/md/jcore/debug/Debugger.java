package md.jcore.debug;

public final class Debugger {

	private Debugger() {
	}

	/**
	 * Logs an event occured in specified class. Event is described as an
	 * @param clazz
	 * @param info
	 */
	public static void info(final Class<?> clazz, final Object info) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("[INFO: ");
		sbuilder.append(clazz.getName());
		sbuilder.append("] \n");
		sbuilder.append(info);
		sbuilder.append("\n");
		System.out.println(sbuilder);
	}

	public static void warning(final Class<?> clazz, final Object info) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("[WARNING: ");
		sbuilder.append(clazz.getName());
		sbuilder.append("] \n");
		sbuilder.append(info);
		sbuilder.append("\n");
		System.err.println(sbuilder);
	}
	
	public static void error(final Class<?> clazz, final Object info) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("[ERROR: ");
		sbuilder.append(clazz.getName());
		sbuilder.append("] \n");
		sbuilder.append(info);
		sbuilder.append("\n");
		System.err.println(sbuilder);
	}
}
