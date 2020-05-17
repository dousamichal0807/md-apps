package md.jgames.jchess.logic;

import java.io.InputStream;
import java.util.Collections;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import md.jcore.debug.Debugger;

/**
 * Database of all loades {@link Opening}s.
 * 
 * @author Michal Douša
 */
public final class OpeningDatabase {

	private static final TreeSet<Opening> openings = new TreeSet<>();

	/**
	 * Returns editable set of all openings in the database
	 * 
	 * @return editable set of all openings in the database
	 */
	public static TreeSet<Opening> editableSet() {
		return openings;
	}

	/**
	 * Returns unmodifiable set of all openings that pass through the given filter
	 * 
	 * @param filter {@link Function}{@code <}{@link Opening}{@code , }{@link Boolean}{@code >}
	 *               to be used as filter
	 * @return the filtered items in an unmodifiable {@link SortedSet}
	 * 
	 * @throws NullPointerException if {@code null} is given as filter or the filter
	 *                              returns {@code null} instead of {@code true} or
	 *                              {@code false}
	 */
	public static SortedSet<Opening> filtered(Function<Opening, Boolean> filter) {
		// If filter == null, throw exception
		if (filter == null)
			throw new NullPointerException("Null passed instead of filter object");

		// TreeSet where we store openings than passed by filter
		TreeSet<Opening> filtered = new TreeSet<>();

		// Filter one by one
		openings.forEach(opening -> {
			// Check if the item passes the filter and if yes, then add it to the
			// filtered set
			if (filter.apply(opening))
				filtered.add(opening);
		});

		// Return unmodifiable set of filtered openings
		return Collections.unmodifiableSortedSet(filtered);
	}

	/**
	 * Loads tabulator-separated values (TSV) file given by {@code stream}
	 * parameter.
	 * 
	 * @param stream {@link InputStream} of the TSV file
	 */
	public static void loadTSV(InputStream stream) {
		// Stream cannot be null
		if (stream == null)
			throw new NullPointerException("Null passed as input stream");
		// Create scanner
		Scanner scanner = new Scanner(stream);
		// Read line by line until end of file reached
		for (int lineNum = 1; scanner.hasNextLine(); lineNum++) {
			// Read next line
			String line = scanner.nextLine();

			// Skip empty lines
			if (!line.isBlank()) {
				// Split by tabulator
				String[] splitted = line.split("\t");
				if (splitted.length != 3) {
					scanner.close();
					throw new IllegalStateException("Data on line " + lineNum + " are corrupted");
				}

				// Try to create opening from the available data
				try {
					// ECO code
					String eco = splitted[0];
					// Opening name
					String name = splitted[1];
					// Moves from initial position
					String[] moveStrings = splitted[2].split(" ");
					Move[] moves = new Move[moveStrings.length];
					for (int i = 0; i < moves.length; i++)
						moves[i] = new Move(moveStrings[i]);
					
					// Create opening
					Opening opening = new Opening(eco, name, moves);
					// Add the opening
					openings.add(opening);
					// Log that new opening was loaded
					Debugger.info(OpeningDatabase.class, "Loaded opening: " + opening);
				} catch (Exception exc) {
					// If given data is illegal
					scanner.close();
					throw new IllegalStateException("Data on line " + lineNum + " are corrupted", exc);
				}
			}
		}
		scanner.close();
	}
}
