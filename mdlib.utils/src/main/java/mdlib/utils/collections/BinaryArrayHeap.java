package mdlib.utils.collections;

import java.util.*;

/**
 * This collection represents a heap. Methods that are not allowed to use in this class (like removing, except peek
 * element, of course) will always return {@code false} and they have no effect calling them.
 *
 * @param <E> type of elements to be added
 * @author Michal Douša
 * @see #size()
 * @see #capacity()
 * @see #getLoadFactor()
 * @see #getComparator()
 */
public final class BinaryArrayHeap<E> extends BinaryHeap<E, BinaryArrayHeap<E>> implements RandomAccess {

    private static final long serialVersionUID = 0x100L;

    private E[] elements;
    private int size;
    private final int initialCapacity;
    private final float loadFactor;

    /**
     * The default constructor for the heap.
     */
    public BinaryArrayHeap() {
        this(10, 1.6f, null);
    }

    /**
     * By this constructor you can set initial capacity and load factor. You can find better explanation of the
     * parameters in the appropriate getters.
     *
     * @param initialCapacity how many elements will heap allocate space for
     * @param loadFactor      the load factor to be set
     * @param comparator      explicit comparator to be used, if you want to use natural ordering, just pass {@code
     *                        null} value
     * @see #capacity()
     * @see #getLoadFactor()
     * @see #getComparator()
     */
    public BinaryArrayHeap(final int initialCapacity, final float loadFactor, final Comparator<? super E> comparator) {
        super(comparator);

        if (loadFactor < 1f)
            throw new IllegalArgumentException("Load factor must be greater or equal to 1");

        this.modCount = 0;
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;

        this.clear();
    }

    // General properties -----------------------------------------------------

    /**
     * The load factor of this heap works on the same principle as the load factor of the standard Java collections in
     * {@code java.util} package. The value must be greater or equal to 1. If the value is exactly 1, it means that when
     * the heap is full and you want to add some element, the heap's capacity will increase by 1 element exactly and
     * after adding the element, the heap is full again. Once the load factor was set in the constructor it cannot be
     * changed to other value
     *
     * @return the load factor of this heap
     */
    public float getLoadFactor() {
        return loadFactor;
    }

    /**
     * @return Capacity (how many elements can be stored in) of the heap
     */
    public int capacity() {
        return elements.length;
    }

    @Override
    public int size() {
        return size;
    }


    // Methods manipulating with elements -------------------------------------

    @Override
    public synchronized boolean offer(final E element) {
        // Cannot add null
        if (element == null)
            return false;
        // Expand the heap if the heap is full
        if (capacity() == size())
            expand();
        // Add new element as the last, save needed indices of used elements in
        // the tree and increase the size and modification count...
        elements[size] = element;
        int addedElemIdx = size;
        int parentIdx = treeParent(addedElemIdx);
        size++;
        modCount++;
        // ...And re-arrange elements that the heap is really heap!
        while (compare(elements[parentIdx], elements[addedElemIdx]) > 0) {
            // While parent is bigger...
            swap(parentIdx, addedElemIdx);
            addedElemIdx = parentIdx;
            parentIdx = treeParent(addedElemIdx);
        }
        return true;
    }

    @Override
    public synchronized boolean add(final E e) {
        if (e == null)
            throw new NullPointerException("Null value cannot be added");
        return offer(e);
    }

    @Override
    public synchronized boolean addAll(final Collection<? extends E> c) {
        // Was it completely successful?
        boolean ok = true;
        // Try to add everything
        for (E element : c)
            ok &= offer(element);
        // Return if it was completely successful
        return ok;
    }

    /**
     * Gets an element with minimum value. When the heap is empty, method throws an exception.
     *
     * @return element with minimum value
     *
     * @throws IllegalStateException when there is no element left
     * @see #peek()
     * @see #poll()
     * @see #remove()
     */
    @Override
    public E element() {
        if (isEmpty())
            throw new IllegalStateException("No elements left");
        return elements[0];
    }

