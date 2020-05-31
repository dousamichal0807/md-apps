package md.jgames.jchess.settings;

import java.util.HashMap;

public class Settings {
	private static final HashMap<String, String> settings = new HashMap<>();
	
	public String get(final String k) {
		return settings.get(k);
	}
	public void set(final String k, final String v) {
		settings.put(k, v);
	}
}
