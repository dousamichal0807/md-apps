package mdlib.utils.collections;

import java.util.Collection;

public interface HeapNode<E, N extends HeapNode<E, N>> extends TreeNode<E, N> {

    @Override
    default Heap<E, N> parentTree() {
        return parentHeap();
    }

    /**
     * Gets the heap where the node is located, same as {@link #parentTree()}.
     *
     * @return the heap where the node is located
     */
    Heap<E, N> parentHeap();
}
