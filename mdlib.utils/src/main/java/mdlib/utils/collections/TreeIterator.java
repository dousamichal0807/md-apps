package mdlib.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface TreeIterator<E> extends Iterator<E> {

    /**
     * Returns the tree which is the {@link TreeIterator} iteration over.
     *
     * @return the tree which the {@link TreeIterator} comes from
     */
    Tree<E, ?> tree();

    // Parent -----------------------------------------------------------------

    /**
     * Works the same way as {@link #hasNext()} or {@link #hasPrevious()} method, but this time is examined if the
     * previously returned element has a parent.
     *
     * @return if the previously returned element has a parent
     */
    boolean hasParent();

    /**
     * Works the same way as {@link #next()} or {@link #previous()} method, but this time the "next" element is the
     * parent of the previously returned element.
     *
     * @return the parent value
     */
    E parent();

    // Neighbours -------------------------------------------------------------

    /**
     * Returns {@code true} if this list iterator has more elements when traversing the list in the forward direction.
     * (In other words, returns {@code true} if {@link #next} would return an element rather than throwing an
     * exception.)
     *
     * @return {@code true} if the list iterator has more elements when traversing the list in the forward direction
     */
    boolean hasNext();

    /**
     * Returns the next element in the list and advances the cursor position. This method may be called repeatedly to
     * iterate through the list, or intermixed with calls to {@link #previous} to go back and forth. (Note that
     * alternating calls to {@code next} and {@code previous} will return the same element repeatedly.)
     *
     * @return the next element in the list
     *
     * @throws NoSuchElementException if the iteration has no next element
     */
    E next();

    /**
     * Returns {@code true} if this list iterator has more elements when traversing the list in the reverse direction.
     * (In other words, returns {@code true} if {@link #previous} would return an element rather than throwing an
     * exception.)
     *
     * @return {@code true} if the list iterator has more elements when traversing the list in the reverse direction
     */
    boolean hasPrevious();

    /**
     * Returns the previous element in the list and moves the cursor position backwards.  This method may be called
     * repeatedly to iterate through the list backwards, or intermixed with calls to {@link #next} to go back and forth.
     * (Note that alternating calls to {@code next} and {@code previous} will return the same element repeatedly.)
     *
     * @return the previous element in the list
     *
     * @throws NoSuchElementException if the iteration has no previous element
     */
    E previous();

    // Children ---------------------------------------------------------------

    /**
     * Works the same way as {@link #hasNext()} or {@link #hasPrevious()} method, but this time is examined if the
     * previously returned element has at least one child.
     *
     * @return if the previously returned element has a parent
     */
    boolean hasChildren();

    /**
     * Works the same way as {@link #next()} or {@link #previous()} method, but this time the "next" element is the
     * first child of the previously returned element.
     *
     * @return the parent value
     *
     * @throws NoSuchElementException if {@link #hasChildren()} returns {@code false}
     */
    E firstChild();

    /**
     * Works the same way as {@link #next()} or {@link #previous()} method, but this time the "next" element is the last
     * child of the previously returned element.
     *
     * @return the parent value
     *
     * @throws NoSuchElementException if {@link #hasChildren()} returns {@code false}
     */
    E lastChild();
}
