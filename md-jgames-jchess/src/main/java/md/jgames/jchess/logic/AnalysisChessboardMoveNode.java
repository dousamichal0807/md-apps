package md.jgames.jchess.logic;

import java.util.Vector;

class AnalysisChessboardMoveNode {
	private final Move move; // immutable
	private final Vector<AnalysisChessboardMoveNode> childNodes; // immutable

	public Move getMove() {
		return move;
	}

	public Vector<AnalysisChessboardMoveNode> getChildNodes() {
		return childNodes;
	}
	
	public AnalysisChessboardMoveNode(Move move) {
		if(move == null)
			throw new NullPointerException();
		this.move = move;
		this.childNodes = new Vector<>();
	}
}
