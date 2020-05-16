package md.jgames.jchess.logic;

import java.util.Vector;

public class AnalysisChessboardMoveTree {
	private Vector<AnalysisChessboardMoveNode> rootNodes;
	
	public Move getMove(int rootIndex, int...indices) {
		AnalysisChessboardMoveNode node = rootNodes.get(rootIndex);
		for(int i = 0; i < indices.length; i++) {
			node = node.getChildNodes().get(indices[i]);
		}
		return node.getMove();
	}
	
	public void addMove(Move move, int rootIndex, int...indices) {
		// TODO pridej tah
	}
}
