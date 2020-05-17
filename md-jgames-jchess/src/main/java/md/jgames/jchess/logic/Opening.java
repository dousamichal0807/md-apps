package md.jgames.jchess.logic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a particular opening with ECO code, opening name and done
 * moves from standard initial position.
 * 
 * @author Michal Douša
 * 
 * @see #Opening(String, String, Move...)
 * @see #getECOCode()
 * @see #getName()
 * @see #moves()
 * @see #asChessboard()
 */
public final class Opening implements Comparable<Opening> {
	private static final Pattern PATTERN_OPENING_ECO_CODE = Pattern.compile("[A-E][0-9]{2}");

	private final String name;
	private final String ecoCode;
	private final Move[] moves;

	/**
	 * Creates opening from given ECO code, name and done moves from standard
	 * initial position.
	 * 
	 * @param eco   ECO code of the opening
	 * @param name  opening name
	 * @param moves done moves as array
	 */
	public Opening(String eco, String name, Move... moves) {
		// Opening name
		if (name == null)
			throw new NullPointerException("Opening name cnnot be null");
		this.name = name;

		// Code in ECO
		if (eco != null && !eco.isEmpty()) {
			Matcher ecoMatcher = PATTERN_OPENING_ECO_CODE.matcher(eco);
			if (!ecoMatcher.matches() && !eco.isBlank())
				throw new IllegalArgumentException("Illegal ECO code");
			this.ecoCode = eco;
		} else {
			this.ecoCode = null;
		}

		// Done moves
		this.moves = new Move[moves.length];
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] == null)
				throw new NullPointerException();
			this.moves[i] = moves[i];
		}
	}

	/**
	 * Returns the name of the opening.
	 * 
	 * @return opening name
	 * 
	 * @see #getECOCode()
	 * @see #moves()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the code of the opening in ECO (Encyclopedia of Chess Openings).
	 * Returns {@code null} if the opening is not present there.
	 * 
	 * @return ECO code of the opening; if the opening is not in ECO, then
	 *         {@code null}
	 *         
	 * @see #getName()
	 * @see #moves()
	 */
	public String getECOCode() {
		return ecoCode;
	}
	
	/**
	 * Returns done moves as a deep clone of array of done moves.
	 * 
	 * @return done moves in array
	 * 
	 * @see #moveList()
	 * @see #asChessboard()
	 */
	public Move[] moves() {
		// Copy moves into new array. It makes sure that the opening cannot be
		// modified (all instances of this class are immutable).
		Move[] m = new Move[moves.length];
		for (int i = 0; i < m.length; i++)
			m[i] = new Move(moves[i]);
		return m;
	}

	/**
	 * Returns unmodifiable {@link java.util.List List} of done moves.
	 * 
	 * @return unmodifiable list of done moves
	 * 
	 * @see #moves()
	 * @see #asChessboard()
	 */
	public List<Move> moveList() {
		// Make sure that the instance stays immutable.
		return List.of(moves);
	}

	/**
	 * Creates a {@link GamePlayChessboard} with moves done same as in the opening.
	 * 
	 * @return chessboard with same moves
	 */
	public GamePlayChessboard asChessboard() {
		// Create chessboard with standard initial position
		GamePlayChessboard chessboard = new GamePlayChessboard();

		// Perform all moves
        for (Move move : moves) chessboard.performMove(move);

		// Return the chessboard
		return chessboard;
	}
	
	@Override
	public String toString() {
		// Ctrate StringBuilder
		StringBuilder sb = new StringBuilder();
		
		// Insert ECO code, if available
		if (ecoCode != null) {
			sb.append('[');
			sb.append(ecoCode);
			sb.append("] ");
		}
		
		// Insert openning name
		sb.append(name);
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object object2) {
		// false if it is not opening
		if (!(object2 instanceof Opening))
			return false;
		
		// Cast to opening
		Opening opening2 = (Opening) object2;
		
		// Both openings must have same moves, e.g. same moves count from standard
		// initial position...
		if (this.moves.length != opening2.moves.length)
			return false;
		
		// ...and we need to compare them one by one
		for (int i = 0; i < this.moves.length; i++) {
			// If there is at least one different move, return false...
			if (!this.moves[i].equals(opening2.moves[i]))
				return false;
		}
		
		// ...otherwise return true
		return true;
	}
	
	@Override
	public int compareTo(Opening opening2) {
		// First, compare by eco code
		int compare = this.ecoCode.compareTo(opening2.ecoCode);
		
		// If they aren't the same than we're done
		if (compare != 0)
			return compare;
		
		// Otherwise, compare by done moves Of course, we want to choose smaller
		// length to be 'i' less than that while iterating.
		for (int i = 0; i < Math.min(this.moves.length, opening2.moves.length); i++) {
			compare = this.moves[i].compareTo(opening2.moves[i]);
			if (compare != 0)
				return compare;
		}
		
		// If they are all same, the last thing to do is to compare them by count of
		// moves
		return Integer.compare(opening2.moves.length, this.moves.length);
	}
}
