package md.jcore.math;

import java.math.*;

public class MDInterval extends MDNumberSet {
    private static final long serialVersionUID = 0x0100L;

    private final boolean fromLeftSideClosed, fromRightSideClosed;
    private BigDecimal leftBound, rightBound;

    public MDInterval(final boolean lc, BigDecimal min, BigDecimal max, final boolean rc) {
        if (min.compareTo(max) >= 0)
            throw new IllegalArgumentException("Left bound is not less than right bound of an interval");
        this.leftBound = min;
        this.rightBound = max;
        this.fromLeftSideClosed = leftBound != null && lc;
        this.fromRightSideClosed = rightBound != null && lc;
    }

    public boolean isFromLeftSideClosed() {
        return fromLeftSideClosed;
    }

    public boolean isFromRightSideClosed() {
        return fromRightSideClosed;
    }

    public BigDecimal getMinimumBound() {
        return leftBound;
    }

    public BigDecimal getMaximumBound() {
        return rightBound;
    }

    public boolean contains(MDNumber number) {
        if (!number.isRealNumber())
            return false;
        return contains(number.getRealPart());
    }

    public boolean contains(BigDecimal n) {
        boolean b1 = this.leftBound == null || (this.fromLeftSideClosed ? n.compareTo(this.leftBound) >= 0 : n.compareTo(this.leftBound) > 0);
        boolean b2 = this.rightBound == null || (this.fromRightSideClosed ? n.compareTo(this.rightBound) <= 0 : n.compareTo(this.rightBound) < 0);
        return b1 && b2;
    }

    public boolean contains(double d) {
        return this.contains(BigDecimal.valueOf(d));
    }

    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        if (leftBound == null && rightBound == null)
            return "R";
        StringBuilder sb = new StringBuilder();
        sb.append(fromLeftSideClosed ? '[' : '(');
        sb.append(leftBound == null ? "\u2013\u221E" : leftBound);
        sb.append("; ");
        sb.append(rightBound == null ? "+\u221E" : rightBound);
        sb.append(fromRightSideClosed ? ']' : ')');
        return sb.toString();
    }

    @Override
    public String toLaTeX() {
        if (leftBound == null && rightBound == null)
            return "\\mathbb{R}";
        StringBuilder sb = new StringBuilder();
        sb.append(fromLeftSideClosed ? "\\left[" : "\\left(");
        sb.append(leftBound == null ? "-\\infty" : leftBound);
        sb.append("; ");
        sb.append(rightBound == null ? "+\\infty" : rightBound);
        sb.append(fromRightSideClosed ? "\\right]" : "\\right)");
        return sb.toString();
    }
}
