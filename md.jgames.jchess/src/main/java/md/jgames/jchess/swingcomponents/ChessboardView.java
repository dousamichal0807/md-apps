package md.jgames.jchess.swingcomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import mdlib.utils.drawing.AdvancedAWTGraphics;
import md.jgames.jchess.logic.Chessboard;
import md.jgames.jchess.logic.ChessboardEvent;
import md.jgames.jchess.logic.ChessboardListener;
import md.jgames.jchess.logic.Move;
import md.jgames.jchess.logic.Utilities;
import md.jgames.jchess.resources.AppResources;

/**
 * Component for showing a chessboard. If {@link #isEnabled()} returns
 * {@code true}, then the user is able to move with chessmen. Otherwise, user is
 * not able to manipulate with che chesboard.
 * 
 * @author Michal Douša
 * 
 * @see #getChessboard()
 * @see #setChessboard(Chessboard)
 */
public final class ChessboardView extends JComponent implements MouseListener, MouseMotionListener, ChessboardListener {

	private static final long serialVersionUID = 1L;

	private Chessboard chessboard;
	private String alternativeText;

	private final TreeMap<Byte, Image> chessmenImagesMap;
	private final TreeMap<String, Color> markedSquares;
	private boolean chessboardReversed;

	/**
	 * Construct a new {@code ChessboardView}. The component will show up given
	 * chessboard.
	 * 
	 * @param chessboard chessboard to be shown
	 * 
	 * @see #getChessboard()
	 * @see #setChessboard(Chessboard)
	 */
	public ChessboardView(final Chessboard chessboard) {
		this.setChessboard(chessboard);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setMinimumSize(new Dimension(200, 200));
		chessmenImagesMap = new TreeMap<>();
		markedSquares = new TreeMap<>();
		selSquarePossibleMoves = new TreeSet<>();
		reset();
	}

	/**
	 * Returns currently showing chessboard.
	 * 
	 * @return the chessboard currently shown
	 * 
	 * @see #setChessboard(Chessboard)
	 */
	public Chessboard getChessboard() {
		return chessboard;
	}

	/**
	 * Sets the chessboard to be shown in the component. if {@code null} is passed,
	 * no chessboard will be shown at all.
	 * 
	 * @param chessboard chessboard to be shown
	 * 
	 * @see #getChessboard()
	 */
	public void setChessboard(final Chessboard chessboard) {
		// Remove this object as listener of the previous chessboard
		if (this.chessboard != null && !this.chessboard.isDisposed())
			this.chessboard.removeChessboardListener(this);
		// Set new chessboard
		this.chessboard = chessboard;
		// Add this object as listener of the new chessboard
		if (this.chessboard != null && !this.chessboard.isDisposed())
			this.chessboard.addChessboardListener(this);
	}

	/**
	 * Returns the alternative text, which is shown when chessboard is set to
	 * {@code null}.
	 * 
	 * @return the alternative text
	 * 
	 * @see #setAlternativeText(String)
	 */
	public String getAlternativeText() {
		return alternativeText;
	}

	/**
	 * Sets the alternative text, which is shown when chessboard is set to
	 * {@code null}. You can also pass {@code null} if you don't want to show
	 * alternative text if there is chessboard set to {@code null}.
	 * 
	 * @param alternativeText the alternative text
	 * 
	 * @see #getAlternativeText()
	 */
	public void setAlternativeText(final String alternativeText) {
		this.alternativeText = alternativeText;
	}

	/**
	 * Returns, if the chessboard is shown upside down, e.g. Black's pieces are
	 * shown in the front of the chessboard.
	 * 
	 * @return if the chessboard is reversed.
	 * 
	 * @see #setChessboardReversed(boolean)
	 * @see #flipChessboard()
	 */
	public boolean isChessboardReversed() {
		return chessboardReversed;
	}

	/**
	 * Sets if the chessboard is shown upside down, e.g. Black's pieces are shown in
	 * the front of the chessboard.
	 * 
	 * @param chessboardReversed if the chessboard shoud be reversed
	 * 
	 * @see #isChessboardReversed()
	 * @see #flipChessboard()
	 */
	public void setChessboardReversed(final boolean chessboardReversed) {
		this.chessboardReversed = chessboardReversed;
		this.repaint();
	}

