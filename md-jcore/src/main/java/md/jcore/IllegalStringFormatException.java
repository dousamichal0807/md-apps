package md.jcore;

public class IllegalStringFormatException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public IllegalStringFormatException() {
		super();
	}

	public IllegalStringFormatException(String m) {
		super(m);
	}
}
