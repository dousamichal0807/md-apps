package md.jcore.geom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import md.jcore.math.MDMatrix;
import md.jcore.math.MDVector;

/**
 * Projector that project 3D points into 2D using orthogonal projection. Uses
 * high-precision {@link BigDecimal} numbers.
 * 
 * @author Michal Douša
 */
public final class MDOrthogonalProjector3D {

	/**
	 * Orthogonal projection matrix from 3D to 2D.
	 */
	public static final MDMatrix MATRIX_PROJECTION_ORTHOGONAL = new MDMatrix(
			new BigDecimal[][] { { BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO },
					{ BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO } });

	/**
	 * Same as {@link MDOrthogonalProjector3D}, but this class uses {@code double}s
	 * instead of {@link BigDecimal}s.
	 * 
	 * @author Michal Douša
	 * @see MDOrthogonalProjector3D
	 */
	public static final class Double implements MDProjector.Double {

		public static final MDMatrix.Double MATRIX_PROJECTION_ORTHOGONAL = new MDMatrix.Double(
				new double[][] { { 1, 0, 0 }, { 0, 1, 0 } });

		private ArrayList<MDRotation3D> rotations;

		public Double() {
			rotations = new ArrayList<MDRotation3D>();
		}

		/**
		 * Returns an {@link ArrayList} of all {@link MDRotation3D}s. This list is editable,
		 * so if you want to rotate, you can add or remove a rotation.
		 * 
		 * @return an {@link ArrayList} of all {@link MDRotation3D}s
		 */
		public ArrayList<MDRotation3D> rotations() {
			return rotations;
		}

		@Override
		public md.jcore.math.MDVector.Double project(md.jcore.math.MDVector.Double vector) {
			AtomicReference<MDMatrix.Double> mtx = new AtomicReference<MDMatrix.Double>(new MDMatrix.Double(vector));
			rotations.forEach(rotation -> {
				mtx.set(rotation.asDoubleMatrix().multiply(mtx.get()));
			});
			MDVector.Double result = MATRIX_PROJECTION_ORTHOGONAL.multiply(mtx.get()).columnAsVector(0);
			return result;
		}
	}
}
