package mdlib.utils.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Queue;

public abstract class Heap<E, N extends HeapNode<E, N>> implements Serializable, Queue<E>, Tree<E, N>, Comparator<E> {

    private final Comparator<? super E> comparator;

    /**
     * Construct a heap using {@link Comparator} to compare two objects. If natural ordering is itended to be
     * used, pass {@code null} as paramater
     *
     * @param comparator the {@link Comparator} instance or {@code null}, as described above
     */
    public Heap(final Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns the {@link Comparator} used to comapare objects when adding to this collection. {@code null} is returned
     * if natural ordering is used instead.
     *
     * @return the {@link Comparator} instance assigned to this heap, as descriped above
     */
    public final Comparator<? super E> getComparator() {
        return comparator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final int compare(final E o1, final E o2) {
        return comparator == null ? ((Comparable<E>) o1).compareTo(o2) : comparator.compare(o1, o2);
    }

    /**
     * Not allowed oparation! Calling this method has no effect and will always return {@code false}
     *
     * @see #poll()
     * @see #remove()
     */
    @Override
    public final boolean remove(final Object o) {
        return false;
    }

    /**
     * Not allowed oparation! Calling this method has no effect and will always return {@code false}
     *
     * @see #poll()
     * @see #remove()
     * @see #clear()
     */
    @Override
    public final boolean removeAll(final Collection<?> c) {
        return false;
    }

    /**
     * Not allowed oparation! Calling this method has no effect and will always return {@code false}
     *
     * @see #poll()
     * @see #remove()
     * @see #clear()
     */
    @Override
    public final boolean retainAll(final Collection<?> c) {
        return false;
    }

    @Override
    public abstract HeapIterator<E> iterator();
}
