package mdlib.jmath.math;


import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * <p>A set with finite count of {@link MDNumber}s.
 * @author Michal Douša
 * @see MDInterval
 * @see MDMatrix
 */
public class MDNumberFiniteSet extends MDNumberSet implements Iterable<MDNumber> {
	private static final long serialVersionUID = 0x0100L;
	
	private final TreeSet<MDNumber> elements;

	@Override
	public boolean contains(final MDNumber n) {
		return elements.contains(n);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public MDNumberFiniteSet(final Collection<MDNumber> collection) {
		elements = new TreeSet<>();
		elements.addAll(collection);
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

		return sb.toString();
	}

	@Override
	public Iterator<MDNumber> iterator() {
		return elements.iterator();
	}

	@Override
	public void forEach(final Consumer<? super MDNumber> action) {
		elements.forEach(action);
	}

	@Override
	public Spliterator<MDNumber> spliterator() {
		return elements.spliterator();
	}

	public static final class Double extends MDNumberSet.Double implements Iterable<MDNumber.Double> {
		private final TreeSet<MDNumber.Double> elements;

		public Double(final Collection<MDNumber.Double> collection) {
			elements = new TreeSet<>();
			elements.addAll(collection);
		}

		@Override
		public boolean contains(final MDNumber.Double number) {
			return elements.contains(number);
		}

		@Override
		public boolean isEmpty() {
			return elements.isEmpty();
		}

		@Override
		public String toString() {
			if (isEmpty())
				return "";

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append('{');

			Iterator<MDNumber.Double> iterator = elements.iterator();
			while(iterator.hasNext()) {
				MDNumber.Double next = iterator.next();
				stringBuilder.append(next);
				if (iterator.hasNext())
					stringBuilder.append("; ");
			}

			return stringBuilder.toString();
		}

		@Override
		public String toLaTeX() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("\\left\\{");
			Iterator<MDNumber.Double> iterator = elements.iterator();
			while (iterator.hasNext()) {
				stringBuilder.append(iterator.next());
				if (iterator.hasNext())
					stringBuilder.append("; ");
			}
			stringBuilder.append("\\right\\}");
			return stringBuilder.toString();
		}

		@Override
		public Iterator<MDNumber.Double> iterator() {
			return elements.iterator();
		}

		@Override
		public void forEach(final Consumer<? super MDNumber.Double> action) {
			elements.forEach(action);
		}

		@Override
		public Spliterator<MDNumber.Double> spliterator() {
			return elements.spliterator();
		}
	}
}
