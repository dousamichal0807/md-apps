package md.jgames.jchess.logic;

import java.util.regex.Matcher;

/**
 * Represents a move on chessboard.
 *
 * @author Michal Douša
 * @see GamePlayChessboard
 */
public final class Move implements Comparable<Move>, Cloneable {

    /**
     * Constant representing that there is no promotion.
     *
     * @see #PROMOTION_ROOK
     * @see #PROMOTION_KNIGHT
     * @see #PROMOTION_BISHOP
     * @see #PROMOTION_QUEEN
     * @see #getPromotion()
     */
    public static final byte PROMOTION_NONE = 0;

    /**
     * Constant representing promotion to the rook.
     *
     * @see #PROMOTION_NONE
     * @see #PROMOTION_KNIGHT
     * @see #PROMOTION_BISHOP
     * @see #PROMOTION_QUEEN
     * @see #getPromotion()
     */
    public static final byte PROMOTION_ROOK = 1;

    /**
     * Constant representing promotion to the knight.
     *
     * @see #PROMOTION_NONE
     * @see #PROMOTION_ROOK
     * @see #PROMOTION_BISHOP
     * @see #PROMOTION_QUEEN
     * @see #getPromotion()
     */
    public static final byte PROMOTION_KNIGHT = 2;

    /**
     * Constant representing promotion to the bishop.
     *
     * @see #PROMOTION_NONE
     * @see #PROMOTION_ROOK
     * @see #PROMOTION_KNIGHT
     * @see #PROMOTION_QUEEN
     * @see #getPromotion()
     */
    public static final byte PROMOTION_BISHOP = 3;

    /**
     * Constant representing promotion to the queen.
     *
     * @see #PROMOTION_NONE
     * @see #PROMOTION_ROOK
     * @see #PROMOTION_KNIGHT
     * @see #PROMOTION_BISHOP
     * @see #getPromotion()
     */
    public static final byte PROMOTION_QUEEN = 4;

    private final String from;
    private final String to;
    private final byte promotion;

    public String getSquareFrom() {
        return from;
    }

    public String getSquareTo() {
        return to;
    }

    public byte getPromotion() {
        return promotion;
    }

    @Override
    public int hashCode() {
        return 4096 * (from.charAt(0) - 'a')
                + 512 * (from.charAt(1) - '1')
                + 64 * (to.charAt(0) - 'a')
                + 8 * (to.charAt(1) - '1')
                + promotion;
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
     * Returns move represented in UCI notation.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(from);
        stringBuilder.append(to);
        if (promotion != PROMOTION_NONE)
            stringBuilder.append(promotionConstToChar(promotion));
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
        return this.from.equals(move2.from) && this.to.equals(move2.to) && this.promotion == move2.promotion;
    }

    @Override
    public int compareTo(final Move move2) {
        return this.toString().compareTo(move2.toString());
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
        Matcher matcher = Utilities.PATTERN_UCI_MOVE.matcher(uci);
        if (!matcher.matches())
            throw new IllegalUCIMoveNotationException(uci);

        this.from = matcher.group(1);
        this.to = matcher.group(2);
        this.promotion = promotionCharToConst(matcher.group(3) == null ? null : matcher.group(3).charAt(0));
    }

    /**
     * Creates {@link Move} object from given hash code.
     *
     * @param hashCode the hash code to be the instance created from
     */
    public Move(int hashCode) {
        if (hashCode < 0 || hashCode > 32764)
            throw new IllegalArgumentException("Invalid move hash code");
        byte[] decomposed = new byte[5];

        for (int i = 4; i >= 0; i--) {
            decomposed[i] = (byte) (hashCode % 8);
            hashCode /= 8;
        }
        this.from = (char) (decomposed[0] + 'a') + "" + (char) (decomposed[1] + '1');
        this.to = (char) (decomposed[2] + 'a') + "" + (char) (decomposed[3] + '1');
        this.promotion = decomposed[4];
    }

    /**
     * Constructs new instance that is deep copy of given move.
     *
     * @param move move to be copied data from
     */
    public Move(final Move move) {
        this.from = move.from;
        this.to = move.to;
        this.promotion = move.promotion;
    }


    /**
     * Returns a character used in UCI move notation for piece promotion
     * corresponding to the promotion constant from this class given by parameter.
     *
     * @param constant the "promotion constant"
     * @return corresponding {@code char} of the constant, or {@code null} if
     * {@link #PROMOTION_NONE} was given as parameter
     * @throws IllegalArgumentException if illegal parameter value was given
     */
    public static Character promotionConstToChar(final byte constant) {
        switch (constant) {
            case PROMOTION_NONE:
                return null;
            case PROMOTION_ROOK:
                return 'r';
            case PROMOTION_KNIGHT:
                return 'n';
            case PROMOTION_BISHOP:
                return 'b';
            case PROMOTION_QUEEN:
                return 'q';
            default:
                throw new IllegalArgumentException("Illegal constant for promotion");
        }
    }

    /**
     * Returns appropriate constant used in this class to mark pawn promotion,
     * according to given input ({@code character} argument). If {@code null} is
     * passed, {@link #PROMOTION_NONE} is returned. If other character than
     * {@code 'R'}, {@code 'r'}, {@code 'N'}, {@code 'n'}, {@code 'B'}, {@code 'b'},
     * {@code 'Q'}, {@code 'q'} is passed, {@link IllegalArgumentException} is
     * thrown.
     *
     * @param character the character in UCI move notation used to indicate pawn
     *                  promotion (if there is no promotion, {@code null} should
     *                  be passed
     * @return appropriate constant from this class indicating promotion of pawn
     */
    public static byte promotionCharToConst(final Character character) {
        if (character == null)
            return PROMOTION_NONE;
        switch (character) {
            case 'r':
            case 'R':
                return PROMOTION_ROOK;
            case 'n':
            case 'N':
                return PROMOTION_KNIGHT;
            case 'b':
            case 'B':
                return PROMOTION_BISHOP;
            case 'q':
            case 'Q':
                return PROMOTION_QUEEN;
            default:
                throw new IllegalArgumentException("Illegal promotion piece: \'" + character + "\'");
        }
    }
}
