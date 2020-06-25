package mdlib.utils.collections;

public interface HeapIterator<E> extends TreeIterator<E> {

    /**
     * Returns the heap which is the {@link HeapIterator} iteration over, same as {@link #heap()} method.
     *
     * @return the heap which the {@link HeapIterator} comes from
     */
    @Override
    default Heap<E, ?> tree() {
        return heap();
    }

    /**
     * Returns the heap which is the {@link HeapIterator} iteration over, same as {@link #tree()} method.
     *
     * @return the heap which the {@link HeapIterator} comes from
     */
    Heap<E, ?> heap();
}
