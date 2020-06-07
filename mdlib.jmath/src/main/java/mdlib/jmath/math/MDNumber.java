package mdlib.jmath.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

/**
 * <p>
 * Represents a hypercomplex number. A hypercomplex number is a non-real number
 * that has any number of imaginary units. So you can represent a typical
 * complex number (<em>a</em> + <em>bi</em>), but also a quaternion (<em>a</em>
 * + <em>bi</em> + <em>cj</em> + <em>dk</em>) or an octonion with this class. A
 * hypercomplex can be written in form
 * </p>
 * 
 * <blockquote style="text-align: center;"><em>a</em><sub>0</sub> +
 * <em>a</em><sub>1</sub><em>i</em><sub>1</sub> +
 * <em>a</em><sub>2</sub><em>i</em><sub>2</sub> + ... +
 * <em>a<sub>n</sub>i<sub>n</sub></em></blockquote>
 * 
 * <p>
 * where <em>a</em><sub>0</sub> is the real part of the hypercomplex,
 * <em>a</em><sub>1</sub>...<em>a<sub>n</sub></em> are imaginary parts of a
 * hypercomplex and <em>i</em><sub>1</sub>...<em>i<sub>n</sub></em> are
 * imaginary units. For all of them is true that their square is -1. For example
 * quaternions are hypercomplexes such that:
 * </p>
 * 
 * <table>
 * <tr>
 * <td><em>a</em><sub>0</sub> = <em>a</em>,</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><em>a</em><sub>1</sub> = <em>b</em>,</td>
 * <td><em>i</em><sub>1</sub> = <em>i</em>,</td>
 * </tr>
 * <tr>
 * <td><em>a</em><sub>2</sub> = <em>c</em>,</td>
 * <td><em>i</em><sub>2</sub> = <em>j</em>,</td>
 * </tr>
 * <tr>
 * <td><em>a</em><sub>3</sub> = <em>d</em>,</td>
 * <td><em>i</em><sub>3</sub> = <em>k</em>.</td>
 * </tr>
 * </table>
 * 
 * @author Michal Douša
 *
 * @see #MDNumber(Number, Number...)
 * @see #getRealPart()
 * @see #getImaginaryPart(int)
 */
public final class MDNumber implements Cloneable, Comparable<MDNumber>, MDMathEntity {
	private static final long serialVersionUID = 0x0100L;

	private final BigDecimal real;
	private final BigDecimal[] imag;

	/**
	 * Constructs a number.
	 * 
	 * @param real The real part of the number
	 * @param imag The imaginary part
	 */
	public MDNumber(final Number real, final Number... imag) {
		// Convert all to BigDecimal
		BigDecimal real0 = new BigDecimal(real == null ? "0" : real.toString());
		BigDecimal[] imag0 = new BigDecimal[imag.length];
		for (int i = 0; i < imag.length; i++)
			imag0[i] = new BigDecimal(imag[i] == null ? "0" : imag[i].toString());

		// Save them
		this.real = real0;
		this.imag = imag0;
	}

	/**
	 * Constructs a hypercomplex number using given {@link MDVector}.
	 * 
	 * @param vector instance of {@link MDVector} to be used
	 */
	public MDNumber(final MDVector vector) {
		this.real = vector.get(0);
		this.imag = new BigDecimal[vector.size() - 1];
		for (int i = 1; i < vector.size(); i++)
			this.imag[i - 1] = vector.get(i);
	}

	/**
	 * Constructs a deep copy of specified {@link MDNumber}.
	 * 
	 * @param number the {@link MDNumber} instance to be copied
	 */
	public MDNumber(final MDNumber number) {
		this.real = number.real;
		this.imag = Arrays.copyOf(number.imag, number.imag.length);
	}

	/**
	 * Gets the real part of a hypercomplex number
	 */
	public BigDecimal getRealPart() {
		return real;
	}

