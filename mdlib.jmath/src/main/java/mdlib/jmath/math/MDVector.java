package mdlib.jmath.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

/**
 * Class represents a mathematical vector with real coordinates.
 * 
 * @author Michal Douša
 *
 * @see #MDVector(Number...)
 * @see #add(MDVector, MathContext)
 * @see #subtract(MDVector, MathContext)
 * @see #crossProduct(MDVector, MathContext)
 * @see #dotProduct(MDVector, MathContext)
 */
public final class MDVector implements MDMathEntity, Comparable<MDVector> {

	private static final long serialVersionUID = 0x0100L;
	
	private final BigDecimal[] coordinates;
	
	/**
	 * Creates new vector. All passed vector coordinates are converted to
	 * {@link java.math.BigDecimal BigDecimal}s. The {@code null} values are
	 * interpreted as zero.
	 * @param coordinates vector coordinates
	 */
	public MDVector(final Number... coordinates) {
		this.coordinates = new BigDecimal[coordinates.length];
		for(int i = 0; i < coordinates.length; i++)
			this.coordinates[i] = new BigDecimal(coordinates[i].toString());
	}
	
	/**
	 * Construct the {@code MDVector} as a representation of
	 * {@link MDNumber MDNumber} hypercomplex number.
	 * 
	 * @param number  the hypercomplex number
	 */
	public MDVector(final MDNumber number) {
		coordinates = new BigDecimal[1 + number.getImaginaryPartsCount()];
		coordinates[0] = number.getRealPart();
		for (int i = 0; i < number.getImaginaryPartsCount(); i++) coordinates[i + 1] = number.getImaginaryPart(i);
	}

	/**
	 * Gets the coordinate at index {@code i}. Idexing starts from zero.
	 * 
	 * @param i the index of coordinate to return
	 * @return the coordinate at index {@code i}
	 * @throws ArrayIndexOutOfBoundsException when {@code i} is smaller than 0 or
	 *         {@code i} is bigger than the count of coordinates
	 */
	public BigDecimal get(final int i) {
		return coordinates[i];
	}
	
	/**
	 * Gets the count of coorditates.
	 * 
	 * @return the count of coorditates
	 */
	public int size() {
		return coordinates.length;
	}
	
	/**
	 * Coverts the {@link MDVector} to a {@link MDNumber} instance.
	 * 
	 * @return converted {@link MDNumber} instance
	 */
	public MDNumber asHypercomplex() {
		return new MDNumber(this);
	}
	
	// TODO: Javadoc
	public MDMatrix asMatrix() {
		return new MDMatrix(this);
	}
	
	/**
	 * Adds two vectors using given {@link java.math.MathContext MathContext}. Both
	 * vectors must have the same number of coordinates.
	 * 
	 * @param w   vector to be added
	 * @param mc  {@link java.math.MathContext MathContext} to be used
	 * @return <b>v&#8407;</b> + <b>w&#8407;</b>, where <b>v&#8407;</b> is {@code this} vector
	 */
	public MDVector add(final MDVector w, final MathContext mc) {
		assertSameSize(this, w);
		MDVector result = new MDVector(this.size());
		for(int i = 0; i < this.size(); i++)
			result.coordinates[i] = this.get(0).add(w.get(0), mc);
		return result;
	}
	
	/**
	 * Subtracts two vectors using given {@link java.math.MathContext MathContext}.
	 * Both vectors must have the same number of coordinates.
	 * 
	 * @param w   the vector to be subtracted
	 * @param mc  {@link java.math.MathContext MathContext} to be used
	 * @return <b>v&#8407;</b> &ndash; <b>w&#8407;</b>, where <b>v&#8407;</b> is {@code this} vector
	 */
	public MDVector subtract(final MDVector w, final MathContext mc) {
		assertSameSize(this, w);
		MDVector result = new MDVector(this.size());
		for(int i = 0; i < this.size(); i++)
			result.coordinates[i] = this.get(0).subtract(w.get(0), mc);
		return result;
	}
	
	/**
	 * Returns dot product of two vectors using given
	 * {@link java.math.MathContext MathContext}. Both vectors must have the same
	 * number of dimensions.
	 * @param w   vector to be {@code this} multiplied by
	 * @param mc  {@link java.math.MathContext MathContext} to be used
	 * @return <b>v&#8407;</b> &middot; <b>w&#8407;</b>, where <b>v&#8407;</b> is {@code this} vector
	 */
	public BigDecimal dotProduct(final MDVector w, final MathContext mc) {
		assertSameSize(this, w);
		BigDecimal result = BigDecimal.ZERO;
		for(int i = 0; i < this.size(); i++)
			result = result.add(this.get(0).multiply(w.get(0), mc), mc);
		return result;
	}
	
	/**
	 * Returns cross product of two vectors using given
	 * {@link java.math.MathContext MathContext}. Both vectors must have the same
	 * number of dimensions.
	 * @param w   vector to be {@code this} multiplied by
	 * @param mc  {@link java.math.MathContext MathContext} to be used
	 * @return <b>v&#8407;</b> &times; <b>w&#8407;</b>
	 */
	public static MDVector crossProduct(final MDVector w, final MathContext mc) {
		// TODO Cross product
		return null;
	}
	
	// Private members --------------------------------------------------------

	private static void assertSameSize(final MDVector vec1, final MDVector vec2) {
		if (vec1.size() != vec2.size())
			throw new IllegalArgumentException("Vectors do not have the same size of dimensions.");
	}

	private static void assertSameSize(final MDVector.Double vec1, final MDVector.Double vec2) {
		if (vec1.coordinates() != vec2.coordinates())
			throw new IllegalArgumentException("Vectors do not have the same size of dimensions.");
	}

