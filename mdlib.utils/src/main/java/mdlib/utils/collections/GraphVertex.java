package mdlib.utils.collections;

import java.util.ArrayList;

public final class GraphVertex<E> {
    private final Graph<E> parentGraph;
    ArrayList<GraphEdge<E>> edges;
    private E value;

    // package-only ctor
    GraphVertex(final Graph<E> parent, final E value) {
        this.value = value;
        this.parentGraph = parent;
        this.edges = new ArrayList<>();
    }

    public Graph<E> getParentGraph() {
        return parentGraph;
    }

    public E getValue() {
        return value;
    }

    public void setValue(final E value) {
        this.value = value;
    }
}
