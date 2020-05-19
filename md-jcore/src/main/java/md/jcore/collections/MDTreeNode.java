package md.jcore.collections;

import java.io.Serializable;
import java.util.Collection;

public interface MDTreeNode extends Serializable {
    /**
     * Gets all children of this node in a {@link java.util.Collection Collection}.
     */
    public Collection<? extends MDTreeNode> childNodes();
}
