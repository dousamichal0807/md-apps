package mdlib.utils.collections;

import java.util.Comparator;

/**
 * This abstract class represents a binary heap. For their implementation see {@link BinaryArrayHeap} and
 * {@link BinaryLinkedHeap}
 *
 * @param <E> elements of the heap
 *
 * @see BinaryArrayHeap
 * @see BinaryLinkedHeap
 *
 * @author Michal Douša
 */
public abstract class BinaryHeap<E, H extends BinaryHeap<E, H>> extends IndexedHeap<E, BinaryHeapNode<E, H>> {

    protected int modCount;

    public BinaryHeap(final Comparator<? super E> comparator) {
        super(comparator);
    }

    /**
     * Returns a {@link BinaryHeapIterator} which you can iterate over with. Same as calling {@link #heapIterator()}.
     *
     * @return same result as {@link #heapIterator()}
     *
     * @see #heapIterator()
     * @see #heapIterator(int)
     */
    @Override
    public final BinaryHeapIterator<E> iterator() {
        return heapIterator();
    }

    /**
     * Returns a {@link BinaryHeapIterator} which you can iterate over with. Same as calling {@link #heapIterator()}.
     *
     * @return same result as {@link #heapIterator()}
     *
     * @see #heapIterator()
     * @see #heapIterator(int)
     */
    @Override
    public final BinaryHeapIterator<E> listIterator() {
        return heapIterator();
    }

    /**
     * Returns a {@link BinaryHeapIterator} with given starting position. Same as calling {@link #heapIterator(int)}
     *
     * @param startIndex index to start iterating from
     * @return same result as {@link #heapIterator(int)}
     *
     * @see #heapIterator()
     */
    @Override
    public final BinaryHeapIterator<E> listIterator(final int startIndex) {
        return heapIterator(startIndex);
    }

    /**
     * Returns a {@link BinaryHeapIterator} which you can iterate over the heap with. Always start by calling
     * {@link BinaryHeapIterator#next()} or method to get the first element, e.g. the minimum element of the heap, or
     * {@link BinaryHeapIterator#element(int)} if you want to jump to other element of the heap.
     *
     * @return an iterator over the heap as described above
     */
    public final BinaryHeapIterator<E> heapIterator() {
        return heapIterator(0);
    }

    public final BinaryHeapIterator<E> heapIterator(final int startIndex) {
        return new BinaryHeapIterator<>(this, startIndex);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final BinaryHeapNode<E, H> getRootNode() {
        return new BinaryHeapNode<>((H) this, 0);
    }

    // +----------------------------------------------------------------------+
    // | Static members                                                       |
    // +----------------------------------------------------------------------+

    public static int treeLeftChild(final int parent) {
        return 2 * parent + 1;
    }

    public static int treeRightChild(final int parent) {
        return 2 * parent + 2;
    }

    public static int treeParent(final int child) {
        return (child - 1) / 2;
    }
}
