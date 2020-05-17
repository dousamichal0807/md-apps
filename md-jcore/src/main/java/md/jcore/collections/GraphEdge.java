package md.jcore.collections;

import md.jcore.Disposable;

public final class GraphEdge<E> implements Disposable {
	private Graph<E> parentGraph;
	private GraphVertex<E> from, to;
	private Float length;
	
	// Constructor -----------------------------------------------------------------
	
	/**
	 * You cannot use this constructor. It's package-private. Use
	 * {@link Graph#createEdge(int, int, float)} instead.
	 * @param length length of the vertex
	 */
	GraphEdge(GraphVertex<E> from, GraphVertex<E> to, float length) {
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
		Disposable.checkIsNotDisposed(this);
		return parentGraph;
	}
	
	public GraphVertex<E> getFrom() {
		Disposable.checkIsNotDisposed(this);
		return from;
	}

	public GraphVertex<E> getTo() {
		Disposable.checkIsNotDisposed(this);
		return to;
	}

	public float getLength() {
		Disposable.checkIsNotDisposed(this);
		return length;
	}
	
	// Disposing -------------------------------------------------------------------

	@Override
	public void dispose() {
		parentGraph.edges.remove(this);
		parentGraph = null;
		from = null;
		to = null;
		length = null;
	}

	@Override
	public boolean isDisposed() {
		return parentGraph == null;
	}
	
	
}
