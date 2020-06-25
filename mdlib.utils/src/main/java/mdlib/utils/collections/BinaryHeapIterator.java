package mdlib.utils.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * An iterator for a binary heap. You can obtain this iterator by calling {@link BinaryHeap#iterator()}, {@link
 * BinaryHeap#listIterator()}, {@link BinaryHeap#listIterator(int)}, {@link BinaryHeap#heapIterator()} or {@link
 * BinaryHeap#heapIterator(int)}
 *
 * @param <E> type of elements of the binary heap
 */
public final class BinaryHeapIterator<E> implements IndexedHeapIterator<E> {

    private final BinaryHeap<E, ?> heap;
    private final int modCount;
    private int currentElementIndex;

    BinaryHeapIterator(final BinaryHeap<E, ?> heap, final int startIndex) {
        this.heap = heap;
        this.modCount = heap.modCount;
        this.currentElementIndex = startIndex - 1;
    }

    private void checkModCount() {
        if (heap.modCount != modCount)
            throw new ConcurrentModificationException("The heap was modified");
    }

    @Override
    public void remove() {
    }

    @Override
    public void set(final E e) {
    }

    @Override
    public void add(final E e) {
    }

    @Override
    public Heap<E, ?> heap() {
        return heap;
    }

    // Parent element -----------------------------------------------------

    @Override
    public boolean hasParent() {
        return currentElementIndex > 0;
    }

    @Override
    public int parentIndex() {
        return hasParent() ? BinaryHeap.treeParent(currentElementIndex) : -1;
    }

    @Override
    public E parent() {
        return null;
    }

    // Next element -------------------------------------------------------

    @Override
    public boolean hasNext() {
        return currentElementIndex < heap.size() - 1;
    }

    @Override
    public int nextIndex() {
        return hasNext() ? (currentElementIndex + 1) : -1;
    }

    @Override
    public E next() {
        return null;
    }

    // Previous element ---------------------------------------------------

    @Override
    public boolean hasPrevious() {
        return currentElementIndex > 0;
    }

    @Override
    public int previousIndex() {
        return hasPrevious() ? (currentElementIndex - 1) : -1;
    }

    @Override
    public E previous() {
        return null;
    }

    // Children -----------------------------------------------------------

    @Override
    public boolean hasChildren() {
        return hasLeftChild();
    }

    @Override
    public int firstChildIndex() {
        return Math.min(leftChildIndex(), rightChildIndex());
    }

    @Override
    public E firstChild() {
        return element(firstChildIndex());
    }

    @Override
    public int lastChildIndex() {
        return Math.max(leftChildIndex(), rightChildIndex());
    }

    @Override
    public E lastChild() {
        return element(lastChildIndex());
    }

    // Left child ---------------------------------------------------------

    public boolean hasLeftChild() {
        return heap.size() > BinaryHeap.treeLeftChild(currentElementIndex);
    }

    public int leftChildIndex() {
        return hasChildren() ? BinaryHeap.treeLeftChild(currentElementIndex) : -1;
    }

    public E leftChild() {
        return element(leftChildIndex());
    }

    // Right child --------------------------------------------------------

    public boolean hasRightChild() {
        return heap.size() > BinaryHeap.treeRightChild(currentElementIndex);
    }

    public int rightChildIndex() {
        return hasRightChild() ? BinaryHeap.treeRightChild(currentElementIndex) : -1;
    }

    public E rightChild() {
        return element(rightChildIndex());
    }

    // element() method for random access ---------------------------------

    public E element(final int index) {
        // The index can be out of range
        if (index < 0 || index >= heap.size())
            throw new NoSuchElementException();

        // Set current element index
        currentElementIndex = index;

        // Return current element
        return heap.get(currentElementIndex);
    }

}
