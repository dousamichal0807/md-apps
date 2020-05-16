package md.jgames.jchess.settings;

import java.util.HashMap;

public class Settings {
	private static HashMap<String, String> settings = new HashMap<String, String>();
	
	public String get(String k) {
		return settings.get(k);
	}
	public void set(String k, String v) {
		settings.put(k, v);
	}
}
