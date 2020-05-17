package md.jcore.math;


import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p>A set with finite count of real (<code>java.math.BigDecimal</code>)
 * or complex (the <code>ComplexNumber</code> class) numbers.
 * @author Michal Douša
 * @see MDInterval
 * @see MDMatrix
 */
public class MDNumberFiniteSet extends MDNumberSet {
	private static final long serialVersionUID = 0x0100L;
	
	private final TreeSet<MDNumber> elements;
	/**
	 * <p>Returns if the set contains specified number.</p>
	 * @param n The specified number
	 * @return If the specified number is in the set (<code>true</code>
	 * or <code>false</code>)
	 */
	public boolean contains(MDNumber n) {
		return elements.contains(n);
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	private MDNumberFiniteSet() {
		this.elements = new TreeSet<>();
	}
	public MDNumberFiniteSet(Collection<MDNumber> collection) {
		this();
		elements.addAll(collection);
	}
	public Object[] toArray() {
		return elements.toArray();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<MDNumber> it = elements.iterator();
		
		sb.append("{");
		while(it.hasNext()) {
			sb.append(it.next());
			if(it.hasNext())
				sb.append("; ");
		}
		sb.append("}");
		
		return sb.toString();
	}
	
	@Override
	public String toLaTeX() {
		StringBuilder sb = new StringBuilder();
		Iterator<MDNumber> it = elements.iterator();
		
		sb.append("\\left\\{");
		while(it.hasNext()) {
			sb.append(it.next().toLaTeX());
			if(it.hasNext())
				sb.append("; ");
		}
		sb.append("\\right\\}");
		
		System.out.println("LaTeX: " + sb.toString());
		return sb.toString();
	}
}