    /**
     * Gets an element with minimum value. When the heap is empty, method returns {@code null}
     *
     * @return element with minimum value or {@code null}, if heap is empty
     *
     * @see #element()
     * @see #poll()
     * @see #remove()
     */
    @Override
    public E peek() {
        if (isEmpty())
            return null;
        return elements[0];
    }

    @Override
    public E get(final int index) {
        // The index may be out of bounds...
        if (index < 0 && index >= size)
            throw new ArrayIndexOutOfBoundsException(index);

        // ...If it is not, return desired element
        return elements[index];
    }

    /**
     * Removes and returns element with minimum value (element on the top). When the heap is empty, method returns
     * {@code null}.
     *
     * @return the element on the top of the heap
     *
     * @throws IllegalStateException when there is no element left
     * @see #peek()
     * @see #element()
     * @see #remove()
     */
    @Override
    public synchronized E poll() {
        if (isEmpty())
            return null;
        // Get the element so we can return it
        E element = element();

        // Replace the element with the last one and decrease count of elements
        // and store the indices of the previously last one and its current children
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;
        int nodeIdx = 0, lchildIdx = 1, rchildIdx = 2;
        E node = elements[0];
        boolean done = false;
        // While we aren't done...
        while (!done) {
            E lchild = null, rchild = null;
            // Get left and right child if they exist, otherwise leave variables
            // set to null
            if (lchildIdx < size)
                lchild = elements[lchildIdx];
            if (rchildIdx < size)
                rchild = elements[rchildIdx];

            if ((lchild == null || compare(node, lchild) <= 0) && (rchild == null || compare(node, rchild) <= 0)) {
                // Both children are bigger or equal or they don't exist, we're done
                done = true;
            } else {
                int swapIndex = lchildIdx;
                // Choose the smaller child. If right child exists and left child is
                // bigger than right one, change swapIndex to the right child index
                if (rchild != null && compare(lchild, rchild) > 0)
                    swapIndex = rchildIdx;
                // And swap!
                swap(nodeIdx, swapIndex);
                // Update nodeIdx, lchildIdx, rchildIdx
                nodeIdx = swapIndex;
                lchildIdx = treeLeftChild(nodeIdx);
                rchildIdx = treeRightChild(nodeIdx);
            }
        }

        // Return the removed element
        return element;
    }

    /**
     * Removes and returns element with minimum value (element on the top). When the heap is empty, method throws an
     * exception.
     *
     * @return the element on the top of the heap
     *
     * @throws IllegalStateException when there is no element left
     * @see #peek()
     * @see #element()
     * @see #poll()
     */
    @Override
    public synchronized E remove() {
        E e = poll();
        if (e == null)
            throw new IllegalStateException("No elements left");
        return e;
    }

    /**
     * Empties the whole heap.
     *
     * @see #poll()
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void clear() {
        elements = (E[]) new Object[initialCapacity];
        size = 0;
        modCount++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(final Object o) {
        E element;

        // Cast to generic type 'E'
        try {
            element = (E) o;
        } catch (ClassCastException exc) {
            return false;
        }

        // If element found return true
        for (int i = 0; i < size; i++)
            if (elements[i].equals(element))
                return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(final Collection<?> c) {

        for (Object o : c)
            try {
                E element = (E) o;
                if (!contains(element))
                    return false;
            } catch (ClassCastException exc) {
                return false;
            }
        return true;
    }

    @Override
    public Object[] toArray() {
        return Arrays.stream(elements, 0, size).toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(final T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @SuppressWarnings("unchecked")
    private void expand() {
        E[] tree2 = (E[]) new Object[(int) (elements.length * loadFactor + 1)];
        System.arraycopy(elements, 0, tree2, 0, elements.length);
        elements = tree2;
    }

    private void swap(final int i1, final int i2) {
        E t = elements[i1];
        elements[i1] = elements[i2];
        elements[i2] = t;
    }
}
