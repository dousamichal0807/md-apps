package mdlib.utils;

/**
 * Equivalent of disposable object in C# programming language.
 * 
 * @author Michal Douša
 *
 */
public interface Disposable {
	/**
	 * Used to dispose an object. Call this method every time, when you stop using
	 * the object.
	 */
	public void dispose();
	
	/**
	 * Returns, if the object is already disposed.
	 * 
	 * @return if the object is already disposed
	 */
	public boolean isDisposed();
	
	
	public static void checkIsNotDisposed(final Disposable obj) {
		if(obj.isDisposed())
			throw new AlreadyDisposedException(obj);
	}
}
