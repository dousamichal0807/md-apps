package mdlib.utils.collections;

public final class GraphEdge<E> {
    private final Graph<E> parentGraph;
    private final GraphVertex<E> from;
    private final GraphVertex<E> to;
    private final float length;

    // Constructor -----------------------------------------------------------------

    /**
     * You cannot use this constructor. It's package-private. Use
     * {@link Graph#createEdge(int, int, float)} instead.
     * @param length length of the vertex
     */
    GraphEdge(final GraphVertex<E> from, final GraphVertex<E> to, final float length) {
        if (from.getParentGraph() != to.getParentGraph())
            throw new IllegalArgumentException("Given vertices are from different graphs");

        this.parentGraph = from.getParentGraph();

        this.from = from;
        this.from.edges.add(this);

        this.to = to;
        this.to.edges.add(this);

        this.length = length;
    }

    // Read-only properties --------------------------------------------------------

    public Graph<E> getParentGraph() {
        return parentGraph;
    }

    public GraphVertex<E> vertexFrom() {
        return from;
    }

    public GraphVertex<E> vertexTo() {
        return to;
    }

    public float getLength() {
        return length;
    }
}
