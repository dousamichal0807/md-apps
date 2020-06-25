package mdlib.utils.collections;

import mdlib.utils.collections.Tree.UnmodifiableTree;
import mdlib.utils.collections.TreeNode.UnmodifiableNode;

public final class MDCollections {

    // Do not create instances!
    private MDCollections() {
    }

    public static <E> UnmodifiableTree<E> unmodifiableTree(final Tree<E, ?> tree) {
        return tree == null ? null : new UnmodifiableTree<>(tree);
    }

    public static <E> UnmodifiableNode<E> unmodifiableTreeNode(final TreeNode<E, ?> node) {
        return node == null ? null : new UnmodifiableNode<>(node);
    }

}
