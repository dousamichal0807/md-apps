package mdlib.utils.collections;

import java.util.ListIterator;

public interface IndexedHeapIterator<E> extends HeapIterator<E>, ListIterator<E> {

    int parentIndex();

    int firstChildIndex();

    int lastChildIndex();
}
