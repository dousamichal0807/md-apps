package mdlib.utils.collections;

import java.util.ArrayList;

public final class Graph<E> {
	final ArrayList<GraphVertex<E>> vertices;
	final ArrayList<GraphEdge<E>> edges;
	
	public Graph() {
		this.vertices = new ArrayList<>();
		this.edges = new ArrayList<>();
	}
	
	public GraphVertex<E> createVertex(final E value) {
		GraphVertex<E> v = new GraphVertex<>(this, value);
		vertices.add(v);
		return v;
	}
	
	public GraphEdge<E> createEdge(final int from, final int to, final float length) {
		GraphEdge<E> edge = new GraphEdge<>(vertices.get(from), vertices.get(to), length);
		edges.add(edge);
		return edge;
	}

	public boolean isEmpty() {
		return vertices.isEmpty();
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
