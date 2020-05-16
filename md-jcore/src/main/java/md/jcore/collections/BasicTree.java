package md.jcore.collections;

import java.io.Serializable;

/**
 * Represents a special tree. It has some benefits:
 * 
 * <ul>
 * <li>You can add as many children to each node as possible.</li>
 * <li>You have direct access to every node in the tree.</li>
 * <li>There can be two or more children with same parent and the same value.</li>
 * <li>You can set the value of a node to {@code null}</li>
 * </ul>
 *
 * @param <E> elements to be saved in nodes.
 * 
 * @author Michal Douša
 * 
 * @see BasicTreeNode
 * @see #getRootNode()
 */
public final class BasicTree<E> implements Serializable {
	private static final long serialVersionUID = 0x100L;

	private BasicTreeNode<E> rootNode;

	/**
	 * Default constractor that sets the root node to a new node containing
	 * {@code null} and no children inside.
	 */
	public BasicTree() {
		this(new BasicTreeNode<E>());
	}

	public BasicTree(BasicTreeNode<E> rootNode) {
		this.setRootNode(rootNode);
	}

	/**
	 * Gets the root node of this tree
	 * 
	 * @return the root node
	 */
	public BasicTreeNode<E> getRootNode() {
		return rootNode;
	}

	/**
	 * Sets the root node of this tree. The root node cannot be set to {@code null}.
	 * 
	 * @param rootNode the root node to be set
	 */
	public void setRootNode(BasicTreeNode<E> rootNode) {
		if (rootNode == null)
			throw new NullPointerException("Root node cannot be set to null");
		this.rootNode = rootNode;
	}
}
