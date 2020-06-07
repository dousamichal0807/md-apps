package md.jsk.mathcalc.math;

import mdlib.jmath.math.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;

public final class Equations {

    // Do not create any instances
    private Equations() {
    }

    /**
     * <p>Solves linear equation</p>
     * <blockquote><var>a</var><var>x</var> + <var>b</var> = 0</blockquote>
     *
     * @param a the <var>a</var> coefficient as above
     * @param b the <var>b</var> coefficient as above
     * @return all roots of given linear equation
     */
    public static strictfp MDNumberSet.Double solveLinear(final double a, final double b) {
        ArrayList<MDNumber.Double> roots = new ArrayList<>(1);
        if (a == 0 && b == 0)
            return new MDInterval.Double(false, Double.MIN_VALUE, Double.MAX_VALUE, false);
        if (b != 0)
            roots.add(new MDNumber.Double(-b / a));
        return new MDNumberFiniteSet.Double(roots);
    }

    /**
     * <p>Solves linear equation</p>
     * <blockquote><var>a</var><var>x</var> + <var>b</var> = 0</blockquote>
     *
     * @param a  the <var>a</var> coefficient as above
     * @param b  the <var>b</var> coefficient as above
     * @param mc {@link MathContext} to be used in calculations
     * @return all roots of given linear equation
     * @throws NullPointerException if {@code a}, {@code b} or {@code mc} is {@code null}
     */
    public static MDNumberSet solveLinear(final BigDecimal a, final BigDecimal b, final MathContext mc) {
        ArrayList<MDNumber> roots = new ArrayList<>(1);
        if (a.compareTo(BigDecimal.ZERO) == 0 && b.compareTo(BigDecimal.ZERO) == 0)
            return new MDInterval(false, null, null, false);
        if (b.compareTo(BigDecimal.ZERO) != 0)
            roots.add(new MDNumber(a.multiply(BigDecimal.valueOf(-1L), mc).divide(b, mc)));
        return new MDNumberFiniteSet(roots);
    }

    /**
     * <p>Solves binomic equation</p>
     * <blockquote><var>a</var><var>x</var><sup><var>n</var></sup> + <var>b</var> = 0</blockquote>
     *
     * @param n the <var>n</var> &isin; &Nopf; coefficient as above
     * @param a the <var>a</var> coefficient as above
     * @param b the <var>b</var> coefficient as above
     * @return all roots of given binomial equation
     */
    @SuppressWarnings("unchecked")
    public static MDNumberSet.Double solveBinomial(final int n, final double a, final double b) {
        if (a == 0 && b == 0)
            return null;
        if (a == 0)
            return new MDNumberFiniteSet.Double(Collections.EMPTY_SET);
        return solveBinomial(n, b / a);
    }

    /**
     * <p>Solves binomic equation</p>
     * <blockquote><var>x</var><sup><var>n</var></sup> + <var>c</var> = 0</blockquote>
     *
     * @param n <var>n</var> &isin; &Nopf; coefficient as above
     * @param c <var>c</var> coefficient as above
     * @return set of all roots of given binomial equation
     */
    public static strictfp MDNumberSet.Double solveBinomial(final int n, final double c) {
        ArrayList<MDNumber.Double> roots = new ArrayList<>(n);

        if (c != 0) {
            double root = MathUtilities.realRoot(Math.abs(c), n);
            for (int i = 0; i < n; i++) {
                double angle = 2.0 * Math.PI / n + (c < 0 ? Math.PI : 0);
                roots.add(new MDNumber.Double(root * Math.cos(angle), root * Math.sin(angle)));
            }
        }
        return new MDNumberFiniteSet.Double(roots);
    }
}
