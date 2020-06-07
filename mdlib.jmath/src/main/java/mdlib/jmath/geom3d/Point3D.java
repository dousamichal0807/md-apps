package mdlib.jmath.geom3d;

import java.math.BigDecimal;

import mdlib.jmath.math.MDVector;

/**
 * This class represents a point in three-dimensional Cartesian coordinate system.
 * 
 * @author Michal Douša
 *
 * @see #getX()
 * @see #getY()
 * @see #getZ()
 */
public class Point3D {

	private final BigDecimal x;
    private final BigDecimal y;
    private final BigDecimal z;

	/**
	 * Gets the <var>x</var>-coordinate of the 3D point.
	 * 
	 * @return <var>x</var>-coordinate of the 3D point
	 */
	public BigDecimal getX() {
		return x;
	}

	/**
	 * Gets the <var>y</var>-coordinate of the 3D point.
	 * 
	 * @return <var>y</var>-coordinate of the 3D point
	 */
	public BigDecimal getY() {
		return y;
	}

	/**
	 * Gets the <var>z</var>-coordinate of the 3D point.
	 * 
	 * @return <var>z</var>-coordinate of the 3D point
	 */
	public BigDecimal getZ() {
		return z;
	}
	
	/**
	 * Converts a 3D point into a {@link MDVector}.
	 * 
	 * @return converted point into a {@link MDVector}
	 */
	public MDVector asVector() {
		return new MDVector(x, y, z);
	}

	/**
	 * Constructs a 3D point using <var>x</var>, <var>y</var> and
	 * <var>z</var>-coordinates.
	 * 
	 * @param x <var>x</var>-coordinate of the 3D point
	 * @param y <var>y</var>-coordinate of the 3D point
	 * @param z <var>z</var>-coordinate of the 3D point
	 */
	public Point3D(final BigDecimal x, final BigDecimal y, final BigDecimal z) {
		if (x == null || y == null || z == null)
			throw new NullPointerException("All coordinates (x, y, z) must not be null");
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * This class represents a point in three-dimensional Cartesian coordinate system.
	 * 
	 * @author Michal Douša
	 *
	 * @see #getX()
	 * @see #getY()
	 * @see #getZ()
	 */
	public static final class Double {

		private final double x;
        private final double y;
        private final double z;

		/**
		 * Gets the <var>x</var>-coordinate of the 3D point.
		 * 
		 * @return <var>x</var>-coordinate of the 3D point
		 */
		public double getX() {
			return x;
		}

		/**
		 * Gets the <var>y</var>-coordinate of the 3D point.
		 * 
		 * @return <var>y</var>-coordinate of the 3D point
		 */
		public double getY() {
			return y;
		}

		/**
		 * Gets the <var>z</var>-coordinate of the 3D point.
		 * 
		 * @return <var>z</var>-coordinate of the 3D point
		 */
		public double getZ() {
			return z;
		}
		
		/**
		 * Converts a 3D point into a {@link MDVector.Double}.
		 * 
		 * @return converted point into a {@link MDVector.Double}
		 */
		public MDVector.Double asVector() {
			return new MDVector.Double(x, y, z);
		}

		/**
		 * Constructs a 3D point using <var>x</var>, <var>y</var> and
		 * <var>z</var>-coordinates.
		 * 
		 * @param x <var>x</var>-coordinate of the 3D point
		 * @param y <var>y</var>-coordinate of the 3D point
		 * @param z <var>z</var>-coordinate of the 3D point
		 */
		public Double(final double x, final double y, final double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

	}
}
