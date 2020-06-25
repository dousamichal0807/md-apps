package mdlib.utils.collections;

import mdlib.utils.collections.Tree.UnmodifiableTree;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a node of a tree. One node cannot be added in more trees than one and also cannot be added to the same
 * tree multiple times. Also, the node stores its value. The value type is determined by the generic type {@code <E>}.
 * Every type of node should store its tree which the node is located in and return it via {@link #parentTree()}
 * method.
 *
 * @param <E> the type of instances to be stored in values of this nodes and its subnodes
 * @param <N> the node type itself
 * @author Michal Douša
 * @see Tree
 * @see Heap
 * @see HeapNode
 * @see MDCollections
 * @see UnmodifiableNode
 */
public interface TreeNode<E, N extends TreeNode<E, N>> extends Serializable {

    /**
     * Returns the parent node of this node. If there is no parent, method should return {@code null}.
     *
     * @return the parent node
     */
    TreeNode<E, N> parentNode();

    /**
     * Returns all children of this node in an unmodifialble {@link java.util.Collection Collection}. This method really
     * should return an unmodifiable collection, because when adding a new node, the collection <em>has</em> to check if
     * the node is not in another tree already
     *
     * @return all child nodes of this node in an unmodified collection
     */
    Collection<N> childNodes();

    /**
     * Adds a child to this node
     *
     * @param child child to be added
     * @throws IllegalStateException if node has been already attached to other node as child
     * @throws NullPointerException  if {@code null} is given as a parameter
     */
    void addChild(N child);

    /**
     * Returns value stored in this node.
     *
     * @return the stored value
     */
    E getValue();

    /**
     * Sets given value to the node.
     *
     * @param value the value to be set to the node.
     */
    void setValue(E value);

    /**
     * Returns how many sub-nodes (e.g. child nodes, children of child nodes and so on) has this node. This method
     * should not count the node itself as a sub-node.
     *
     * @return count of sub-nodes of this node
     */
    default int subNodesCount() {
        // Atomic reference where should be stored number total number of sub-nodes. Start with the value of # of
        // children
        int count = childNodes().size();

        // For each child node get number of their subnodes and add it to total # of sub-nodes
        for (TreeNode<E, N> childNode : childNodes())
            count = +childNode.subNodesCount();

        // Return calculated # of sub-nodes
        return count;
    }

    /**
     * Returns the depth of this node as it was a tree, as described below:
     * <ul>
     *     <li>if this node has no children, then the depth is 0,</li>
     *     <li>if this node has children, but all of them has no sub-nodes, the depth is 1,</li>
     *     <li>if there are children of children of this node, but no deeprer, the depth is 2</li>
     *     <li>and so on</li>
     * </ul>
     *
     * @return the depth of imaginary tree which would have this node as the root node.
     */
    default int depth() {
        // If there is no child, depth is 0
        if (childNodes().isEmpty())
            return 0;

        // Get the depth of a child with its maximum value
        int maxChildDepth = 0;
        for (TreeNode<E, N> childNode : childNodes())
            maxChildDepth = Math.max(maxChildDepth, childNode.depth());

        // Return the maxChildDepth, but also child nodes make one layer (or ply) of the sub-tree
        return maxChildDepth + 1;
    }

    /**
     * Returns the tree where the node is located in, or {@code null} if it is not in any tree yet.
     *
     * @return the node's parent tree
     */
    Tree<E, N> parentTree();

    /**
     * Represents an unmodifiable version of {@link TreeNode}. This class is package-private, use {@link
     * MDCollections#unmodifiableTree(Tree)} method to create {@link UnmodifiableNode} instance instead.
     *
     * @param <E> type of elements stored in the tree
     * @see Tree.UnmodifiableTree
     * @see MDCollections#unmodifiableTree(Tree)
     */
    final class UnmodifiableNode<E> implements TreeNode<E, UnmodifiableNode<E>> {

        private final TreeNode<E, ?> node;

        UnmodifiableNode(final TreeNode<E, ?> node) {
            // Requre non-null node
            Objects.requireNonNull(node, "Cannot pass null as argument");
            // Assign values
            this.node = node;
        }

        @Override
        public UnmodifiableNode<E> parentNode() {
            return node.parentNode() == null ? null : new UnmodifiableNode<>(node.parentNode());
        }

        @Override
        public List<UnmodifiableNode<E>> childNodes() {
            // Create list of nodes
            ArrayList<UnmodifiableNode<E>> list = new ArrayList<>();
            // Add all of them as unmodifiable
            for (TreeNode<E, ?> child : node.childNodes())
                list.add(new UnmodifiableNode<>(child));
            // Also return list as unmodifiable
            return Collections.unmodifiableList(list);
        }

        @Override
        public E getValue() {
            return node.getValue();
        }

        @Override
        public UnmodifiableTree<E> parentTree() {
            return node.parentTree() == null ? null : new UnmodifiableTree<>(node.parentTree());
        }

        // Unsupported operations ---------------------------------------------

        /**
         * Unsupported operation. This method always throws an {@link UnsupportedOperationException}.
         *
         * @param value the value which should be set to the node, but it won't
         * @throws UnsupportedOperationException always
         */
        @Override
        public void setValue(final E value) {
            throw new UnsupportedOperationException();
        }

        /**
         * Unsupported operation. This method always throws an {@link UnsupportedOperationException}.
         *
         * @param child child which should attached to the node, but it won't
         * @throws UnsupportedOperationException always
         */
        @Override
        public void addChild(final UnmodifiableNode<E> child) {
            throw new UnsupportedOperationException();
        }
    }
}
