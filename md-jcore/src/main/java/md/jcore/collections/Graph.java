package md.jcore.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Graph<E> {
	final ArrayList<GraphVertex<E>> vertices;
	final ArrayList<GraphEdge<E>> edges;
	
	public Graph() {
		this.vertices = new ArrayList<>();
		this.edges = new ArrayList<>();
	}
	
	public GraphVertex<E> createVertex(E value) {
		GraphVertex<E> v = new GraphVertex<>(this, value);
		vertices.add(v);
		return v;
	}
	
	public GraphEdge<E> createEdge(int from, int to, float length) {
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
