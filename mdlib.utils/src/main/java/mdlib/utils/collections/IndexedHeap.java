package mdlib.utils.collections;

import java.util.*;

public abstract class IndexedHeap<E, N extends HeapNode<E, N>> extends Heap<E, N> implements List<E> {

    /**
     * Construct a binary heap using {@link Comparator} to compare two objects. If natural ordering is itended to be
     * used, pass {@code null} as paramater
     *
     * @param comparator the {@link Comparator} instance or {@code null}, as described above
     */
    public IndexedHeap(final Comparator<? super E> comparator) {
        super(comparator);
    }

    /**
     * Not allowed operiation! Calling this method has no effect and it will return always {@code null}.
     *
     * @return {@code null}
     */
    @Override
    public final synchronized E set(final int index, final E element) {
        return null;
    }

    /**
     * Not allowed operiation! Calling this method has no effect and it will return always {@code null}.
     *
     * @return {@code null}
     */
    @Override
    public E remove(final int index) {
        return null;
    }

    /**
     * Not allowed operation! Always returns an empty {@link List}.
     * @param fromIndex does not matter what is passed at all
     * @param toIndex does not matter what is passed at all
     * @return an empty {@link List}
     */
    @Override
    public final List<E> subList(final int fromIndex, final int toIndex) {
        return Collections.emptyList();
    }

    /**
     * Same as {@link #add(Object)}, parameter {@code index} is ignored.
     *
     * @param element passing this argument to {@link #add(Object)} has the same effect
     * @param index   parameter is ignored
     */
    @Override
    public final synchronized void add(final int index, final E element) {
        add(element);
    }

    /**
     * Same as {@link #addAll(Collection)}, parameter {@code index} is ignored.
     *
     * @param index    parameter is ignored
     * @param elements passing this argument to {@link #addAll(Collection)} has the same effect
     * @return the same {@code boolean} value as {@link #addAll(Collection)} method
     */
    @Override
    public final synchronized boolean addAll(final int index, final Collection<? extends E> elements) {
        return addAll(elements);
    }

    @Override
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final int indexOf(final Object obj) {
        // Go though the entire heap and check if it contains that element; if it contains, return the desired index
        for (int index = 0; index < size(); index++) {
            if (get(index).equals(obj))
                return index;
        }

        // If not, return -1, e.g. not found
        return -1;
    }

    @Override
    public final int lastIndexOf(final Object obj) {
        // Go though the entire heap and check if it contains that element; if it contains, return the desired index
        for (int index = size() - 1; index >= 0; index--) {
            if (get(index).equals(obj))
                return index;
        }

        // If not, return -1, e.g. not found
        return -1;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (IndexedHeapIterator<E> iterator = iterator(); iterator.hasNext();) {
            sb.append(iterator.next());
            if (iterator.hasNext())
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public abstract IndexedHeapIterator<E> iterator();
}