	/**
	 * Gets the <em>n</em><sup>th</sup> imaginary part of a hypercomplex number
	 * 
	 * @return the <em>n</em><sup>th</sup> imaginary part of a hypercomplex number
	 * @throws ArrayIndexOutOfBoundsException when <em>n</em> &lt; 0
	 */
	public BigDecimal getImaginaryPart(final int n) {
		if (n < 0)
			throw new ArrayIndexOutOfBoundsException("n must be 0 or greater");
		if (n >= imag.length)
			return BigDecimal.ZERO;
		else
			return imag[n];
	}

	/**
	 * Gets count of imaginary parts. Usual complex number has only one imaginary
	 * part, but for example quaternion has 4 imaginary parts and octonion has 8
	 * imaginary parts.
	 * 
	 * @return number of imaginary parts
	 */
	public int getImaginaryPartsCount() {
		return imag.length;
	}

	/**
	 * Returns representation of the number as a vector.
	 * 
	 * @return the number represented as {@link MDVector MDVector}
	 */
	public MDVector asVector() {
		return new MDVector(this);
	}

	@Override
	public String toString() {
		if (isRealNumber())
			return real.toString();
		if (isOctonion()) {
			StringBuilder sb = new StringBuilder();
			if (getRealPart().compareTo(BigDecimal.ZERO) != 0)
				sb.append(getRealPart());
			for (int i = 0; i < imag.length; i++) {
				BigDecimal im = getImaginaryPart(i);
				int sgn = im.compareTo(BigDecimal.ZERO);
				if (sgn != 0) {
					if (sgn > 0)
						sb.append('+');
					sb.append(im);
					if (i < 4)
						sb.append((char) ('i' + i));
					else {
						sb.append('l');
						sb.append('i' + i - 4);
					}
				}
			}
			return sb.toString();
		}
		return "!#NO_PLAIN_TEXT";
	}

