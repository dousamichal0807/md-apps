package md.jgames.jchess.testing;

import md.jgames.jchess.logic.AnalysisChessboard;
import md.jgames.jchess.logic.Move;
import md.jgames.jchess.logic.Utilities;
import mdlib.utils.collections.TreeNode.UnmodifiableNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalysisChessboardTest {

    @Test
    public void test1() {
        // Create chessboard
        AnalysisChessboard chessboard = new AnalysisChessboard();

        // Create some moves...
        Move e2e4 = new Move("e2e4");
        Move e7e5 = new Move("e7e5");

        // ...Perform them
        chessboard.performMove(e2e4);
        chessboard.performMove(e7e5);

        // Undo last move
        chessboard.undo();

        // Assertions ---------------------------------------------------------

        // Done moves
        assertArrayEquals(chessboard.doneMoves().toArray(), new Move[] {e2e4});
        // FEN
        assertEquals(Utilities.FEN_STARTING_POSITION, chessboard.startingFEN(), "Starting FEN not matching");
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1", chessboard.currentFEN(), "Current FEN not watching");
        // Move tree
        UnmodifiableNode<Move> e2e4Expected = chessboard.getRootNode().childNodes().get(0);
        assertEquals(e2e4, e2e4Expected.getValue(), "Invalid tree structure: " + e2e4Expected.getValue() + "instead of e2e4");
        UnmodifiableNode<Move> e5e7Expected = e2e4Expected.childNodes().get(0);
        assertEquals(e2e4, e2e4Expected.getValue(), "Invalid tree structure: " + e5e7Expected.getValue() + "instead of e7e5");
    }
}
