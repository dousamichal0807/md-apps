package md.jgames.jchess.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.Vector;

import md.jcore.collections.BasicTree;
import md.jcore.collections.BasicTreeNode;
import md.jcore.io.ExecutableProcess;

public class AnalysisChessboard extends Chessboard {
	private String startingFEN, currentFEN;
	private BasicTree<Move> moveTree;
	private Vector<Integer> doneMoves;
	
	private ExecutableProcess stockfishProcess;

	@Override
	public String getStartingFEN() {
		return startingFEN;
	}

	@Override
	public String getCurrentFEN() {
		return currentFEN;
	}

	@Override
	public int doneMovesCount() {
		return doneMoves.size();
	}
	
	@Override
	public List<Move> doneMoves() {
		ArrayList<Move> moves = new ArrayList<>(doneMoves.size());
		BasicTreeNode<Move> node = moveTree.getRootNode();
		for (int i = 0; i < doneMoves.size(); i++) {
			node = node.getChildren().get(i);
			moves.add(node.getValue());
		}
		return Collections.unmodifiableList(moves);
	}

	@Override
	public void reset(String fen) {
		Utilities.checkFEN(fen);
		
	}

	@Override
	public void undo() {
		if (!doneMoves.isEmpty()) {
			doneMoves.remove(doneMoves.size() - 1);
			update();
		}
	}

	@Override
	public void redo() {
	}

	@Override
	public void performMove(Move move) {
		// TODO Auto-generated method stub

	}

	@Override
	public SortedSet<Move> possibleMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[][] pieces() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public byte pieceAt(String square) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void update() {
		if (stockfishProcess == null) {
			stockfishProcess = Utilities.createStockfishProcess();
			stockfishProcess.start();
		}
	}

	@Override
	public void dispose() {
		if(!isDisposed()) {
			if(stockfishProcess != null && stockfishProcess.isAlive()) {
				stockfishProcess.close();
				stockfishProcess = null;
			}
			startingFEN = null;
			currentFEN = null;
			moveTree = null;
			doneMoves = null;
		}
	}
	
	@Override
	public boolean isDisposed() {
		return moveTree == null;
	}
}
