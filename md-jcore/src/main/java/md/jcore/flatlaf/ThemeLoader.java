package md.jcore.flatlaf;

import java.io.InputStream;

public final class ThemeLoader {
	public static InputStream asInputStream(String themeID) {
		return ThemeLoader.class.getResourceAsStream(themeID + ".theme.json");
	}
}
