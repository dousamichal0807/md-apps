package mdlib.utils;

public class AlreadyDisposedException extends IllegalStateException {
	private static final long serialVersionUID = 0x0100L;
	
	public AlreadyDisposedException(final Disposable object) {
		super("Object of type " + object.getClass().getName() + " already disposed:" + object);
	}
}
