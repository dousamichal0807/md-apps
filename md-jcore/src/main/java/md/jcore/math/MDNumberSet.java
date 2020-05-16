package md.jcore.math;

public abstract class MDNumberSet implements MDMathEntity {
	private static final long serialVersionUID = 0x0100L;
	
	public abstract boolean contains(MDNumber v);
	public abstract boolean isEmpty();
}
