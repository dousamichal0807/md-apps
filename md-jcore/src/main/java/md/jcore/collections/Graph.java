package md.jcore.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Graph<E> implements Collection<E> {
	ArrayList<GraphVertex<E>> vertices;
	ArrayList<GraphEdge<E>> edges;
	
	public Graph() {
		this.vertices = new ArrayList<GraphVertex<E>>();
		this.edges = new ArrayList<GraphEdge<E>>();
	}
	
	public GraphVertex<E> createVertex(E value) {
		GraphVertex<E> v = new GraphVertex<E>(this, value);
		vertices.add(v);
		return v;
	}
	
	public GraphEdge<E> createEdge(int from, int to, float length) {
		GraphEdge<E> edge = new GraphEdge<E>(vertices.get(from), vertices.get(to), length);
		edges.add(edge);
		return edge;
	}

	@Override
	public int size() {
		return vertices.size() + edges.size();
	}

	@Override
	public boolean isEmpty() {
		return vertices.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	@Override
	public boolean add(E e) {
		return createVertex(e) != null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
