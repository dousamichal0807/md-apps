package md.jgames.jchess.settings;

import java.util.HashMap;

public class Settings {
	private static final HashMap<String, String> settings = new HashMap<>();
	
	public String get(String k) {
		return settings.get(k);
	}
	public void set(String k, String v) {
		settings.put(k, v);
	}
}
