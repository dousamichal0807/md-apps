package md.jgames.jchess.logic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A class for serializing {@link GamePlayChessboard}.
 * 
 * @author Michal Douša
 * 
 * @see #SerializableGamePlayChessboard(GamePlayChessboard)
 * @see GamePlayChessboard#GamePlayChessboard(SerializableGamePlayChessboard)
 */
public class SerializableGamePlayChessboard implements Serializable {
	private static final long serialVersionUID = 0x0100L;

	private final String startingFEN;
	private final short[] moves;
	private final int movesDone;
	
	public String getStartingFEN() {
		return startingFEN;
	}

	public short[] getMoves() {
		return Arrays.copyOf(moves, moves.length);
	}

	public int getDoneMovesCount() {
		return movesDone;
	}
	
	public SerializableGamePlayChessboard(final GamePlayChessboard chessboard) {
		List<Move> moveList = chessboard.getAllMoves();
		
		startingFEN = chessboard.startingFEN();
		moves = new short[moveList.size()];
		movesDone = chessboard.doneMovesCount();
		
		for(int i = 0; i < moves.length; i++) moves[i] = moveList.get(i).shortHashCode();
	}
}
