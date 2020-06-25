package mdlib.utils.collections;

import mdlib.utils.ObjectUtilities;

import java.util.*;

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
public final class BasicTreeNode<E> implements TreeNode<E, BasicTreeNode<E>> {
    private static final long serialVersionUID = 0x100L;

    private final ArrayList<BasicTreeNode<E>> children;
    private E value;
    private BasicTree<E> parentTree;
    private BasicTreeNode<E> parentNode;

    @Override
    public List<BasicTreeNode<E>> childNodes() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public BasicTree<E> parentTree() {
        return parentTree;
    }

    @Override
    public BasicTreeNode<E> parentNode() {
        return parentNode;
    }


    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object o2) {
        try {
            BasicTreeNode<E> n2 = (BasicTreeNode<E>) o2;
            return this.getValue().equals(n2.getValue());
        } catch (ClassCastException exc) {
            return false;
        }
    }

    @Override
    public E getValue() {
        return value;
    }

    /**
     * Sets the value that has the node.
     *
     * @param value  the value that will be stored in the node
     */
    public void setValue(final E value) {
        this.value = value;
    }

    /**
     * Constructor wher you can specify the initial value.
     *
     * @param value  the initial value
     */
    public BasicTreeNode(final E value) {
        this(value, null);
    }

    BasicTreeNode(final E value, final BasicTree<E> parentTree) {
        this.children = new ArrayList<>();
        this.setValue(value);
        this.setParentTree(parentTree);
    }

    /**
     * Constructor with default initial value set to {@code null}.
     */
    public BasicTreeNode() {
        this(null);
    }

    /**
     * Adds a child on specified index.
     *
     * @param index the index where to put the child
     * @param node the child to be put
     *
     * @throws IllegalStateException if given node is already attached as child of another node
     */
    public void addChild(final int index, final BasicTreeNode<E> node) {
        // Node cannot be null
        Objects.requireNonNull(node, "Cannot add null to children list");
        // Node cannot be already attached
        ObjectUtilities.requireNull(node.parentNode, new IllegalStateException("Node is already attached to other node"));
        // Set parent node and tree
        node.parentNode = this;
        node.setParentTree(this.parentTree);
        // Add to the list of nodes
        children.add(index, node);
    }

    /**
     * Adds a child to the end of the list.
     *
     * @param node the child to be put
     *
     * @throws IllegalStateException if given node is already attached as child of another node
     */
    public void addChild(final BasicTreeNode<E> node) {
        addChild(children.size(), node);
    }

    /**
     * Adds all nodes in given {@link Collection} as children. First node will be at given starting index.
     *
     * @param startIndex index where to start
     * @param nodes      the {@link Collection} of nodes
     *
     * @throws IllegalStateException under same circumstances when calling {@link #addChild(int, BasicTreeNode)} &ndash;
     *                               it is called on every node in the collection
     * @throws NullPointerException  if given {@link Collection} is {@code null}, or at least one of the elements
     */
    public void addChildren(final int startIndex, final Collection<BasicTreeNode<E>> nodes) {
        Iterator<BasicTreeNode<E>> iterator = nodes.iterator();
        for (int nextNodeIndex = startIndex; iterator.hasNext(); nextNodeIndex++)
            addChild(nextNodeIndex, iterator.next());
    }

    public void addChildren(final Collection<BasicTreeNode<E>> nodes) {
        addChildren(children.size(), nodes);
    }

    @SafeVarargs
    public final void addChildren(final int startIndex, final BasicTreeNode<E>... nodes) {
        addChildren(startIndex, List.of(nodes));
    }

    public void removeChild(final int index) {
        // Get the child
        BasicTreeNode<E> toRemove = children.get(index);
        // Set parent tree and node to null
        toRemove.setParentTree(null);
        toRemove.parentNode = null;
        // Remove the child
        children.remove(index);
    }

    public void removeChild(final BasicTreeNode<E> child) {
        // Get index of the child
        int index = children.indexOf(child);
        // Is child preset in this node as a child?
        if (index < 0)
            throw new NoSuchElementException("Given child not present in the list of children of this node");
        // Remove child using removeChild(int index) method
        removeChild(index);
    }

    public void removeChildren(final int... indices) {
        // Don't forget - elements are shifted when one is removed
        // Sort the indices
        Arrays.sort(indices);
        // Start by the last element
        for (int index = indices.length - 1; index >= 0; index--)
            removeChild(index);
    }

    public void removeChildren(final Collection<BasicTreeNode<E>> nodes) {
        // Create array with nodes' indices
        int[] indicesArray = new int[nodes.size()];
        // For every node get its index
        int ii = 0; // <-- Where to save in the array
        for (BasicTreeNode<E> node : nodes) {
            int index = children.indexOf(node);
            // If the element is not present, throw an exception
            if (index < 0)
                throw new NoSuchElementException("At least one given child not present in the list of children of this node");
            // Write to the array + automatically increment `ii` variable afer
            indicesArray[ii++] = index;
        }
        // Use removeChildren(int...)
        removeChildren(indicesArray);
    }

    public void clearChildren() {
        // De-attach each child
        for (BasicTreeNode<E> child : children) {
            child.parentNode = null;
            child.setParentTree(null);
        }
        // Clear the list
        children.clear();
    }

    private void setParentTree(final BasicTree<E> parent) {
        this.parentTree = parent;
        for (BasicTreeNode<E> child : children)
            setParentTree(parent);
    }
}
