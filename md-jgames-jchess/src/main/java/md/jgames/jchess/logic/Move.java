package md.jgames.jchess.logic;

import java.util.regex.Matcher;

/**
 * Represents a move on chessboard.
 * 
 * @author Michal Douša
 * @see GamePlayChessboard
 */
public final class Move implements Comparable<Move> {
	private String from, to;
	private Character promotingPiece;

	public String getSquareFrom() {
		return from;
	}

	public String getSquareTo() {
		return to;
	}

	public Character getPromotingPiece() {
		return promotingPiece;
	}

	public int hashCode() {
		int hash = 0;
		hash += 4096 * (from.charAt(0) - 'a');
		hash += 512 * (from.charAt(1) - '1');
		hash += 64 * (to.charAt(0) - 'a');
		hash += 8 * (to.charAt(1) - '1');
		if (promotingPiece != null) {
			switch (promotingPiece) {
			case 'r':
				hash += 1;
				break;
			case 'n':
				hash += 2;
				break;
			case 'b':
				hash += 3;
				break;
			case 'q':
				hash += 4;
				break;
			}
		}
		return hash;
	}
	
	public short shortHashCode() {
		return (short) hashCode();
	}

	/**
	 * Returns move represented in UCI notation.
	 */
	public String toString() {
		return from + to + (promotingPiece == null ? "" : promotingPiece);
	}

	public int compareTo(Move move2) {
		return this.toString().compareTo(move2.toString());
	}

	public Move(String from, String to, Character pp) {
		if (!Utilities.isValidSquare(from) || !Utilities.isValidSquare(to))
			throw new IllegalArgumentException("\'" + from + "\' or \'" + to + "\' is not valid square.");
		if (pp != null) {
			pp = Character.toLowerCase(pp);
			switch (pp) {
			case 'q':
			case 'b':
			case 'n':
			case 'r':
				break;
			default:
				throw new IllegalArgumentException("\'" + pp + "\' is not valid promoting piece.");
			}
		}
		this.from = from;
		this.to = to;
		this.promotingPiece = pp;
	}

	public Move(String uci) {
		Matcher matcher = Utilities.PATTERN_UCI_MOVE.matcher(uci);
		if(!matcher.matches())
			throw new IllegalUCIMoveNotation(uci);
		
		this.from = matcher.group(1);
		this.to = matcher.group(2);
		
		String pp = matcher.group(3);
		if (pp != null)
			this.promotingPiece = pp.charAt(0);
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
		switch (decomposed[4]) {
		case 0:
			promotingPiece = null;
			break;
		case 1:
			promotingPiece = 'r';
			break;
		case 2:
			promotingPiece = 'n';
			break;
		case 3:
			promotingPiece = 'b';
			break;
		case 4:
			promotingPiece = 'q';
			break;
		}
	}

	public Move(Move move) {
		this.from = move.from;
		this.to = move.to;
		this.promotingPiece = move.promotingPiece;
	}
}
