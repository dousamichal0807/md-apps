package mdlib.utils.collections;

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
 * @see #getRootNode()
 */
public final class BasicTree<E> implements Tree<E, BasicTreeNode<E>> {
    private static final long serialVersionUID = 0x100L;

    private final BasicTreeNode<E> rootNode;

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
    public BasicTree(final E rootNodeValue) {
        rootNode = new BasicTreeNode<>(rootNodeValue, this);
    }

    @Override
    public BasicTreeNode<E> getRootNode() {
        return rootNode;
    }
}