	/**
	 * Flips the chessboard, e.g. toggles {@code true}/{@code false} if the
	 * chessboard should be reversed.
	 * 
	 * @see #setChessboardReversed(boolean)
	 * @see #isChessboardReversed()
	 */
	public void flipChessboard() {
		this.chessboardReversed = !this.chessboardReversed;
		this.repaint();
	}

	public void putImageForPiece(final byte p, final Image i) {
		if (i == null)
			chessmenImagesMap.remove(p);
		else
			chessmenImagesMap.put(p, i);
	}

	public Image getImageForPiece(final byte piece) {
		return chessmenImagesMap.get(piece);
	}

	public void putChessboardImage(final Image i) {
		putImageForPiece(Chessboard.PIECE_NONE, i);
	}

	public Image getChessboardImage() {
		return getImageForPiece(Chessboard.PIECE_NONE);
	}

	public void markSquare(final String sq, final Color c) {
		Utilities.assertSquareValidity(sq);
		if (c == null || c.getAlpha() == 0)
			markedSquares.remove(sq);
		else
			markedSquares.put(sq, c);
	}

	public void unmarkSquare(final String sq) {
		markedSquares.remove(sq);
	}

	public Color getMarkForSquare(final String sq) {
		return markedSquares.get(sq);
	}

	public void reset() {
		for (char ch : new char[] { 'k', 'q', 'r', 'n', 'b', 'p' }) {
			this.putImageForPiece(Utilities.pieceCharToConstant(ch),
					AppResources.loadImageResource("default-piece-b" + ch + ".png"));
			this.putImageForPiece(Utilities.pieceCharToConstant(Character.toUpperCase(ch)),
					AppResources.loadImageResource("default-piece-w" + ch + ".png"));
		}
		this.putChessboardImage(AppResources.loadImageResource("default-chessboard.png"));
	}

	// Painting & Mouse event handling ----------------------------------------

	private String selectedSquare;
	private final TreeSet<Move> selSquarePossibleMoves;
	private boolean mouseDown;

	public Rectangle getChessboardBounds() {
		Border border = getBorder();
		Insets insets = border == null ? new Insets(0, 0, 0, 0) : border.getBorderInsets(this);
		int w = this.getWidth() - insets.left - insets.right;
		int h = this.getHeight() - insets.top - insets.bottom;
		int s = Math.min(w, h) / 8 * 8; // Size must be divisible by 8
		int x = insets.left + (w - s) / 2;
		int y = insets.top + (h - s) / 2;
		return new Rectangle(x, y, s, s);
	}

	public Rectangle getSquareBounds(final String square) {
		Utilities.assertSquareValidity(square);
		int x = square.charAt(0) - 'a';
		int y = square.charAt(1) - '1';
		Rectangle bounds = this.getChessboardBounds();
		int sqsz = this.getChessboardBounds().width / 8;
		int realx, realy;
		if (!isChessboardReversed()) {
			realx = bounds.x + x * sqsz;
			realy = bounds.y + (7 - y) * sqsz;
		} else {
			realx = bounds.x + (7 - x) * sqsz;
			realy = bounds.y + y * sqsz;
		}
		return new Rectangle(realx, realy, sqsz, sqsz);
	}

	public void selectSquare(final String square) {
		selectedSquare = null;
		selSquarePossibleMoves.clear();

		if (square != null) {
			Utilities.assertSquareValidity(square);
			selectedSquare = square;
			selSquarePossibleMoves.addAll(chessboard.possibleMovesFor(square));
		}
	}

