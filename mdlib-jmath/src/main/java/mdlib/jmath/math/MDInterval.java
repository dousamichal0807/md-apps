package mdlib.jmath.math;

import java.math.BigDecimal;

/**
 * This class represents an interval of real numbers.
 */
public class MDInterval extends MDNumberSet {
    private static final long serialVersionUID = 0x0100L;

    private final boolean leftClosed, rightClosed;
    private final BigDecimal leftBound, rightBound;

    /**
     * Custructs an interval
     *
     * @param leftClosed  if the interval shoud be left-closed
     * @param leftBound   the left bound of the interval; pass {@code null} for negative infinity.
     * @param rightBound  the right bound of the interval; pass {@code null} for positive infinity.
     * @param rightClosed if the interval shoud be right-closed
     */
    public MDInterval(final boolean leftClosed, final BigDecimal leftBound, final BigDecimal rightBound, final boolean rightClosed) {
        if (leftBound != null && rightBound != null && leftBound.compareTo(rightBound) >= 0)
            throw new IllegalArgumentException("Left bound is not less than right bound of an interval");
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.leftClosed = this.leftBound != null && leftClosed;
        this.rightClosed = rightBound != null && rightClosed;
    }

    /**
     * Returns, if the interval is left-closed.
     *
     * @return if the interval is left-closed
     */
    public boolean isLeftClosed() {
        return leftClosed;
    }

    /**
     * Returns, if the interval is right-closed
     *
     * @return if the interval is right-closed
     */
    public boolean isRightClosed() {
        return rightClosed;
    }

    public BigDecimal getMinimumBound() {
        return leftBound;
    }

    public BigDecimal getMaximumBound() {
        return rightBound;
    }

    @Override
    public boolean contains(final MDNumber number) {
        if (!number.isRealNumber())
            return false;
        return contains(number.getRealPart());
    }

    public boolean contains(final BigDecimal n) {
        boolean b1 = this.leftBound == null || (this.leftClosed ? n.compareTo(this.leftBound) >= 0 : n.compareTo(this.leftBound) > 0);
        boolean b2 = this.rightBound == null || (this.rightClosed ? n.compareTo(this.rightBound) <= 0 : n.compareTo(this.rightBound) < 0);
        return b1 && b2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        if (leftBound == null && rightBound == null)
            return "R";
        StringBuilder sb = new StringBuilder();
        sb.append(leftClosed ? '[' : '(');
        sb.append(leftBound == null ? "\u2013\u221E" : leftBound);
        sb.append("; ");
        sb.append(rightBound == null ? "+\u221E" : rightBound);
        sb.append(rightClosed ? ']' : ')');
        return sb.toString();
    }

    @Override
    public String toLaTeX() {
        if (leftBound == null && rightBound == null)
            return "\\mathbb{R}";
        StringBuilder sb = new StringBuilder();
        sb.append(leftClosed ? "\\left[" : "\\left(");
        sb.append(leftBound == null ? "-\\infty" : leftBound);
        sb.append("; ");
        sb.append(rightBound == null ? "+\\infty" : rightBound);
        sb.append(rightClosed ? "\\right]" : "\\right)");
        return sb.toString();
    }

    public static final class Double extends MDNumberSet.Double {
        private static final long serialVersionUID = 0x0100L;

        private final boolean fromLeftSideClosed, fromRightSideClosed;
        private final double leftBound;
        private final double rightBound;

        public Double(final boolean lc, final double min, final double max, final boolean rc) {
            if (min >= max)
                throw new IllegalArgumentException("Left bound is not less than right bound of an interval");
            this.leftBound = min;
            this.rightBound = max;
            this.fromLeftSideClosed = lc;
            this.fromRightSideClosed = rc;
        }

        public boolean isFromLeftSideClosed() {
            return fromLeftSideClosed;
        }

        public boolean isFromRightSideClosed() {
            return fromRightSideClosed;
        }

        public double getMinimumBound() {
            return leftBound;
        }

        public double getMaximumBound() {
            return rightBound;
        }

        @Override
        public boolean contains(final MDNumber.Double number) {
            if (!number.isRealNumber())
                return false;
            return contains(number.getRealPart());
        }

        public boolean contains(final double n) {
            boolean b1 = this.leftBound == java.lang.Double.MIN_VALUE || (this.fromLeftSideClosed ? n >= this.leftBound : n > this.leftBound);
            boolean b2 = this.rightBound == java.lang.Double.MAX_VALUE || (this.fromRightSideClosed ? n <= this.leftBound : n < this.leftBound);
            return b1 && b2;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String toString() {
            if (leftBound == java.lang.Double.MIN_VALUE && rightBound == java.lang.Double.MAX_VALUE)
                return "R";
            StringBuilder sb = new StringBuilder();
            sb.append(fromLeftSideClosed ? '[' : '(');
            sb.append(leftBound == java.lang.Double.MIN_VALUE ? "\u2013\u221E" : leftBound);
            sb.append("; ");
            sb.append(rightBound == java.lang.Double.MAX_VALUE ? "+\u221E" : rightBound);
            sb.append(fromRightSideClosed ? ']' : ')');
            return sb.toString();
        }

        @Override
        public String toLaTeX() {
            if (leftBound == java.lang.Double.MIN_VALUE && rightBound == java.lang.Double.MAX_VALUE)
                return "\\mathbb{R}";
            StringBuilder sb = new StringBuilder();
            sb.append(fromLeftSideClosed ? "\\left[" : "\\left(");
            sb.append(leftBound == java.lang.Double.MIN_VALUE ? "-\\infty" : leftBound);
            sb.append("; ");
            sb.append(rightBound == java.lang.Double.MAX_VALUE ? "+\\infty" : rightBound);
            sb.append(fromRightSideClosed ? "\\right]" : "\\right)");
            return sb.toString();
        }
    }
}
