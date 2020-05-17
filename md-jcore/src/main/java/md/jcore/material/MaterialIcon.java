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
 * @see MaterialIconPathCommand
 */
public final class MaterialIcon implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<MaterialIconPathCommand> path;
	
	public ArrayList<MaterialIconPathCommand> getPath() {
		return path;
	}
	
	public MaterialIcon() {
		path = new ArrayList<>();
	}
	
	/**
	 * Get the Material Design icon as the <code>java.awt.geom.Path2D</code> instance.
	 * @param scale By this parameter the result can be scaled.
	 * @return Material icon represented by the <code>java.awt.geom.Path2D</code> instance.
	 */
	public Path2D.Float getAWTPath(float scale) {
		Path2D.Float path2d = new Path2D.Float();
		path.forEach(pathCommand -> {
			int command = pathCommand.getCommand();
			float[] args = pathCommand.getArguments();
			
			switch(command) {
			case MaterialIconPathCommand.CMD_CLOSE_PATH:
				path2d.closePath();
				break;
			case MaterialIconPathCommand.CMD_MOVE_TO:
				path2d.moveTo(args[0] * scale, args[1] * scale);
				break;
			case MaterialIconPathCommand.CMD_LINE_TO:
				path2d.lineTo(args[0] * scale, args[1] * scale);
				break;
			case MaterialIconPathCommand.CMD_QUAD_TO:
				path2d.quadTo(args[0] * scale, args[1] * scale,
						args[2] * scale, args[3] * scale);
				break;
			case MaterialIconPathCommand.CMD_CURVE_TO:
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
}
