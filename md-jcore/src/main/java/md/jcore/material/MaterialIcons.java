package md.jcore.material;

import static md.jcore.material.MaterialIconPathCommand.*;

/**
 * Class containing static methods for creating specific icons.
 * 
 * @author Michal Douša
 * @see MaterialIcon
 */
public final class MaterialIcons {

	/**
	 * Creates the 'x' icon.
	 * 
	 * @return the 'x' icon
	 */
	public static MaterialIcon createClearIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 5.707f, 4.293f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4.293f, 5.707f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 10.586f, 12f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4.293f, 18.293f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 5.707f, 19.707f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 12f, 13.414f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 18.293f, 19.707f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 19.707f, 18.293f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 13.414f, 12));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 19.707f, 5.707f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 18.293f, 4.293f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 12f, 10.586f));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		return mi;
	}
	
	/**
	 * Creates the "more" icon with 3 dots organized vertically.
	 * 
	 * @return the "more" icon
	 */
	public static MaterialIcon createMoreIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		for (int n = 0; n < 3; n++) {
			mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 12, 4 + 6 * n));
			mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 14, 4 + 6 * n, 14, 6 + 6 * n));
			mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 14, 8 + 6 * n, 12, 8 + 6 * n));
			mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 10, 8 + 6 * n, 10, 6 + 6 * n));
			mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 10, 4 + 6 * n, 12, 4 + 6 * n));
			mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		}
		
		return mi;
	}

	/**
	 * Creates the '+' icon.
	 * 
	 * @return the '+' icon
	 */
	public static MaterialIcon createPlusIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 11, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 13, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 13, 11));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 11));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 13));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 13, 13));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 13, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 11, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 11, 13));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 13));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 11));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 11, 11));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		return mi;
	}

	/**
	 * Creates the typical save icon with the diskette.
	 * 
	 * @return the save icon
	 */
	public static MaterialIcon createSaveIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 2, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 2, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 2, 22, 4, 22));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 8, 22));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 8, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 16, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 16, 22));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 22));
		mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 22, 22, 22, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 22, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 18, 2));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 16, 2));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 16, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 2));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 2));
		mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 2, 2, 2, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 8, 2));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 10, 2));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 10, 8));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 8, 8));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		return mi;
	}
	
	/**
	 * Creates maximize button icon oftenly used in application's title bar
	 * 
	 * @return the maximize icon
	 */
	public static MaterialIcon createWindowMaximizeIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 4, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 18, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 18, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		return mi;
	}
	
	/**
	 * Creates maximize button icon oftenly used in application's title bar
	 * 
	 * @return the maximize icon
	 */
	public static MaterialIcon createWindowMinimizeIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 4, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		return mi;
	}

	/**
	 * Creates restore button icon oftenly used in application's title bar. Button
	 * is used to switch from maximized state to normal.
	 * 
	 * @return the restore icon
	 */
	public static MaterialIcon createWindowRestoreIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 4, 8));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 16, 20));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 16, 8));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 4, 8));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 14, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 14, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 8, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 4));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 20, 16));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 18, 16));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 18, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 8, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		return mi;
	}

	/**
	 * Creates icon similar to the YouTube product icon.
	 * 
	 * @return YouTube icon
	 */
	public static MaterialIcon createYouTubeIcon() {
		MaterialIcon mi = new MaterialIcon();
		
		mi.getPath().add(new MaterialIconPathCommand(CMD_MOVE_TO, 18, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 22, 6, 22, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 14, 12));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 10, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 10, 14));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 14, 12));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 22, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 22, 14));
		mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 22, 18, 18, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 6, 18));
		mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 2, 18, 2, 14));
		mi.getPath().add(new MaterialIconPathCommand(CMD_LINE_TO, 2, 10));
		mi.getPath().add(new MaterialIconPathCommand(CMD_QUAD_TO, 2, 6, 6, 6));
		mi.getPath().add(new MaterialIconPathCommand(CMD_CLOSE_PATH));
		
		return mi;
	}
}
