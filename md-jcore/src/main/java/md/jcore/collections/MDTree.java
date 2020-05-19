package md.jcore.collections;

import java.io.Serializable;

public interface MDTree extends Serializable {
    /**
     * Returns the root node of this tree.
     *
     * @return the root node
     */
    public MDTreeNode rootNode();
}
