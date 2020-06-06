package mdlib.utils;

public class IllegalStringFormatException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public IllegalStringFormatException() {
		super();
	}

	public IllegalStringFormatException(final String m) {
		super(m);
	}
}
