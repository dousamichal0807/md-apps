package md.jcore.collections;

import java.io.Serializable;

/**
 * Represents a basic tree with no limitation. It has some benefits:
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
 * @see #rootNode()
 */
public final class BasicTree<E> implements MDTree {
	private static final long serialVersionUID = 0x100L;

	private BasicTreeNode<E> rootNode;

	/**
	 * Default constractor that sets the root node to a new node containing
	 * {@code null} and no children inside.
	 */
	public BasicTree() {
		this(null);
	}

	/**
	 * Creates {@link BasicTree} instance with specified value stored in the root
	 * node of this tree.
	 *
	 * @param rootNodeValue value of the root node
	 */
	public BasicTree(E rootNodeValue) {
		this.setRootNode(new BasicTreeNode<>(rootNodeValue));
	}

	@Override
	public BasicTreeNode<E> rootNode() {
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

	/**
	 * Immutable version of {@link BasicTree}. It holds reference to regular
	 * editable {@link BasicTree}.
	 *
	 * @param <E> elements stored in this tree
	 */
	public static final class Unmodifiable<E> implements MDTree {
		private final BasicTree<E> tree;

		Unmodifiable(BasicTree<E> tree) {
			if (tree == null)
				throw new NullPointerException();
			this.tree = tree;
		}

		@Override
		public BasicTreeNode.Unmodifiable<E> rootNode() {
			return new BasicTreeNode.Unmodifiable<>(tree.rootNode);
		}
	}
}
