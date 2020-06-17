package md.jgames.jchess.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a move on chessboard.
 *
 * @author Michal Douša
 * @see GamePlayChessboard
 */
public final class Move implements Comparable<Move>, Cloneable {

    /**
     * Regex pattern for UCI move notation.
     *
     * @see Move
     */
    public static final Pattern PATTERN_UCI_MOVE = Pattern.compile("([a-h][1-8])([a-h][1-8])([qrbn])?");

    private final Square from;
    private final Square to;
    private final PawnPromotion pawnPromotion;

    /**
     * Returns the square, where the piece moves from.
     *
     * @return square which the piece moves from
     */
    public Square squareFrom() {
        return from;
    }

    /**
     * Returns the square, where the piece moves to.
     *
     * @return square which the piece moves to
     */
    public Square squareTo() {
        return to;
    }

    /**
     * Returns which of pawn promotion should be done during performing the move.
     *
     * @return Some value from {@link PawnPromotion} enumeration, as specified above.
     */
    public PawnPromotion pawnPromotion() {
        return pawnPromotion;
    }

    @Override
    public int hashCode() {
        return (from.hashCode() >> 9) | (to.hashCode() >> 3) | pawnPromotion.hashCode();
    }

    /**
     * Returns same number as {@link #hashCode()}, casted to {@code short}.
     *
     * @return hash code as {@code short}
     */
    public short shortHashCode() {
        return (short) hashCode();
    }

    /**
     * Returns move represented in UCI notation used by chess engines.
     *
     * @return move in UCI notation
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(from);
        stringBuilder.append(to);
        stringBuilder.append(pawnPromotion);
        return stringBuilder.toString();
    }

    /**
     * Returns boolean value, if this and other given object is equal.
     *
     * @param o2 other object to be compared
     * @return if {@code o2} is same as this move (if {@code o2} is not instance of {@link Move}, then automatically
     * {@code false} is returned)
     */
    @Override
    public boolean equals(final Object o2) {
        if (!(o2 instanceof Move))
            return false;
        Move move2 = (Move) o2;
        return from.equals(move2.from) && to.equals(move2.to) && this.pawnPromotion == move2.pawnPromotion;
    }

