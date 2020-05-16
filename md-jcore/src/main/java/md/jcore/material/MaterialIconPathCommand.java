package md.jcore.material;

import java.io.Serializable;

/**
 * Represents one drawing command to be done in MaterialIcon class
 * @author Michal Douša
 * @see MaterialIcon
 */
public final class MaterialIconPathCommand implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Represents command <code>closePat()</code> in <code>java.awt.Path2D</code>.
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

	private float[] arguments;
	private int command;

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
	public MaterialIconPathCommand(int command, float...arguments) {
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
		
		for (int i = 0; i < arguments.length; i++) {
			if(arguments[i] < 0 || arguments[i] > 24) {
				throw new IllegalArgumentException("Argument at index " + i + " out of range");
			}
		}
		
		this.arguments = arguments;
		this.command = command;
	}
}
