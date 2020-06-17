package md.jgames.jchess.components;

import mdlib.utils.FilteredAtomicReference;

import java.awt.image.BufferedImage;
import java.util.Objects;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public final class ChessboardViewConfiguration {

    // Initial empty chessboard image
    private static final Image INITTIAL_CHESSBOARD_IMAGE = SwingFXUtils.toFXImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), null);
    // Chessboard image filtered field
    private static final FilteredAtomicReference<Image> chessboardImage = new FilteredAtomicReference<>(Objects::nonNull, INITTIAL_CHESSBOARD_IMAGE);

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
}
