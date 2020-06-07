package mdlib.utils.langpack;

import java.io.InputStream;
import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LanguagePack {

    // Do not create any instances
    private LanguagePack() {
    }

    private static final TreeMap<String, String> dictionary = new TreeMap<>();

    public static final Pattern PATTERN_KEY = Pattern.compile("[A-Za-z0-9]+(:?\\.[A-Za-z0-9]+)*");

    /**
     * Returns unmodifiable map with all loaded phrases.
     *
     * @return unmodifiable dictionary
     * @see #load(InputStream)
     */
    public static SortedMap<String, String> dictionary() {
        return Collections.unmodifiableSortedMap(dictionary);
    }

    /**
     * Loads data from given {@link InputStream} into dictionary.
     *
     * @param inputStream input stream to load data from
     * @throws IllegalStateException if data from input stram are invalid
     * @throws NullPointerException  if {@code null} is passed insted of valid
     *                               {@link InputStream} instance.
     * @see #dictionary()
     */
    public static void load(final InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        TreeMap<String, String> temporary = new TreeMap<>();
        int lineNumber = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] splitted = line.split("=", 2);
                if (splitted.length != 2)
                    throw new IllegalStateException("Illegal data at line " + lineNumber + ": key and value are not separated by equal sign");
                String key = splitted[0].trim();
                String value = splitted[1].trim();
                Matcher keyMatcher = PATTERN_KEY.matcher(splitted[0]);
                if (!keyMatcher.matches())
                    throw new IllegalStateException("Illegal data at line " + lineNumber + ": invalid key");
                temporary.put(key, value);
            }
            lineNumber++;
        }
        dictionary.putAll(temporary);
    }
}
