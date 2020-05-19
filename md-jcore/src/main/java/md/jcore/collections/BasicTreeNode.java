package md.jcore.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents node of the {@link BasicTree SpecialTree} class. You can find more
 * about advantages and disadvantages of these nodes in the
 * {@link BasicTree SpecialTree} class Javadoc
 * 
 * @author Michal Douša
 *
 * @param <E> type of values to be stored in this node and its children
 * 
 * @see #childNodes()
 * @see #getValue()
 * @see #setValue(Object)
 */
public final class BasicTreeNode<E> implements MDTreeNode {
	private static final long serialVersionUID = 0x100L;
	
	private final ArrayList<BasicTreeNode<E>> children;
	private E value;

	@Override
	public ArrayList<BasicTreeNode<E>> childNodes() {
		return children;
	}

	/**
	 * Gets the value that has the node.
	 * 
	 * @return the value stored in the node
	 */
	public E getValue() {
		return value;
	}

	/**
	 * Sets the value that has the node.
	 * 
	 * @param value  the value that will be stored in the node
	 */
	public void setValue(E value) {
		this.value = value;
	}

	/**
	 * Constructor wher you can specify the initial value.
	 * 
	 * @param value  the initial value
	 */
	public BasicTreeNode(E value) {
		this.setValue(value);
		this.children = new ArrayList<>();
	}
	
	/**
	 * Constructor with default initial value set to {@code null}.
	 */
	public BasicTreeNode() {
		this(null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object o2) {
		try {
			BasicTreeNode<E> n2 = (BasicTreeNode<E>) o2;
			return this.getValue().equals(n2.getValue());
		} catch (ClassCastException exc) {
			return false;
		}
	}

	public static final class Unmodifiable<E> implements MDTreeNode {
		private final BasicTreeNode<E> node;

		Unmodifiable(final BasicTreeNode<E> node) {
			if (node == null)
				throw new NullPointerException();
			this.node = node;
		}

		@Override
		public List<BasicTreeNode.Unmodifiable<E>> childNodes() {
			// Create list
			ArrayList<BasicTreeNode.Unmodifiable<E>> nodes = new ArrayList<>();
			// Add immutable instances of children to the list
			for(BasicTreeNode<E> childNode : node.childNodes())
				nodes.add(new Unmodifiable<>(childNode));
			// Return list as immutable list
			return Collections.unmodifiableList(nodes);
		}
	}
}
