package md.jgames.jchess.components;

import md.jgames.jchess.logic.Chessboard;
import md.jgames.jchess.logic.Move;
import md.jgames.jchess.logic.Move.PawnPromotion;
import md.jgames.jchess.logic.Square;
import mdlib.utils.JFXUtilities;

import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public final class ChessboardView extends Pane {

    private Chessboard chessboard;
    private boolean chessboardReversed;
    private String alternativeText;

    private final Canvas canvas;
    private final AnimationTimer animationTimer;

    public Chessboard getChessboard() {
        return chessboard;
    }

    /**
     * Sets a new {@link Chessboard} showing in this {@link ChessboardView}. Using second argument you can decide if you
     * want to dispose the previous {@link Chessboard} instance. If second argument is set to {@code true} and previous
     * chessboard is set to {@code null}, no exception will be thrown and request to dispose previous chessboard will be
     * ignored.
     *
     * @param chessboard new {@link Chessboard} to be shown
     * @param disposePrevious {@code boolean} value, if you want to dispose the previous {@link Chessboard} instance
     */
    public void setChessboard(final Chessboard chessboard, final boolean disposePrevious) {
        if (disposePrevious && this.chessboard != null)
            this.chessboard.dispose();

        this.chessboard = chessboard;
    }

    /**
     * Sets a new {@link Chessboard} instance to be shown. Previous {@link Chessboard} will not be disposed.
     *
     * @param chessboard new {@link Chessboard} instance to be set
     */
    public void setChessboard(final Chessboard chessboard) {
        setChessboard(chessboard, false);
    }

    /**
     * Returns if the chessboard should be displayed with black pieces in front.
     *
     * @return if the chessboard should be displayed reversed
     */
    public boolean isChessboardReversed() {
        return chessboardReversed;
    }

    /**
     * Used to set if the chessboard should be displayed with black pieces in front
     *
     * @param chessboardReversed if the chessboard should be displayed reversed
     */
    public void setChessboardReversed(final boolean chessboardReversed) {
        this.chessboardReversed = chessboardReversed;
    }

    public String getAlternativeText() {
        return alternativeText;
    }

    public void setAlternativeText(final String alternativeText) {
        this.alternativeText = alternativeText.trim();
    }

    /**
     * Creates a {@link ChessboardView} component.
     */
    public ChessboardView() {
        // Animation timer
        this.animationTimer = JFXUtilities.animationTimer(this::repaint);
        this.animationTimer.start(); // Not ideal!

        // Canvas setup
        this.canvas = new Canvas();
        this.widthProperty().addListener((observable, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        this.heightProperty().addListener((observable, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        this.getChildren().addAll(canvas);

        // Canvas mouse events
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, JFXUtilities.eventHandler(this::onMousePressed));
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, JFXUtilities.eventHandler(this::onMouseReleased));
    }

    // Auxiliary methods and fields

    private Square selectedSquare;
    private SortedSet<Move> selSquarePossibleMoves;
    private boolean mouseDown;

    private Rectangle2D chessboardBounds() {
        int size = (int) Math.min(getWidth(), getHeight()) * 7 / 64 * 8;
        int offsetX = (int) ((getWidth() - size) / 2.0);
        int offsetY = (int) ((getHeight() - size) / 2.0);
        return new Rectangle2D(offsetX, offsetY, size, size);
    }

    private strictfp Rectangle2D rectangleFor(final Square square) {
        Rectangle2D chessboard = chessboardBounds();

        // square size is chessboard size divided by 8, because we have 8x8 squares
        double squareSize = chessboard.getHeight() / 8.0;

        // get square x and y in range from 0 to 7
        byte x = (byte) (chessboardReversed ? 7 - square.file() : square.file());
        byte y = (byte) (chessboardReversed ? square.rank() : 7 - square.rank());

        // Where the square really is (its upper-left corner coordinates)
        double realX = chessboard.getMinX() + x * squareSize;
        double realY = chessboard.getMinY() + y * squareSize;

        return new Rectangle2D(realX, realY, squareSize, squareSize);
    }

    private Square squareAt(final Point2D point) {
        if (chessboardBounds().contains(point))
            for (byte file = 0; file < 8; file++)
                for (byte rank = 0; rank < 8; rank++) {
                    Square square = new Square(rank, file);
                    Rectangle2D rectangle = rectangleFor(square);
                    if (rectangle.contains(point))
                        return square;
                }
        return null;
    }

    // Painting

    private void repaint() {
        // Get GraphicsContext and clear canvas
        GraphicsContext gctx = canvas.getGraphicsContext2D();
        gctx.clearRect(0, 0, getWidth(), getHeight());

        // Draw chessboard if it is not set to null, otherwise draw alternative text
        if (chessboard != null) {
            // Draw chessboard
            JFXUtilities.canvasDrawImage(gctx, ChessboardViewConfiguration.chessboardImage().get(), chessboardBounds());
        } else {
            // Draw alternative text
        }
    }

    // Event handling

    private void onMousePressed(final MouseEvent event) {
        Point2D mousePoint = new Point2D(event.getSceneX(), event.getSceneY());
        Square mouseSquare = squareAt(mousePoint);

        if (!isDisabled() && mouseSquare != null && selectedSquare != null && !selSquarePossibleMoves.contains(new Move(selectedSquare, mouseSquare, PawnPromotion.NONE))) {
            this.selectedSquare = mouseSquare;
            this.selSquarePossibleMoves = chessboard.possibleMovesFor(selectedSquare);
            this.mouseDown = true;
        }
    }

    private void onMouseReleased(final MouseEvent event) {
        this.mouseDown = false;
        Point2D mousePoint = new Point2D(event.getSceneX(), event.getSceneY());
        Square mouseSquare = squareAt(mousePoint);

        if (!isDisabled() && mouseSquare != null) {
            TreeSet<Move> moves = new TreeSet<>();
            for (Move move : selSquarePossibleMoves)
                if (move.squareTo().equals(mouseSquare))
                    moves.add(move);
            if (moves.size() == 1) {
                // If there is exactly one move
                chessboard.performMove(moves.first());
                selectedSquare = null;
                selSquarePossibleMoves = null;
            } else if (moves.size() > 1) {
                // There can be 4 different UCI moves when pawn promoting
                // Construct ListView and show it in an Alert
                ListView<Move> moveListView = new ListView<>();
                moveListView.getItems().addAll(moves);
                // The alert box
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Select move");
                alert.setHeaderText(null);
                alert.setContentText("Select move which you want do perform:");
                alert.getDialogPane().setContent(moveListView);
                // Open alert box and perform selected move, if confirmed
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Move todo = moveListView.getSelectionModel().getSelectedItem();
                    chessboard.performMove(todo);
                }
            }
        }
    }
}
