package md.jcore.collections;

import java.util.ArrayList;

import md.jcore.Disposable;

public final class GraphVertex<E> implements Disposable {
	private Graph<E> parentGraph;
	ArrayList<GraphEdge<E>> edges;
	private E value;
	
	GraphVertex(Graph<E> parent, E value) {
		this.value = value;
		this.parentGraph = parent;
		this.edges = new ArrayList<GraphEdge<E>>();
	}

	public Graph<E> getParentGraph() {
		Disposable.checkIsNotDisposed(this);
		return parentGraph;
	}
	
	public E getValue() {
		Disposable.checkIsNotDisposed(this);
		return value;
	}
	
	public void setValue(E value) {
		Disposable.checkIsNotDisposed(this);
		this.value = value;
	}
	
	// Disposing -------------------------------------------------------------------

	@Override
	public void dispose() {
		parentGraph.vertices.remove(this);
		edges.forEach(edge -> edge.dispose());
		
		parentGraph = null;
		edges = null;
		value = null;
		
		System.gc();
	}

	@Override
	public boolean isDisposed() {
		return parentGraph == null;
	}
}
