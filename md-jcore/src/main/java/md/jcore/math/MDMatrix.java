package md.jcore.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

/**
 * <p>
 * Represents mathematical matrix of {@link BigDecimal}s.
 * </p>
 * <p>
 * <b>Attention!</b> Rows and columns are numbered from zero!
 * </p>
 * 
 * @author Michal Douša
 *
 */
public final class MDMatrix implements Cloneable, MDMathEntity {
	
	private static final long serialVersionUID = 0x0100L;

	private BigDecimal[][] data;

	public MDMatrix(final BigDecimal[][] data) {
		checkMatrixData(data);
		this.data = new BigDecimal[data.length][data[0].length];
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[0].length; col++) {
				BigDecimal value = data[row][col];
				if (value == null)
					throw new NullPointerException("Null at row " + row + " column " + col);
				this.data[row][col] = value;
			}
		}
	}

	public MDMatrix(final Number[][] data) {
		checkMatrixData(data);
		this.data = new BigDecimal[data.length][data[0].length];
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[0].length; col++) {
				Number value = data[row][col];
				if (value == null)
					throw new NullPointerException("Null at row " + row + " column " + col);
				this.data[row][col] = new BigDecimal(value.toString());
			}
		}
	}

	/**
	 * Constructs a deep copy of given matrix. Hase same effect as using the
	 * {@link #clone()} method.
	 * 
	 * @param mtx matrix to be cloned
	 */
	public MDMatrix(final MDMatrix mtx) {
		if (mtx == null)
			throw new NullPointerException();
		this.data = mtx.data.clone();
	}

	/**
	 * Creates a sigle-column matrix using given {@link MDVector}. Every
	 * <var>n</var><sup>th</sup> coordinate of a {@link MDVector} will be stored in
	 * matrix <var>n</var><sup>th</sup> row and first column (at index 0).
	 * 
	 * @param vectors {@link MDVector} to create a matrix from
	 * 
	 * @throws NullPointerException if {@code null} is given
	 */
	public MDMatrix(final MDVector... vectors) {
		int rows = vectors[0].size();
		int cols = vectors.length;
		this.data = new BigDecimal[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.data[row][col] = vectors[col].get(row);
			}
		}
		checkMatrixData(this.data);
	}

	/**
	 * Gets a number located at specific position
	 * 
	 * @param row    the number of row
	 * @param column the number of column
	 * 
	 * @return <var>a</var><sub><var>r</var><var>c</var></sub>
	 */
	public BigDecimal get(final int row, final int column) {
		return this.data[row][column];
	}

	public int rows() {
		return this.data.length;
	}

	public int columns() {
		return this.data[0].length;
	}
	
	public MDVector columnAsVector(final int column) {
		BigDecimal[] vector = new BigDecimal[rows()];
		for (int row = 0; row < vector.length; row++)
			vector[row] = data[row][column];
		return new MDVector(vector);
	}

	public boolean isSquareMatrix() {
		return rows() == columns();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < data.length; i++) {
			sb.append(Arrays.toString(data[i]));
			if (i < data.length - 1)
				sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}

	@Override
	public String toLaTeX() {
		StringBuilder sb = new StringBuilder();
		sb.append("\\left[ \\begin{align*} ");
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				sb.append(data[i][j]);
				if (j < columns() - 1)
					sb.append(" & ");
				else if (i < rows() - 1)
					sb.append(" \\\\ ");
			}
		}
		sb.append(" \\end{align*} \\right]");
		return sb.toString();
	}
	
	@Override
	public MDMatrix clone() {
		return new MDMatrix(this);
	}

	public MDMatrix add(final MDMatrix mtx2, final MathContext mc) {
		if (this.rows() != mtx2.rows() || this.columns() != mtx2.columns())
			throw new IllegalArgumentException("Different matrix sizes");

		BigDecimal[][] data = new BigDecimal[rows()][columns()];
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < columns(); c++) {
				data[r][c] = get(r, c).add(mtx2.get(r, c), mc);
			}
		}
		return new MDMatrix(data);
	}

	public MDMatrix subtract(final MDMatrix mtx2, final MathContext mc) {
		if (this.rows() != mtx2.rows() || this.columns() != mtx2.columns())
			throw new IllegalArgumentException("Different matrix sizes");

		BigDecimal[][] data = new BigDecimal[rows()][columns()];
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < columns(); c++) {
				data[r][c] = get(r, c).subtract(mtx2.get(r, c), mc);
			}
		}
		return new MDMatrix(data);
	}

	public MDMatrix multiply(final MDMatrix mtx2, final MathContext mc) {
		if (this.columns() != mtx2.rows())
			throw new IllegalArgumentException("Cannot multiply given matrices");

		BigDecimal[][] data = new BigDecimal[this.rows()][mtx2.columns()];
		for (int row = 0; row < this.rows(); row++) {
			for (int col = 0; col < mtx2.columns(); col++) {
				BigDecimal sum = BigDecimal.ZERO;
				for (int k = 0; k < this.columns(); k++)
					sum = sum.add(this.get(row, k).multiply(mtx2.get(k, col)), mc);
				data[row][col] = sum;
			}
		}

		return new MDMatrix(data);
	}
	
	public BigDecimal determinant(MathContext mc) {
		// TODO determinant
		return null;
	}

	public MDMatrix pow(final int power, final MathContext mc) {
		if (!isSquareMatrix())
			throw new IllegalStateException("Matrix is not a square matrix");
		// TODO pow()
		return null;
	}

	// Static methods --------------------------------------------------------------

	private static void checkMatrixData(final Number[][] data) {
		if (data.length == 0)
			throw new IllegalArgumentException("2D array is empty");
		int w = data[0].length;
		for (Number[] row : data) {
			if (row.length != (int) w || w == 0 || (w == 1 && data.length == 1))
				throw new IllegalArgumentException("2D array is inconsistent");
			for (Number cell : row)
				if (cell == null)
					throw new IllegalArgumentException("There is a null value in the 2D array");
		}
	}
	
	private static void checkMatrixData(final double[][] data2) {
		if (data2.length == 0)
			throw new IllegalArgumentException("2D array is empty");
		int w = data2[0].length;
		for (double[] row : data2) {
			if (row.length != (int) w || w == 0 || (w == 1 && data2.length == 1))
				throw new IllegalArgumentException("2D array is inconsistent");
		}
	}
	
	// Double class ----------------------------------------------------------------

	public static final class Double implements Cloneable, MDMathEntity {
		
		private static final long serialVersionUID = 0x0100L;
		
		private double[][] data;

		public Double(final double[][] data) {
			checkMatrixData(data);
			this.data = new double[data.length][data[0].length];
			for (int row = 0; row < data.length; row++) {
				this.data[row] = Arrays.copyOf(data[row], data[row].length);
			}
		}

		/**
		 * Constructs a deep copy of given matrix. Hase same effect as using the
		 * {@link #clone()} method.
		 * 
		 * @param mtx matrix to be cloned
		 */
		public Double(final Double mtx) {
			if (mtx == null)
				throw new NullPointerException();
			this.data = mtx.data.clone();
		}

		/**
		 * Creates a sigle-column matrix using given {@link MDVector}. Every
		 * <var>n</var><sup>th</sup> coordinate of a {@link MDVector} will be stored in
		 * matrix <var>n</var><sup>th</sup> row and first column (at index 0).
		 * 
		 * @param vectors {@link MDVector}s to create a matrix from
		 * 
		 * @throws NullPointerException if {@code null} is given
		 */
		public Double(final MDVector.Double... vectors) {
			int rows = vectors[0].coordinates();
			int cols = vectors.length;
			this.data = new double[rows][cols];
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					this.data[row][col] = vectors[col].get(row);
				}
			}
			checkMatrixData(this.data);
		}

		/**
		 * Gets a number located at specific position
		 * 
		 * @param row    the number of row
		 * @param column the number of column
		 * 
		 * @return <var>a</var><sub><var>r</var><var>c</var></sub>
		 */
		public double get(final int row, final int column) {
			return this.data[row][column];
		}
		
		public int rows() {
			return this.data.length;
		}

		public int columns() {
			return this.data[0].length;
		}
		
		public MDVector.Double columnAsVector(final int column) {
			double[] vector = new double[rows()];
			for (int row = 0; row < vector.length; row++)
				vector[row] = data[row][column];
			return new MDVector.Double(vector);
		}

		public boolean isSquareMatrix() {
			return rows() == columns();
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for (int i = 0; i < data.length; i++) {
				sb.append(Arrays.toString(data[i]));
				if (i < data.length - 1)
					sb.append(", ");
			}
			sb.append(']');
			return sb.toString();
		}
		
		@Override
		public String toLaTeX() {
			StringBuilder sb = new StringBuilder();
			sb.append("\\left[ \\begin{align*} ");
			for (int i = 0; i < rows(); i++) {
				for (int j = 0; j < columns(); j++) {
					sb.append(data[i][j]);
					if (j < columns() - 1)
						sb.append(" & ");
					else if (i < rows() - 1)
						sb.append(" \\\\ ");
				}
			}
			sb.append(" \\end{align*} \\right]");
			return sb.toString();
		}
		
		public strictfp Double add(final Double mtx2) {
			if (this.rows() != mtx2.rows() || this.columns() != mtx2.columns())
				throw new IllegalArgumentException("Different matrix sizes");

			double[][] data = new double[rows()][columns()];
			for (int r = 0; r < rows(); r++) {
				for (int c = 0; c < columns(); c++) {
					data[r][c] = this.get(r, c) + mtx2.get(r, c);
				}
			}
			return new Double(data);
		}

		public strictfp Double subtract(final Double mtx2) {
			if (this.rows() != mtx2.rows() || this.columns() != mtx2.columns())
				throw new IllegalArgumentException("Different matrix sizes");

			double[][] data = new double[rows()][columns()];
			for (int r = 0; r < rows(); r++) {
				for (int c = 0; c < columns(); c++) {
					data[r][c] = this.get(r, c) - mtx2.get(r, c);
				}
			}
			return new Double(data);
		}

		public strictfp Double multiply(final Double mtx2) {
			if (this.columns() != mtx2.rows())
				throw new IllegalArgumentException("Cannot multiply given matrices");

			double[][] data = new double[this.rows()][mtx2.columns()];
			for (int row = 0; row < this.rows(); row++) {
				for (int col = 0; col < mtx2.columns(); col++) {
					double sum = .0;
					for (int k = 0; k < this.columns(); k++)
						sum += this.get(row, k) * mtx2.get(k, col);
					data[row][col] = sum;
				}
			}

			return new Double(data);
		}
		
		public strictfp double determinant() {
			// TODO determinant()
			return 0;
		}

		public strictfp Double pow(final int power, final MathContext mc) {
			if (!isSquareMatrix())
				throw new IllegalStateException("Matrix is not a square matrix");
			Double powMtx = this;
			for (int i = 1; i < power; i++)
				powMtx = powMtx.multiply(this);
			return powMtx;
		}

	}

}
