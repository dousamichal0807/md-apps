package md.jgames.jchess.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import md.jcore.IllegalStringFormatException;

public final class LanguagePack {

	private static HashMap<String, String> strings;

	public static String get(String key) {
		key = key.trim().toLowerCase();
		return strings.get(key);
	}

	public static void put(String key, String value) {
		key = key.trim().toLowerCase();
		if (!strings.containsKey(key))
			throw new IllegalArgumentException("\'" + key + "\' is not valid key.");
	}

	public static void load(String l) throws IOException, IllegalStringFormatException {
		load(new FileInputStream("langpacks" + File.separator + l + ".md-jchess-langpack"));
	}

	private static void load(FileInputStream fis) throws IllegalStringFormatException {
		Scanner sc = new Scanner(fis);
		while (sc.hasNext()) {
			String line = sc.nextLine().trim();
			if (!line.isEmpty()) {
				String[] lsplit = line.split("=", 1);
				if (lsplit.length != 2) {
					sc.close();
					throw new IllegalStringFormatException("Line \'" + line + "\' is incorrectly formatted!");
				}
				strings.put(lsplit[0].trim().toLowerCase(), lsplit[1].trim());
			}
		}
		sc.close();
	}

	public static void reset() {

	}
}
