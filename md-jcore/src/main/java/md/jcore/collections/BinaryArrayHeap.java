package md.jcore.collections;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Queue;

/**
 * This collection represents a heap. Methods that are not allowed to use in
 * this class (like removing, except peek element, of course) will always return
 * {@code false} and they have no effect calling them.
 * 
 * @author Michal Douša
 *
 * @param <E> type of elements to be added
 * 
 * @see #size()
 * @see #capacity()
 * @see #getLoadFactor()
 * @see #getComparator()
 */
public final class BinaryArrayHeap<E> implements Serializable, Queue<E>, Comparator<E> {
	private static final long serialVersionUID = 0x100L;
	
	private E[] tree;
	private int size, modCount;
	private final int initialCapacity;
	private final float loadFactor;
	private final Comparator<? super E> comparator;

	/**
	 * The default constructor for the heap.
	 */
	public BinaryArrayHeap() {
		this(10, 1.6f, null);
	}

	/**
	 * By this constructor you can set initial capacity and load factor. You can
	 * find better explanation of the parameters in the appropriate getters.
	 * 
	 * @param initialCapacity how many elements will heap allocate space for
	 * @param loadFactor      the load factor to be set
	 * @param comparator      explicit comparator to be used, if you want to use
	 *                        natural ordering, just pass {@code null} value
	 * 
	 * @see #capacity()
	 * @see #getLoadFactor()
	 * @see #getComparator()
	 */
	public BinaryArrayHeap(final int initialCapacity, final float loadFactor, final Comparator<? super E> comparator) {
		if (loadFactor < 1f)
			throw new IllegalArgumentException("Load factor must be greater or equal to 1");

		this.modCount = 0;
		this.initialCapacity = initialCapacity;
		this.loadFactor = loadFactor;
		this.comparator = comparator;

		this.clear();
	}

	// General properties -----------------------------------------------------

	/**
	 * The load factor of this heap works on the same principle as the load factor
	 * of the standard Java collections in {@code java.util} package. The value must
	 * be greater or equal to 1. If the value is exactly 1, it means that when the
	 * heap is full and you want to add some element, the heap's capacity will
	 * increase by 1 element exactly and after adding the element, the heap is full
	 * again. Once the load factor was set in the constructor it cannot be changed
	 * to other value
	 * 
	 * @return the load factor of this heap
	 */
	public float getLoadFactor() {
		return loadFactor;
	}

	/**
	 * Returns the explicit comparator for comparing elements in the heap. If no
	 * comparator was set (or was set to {@code null}), this method returns
	 * {@code null}. Once a comparator is set in the constructor it cannot be
	 * changed to other value.
	 * 
	 * @return the comparator used to compare elements
	 */
	public Comparator<? super E> getComparator() {
		return comparator;
	}

