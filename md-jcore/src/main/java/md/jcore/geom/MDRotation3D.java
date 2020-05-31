package md.jcore.geom;

import md.jcore.math.MDMatrix;

public class MDRotation3D {

	/**
	 * Represents constant for X axis.
	 * 
	 * @see #getRotationAxis()
	 */
	public static final byte AXIS_X = 0;

	/**
	 * Represents constant for Y axis.
	 * 
	 * @see #getRotationAxis()
	 */
	public static final byte AXIS_Y = 1;

	/**
	 * Represents constant for Z axis.
	 * 
	 * @see #getRotationAxis()
	 */
	public static final byte AXIS_Z = 2;
	
	private final double angle;
	private final byte axis;

	public MDRotation3D(final byte axis, final double angle) {
		if (axis < 0 || axis > 3)
			throw new IllegalArgumentException("Illegal axis");
		this.angle = angle;
		this.axis = axis;
	}

	/**
	 * Returns the angle in radians which is the projecton rotated by.
	 * 
	 * @return the rotation angle in radians
	 */
	public double getRotationAngle() {
		return angle;
	}

	public byte getRotationAxis() {
		return axis;
	}

	public MDMatrix asMatrix() {
		return null;
	}

	public MDMatrix.Double asDoubleMatrix() {
		double[][] mtx = null;
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		switch (axis) {
		case AXIS_X:
			mtx = new double[][] { { 1, 0, 0 }, { 0, cos, -sin }, { 0, sin, cos } };
			break;
		case AXIS_Y:
			mtx = new double[][] { { cos, 0, sin }, { 0, 1, 0 }, { -sin, 0, cos } };
			break;
		case AXIS_Z:
			mtx = new double[][] { { cos, -sin, 0 }, { sin, cos, 0 }, { 0, 0, 1 } };
		}
		return new MDMatrix.Double(mtx);
	}
}
