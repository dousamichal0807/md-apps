package md.jgames.jchess.components;

import md.jgames.jchess.logic.Chessboard;
import md.jgames.jchess.logic.Move;
import mdlib.utils.Filter;
import mdlib.utils.FilteredAtomicReference;
import mdlib.utils.JFXUtilities;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public final class ChessboardView extends Canvas {

    // Initial empty chessboard image
    private static final Image INITTIAL_CHESSBOARD_IMAGE = SwingFXUtils.toFXImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), null);

    // Chessboard image filtered field
    private static final FilteredAtomicReference<Image> chessboardImage = new FilteredAtomicReference<>(Filter.FILTER_NOT_NULL, INITTIAL_CHESSBOARD_IMAGE);

    /**
     * Returns {@link FilteredAtomicReference} to image used as chessboard in this component. Caannot pass {@code null}
     * as value.
     *
     * @return {@link FilteredAtomicReference} to the chessboard image
     *
     * @see FilteredAtomicReference#set(Object)
     */
    public static FilteredAtomicReference<Image> chessboardImage() {
        return chessboardImage;
    }

    private Chessboard chessboard;
    private boolean chessboardReversed;

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
    public void setChessboard(Chessboard chessboard, boolean disposePrevious) {
        if (disposePrevious && this.chessboard != null)
            this.chessboard.dispose();

        this.chessboard = chessboard;
    }

    /**
     * Sets a new {@link Chessboard} instance to be shown. Previous {@link Chessboard} will not be disposed.
     *
     * @param chessboard new {@link Chessboard} instance to be set
     */
    public void setChessboard(Chessboard chessboard) {
        setChessboard(chessboard, false);
    }

    public boolean isChessboardReversed() {
        return chessboardReversed;
    }

    public void setChessboardReversed(boolean chessboardReversed) {
        this.chessboardReversed = chessboardReversed;
    }

    private final AnimationTimer animationTimer;
    private String selectedSquare;
    private SortedSet<Move> selSquarePossibleMoves;
    private boolean mouseDown;

    /**
     * Creates a {@link ChessboardView} component.
     */
    public ChessboardView() {
        // Animation timer
        animationTimer = JFXUtilities.animationTimer(this::repaint);

        Bindings.selectBoolean(sceneProperty(), "window", "showing").addListener((observable, oldValue, newValue) -> {
            if (newValue)
                animationTimer.start();
            else
                animationTimer.stop();
        });

        // Mouse events
        addEventHandler(MouseEvent.MOUSE_PRESSED, JFXUtilities.eventHandler(this::onMousePressed));
        addEventHandler(MouseEvent.MOUSE_RELEASED, JFXUtilities.eventHandler(this::onMouseReleased));
    }

    // Auxiliary methods

    private Rectangle2D chessboardBounds() {
        int size = (int) Math.min(getWidth(), getHeight()) / 8 * 7;
        int offsetX = (int) ((getWidth() - size) / 2.0);
        int offsetY = (int) ((getHeight() - size) / 2.0);
        return new Rectangle2D(offsetX, offsetY, size, size);
    }

    private strictfp Rectangle2D rectangleFor(final String square) {
        Rectangle2D chessboard = chessboardBounds();
        double squareSize = chessboard.getHeight() / 8.0;
        byte x, y;
        if (chessboardReversed) {
            x = (byte) ('h' - square.charAt(0));
            y = (byte) (square.charAt(1) - '1');
        } else {
            x = (byte) (square.charAt(0) - 'a');
            y = (byte) ('8' - square.charAt(1));
        }
        double realX = chessboard.getMinX() + x * squareSize;
        double realY = chessboard.getMinY() + y * squareSize;
        return new Rectangle2D(realX, realY, squareSize, squareSize);
    }

    private String squareAt(final Point2D point) {
        if (chessboardBounds().contains(point))
            for (char file = 'a'; file <= 'h'; file++)
                for (char rank = '1'; rank <= '8'; rank++) {
                    String square = new String(new char[] {rank, file});
                    Rectangle2D rectangle = rectangleFor(square);
                    if (rectangle.contains(point))
                        return square;
                }
        return null;
    }

    // Painting

    private void repaint() {
        // Get GraphicsContext and clear canvas
        GraphicsContext gctx = getGraphicsContext2D();
        gctx.clearRect(0, 0, getWidth(), getHeight());
        if (chessboard != null) {
            // Draw chessboard
            JFXUtilities.canvasDrawImage(gctx, chessboardImage.get(), chessboardBounds());
        }
    }

    // Event handling

    private void onMousePressed(MouseEvent event) {
        Point2D mousePoint = new Point2D(event.getSceneX(), event.getSceneY());
        String mouseSquare = squareAt(mousePoint);

        if (!isDisabled() && mouseSquare != null && selectedSquare != null && !selSquarePossibleMoves.contains(new Move(selectedSquare + mouseSquare))) {
            this.selectedSquare = mouseSquare;
            this.selSquarePossibleMoves = chessboard.possibleMovesFor(selectedSquare);
            this.mouseDown = true;
        }
    }

    private void onMouseReleased(MouseEvent event) {
        this.mouseDown = false;
        Point2D mousePoint = new Point2D(event.getSceneX(), event.getSceneY());
        String mouseSquare = squareAt(mousePoint);

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
