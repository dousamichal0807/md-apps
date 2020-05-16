package md.jcore.debug;

public class Debugger {

	public static void info(Class<?> clazz, Object info) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("[INFO: ");
		sbuilder.append(clazz.getName());
		sbuilder.append("] ");
		sbuilder.append(info);
		System.out.println(sbuilder.toString());
	}

	public static void warning(Class<?> clazz, Object info) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("[WARNING: ");
		sbuilder.append(clazz.getName());
		sbuilder.append("] ");
		sbuilder.append(info);
		System.err.println(sbuilder.toString());		
	}
	
	public static void error(Class<?> clazz, Object info) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("[ERROR: ");
		sbuilder.append(clazz.getName());
		sbuilder.append("] ");
		sbuilder.append(info);
		System.err.println(sbuilder.toString());
	}
}
