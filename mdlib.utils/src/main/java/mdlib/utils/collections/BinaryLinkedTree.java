package mdlib.utils.collections;

public final class BinaryLinkedTree<E> implements Tree<E, BinaryLinkedTreeNode<E>> {
    private boolean nullPermitted;
    private BinaryLinkedTreeNode<E> rootNode;

    /**
     * Constructs a {@link BinaryLinkedTree}. You can specify if in the tree's nodes {@code null} can be stored as a value.
     * Once you specify if {@code null} is permitted in the tree and its root node, there is no going back.
     *
     * @param nullPermitted if {@code null} can be stored as a value of the tree's nodes
     * @param rootNode the root node of this tree
     */
    public BinaryLinkedTree(final boolean nullPermitted, final BinaryLinkedTreeNode<E> rootNode) {
        this.nullPermitted = nullPermitted;
        this.rootNode = rootNode;
    }

    /**
     * Returns if {@code null} is permitted as a value of the tree's nodes.
     *
     * @return if {@code null} is permitted
     */
    public boolean isNullPermitted() {
        return nullPermitted;
    }

    /**
     * Sets if {@code null} should be permitted as a value of the tree's nodes. If the value is switching from
     * {@code true} to {@code false}, all nodes will be checked what value do they contain. If there is found such a
     * node, method throws {@link NullPointerException} and value will be switched back to {@code false}.
     *
     * @param nullPermitted should be {@code null} is permitted
     *
     * @throws NullPointerException as described above
     */
    public void setNullPermitted(final boolean nullPermitted) {
        this.nullPermitted = nullPermitted;
    }

    @Override
    public BinaryLinkedTreeNode<E> getRootNode() {
        return rootNode;
    }

    public void setRootNode(final BinaryLinkedTreeNode<E> rootNode) {
        this.rootNode = rootNode;
    }
}