	@Override
	public String toLaTeX() {
		if (isRealNumber())
			return real.toString();
		if (isOctonion()) {
			StringBuilder sb = new StringBuilder();
			if (getRealPart().compareTo(BigDecimal.ZERO) != 0)
				sb.append(getRealPart());
			for (int i = 0; i < imag.length; i++) {
				BigDecimal im = getImaginaryPart(i);
				int sgn = im.compareTo(BigDecimal.ZERO);
				if (sgn != 0) {
					if (sgn > 0)
						sb.append('-');
					sb.append(im);
					sb.append("\\mathbf{");
					if (i < 4) sb.append((char) ('i' + i));
					else {
						sb.append('l');
						sb.append('i' + i - 4);
					}
					sb.append('}');
				}
			}
			return sb.toString();
		}
		return "!#NO_PLAIN_TEXT";
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public MDNumber clone() {
		return new MDNumber(this);
	}

	/**
	 * Returns if the number is (at least approximately with given
	 * {@link java.math.MathContext MathContext}) a natural number. If instead of
	 * {@link java.math.MathContext MathContext} object the {@code null} value is
	 * given, it returns if the number is <em>exactly</em> a natural number.
	 * 
	 * @param mc {@link java.math.MathContext MathContext} to be the number rounded
	 *           by first
	 * @return if {@code this} &isin; &naturals;
	 */
	public boolean isNaturalNumber(final MathContext mc) {
		return isInteger(mc) && real.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * Returns if the number is (at least approximately with given
	 * {@link java.math.MathContext MathContext}) an integer. If instead of
	 * {@link java.math.MathContext MathContext} object the {@code null} value is
	 * given, it returns if the number is <em>exactly</em> an integer.
	 * 
	 * @param mc {@link java.math.MathContext MathContext} to be the number rounded
	 *           by first
	 */
	public boolean isInteger(final MathContext mc) {
		try {
			BigDecimal r1 = mc != null ? real.round(mc) : real;
			r1.toBigIntegerExact();
			return noImagPartFrom(0);
		} catch (ArithmeticException exc) {
			return false;
		}
	}

	/**
	 * Returns if the number is a real number
	 * 
	 * @return if {@code this} &isin; &reals;
	 * 
	 * @see #isComplexNumber()
	 * @see #isQuaternion()
	 * @see #isOctonion()
	 * @see #isSedenion()
	 */
	public boolean isRealNumber() {
		// All imaginary parts must be equal to zero
		return noImagPartFrom(0);
	}

	/**
	 * Returns if the number is a complex number
	 * 
	 * @return if {@code this} &isin; &complexes;
	 * 
	 * @see #isRealNumber()
	 * @see #isQuaternion()
	 * @see #isOctonion()
	 * @see #isSedenion()
	 */
	public boolean isComplexNumber() {
		// Only one imaginary part
		return noImagPartFrom(1);
	}

	/**
	 * Returns if the number is a quaternion
	 * 
	 * @return if {@code this} &isin; &Hopf; (&Hopf; is set of all quaternions)
	 * 
	 * @see #isRealNumber()
	 * @see #isComplexNumber()
	 * @see #isOctonion()
	 * @see #isSedenion()
	 */
	public boolean isQuaternion() {
		// 3 imaginary parts at maximum
		return noImagPartFrom(3);
	}

	/**
	 * Returns if the number is a octonion
	 * 
	 * @return if {@code this} &isin; &Oopf; (&Oopf; is set of all octonions)
	 * 
	 * @see #isRealNumber()
	 * @see #isComplexNumber()
	 * @see #isQuaternion()
	 * @see #isSedenion()
	 */
	public boolean isOctonion() {
		// 7 imaginary parts at maximum
		return noImagPartFrom(8);
	}

	/**
	 * Returns if the number is a sedenion
	 * 
	 * @return if {@code this} &isin; &Sopf; (&Sopf; is set of all sedenions)
	 * 
	 * @see #isRealNumber()
	 * @see #isComplexNumber()
	 * @see #isQuaternion()
	 * @see #isOctonion()
	 */
	public boolean isSedenion() {
		// 15 imaginary parts at maximum
		return noImagPartFrom(15);
	}

	@Override
	public int compareTo(final MDNumber num) {
		int c = this.real.compareTo(num.real);
		if (c != 0)
			return c;
		for (int i = 0; i < Math.max(this.getImaginaryPartsCount(), num.getImaginaryPartsCount()); i++) {
			c = this.getImaginaryPart(i).compareTo(num.getImaginaryPart(i));
			if (c != 0)
				return c;
		}
		return c;
	}

	// TODO Javadoc
	public BigDecimal magnitude(final MathContext mc) {
		BigDecimal s = real.round(mc).pow(2, mc);
		for (BigDecimal bigDecimal : imag) s = s.add(bigDecimal.round(mc).pow(2, mc));
		return MathUtilities.realRoot(s, 2, mc);
	}

	// TODO Javadoc
	public MDNumber conjugate(final MathContext mc) {
		BigDecimal re = this.real.round(mc);
		BigDecimal[] im = new BigDecimal[this.imag.length];
		for (int i = 0; i < im.length; i++)
			im[i] = this.imag[i].multiply(BigDecimal.valueOf(-1L), mc);
		return new MDNumber(re, im);
	}

	/**
	 * Adds this hypercomplex number with another hypercomplex number.
	 * 
	 * @param augend the other hypercomplex to be added
	 * @return {@code this + augend}
	 */
	public MDNumber add(final MDNumber augend, final MathContext mc) {
		int imagcount = Math.max(this.imag.length, augend.imag.length);

		BigDecimal real = this.real.add(augend.real, mc);
		BigDecimal[] imag = new BigDecimal[imagcount];
		for (int i = 0; i < imagcount; i++)
			imag[i] = this.getImaginaryPart(i).add(augend.getImaginaryPart(i), mc);
		return new MDNumber(real, imag);
	}

	/**
	 * Substracts this hypercomplex number and the other hypercomplex number.
	 * 
	 * @param subtrahend the other hypercomplex to be subtracted
	 * @return {@code this - subtrahend}
	 */
	public MDNumber subtract(final MDNumber subtrahend, final MathContext mc) {
		int imagcount = Math.max(this.imag.length, subtrahend.imag.length);

		BigDecimal real = this.real.subtract(subtrahend.real, mc);
		BigDecimal[] imag = new BigDecimal[imagcount];
		for (int i = 0; i < imagcount; i++)
			imag[i] = this.getImaginaryPart(i).subtract(subtrahend.getImaginaryPart(i), mc);
		return new MDNumber(real, imag);
	}

	/**
	 * Returns the {@code n}-th root of the complex number.
	 * 
	 * @param n  the grade of root
	 * @param mc the {@link MathContext} instance
	 * @return {@code n}-th root of {@code this}
	 * @throws IllegalStateException if the is not a complex number, e.g. the number
	 *                               has more than one imaginary components
	 * 
	 * @see #isComplexNumber()
	 */
	public MDNumber root(final int n, final MathContext mc) {
		if (!isComplexNumber())
			throw new IllegalArgumentException("Cannot compute root of non-complex number");

		double newAngle = Math.atan(imag[0].divide(real, mc).doubleValue()) / n;
		BigDecimal newMagnitude = MathUtilities.realRoot(magnitude(mc), n, mc);

		BigDecimal re = BigDecimal.valueOf(Math.cos(newAngle)).multiply(newMagnitude, mc);
		BigDecimal im = BigDecimal.valueOf(Math.sin(newAngle)).multiply(newMagnitude, mc);

		return new MDNumber(re, im);
	}

	/**
	 * Returns square root of the complex number.
	 * 
	 * @param mc the {@link MathContext} instance
	 * @return square root of {@code this}
	 * @throws IllegalStateException if the is not a complex number, e.g. the number
	 *                               has more than one imaginary components
	 * 
	 * @see #isComplexNumber()
	 * @see #root(int, MathContext)
	 * @see #cbrt(MathContext)
	 */
	public MDNumber sqrt(final MathContext mc) {
		return root(2, mc);
	}

	/**
	 * Returns cube root of the complex number.
	 * 
	 * @param mc the {@link MathContext} instance
	 * @return cube root of {@code this}
	 * @throws IllegalStateException if the is not a complex number, e.g. the number
	 *                               has more than one imaginary components
	 * 
	 * @see #isComplexNumber()
	 * @see #root(int, MathContext)
	 * @see #sqrt(MathContext)
	 */
	public MDNumber cbrt(final MathContext mc) {
		return root(3, mc);
	}

	private boolean noImagPartFrom(final int startIndex) {
		for (int i = startIndex; i < imag.length; i++)
			if (imag[i].compareTo(BigDecimal.ZERO) != 0)
				return false;
		return true;
	}

	/**
	 * <p>
	 * Represents a hypercomplex number. A hypercomplex number is a non-real number
	 * that has any number of imaginary units. So you can represent a typical
	 * complex number (<em>a</em> + <em>bi</em>), but also a quaternion (<em>a</em>
	 * + <em>bi</em> + <em>cj</em> + <em>dk</em>) or an octonion with this class. A
	 * hypercomplex can be written in form
	 * </p>
	 * 
	 * <blockquote style="text-align: center;"><em>a</em><sub>0</sub> +
	 * <em>a</em><sub>1</sub><em>i</em><sub>1</sub> +
	 * <em>a</em><sub>2</sub><em>i</em><sub>2</sub> + ... +
	 * <em>a<sub>n</sub>i<sub>n</sub></em></blockquote>
	 * 
	 * <p>
	 * where <em>a</em><sub>0</sub> is the real part of the hypercomplex,
	 * <em>a</em><sub>1</sub>...<em>a<sub>n</sub></em> are imaginary parts of a
	 * hypercomplex and <em>i</em><sub>1</sub>...<em>i<sub>n</sub></em> are
	 * imaginary units. For all of them is true that their square is -1. For example
	 * quaternions are hypercomplexes such that:
	 * </p>
	 * 
	 * <table>
	 * <tr>
	 * <td><em>a</em><sub>0</sub> = <em>a</em>,</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td><em>a</em><sub>1</sub> = <em>b</em>,</td>
	 * <td><em>i</em><sub>1</sub> = <em>i</em>,</td>
	 * </tr>
	 * <tr>
	 * <td><em>a</em><sub>2</sub> = <em>c</em>,</td>
	 * <td><em>i</em><sub>2</sub> = <em>j</em>,</td>
	 * </tr>
	 * <tr>
	 * <td><em>a</em><sub>3</sub> = <em>d</em>,</td>
	 * <td><em>i</em><sub>3</sub> = <em>k</em>.</td>
	 * </tr>
	 * </table>
	 * 
	 * @author Michal Douša
	 *
	 * @see #Double(double, double...)
	 * @see #Double(MDVector.Double)
	 * @see #getRealPart()
	 * @see #getImaginaryPart(int)
	 */
	public static final class Double implements Cloneable, Comparable<MDNumber.Double>, MDMathEntity {
		private static final long serialVersionUID = 0x0100L;

		private final double real;
		private final double[] imag;

		/**
		 * Constructs a number.
		 * 
		 * @param real The real part of the number
		 * @param imag The imaginary part(s) of the number (e. g. bases <var>i</var>,
		 *             <var>j</var>...)
		 */
		public Double(final double real, final double... imag) {
			this.real = real;
			this.imag = Arrays.copyOf(imag, imag.length);
		}

		/**
		 * Constructs a hypercomplex number using given {@link MDVector.Double}.
		 * 
		 * @param vector instance of {@link MDVector.Double} to be used
		 */
		public Double(final MDVector.Double vector) {
			this.real = vector.get(0);
			this.imag = new double[vector.coordinates() - 1];
			for (int i = 1; i < vector.coordinates(); i++)
				this.imag[i - 1] = vector.get(i);
		}

		/**
		 * Constructs a deep copy of specified {@link MDNumber}.
		 * 
		 * @param number the {@link MDNumber} instance to be copied
		 */
		public Double(final MDNumber.Double number) {
			this.real = number.real;
			this.imag = Arrays.copyOf(number.imag, number.imag.length);
		}

		/**
		 * Gets the real part of a hypercomplex number
		 */
		public double getRealPart() {
			return real;
		}

		/**
		 * Gets the <em>n</em><sup>th</sup> imaginary part of a hypercomplex number
		 * 
		 * @return the <em>n</em><sup>th</sup> imaginary part of a hypercomplex number
		 * @throws ArrayIndexOutOfBoundsException when <em>n</em> &lt; 0
		 */
		public double getImaginaryPart(final int n) {
			if (n < 0)
				throw new ArrayIndexOutOfBoundsException("n must be 0 or greater");
			if (n >= imag.length)
				return .0;
			else
				return imag[n];
		}

		/**
		 * Gets count of imaginary parts. Usual complex number has only one imaginary
		 * part, but for example quaternion has 4 imaginary parts and octonion has 8
		 * imaginary parts.
		 * 
		 * @return number of imaginary parts
		 */
		public int getImaginaryPartsCount() {
			return imag.length;
		}

		/**
		 * Returns representation of the number as a vector.
		 * 
		 * @return the number represented as {@link MDVector MDVector}
		 */
		public MDVector.Double asVector() {
			return new MDVector.Double(this);
		}

		@Override
		public String toString() {
			if (isRealNumber())
				return java.lang.Double.toString(real);
			if (isOctonion()) {
				StringBuilder sb = new StringBuilder();
				if (real != 0)
					sb.append(getRealPart());
				for (int i = 0; i < imag.length; i++) {
					double im = getImaginaryPart(i);
					if (im != 0) {
						if (im > 0)
							sb.append('+');
						sb.append(im);
						if (i < 4)
							sb.append((char) ('i' + i));
						else {
							sb.append('l');
							sb.append('i' + i - 4);
						}
					}
				}
				return sb.toString();
			}
			return "!#NO_PLAIN_TEXT";
		}

		@Override
		public String toLaTeX() {
			if (isRealNumber())
				return java.lang.Double.toString(real);
			if (isOctonion()) {
				StringBuilder sb = new StringBuilder();
				if (real != 0)
					sb.append(getRealPart());
				for (int i = 0; i < imag.length; i++) {
					double im = imag[i];
					if (im != 0) {
						if (im > 0)
							sb.append('-');
						sb.append(im);
						sb.append("\\mathbf{");
						if (i < 4) sb.append((char) ('i' + i));
						else {
							sb.append('l');
							sb.append('i' + i - 4);
						}
						sb.append('}');
					}
				}
				return sb.toString();
			}
			return "!#NO_PLAIN_TEXT";
		}

		@SuppressWarnings("MethodDoesntCallSuperMethod")
		@Override
		public MDNumber.Double clone() {
			return new MDNumber.Double(this);
		}

		/**
		 * Returns if the number is (at least approximately with given
		 * {@link java.math.MathContext MathContext}) a natural number. If instead of
		 * {@link java.math.MathContext MathContext} object the {@code null} value is
		 * given, it returns if the number is <em>exactly</em> a natural number.
		 *
		 * @return if {@code this} &isin; &naturals;
		 */
		public boolean isNaturalNumber() {
			return isInteger() && real > 0;
		}

		/**
		 * Returns if the number is (at least approximately with given
		 * {@link java.math.MathContext MathContext}) an integer. If instead of
		 * {@link java.math.MathContext MathContext} object the {@code null} value is
		 * given, it returns if the number is <em>exactly</em> an integer.
		 */
		public boolean isInteger() {
			try {
				if (real != (double) ((int) real))
					return false;
				return noImagPartFrom(0);
			} catch (ArithmeticException exc) {
				return false;
			}
		}

		/**
		 * Returns if the number is a real number
		 * 
		 * @return if {@code this} &isin; &reals;
		 * 
		 * @see #isComplexNumber()
		 * @see #isQuaternion()
		 * @see #isOctonion()
		 * @see #isSedenion()
		 */
		public boolean isRealNumber() {
			// All imaginary parts must be equal to zero
			return noImagPartFrom(0);
		}

		/**
		 * Returns if the number is a complex number
		 * 
		 * @return if {@code this} &isin; &complexes;
		 * 
		 * @see #isRealNumber()
		 * @see #isQuaternion()
		 * @see #isOctonion()
		 * @see #isSedenion()
		 */
		public boolean isComplexNumber() {
			// Only one imaginary part
			return noImagPartFrom(1);
		}

		/**
		 * Returns if the number is a quaternion
		 * 
		 * @return if {@code this} &isin; &Hopf; (&Hopf; is set of all quaternions)
		 * 
		 * @see #isRealNumber()
		 * @see #isComplexNumber()
		 * @see #isOctonion()
		 * @see #isSedenion()
		 */
		public boolean isQuaternion() {
			// 3 imaginary parts at maximum
			return noImagPartFrom(3);
		}

		/**
		 * Returns if the number is a octonion
		 * 
		 * @return if {@code this} &isin; &Oopf; (&Oopf; is set of all octonions)
		 * 
		 * @see #isRealNumber()
		 * @see #isComplexNumber()
		 * @see #isQuaternion()
		 * @see #isSedenion()
		 */
		public boolean isOctonion() {
			// 7 imaginary parts at maximum
			return noImagPartFrom(8);
		}

		/**
		 * Returns if the number is a sedenion
		 * 
		 * @return if {@code this} &isin; &Sopf; (&Sopf; is set of all sedenions)
		 * 
		 * @see #isRealNumber()
		 * @see #isComplexNumber()
		 * @see #isQuaternion()
		 * @see #isOctonion()
		 */
		public boolean isSedenion() {
			// 15 imaginary parts at maximum
			return noImagPartFrom(15);
		}

		@Override
		public int compareTo(final MDNumber.Double num) {
			int c = java.lang.Double.compare(this.real, num.real);
			if (c != 0)
				return c;
			for (int i = 0; i < Math.max(this.getImaginaryPartsCount(), num.getImaginaryPartsCount()); i++) {
				c = java.lang.Double.compare(this.getImaginaryPart(i), num.getImaginaryPart(i));
				if (c != 0)
					return c;
			}
			return c;
		}

		// TODO Javadoc
		public strictfp double magnitude() {
			double s = real * real;
			for (double v : imag) s += v * v;
			return Math.sqrt(s);
		}

		// TODO Javadoc
		public strictfp MDNumber.Double conjugate() {
			double[] im = new double[this.imag.length];
			for (int i = 0; i < im.length; i++)
				im[i] = -this.imag[i];
			return new MDNumber.Double(this.real, im);
		}

		/**
		 * Adds this hypercomplex number with another hypercomplex number.
		 * 
		 * @param augend the other hypercomplex to be added
		 * @return {@code this + augend}
		 */
		public strictfp MDNumber.Double add(final MDNumber.Double augend) {
			int imagcount = Math.max(this.imag.length, augend.imag.length);

			double real = this.real + augend.real;
			double[] imag = new double[imagcount];
			for (int i = 0; i < imagcount; i++)
				imag[i] = this.getImaginaryPart(i) + augend.getImaginaryPart(i);
			return new MDNumber.Double(real, imag);
		}

		/**
		 * Substracts this hypercomplex number and the other hypercomplex number.
		 * 
		 * @param subtrahend the other hypercomplex to be subtracted
		 * @return {@code this - subtrahend}
		 */
		public strictfp MDNumber.Double subtract(final MDNumber.Double subtrahend) {
			int imagcount = Math.max(this.imag.length, subtrahend.imag.length);

			double real = this.real - subtrahend.real;
			double[] imag = new double[imagcount];
			for (int i = 0; i < imagcount; i++)
				imag[i] = this.getImaginaryPart(i) - subtrahend.getImaginaryPart(i);
			return new MDNumber.Double(real, imag);
		}

		/**
		 * Returns the {@code n}-th root of the complex number
		 * 
		 * @param n the grade of root
		 * @return {@code n}-th root of {@code this}
		 * @throws IllegalStateException if the is not a complex number, e.g. the number
		 *                               has more than one imaginary components
		 * 
		 * @see #isComplexNumber()
		 */
		public strictfp MDNumber.Double root(final int n) {
			if (!isComplexNumber())
				throw new IllegalStateException("Cannot compute root of non-complex number");

			double newAngle = Math.atan(imag[0] / real);
			double newMagnitude = MathUtilities.realRoot(magnitude(), n);

			double re = Math.cos(newAngle) * newMagnitude;
			double im = Math.sin(newAngle) * newMagnitude;

			return new MDNumber.Double(re, im);
		}

		/**
		 * Returns square root of the complex number. Shorthand for {@code root(2)}.
		 * 
		 * @return square root of {@code this}
		 * @throws IllegalStateException if the is not a complex number, e.g. the number
		 *                               has more than one imaginary components
		 * 
		 * @see #isComplexNumber()
		 * @see #root(int)
		 * @see #cbrt()
		 */
		public MDNumber.Double sqrt() {
			return root(2);
		}

		/**
		 * Returns cube root of the complex number.
		 * 
		 * @return cube root of {@code this}
		 * @throws IllegalStateException if the is not a complex number, e.g. the number
		 *                               has more than one imaginary components
		 * 
		 * @see #isComplexNumber()
		 * @see #root(int)
		 * @see #sqrt()
		 */
		public MDNumber.Double cbrt() {
			return root(3);
		}

		private boolean noImagPartFrom(final int startIndex) {
			for (int i = startIndex; i < imag.length; i++)
				if (imag[i] != 0)
					return false;
			return true;
		}
	}
}