	@Override
	public void paint(final Graphics g0) {
		super.paint(g0);

		Graphics2D g2d = (Graphics2D) g0;
		AdvancedAWTGraphics gadv = new AdvancedAWTGraphics(g2d);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		if (chessboard != null) {
			gadv.paintImage(this.getChessboardImage(), this.getChessboardBounds());

			for (char rank = '1'; rank <= '8'; rank++)
                for (char file = 'a'; file <= 'h'; file++) {
                    String sq = file + "" + rank;
                    Rectangle sqb = this.getSquareBounds(sq);
                    Color mark = this.getMarkForSquare(sq);
                    if (mark != null)
                        gadv.fillRectangle(mark, sqb);
                    byte piece = chessboard.pieceAt(sq);
                    if (piece != Chessboard.PIECE_NONE) {
                        Image pieceimage = this.getImageForPiece(piece);
                        if (pieceimage != null) {
                            if (!mouseDown || selectedSquare == null || !selectedSquare.equals(sq))
                                gadv.paintImage(pieceimage, sqb);
                        } else {
                            g2d.setColor(Color.BLUE);
                            g2d.drawString(Character.toString(piece), sqb.x + sqb.width / 2, sqb.y + sqb.height / 2);
                        }
                    }
                }
			selSquarePossibleMoves.forEach(move -> {
				Rectangle sqrect = getSquareBounds(move.squareTo());
				int s = sqrect.width / 3, x = sqrect.x, y = sqrect.y;
				gadv.fillEllipse(new Color(0, 0, 0, 64), new Ellipse2D.Float(x + s, y + s, s, s));
			});

			if (mouseDown && selectedSquare != null) {
				byte p = this.chessboard.pieceAt(selectedSquare);
				if (p != Chessboard.PIECE_NONE) {
					Image img = this.getImageForPiece(p);
					if (img != null) {
						Rectangle rect = this.getSquareBounds(selectedSquare);
						if (this.getMousePosition() != null) {
							rect.setLocation(this.getMousePosition());
							rect.x = rect.x - rect.width / 2;
							rect.y = rect.y - rect.width / 2;
						}
						gadv.paintImage(img, rect);
					}
				}
			}
		} else {
			// TODO: draw alternative text
		}
	}

	@Override
	public boolean isLightweight() {
		return isDisplayable();
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		this.repaint();
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		Point mousePos = e.getPoint();

		if (this.isEnabled() && this.getChessboardBounds().contains(mousePos)) {
			String sqMouseDown = null;

			for (char f = 'a'; f <= 'h' && sqMouseDown == null; f++)
                for (char r = '1'; r <= '8' && sqMouseDown == null; r++) {
                    String sq = f + "" + r;
                    Rectangle sqrect = this.getSquareBounds(sq);
                    if (sqrect.contains(mousePos))
                        sqMouseDown = sq;
                }

			if (selectedSquare != null && sqMouseDown != null) for (Move move : selSquarePossibleMoves)
                if (move.squareTo().equals(sqMouseDown)) {
                    repaint();
                    return;
                }

			this.selectSquare(sqMouseDown);
			this.mouseDown = true;

			this.repaint();
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		this.mouseDown = false;

		if (this.isEnabled()) {
			Point p = e.getPoint();
			String squareMouseOver = null;
			for (char f = 'a'; f <= 'h' && squareMouseOver == null; f++)
                for (char r = '1'; r <= '8' && squareMouseOver == null; r++) {
                    String sq = f + "" + r;
                    Rectangle sqrect = this.getSquareBounds(sq);
                    if (sqrect.contains(p))
                        squareMouseOver = sq;
                }
			if (squareMouseOver != null) {
				TreeSet<Move> moves = new TreeSet<>();
				for (Move move : selSquarePossibleMoves)
                    if (squareMouseOver.equals(move.squareTo()))
                        moves.add(move);
				if (moves.size() == 1) {
					chessboard.performMove(moves.first());
					selectSquare(null);
				} else if (moves.size() > 1) {
					Move selMove = (Move) JOptionPane.showInputDialog(this, "Select move to perform:",
							"Move " + selectedSquare + "→" + squareMouseOver, JOptionPane.OK_CANCEL_OPTION, null,
							moves.toArray(), moves.first());
					if (selMove != null) {
						chessboard.performMove(selMove);
						selectSquare(null);
					}
				}
			}
			this.repaint();
		}

	}

	@Override
	public void moveDone(final ChessboardEvent evt) {
		this.selectSquare(null);
		this.repaint();
	}

	@Override
	public void moveUndone(final ChessboardEvent evt) {
		this.selectSquare(null);
		this.repaint();
	}

	@Override
	public void moveRedone(final ChessboardEvent evt) {
		this.selectSquare(null);
		this.repaint();
	}
}