    @Override
    public int compareTo(final Move move2) {
        return Integer.compare(this.hashCode(), move2.hashCode());
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Move clone() {
        return new Move(this);
    }

    /**
     * Creates instance of {@link Move} from given UCI move notation.
     *
     * @param uci UCI move notation
     * @see #Move(int)
     */
    public Move(final String uci) {
        Matcher matcher = PATTERN_UCI_MOVE.matcher(uci);
        if (!matcher.matches())
            throw new IllegalArgumentException("Illegal UCI move notation: \'" + uci + "\'");

        this.from = new Square(matcher.group(1));
        this.to = new Square(matcher.group(2));
        this.pawnPromotion = PawnPromotion.fromChar(matcher.group(3) == null ? null : matcher.group(3).charAt(0));
    }

    /**
     * Creates move from a square that piece is moving from, a square that piece is moving to and if there is a pawn
     * promotion.
     *
     * @param from          the square that piece is moving from
     * @param to            the square that piece is moving to
     * @param pawnPromotion pawn promotion information
     * @throws NullPointerException if at least one passed argument is {@code null}.
     */
    public Move(final Square from, final Square to, final PawnPromotion pawnPromotion) {
        // Require non-null squares
        Objects.requireNonNull(from, "Square from cannot be null");
        Objects.requireNonNull(from, "Square to cannot be null");
        // Assign values
        this.from = from;
        this.to = to;
        this.pawnPromotion = pawnPromotion;
    }

    /**
     * Creates move from a square that piece is moving from, a square that piece is moving to. While using this
     * contructor you are saying, that there is no pawn promotion.
     *
     * @param from the square that piece is moving from
     * @param to   the square that piece is moving to
     * @throws NullPointerException if at least one passed argument is {@code null}.
     * @see #Move(Square, Square, PawnPromotion)
     */
    public Move(final Square from, final Square to) {
        this(from, to, PawnPromotion.NONE);
    }

    /**
     * Creates {@link Move} object from given valid hash code. If invalid hash code is given, {@link
     * IllegalArgumentException} will be thrown
     *
     * @param hashCode the hash code to be the instance created from
     * @throws IllegalArgumentException if invalid hash code is given
     * @throws NullPointerException     if at least one passed argument is {@code null}.
     */
    public Move(final int hashCode) {
        if (hashCode < 0 || hashCode > 32764)
            throw new IllegalArgumentException("Invalid move hash code");

        // Get square from, square to and promotion hash code
        int hcFrom = (hashCode >> 9) & 64;
        int hcTo = (hashCode >> 3) & 64;
        int hcPromotion = hashCode & 8;

        // Assign values
        this.from = new Square(hcFrom);
        this.to = new Square(hcTo);
        this.pawnPromotion = PawnPromotion.fromHashCode(hcPromotion);
    }

    /**
     * Constructs new instance that is deep copy of given move.
     *
     * @param move move to be copied data from
     */
    public Move(final Move move) {
        this.from = move.from;
        this.to = move.to;
        this.pawnPromotion = move.pawnPromotion;
    }

    /**
     * An enumeration-like class defining finite values. Used to indicate pawn promotion. This class is not a Java
     * enumeration because of disability of overriding {@link #hashCode()} method.
     *
     * @author Michal Douša
     */
    public static final class PawnPromotion implements Comparable<PawnPromotion> {

        private static final HashSet<PawnPromotion> values = new HashSet<>();

        public static final PawnPromotion NONE = new PawnPromotion(null, (byte) 0);
        public static final PawnPromotion ROOK = new PawnPromotion('r', (byte) 1);
        public static final PawnPromotion KNIGHT = new PawnPromotion('n', (byte) 2);
        public static final PawnPromotion BISHOP = new PawnPromotion('b', (byte) 3);
        public static final PawnPromotion QUEEN = new PawnPromotion('q', (byte) 4);

        private final Character character;
        private final byte hashCode;

        // ctor
        private PawnPromotion(final Character character, final byte hashCode) {
            this.character = character;
            this.hashCode = hashCode;
            values.add(this);
        }

        /**
         * Converts {@link PawnPromotion} value to corresponding character.
         *
         * @return character used in UCI notation
         */
        public Character toChar() {
            return character;
        }

        /**
         * Returns same result as {@link #hashCode()} method, but as {@code byte} instead of {@code int}.
         *
         * @return hash code as a {@code byte value}
         */
        public byte byteHashCode() {
            return hashCode;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(final Object obj) {
            // is obj instance of PawnPromotion?
            if (!(obj instanceof PawnPromotion))
                return false;

            // We can cast the object
            PawnPromotion pp2 = (PawnPromotion) obj;
            return this.hashCode == pp2.hashCode;
        }

        @Override
        public String toString() {
            return character == null ? "" : Character.toString(character);
        }

        @Override
        public int compareTo(final PawnPromotion obj) {
            return Byte.compare(this.hashCode, obj.hashCode);
        }

        // Static members -----------------------------------------------------

        public static Set<PawnPromotion> values() {
            return Collections.unmodifiableSet(values);
        }

        public static PawnPromotion fromChar(final Character character) {
            for (PawnPromotion value : PawnPromotion.values())
                if (value.character == character)
                    return value;
            throw new IllegalArgumentException("Invalid character passed");
        }

        public static PawnPromotion fromHashCode(final int hashCode) {
            for (PawnPromotion value : PawnPromotion.values())
                if (value.hashCode() == hashCode)
                    return value;
            throw new IllegalArgumentException("Invalid hash code passed");
        }
    }
}
