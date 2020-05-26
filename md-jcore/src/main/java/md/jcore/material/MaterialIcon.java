package md.jcore.material;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author dousamichal
 * @see MaterialIcons
 * @see PathCommand
 */
public final class MaterialIcon implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<PathCommand> path;
	
	public ArrayList<PathCommand> getPath() {
		return path;
	}

	/**
	 * Creates an empty Material Design icon.
	 */
	public MaterialIcon() {
		path = new ArrayList<>();
	}
	
	/**
	 * Get the Material Design icon as the {@link Path2D} instance.
	 * @param scale By this parameter the result can be scaled.
	 * @return Material icon represented by the {@link Path2D} instance.
	 */
	public Path2D.Float getAWTPath(final float scale) {
		Path2D.Float path2d = new Path2D.Float();
		path.forEach(pathCommand -> {
			int command = pathCommand.getCommand();
			float[] args = pathCommand.getArguments();
			
			switch(command) {
			case PathCommand.CMD_CLOSE_PATH:
				path2d.closePath();
				break;
			case PathCommand.CMD_MOVE_TO:
				path2d.moveTo(args[0] * scale, args[1] * scale);
				break;
			case PathCommand.CMD_LINE_TO:
				path2d.lineTo(args[0] * scale, args[1] * scale);
				break;
			case PathCommand.CMD_QUAD_TO:
				path2d.quadTo(args[0] * scale, args[1] * scale,
						args[2] * scale, args[3] * scale);
				break;
			case PathCommand.CMD_CURVE_TO:
				path2d.curveTo(args[0] * scale, args[1] * scale,
						args[2] * scale, args[3] * scale,
						args[4] * scale, args[5] * scale);
				break;
			}
		});
		return path2d;
	}
	
	/**
	 * Creates image from this object.
	 */
	public BufferedImage getAWTImage(float scale, Color color) {
		int size = (int) (24f * scale + .5f);
		
		Path2D.Float path = getAWTPath(scale);
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(color);
		graphics.fill(path);
		
		return image;
	}

	/**
	 * Represents one drawing command to be done in MaterialIcon class
	 * @author Michal Douša
	 * @see MaterialIcon
	 */
	public static final class PathCommand implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * Represents command <code>closePath()</code> in <code>java.awt.Path2D</code>.
		 * Has no argument.
		 */
		public static final int CMD_CLOSE_PATH = 0;

		/**
		 * Represents command <code>moveTo(x, y)</code> in <code>java.awt.Path2D</code>.
		 * Has 2 arguments.
		 */
		public static final int CMD_MOVE_TO = 1;

		/**
		 * Represents command <code>lineTo(x, y)</code> in <code>java.awt.Path2D</code>.
		 * Has 2 arguments.
		 */
		public static final int CMD_LINE_TO = 2;

		/**
		 * Represents command <code>quadTo(x1, y1, x2, y2)</code> in
		 * <code>java.awt.Path2D</code>. Has 4 arguments.
		 */
		public static final int CMD_QUAD_TO = 3;

		/**
		 * Represents command <code>curveTo(x1, y1, x2, y2, x3, y3)</code> in
		 * <code>java.awt.Path2D</code>. Has 6 arguments.
		 */
		public static final int CMD_CURVE_TO = 4;

		private final float[] arguments;
		private final int command;

		/**
		 * Gets passed arguments.
		 * @return Passed arguments
		 */
		public float[] getArguments() {
			return arguments;
		}

		/**
		 * Gets passed drawing command.
		 * @return Passed command
		 */
		public int getCommand() {
			return command;
		}

		/**
		 * One and only constructor for this class. This class is immutable.
		 * @param command Drawing command to be done
		 * @param arguments Arguments to pass to the drawing command
		 */
		public PathCommand(final int command, final float...arguments) {
			int argcount;
			switch(command) {
			case CMD_CLOSE_PATH:
				argcount = 0;
				break;
			case CMD_MOVE_TO:
			case CMD_LINE_TO:
				argcount = 2;
				break;
			case CMD_QUAD_TO:
				argcount = 4;
				break;
			case CMD_CURVE_TO:
				argcount = 6;
				break;
			default:
				throw new IllegalArgumentException("Illegal command was passed");
			}

			if(argcount != arguments.length)
				throw new IllegalArgumentException("Count of arguments for specified command is wrong. Expected " +
						argcount + " commands, received " + arguments.length + " commands");

			for (int i = 0; i < arguments.length; i++)
				if (arguments[i] < 0 || arguments[i] > 24)
					throw new IllegalArgumentException("Argument at index " + i + " out of range");

			this.arguments = arguments;
			this.command = command;
		}
	}
}
