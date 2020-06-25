package mdlib.utils.collections;

import mdlib.utils.ObjectUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BinaryLinkedTreeNode<E> implements TreeNode<E, BinaryLinkedTreeNode<E>> {
    private BinaryLinkedTreeNode<E> parentNode;
    private BinaryLinkedTreeNode<E> leftChild, rightChild;
    private BinaryLinkedTree<E> parentTree;
    private E value;

    /**
     * Creates a {@link BinaryLinkedTreeNode}. You can specify value, left and right child.
     *
     * @param value value to be set
     * @param leftChild left child to be set
     * @param rightChild right child to be set
     */
    public BinaryLinkedTreeNode(final E value, final BinaryLinkedTreeNode<E> leftChild, final BinaryLinkedTreeNode<E> rightChild) {
        setValue(value);
        setLeftChild(leftChild);
        setRightChild(rightChild);
    }

    /**
     * Creates a {@link BinaryLinkedTreeNode}. You can specify value. Left and right child will be automatically set to
     * {@code null}.
     *
     * @param value value to be set
     */
    public BinaryLinkedTreeNode(final E value) {
        this(value, null, null);
    }

    /**
     * Cretates a {@link BinaryLinkedTreeNode}. All value, left and right child initial values are {@code null}.
     */
    public BinaryLinkedTreeNode() {
        this(null);
    }

    private void setParentTree(final BinaryLinkedTree<E> tree) {
        // Check null value
        if (value == null && !tree.isNullPermitted())
            throw new NullPointerException("Found a node with null value");

        // Set new parent tree to left and right child
        if (leftChild != null)
            leftChild.setParentTree(tree);
        if (rightChild != null)
            rightChild.setParentTree(tree);

        // If there is not than null problem as well, we can assign the new tree
        this.parentTree = tree;
    }

    /**
     * Returns the left child of this node, or {@code null} if there is not attached left child to the node.
     *
     * @return left child, if exists, otherwise {@code null}
     */
    public BinaryLinkedTreeNode<E> getLeftChild() {
        return leftChild;
    }

    /**
     * Sets the left child of the tree. To de-attach current left node, pass {@code null} as the argument.
     *
     * @param leftChild the left child to be set
     *
     * @throws IllegalStateException if the specified node is already attached to other node as a child
     */
    public void setLeftChild(final BinaryLinkedTreeNode<E> leftChild) {
        // Check new value and assign some properties
        if (leftChild != null) {
            // No parent is needed
            ObjectUtilities.requireNull(leftChild.parentNode, new IllegalStateException("Node already attached"));
            // Assign parent node and tree
            leftChild.parentNode = this;
            leftChild.setParentTree(this.parentTree);
        }
        // De-attach the old one, if there is and attaching a new node was successfull
        if (this.leftChild != null) {
            // No parent node
            this.leftChild.parentNode = null;
            // No parent tree since it is not attached
            this.leftChild.setParentTree(null);
        }
        // Set the new one
        this.leftChild = leftChild;
    }

    /**
     * Returns the right child of this node, or {@code null} if there is not attached right child to the node.
     *
     * @return right child, if exists, otherwise {@code null}
     */
    public BinaryLinkedTreeNode<E> getRightChild() {
        return rightChild;
    }

    /**
     * Sets the right child of the tree. To de-attach current right node, pass {@code null} as the argument.
     *
     * @param rightChild the right child to be set
     *
     * @throws IllegalStateException if the specified node is already attached to other node as a child
     */
    public void setRightChild(final BinaryLinkedTreeNode<E> rightChild) {
        // Check new value and assign some properties
        if (rightChild != null) {
            // No parent is needed
            ObjectUtilities.requireNull(rightChild.parentNode, new IllegalStateException("Node already attached"));
            // Assign parent node and tree
            rightChild.parentNode = this;
            rightChild.setParentTree(this.parentTree);
        }
        // De-attach the old one, if there is and attaching a new node was successfull
        if (this.rightChild != null) {
            // No parent node
            this.rightChild.parentNode = null;
            // No parent tree since it is not attached
            this.rightChild.setParentTree(null);
        }
        // Set the new one
        this.rightChild = rightChild;
    }

    /**
     * Unsupported operation! Use {@link #setLeftChild(BinaryLinkedTreeNode)} or {@link #setRightChild(BinaryLinkedTreeNode)} instead.
     *
     * @param node node that should be added as a child (but it won't, of course)
     */
    @Override
    public void addChild(final BinaryLinkedTreeNode<E> node) {
        throw new UnsupportedOperationException("Cannot add a child node using this method");
    }

    @Override
    public BinaryLinkedTreeNode<E> parentNode() {
        return parentNode;
    }

    @Override
    public List<BinaryLinkedTreeNode<E>> childNodes() {
        // Create ArrayList, there will be no more than 2 children
        ArrayList<BinaryLinkedTreeNode<E>> nodes = new ArrayList<>(2);
        // Add left child, if there is
        if (leftChild != null)
            nodes.add(leftChild);
        // Add right child, ifthere is
        if (rightChild != null)
            nodes.add(rightChild);
        // Return unmodifiable
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public E getValue() {
        return value;
    }

    /**
     * Sets the value stored in this node.
     *
     * @param value the value to be stored
     */
    public void setValue(final E value) {
        // Check if value == null and parent tree permits null, if there is
        if (value == null && parentTree != null && !parentTree.isNullPermitted())
            throw new NullPointerException("null is not allowed by the tree");

        this.value = value;
    }

    @Override
    public BinaryLinkedTree<E> parentTree() {
        return parentTree;
    }
}
