package md.jcore.geom;

/**
 * Represents a line in 3D space. Uses high-precision
 * {@link java.math.BigDecimal BigDecimal}s. If you want to use {@code double}, use
 * {@link MDLine3D.Double} instead.
 *
 * @author Michal Douša
 * @see #MDLine3D(MDPoint3D, MDPoint3D)
 * @see #getPointFrom()
 * @see #getPointTo()
 */
public final class MDLine3D {

    private final MDPoint3D from, to;

    /**
     * Construct a 3D line from point where the line started and ended
     *
     * @param from point where the line started
     * @param to   point where the line ended
     */
    public MDLine3D(final MDPoint3D from, final MDPoint3D to) {
        if (from == null || to == null)
            throw new NullPointerException("None of given points to construct line can be null");
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the 3D point where the line starts.
     *
     * @return the 3D point where the line starts
     */
    public MDPoint3D getPointFrom() {
        return from;
    }

    /**
     * Returns the 3D point where the line ends.
     *
     * @return the 3D point where the line ends
     */
    public MDPoint3D getPointTo() {
        return to;
    }

    /**
     * Represents a line in 3D space. Uses {@code double} data type. If you want to
     * use high-precision {@link java.math.BigDecimal BigDecimal}s, use {@link MDLine3D} instead.
     *
     * @author Michal Douša
     * @see #MDLine3D(MDPoint3D, MDPoint3D)
     * @see #getPointFrom()
     * @see #getPointTo()
     */
    public static final class Double {

        private final MDPoint3D.Double from, to;

        /**
         * Construct a 3D line from point where the line started and ended
         *
         * @param from point where the line started
         * @param to   point where the line ended
         */
        public Double(final MDPoint3D.Double from, final MDPoint3D.Double to) {
            if (from == null || to == null)
                throw new NullPointerException("None of given points to construct line can be null");
            this.from = from;
            this.to = to;
        }

        /**
         * Returns the 3D point where the line starts.
         *
         * @return the 3D point where the line starts
         */
        public MDPoint3D.Double getPointFrom() {
            return from;
        }

        /**
         * Returns the 3D point where the line ends.
         *
         * @return the 3D point where the line ends
         */
        public MDPoint3D.Double getPointTo() {
            return to;
        }
    }
}
