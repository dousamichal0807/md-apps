package md.jcore.geom;

import md.jcore.math.MDVector;

public interface MDProjector {
	public MDVector project(MDVector vector);
	
	public static interface Double {
		public MDVector.Double project(MDVector.Double vector);
	}
}
