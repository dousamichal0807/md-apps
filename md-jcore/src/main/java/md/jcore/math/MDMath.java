package md.jcore.math;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Has some static methods and constants useful in mathematics.
 * 
 * @author Michal Douša
 */
public final class MDMath {

	private MDMath() {
	}

	/**
	 * <p>
	 * The Avogadro constant <var>N</var><sub>A</sub>. It says how many particles
	 * are in 1 mol of every chemical compound.
	 * </p>
	 * <p>
	 * <strong>Value:</strong> <var>N</var><sub>A</sub> = 6.02214076 &middot;
	 * 10<sup>23</sup> mol<sup>-1</sup> exactly (after redefinition of SI units in
	 * 2019)
	 * </p>
	 */
	public static final BigDecimal AVOGADRO = new BigDecimal("6.02214076e+23");

	/**
	 * <p>
	 * The exact value of elementary electric charge after redefinition of SI units
	 * in 2019.
	 * </p>
	 * <p>
	 * <strong>Value</strong>: <var>q</var><sub>E</sub> = 1.602176634 &middot;
	 * 10<sup>-19</sup> C exactly
	 * </p>
	 */
	public static final BigDecimal ELEMENTARY_CHARGE = new BigDecimal("1.602176634e-19");

	/**
	 * <p>
	 * The famous Euler number <var>e</var> with precision of 1000 digits.
	 * </p>
	 * <p>
	 * <strong>Value:</strong> 2.718281&hellip; (irrational number)
	 * </p>
	 */
	public static final BigDecimal EULER = new BigDecimal(
			"2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350354");

	/**
	 * <p>
	 * Faraday constant is product of Avogadro constant and elementary electric
	 * charge:
	 * </p>
	 * <p>
	 * <strong>Value:</strong> <var>F</var> = <var>N</var><sub>A</sub> &middot;
	 * <var>q</var><sub>E</sub> = 96485.3321233100184
	 * </p>
	 */
	public static final BigDecimal FARADAY = new BigDecimal("96485.3321233100184");

	/**
	 * <p>
	 * Constant geometrically defined as ratio between perimeter of circle and its
	 * diameter.
	 * </p>
	 * <p>
	 * <strong>Value:</strong> 3.141592&hellip; (irrational number)
	 * </p>
	 */
	public static final BigDecimal PI = new BigDecimal(
			"3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989");

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
	public static BigDecimal rootOfNonnegativeReal(final BigDecimal base, final int grade, final MathContext mc) {
		if (base == null || mc == null)
			throw new NullPointerException("null was passed");
		if (grade <= 0 || mc.getPrecision() < 0 || base.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("Illegal arguments passed");
		if (grade == 1 || base.compareTo(BigDecimal.ZERO) == 0)
			return base;

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
	public static double rootOfNonnegativeReal(final double base, final int n) {
		return Math.pow(base, 1.0 / n);
	}
}