	@Override
	public int compareTo(final MDVector vector2) {
		if (this.size() != vector2.size())
			throw new IllegalArgumentException("Vectors have different number of dimensions");
		for (int i = 0; i < size(); i++) {
			int compare = this.get(i).compareTo(vector2.get(i));
			if (compare != 0)
				return compare;
		}
		return 0;
	}

	@Override
	public String toString() {
		return Arrays.toString(coordinates);
	}
	
	@Override
	public String toLaTeX() {
		// TODO Auto-generated method stub
		return null;
	}

	// Double class ----------------------------------------------------------------
	
	/**
	 * Class represents a mathematical vector with real coordinates.
	 * 
	 * @author Michal Douša
	 *
	 * @see #Double(double...)
	 * @see #Double(MDNumber.Double)
	 * @see #add(Double)
	 * @see #subtract(Double)
	 * @see #dotProduct(Double)
	 * @see #crossProduct(Double)
	 */
	public static final class Double implements MDMathEntity, Comparable<MDVector.Double> {
		
		private static final long serialVersionUID = 0x0100L;
		
		private final double[] coordinates;
		
		/**
		 * Creates new vector.
		 *
		 * @param coordinates vector coordinates
		 */
		public Double(final double... coordinates) {
			this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
		}
		
		/**
		 * Construct the {@code MDVector} as a representation of
		 * {@link MDNumber MDNumber} hypercomplex number.
		 * 
		 * @param number  the hypercomplex number
		 */
		public Double(final MDNumber.Double number) {
			this(1 + number.getImaginaryPartsCount());
			coordinates[0] = number.getRealPart();
			for (int i = 0; i < number.getImaginaryPartsCount(); i++)
				coordinates[i + 1] = number.getImaginaryPart(i);
		}

		/**
		 * Gets the coordinate at index {@code i}. Idexing starts from zero.
		 * 
		 * @param i the index of coordinate to return
		 * @return the coordinate at index {@code i}
		 * @throws ArrayIndexOutOfBoundsException when {@code i} is smaller than 0 or
		 *         {@code i} is bigger than the count of coordinates
		 */
		public double get(final int i) {
			return coordinates[i];
		}
		
		/**
		 * Gets the count of coorditates.
		 * 
		 * @return the count of coorditates
		 */
		public int coordinates() {
			return coordinates.length;
		}
		
		/**
		 * Coverts the {@link MDVector.Double} to a {@link MDNumber.Double}
		 * instance.
		 * 
		 * @return converted {@link MDNumber.Double} instance
		 */
		public MDNumber.Double asHypercomplex() {
			return new MDNumber.Double(this);
		}
		
		/**
		 * Adds two vectors using given {@link java.math.MathContext MathContext}. Both
		 * vectors must have the same number of coordinates.
		 * 
		 * @param w   vector to be added
		 * @return <b>v&#8407;</b> + <b>w&#8407;</b>, where <b>v&#8407;</b> is {@code this} vector
		 */
		public MDVector.Double add(final MDVector.Double w) {
			assertSameSize(this, w);
			MDVector.Double result = new MDVector.Double(this.coordinates());
			for(int i = 0; i < this.coordinates(); i++)
				result.coordinates[i] = this.get(0) + w.get(0);
			return result;
		}
		
		/**
		 * Subtracts two vectors using given {@link java.math.MathContext MathContext}.
		 * Both vectors must have the same number of coordinates.
		 * 
		 * @param w   the vector to be subtracted
		 * @return <b>v&#8407;</b> &ndash; <b>w&#8407;</b>, where <b>v&#8407;</b> is {@code this} vector
		 */
		public MDVector.Double subtract(final MDVector.Double w) {
			assertSameSize(this, w);
			MDVector.Double result = new MDVector.Double(this.coordinates());
			for(int i = 0; i < this.coordinates(); i++)
				result.coordinates[i] = this.get(0) - w.get(0);
			return result;
		}
		
		/**
		 * Returns dot product of two vectors using given
		 * {@link java.math.MathContext MathContext}. Both vectors must have the same
		 * number of dimensions.
		 * @param w   vector to be {@code this} multiplied by
		 * @return <b>v&#8407;</b> &middot; <b>w&#8407;</b>, where <b>v&#8407;</b> is {@code this} vector
		 */
		public double dotProduct(final MDVector.Double w) {
			assertSameSize(this, w);
			double result = .0;
			for(int i = 0; i < this.coordinates(); i++)
				result += this.get(0) * w.get(0);
			return result;
		}
		
		/**
		 * Returns cross product of two vectors using given
		 * {@link java.math.MathContext MathContext}. Both vectors must have the same
		 * number of dimensions.
		 * @param w   vector to be {@code this} multiplied by
		 * @return <b>v&#8407;</b> &times; <b>w&#8407;</b>
		 */
		public MDVector.Double crossProduct(final MDVector.Double w) {
			// TODO Cross product
			return null;
		}

		@Override
		public int compareTo(final MDVector.Double vector2) {
			if (this.coordinates() != vector2.coordinates())
				throw new IllegalArgumentException("Vectors have different number of dimensions");
			for (int i = 0; i < coordinates(); i++) {
				int compare = java.lang.Double.compare(this.get(i), vector2.get(i));
				if (compare != 0)
					return compare;
			}
			return 0;
		}

		@Override
		public String toString() {
			return Arrays.toString(coordinates);
		}

		@Override
		public String toLaTeX() {
			// TODO toLaTeX
			return null;
		}

	}
}
