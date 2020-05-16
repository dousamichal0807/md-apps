package md.jcore.collections;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents node of the {@link BasicTree SpecialTree} class. You can find more
 * about advantages and disadvantages of these nodes in the
 * {@link BasicTree SpecialTree} class Javadoc
 * 
 * @author Michal Douša
 *
 * @param <E> type of values to be stored in this node and its children
 * 
 * @see #getChildren()
 * @see #getValue()
 * @see #setValue(Object)
 */
public final class BasicTreeNode<E> implements Serializable {
	private static final long serialVersionUID = 0x100L;
	
	private final ArrayList<BasicTreeNode<E>> children;
	private E value;

	/**
	 * Gets all the children organised in a {@link java.util.HashSet HashSet}.
	 * 
	 * @return the {@link java.util.HashSet HashSet} where are all children stored
	 */
	public ArrayList<BasicTreeNode<E>> getChildren() {
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
		this.children = new ArrayList<BasicTreeNode<E>>();
	}
	
	/**
	 * Constructor with default initial value set to {@code null}.
	 */
	public BasicTreeNode() {
		this(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o2) {
		if (!(o2 instanceof BasicTreeNode))
			return false;
		BasicTreeNode<E> n2 = (BasicTreeNode<E>) o2;
		return this.getValue().equals(n2.getValue());
	}
}