	/**
	 * @return Capacity (how many elements can be stored in) of the heap
	 */
	public int capacity() {
		return tree.length;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Returns if the heap is empty
	 * 
	 * @return if size of the heap is equal to 0
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the heap represented as {@code String}, similarly as casual Java
	 * collection in {@code java.util} package
	 * 
	 * @return The heap represented as {@code String}.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append(tree[i]);
			if (i != size - 1)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(final E o1, final E o2) {
		return comparator == null ? ((Comparable<E>) o1).compareTo(o2) : comparator.compare(o1, o2);
	}

	// Methods manipulating with elements -------------------------------------

	@Override
	public synchronized boolean offer(final E element) {
		if (element == null)
			// throw new NullPointerException("Cannot add null");
			return false;
		// Add a new ply if the heap is full
		if (capacity() == size())
			expand();
		// Add new element as the last, save needed indices of used elements in
		// the tree and increase the size and modification count...
		tree[size] = element;
		int addedElemIdx = size;
		int parentIdx = treeParent(addedElemIdx);
		size++;
		modCount++;
		// ...And re-arrange elements that the heap is really heap!
		while (compare(tree[parentIdx], tree[addedElemIdx]) > 0) {
			// While parent is bigger...
			swap(parentIdx, addedElemIdx);
			addedElemIdx = parentIdx;
			parentIdx = treeParent(addedElemIdx);
		}
		return true;
	}

	@Override
	public synchronized boolean add(final E e) {
		if(e == null)
			throw new NullPointerException("Null value cannot be added");
		return offer(e);
	}

	@Override
	public synchronized boolean addAll(final Collection<? extends E> c) {
		Iterator<? extends E> iterator = c.iterator();
		boolean added = true;

		while (iterator.hasNext()) {
			E next = iterator.next();
			added &= add(next);
		}
		return added;
	}

	/**
	 * Gets an element with minimum value. When the heap is empty, method throws an
	 * exception.
	 * 
	 * @return element with minimum value
	 * 
	 * @throws IllegalStateException when there is no element left
	 * 
	 * @see #peek()
	 * @see #poll()
	 * @see #remove()
	 */
	@Override
	public E element() {
		if (isEmpty())
			throw new IllegalStateException("No elements left");
		return tree[0];
	}

	/**
	 * Gets an element with minimum value. When the heap is empty, method returns
	 * {@code null}
	 * 
	 * @return element with minimum value
	 * 
	 * @throws IllegalStateException when there is no element left
	 * 
	 * @see #element()
	 * @see #poll()
	 * @see #remove()
	 */
	@Override
	public E peek() {
		if (isEmpty())
			return null;
		return tree[0];
	}

	/**
	 * Removes and returns element with minimum value (element on the top). When the
	 * heap is empty, method returns {@code null}.
	 * 
	 * @return the element on the top of the heap
	 * 
	 * @throws IllegalStateException when there is no element left
	 * 
	 * @see #peek()
	 * @see #element()
	 * @see #remove()
	 */
	@Override
	public synchronized E poll() {
		if(isEmpty())
			return null;
		// Get the element so we can return it
		E element = element();

		// Replace the element with the last one and decrease count of elements
		// and store the indices of the previously last one and its current children
		tree[0] = tree[size - 1];
		tree[size - 1] = null;
		size--;
		int nodeIdx = 0, lchildIdx = 1, rchildIdx = 2;
		E node = tree[0];
		boolean done = false;
		// While we aren't done...
		while (!done) {
			E lchild = null, rchild = null;
			// Get left and right child if they exist, otherwise leave variables
			// set to null
			if (lchildIdx < size)
				lchild = tree[lchildIdx];
			if (rchildIdx < size)
				rchild = tree[rchildIdx];

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
	 * Removes and returns element with minimum value (element on the top). When the
	 * heap is empty, method throws an exception.
	 * 
	 * @return the element on the top of the heap
	 * 
	 * @throws IllegalStateException when there is no element left
	 * 
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
	 * Not allowed oparation! Calling this method has no effect and will always
	 * return {@code false}
	 * 
	 * @see #poll()
	 * @see #remove()
	 */
	@Override
	public boolean remove(final Object o) {
		return false;
	}

	/**
	 * Not allowed oparation! Calling this method has no effect and will always
	 * return {@code false}
	 * 
	 * @see #poll()
	 * @see #remove()
	 * @see #clear()
	 */
	@Override
	public boolean removeAll(final Collection<?> c) {
		return false;
	}

	/**
	 * Not allowed oparation! Calling this method has no effect and will always
	 * return {@code false}
	 * 
	 * @see #poll()
	 * @see #remove()
	 * @see #clear()
	 */
	@Override
	public boolean retainAll(final Collection<?> c) {
		return false;
	}

	/**
	 * Empties the whole heap.
	 * 
	 * @see #poll()
	 */
	@Override
	public synchronized void clear() {
		tree = (E[]) new Object[initialCapacity];
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
			if (tree[i].equals(element))
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
	public Iterator<E> iterator() {
		return new HeapIterator<>(this);
	}

	@Override
	public Object[] toArray() {
		return Arrays.stream(tree, 0, size).toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		System.arraycopy(tree, 0, a, 0, Math.min(size, a.length));
		return a;
	}

	// Private methods --------------------------------------------------------

	private void expand() {
		E[] tree2 = (E[]) new Object[(int) (tree.length * loadFactor + 1)];
		System.arraycopy(tree, 0, tree2, 0, tree.length);
		tree = tree2;
	}

	private void swap(final int i1, final int i2) {
		E t = tree[i1];
		tree[i1] = tree[i2];
		tree[i2] = t;
	}

	private static int treeLeftChild(final int parent) {
		return 2 * parent + 1;
	}

	private static int treeRightChild(final int parent) {
		return 2 * parent + 2;
	}

	private static int treeParent(final int child) {
		return (child - 1) / 2;
	}

	// Private classes --------------------------------------------------------

	private static final class HeapIterator<F> implements Iterator<F> {
		private final int modCount;
		private int nextElemIndex;
		private final BinaryArrayHeap<? extends F> heap;

		HeapIterator(final BinaryArrayHeap<? extends F> heap) {
			this.heap = heap;
			this.modCount = heap.modCount;
			this.nextElemIndex = 0;
		}

		@Override
		public boolean hasNext() {
			return nextElemIndex < heap.size();
		}

		@Override
		public F next() {
			if (heap.modCount != modCount)
				throw new ConcurrentModificationException("Heap was changed outside of this iterator");
			if (!hasNext())
				throw new IllegalStateException("Heap has no more elements");

			F element = heap.tree[nextElemIndex];
			nextElemIndex++;
			return element;
		}

	}
}
