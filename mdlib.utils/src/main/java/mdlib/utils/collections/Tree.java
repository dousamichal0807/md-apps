package mdlib.utils.collections;

import mdlib.utils.collections.TreeNode.UnmodifiableNode;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a tree. There are {@link TreeNode} instances as nodes which also have assingned a value.
 *
 * @param <E> values of the nodes in the tree
 */
public interface Tree<E, N extends TreeNode<E, N>> extends Serializable {

    /**
     * Returns the root node of this tree.
     *
     * @return the root node
     */
    TreeNode<E, N> getRootNode();

    /**
     * Returns the number of nodes in the entire tree.
     *
     * @return # of nodes in this tree
     */
    default int nodesCount() {
        // # of root node's sub-nodes + 1 for the root node
        return getRootNode().subNodesCount() + 1;
    }

    /**
     * Class for an unmodifiable version of a {@link Tree}. This class is package-private, use {@link
     * MDCollections#unmodifiableTree(Tree)} to create an instance of this class.
     *
     * @param <E> type of elements of the tree
     */
    final class UnmodifiableTree<E> implements Tree<E, UnmodifiableNode<E>> {

        private final Tree<E, ?> tree;

        /**
         * Constructor, where you have to pass a non-{@code null} instance of a {@link Tree}.
         *
         * @param tree the {@link Tree} instance to create an {@link UnmodifiableTree} from
         */
        UnmodifiableTree(final Tree<E, ?> tree) {
            // Require non-null
            Objects.requireNonNull(tree, "Tree cannot be null");
            // Assign value
            this.tree = tree;
        }

        @Override
        public UnmodifiableNode<E> getRootNode() {
            return tree.getRootNode() == null ? null : new UnmodifiableNode<>(tree.getRootNode());
        }
    }
}
