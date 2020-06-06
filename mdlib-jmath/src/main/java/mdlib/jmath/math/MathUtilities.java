package mdlib.jmath.math;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Has some static methods and constants useful in mathematics.
 * 
 * @author Michal Douša
 */
public final class MathUtilities {

	private MathUtilities() {
	}

	/**
	 * Count the root of <em>non-negative</em> real number using given
	 * {@link MathContext}
	 * 
	 * @param base  the number under the root
	 * @param grade the grade of the root
	 * @param mc    the given {@link MathContext} object to calculate desired
	 *              precision
	 * @return <var>n</var><sup>th</sup> root of <var>x</var>, where <var>n</var> is
	 *         {@code grade} and <var>x</var> {@code base}
	 * @throws NullPointerException     if {@code base} or {@code mc} is
	 *                                  {@code null}
	 * @throws IllegalArgumentException if {@code grade} is less or equal to zero or
	 *                                  {@code mc}'s precision is less than zero or
	 *                                  {@code base} is less than zero
	 */
	public static BigDecimal realRoot(final BigDecimal base, final int grade, final MathContext mc) {
		if (base == null || mc == null)
			throw new NullPointerException("null was passed");
		if (grade <= 0 || mc.getPrecision() < 0 || base.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("Illegal arguments passed");
		if (grade == 1 || base.compareTo(BigDecimal.ZERO) == 0)
			return base;
		if (grade % 2 == 0 && base.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("Even root of negative number is not a real number");

		BigDecimal r = BigDecimal.ZERO;
		for (int i = (int) -Math.log10(base.doubleValue()); i < mc.getPrecision() + 2; i++) {
			BigDecimal unit = BigDecimal.TEN;
			if (i < 0) unit = unit.pow(-i);
			else unit = BigDecimal.valueOf(1.0).divide(unit.pow(i), mc);
			while (r.pow(grade).compareTo(base) < 1) {
				if (r.pow(grade).compareTo(base) == 0)
					return r.round(mc);
				r = r.add(unit);
			}
			r = r.subtract(unit);
		}
		return r.round(mc);
	}

	/**
	 * Calculates root of any non-negative real number.
	 * @param base the number to take root from
	 * @param n    the grade of root
	 * @return {@code n}-th root of {@code base}
	 */
	public static strictfp double realRoot(final double base, final int n) {
		if (base < 0 && n % 2 == 0)
			throw new IllegalArgumentException("Even root of negative number is not a real number");
		return Math.pow(base, 1.0 / n);
	}
}