package mdlib.utils.collections;

import java.util.*;

public final class BinaryHeapNode<E, H extends BinaryHeap<E, H>> implements HeapNode<E, BinaryHeapNode<E, H>> {
    private final H parentHeap;
    private final int elementIndex;
    private final int modCount;

    /**
     * Creates a heap node from its parent heap and its index.
     *
     * @param parentHeap node's parent heap
     * @param elementIndex node's element index
     *
     * @throws NullPointerException if {@code null} is given as first argument
     * @throws ArrayIndexOutOfBoundsException if element index is out of range
     */
    BinaryHeapNode(final H parentHeap, final int elementIndex) {
        // Check parameters validity
        Objects.requireNonNull(parentHeap, "Cannot passs null as an argument");
        if (elementIndex < 0 || elementIndex > parentHeap.size())
            throw new ArrayIndexOutOfBoundsException(elementIndex);
        // Assign values
        this.parentHeap = parentHeap;
        this.elementIndex = elementIndex;
        this.modCount = parentHeap.modCount;
    }

    private void checkModCount() {
        if (parentHeap.modCount != modCount)
            throw new ConcurrentModificationException("Heap was modified when observing its structure.");
    }

    @Override
    public BinaryHeapNode<E, H> parentNode() {
        // Check modification count
        checkModCount();
        // If the node is the root node, return null
        return elementIndex == 0 ? null : new BinaryHeapNode<>(parentHeap, BinaryHeap.treeParent(elementIndex));
    }

    @Override
    public List<BinaryHeapNode<E, H>> childNodes() {
        // Check modification count
        checkModCount();
        // Create a list
        ArrayList<BinaryHeapNode<E, H>> nodes = new ArrayList<>(2);
        // Get left child and right child indices
        int lchild = BinaryHeap.treeLeftChild(elementIndex);
        int rchild = BinaryHeap.treeRightChild(elementIndex);
        // Has given node left child?
        if (parentHeap.size() > lchild)
            nodes.add(new BinaryHeapNode<>(parentHeap, lchild));
        // Has given node left child?
        if (parentHeap.size() > rchild)
            nodes.add(new BinaryHeapNode<>(parentHeap, rchild));
        // Return as unmodifiable list
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Unsupported operation! Method will always return {@link UnsupportedOperationException}
     *
     * @param child child to be added
     */
    @Override
    public void addChild(final BinaryHeapNode<E, H> child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E getValue() {
        // Check modification count
        checkModCount();
        // Return the desired element
        return parentHeap.get(elementIndex);
    }

    @Override
    public void setValue(final E value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the parent heap, same as {@link #parentTree()} does.
     *
     * @return the parent heap
     */
    @Override
    public H parentHeap() {
        // Check modification count
        checkModCount();
        // Return parent heap
        return parentHeap;
    }
}
