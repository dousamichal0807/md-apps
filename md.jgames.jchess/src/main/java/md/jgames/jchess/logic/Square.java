package md.jgames.jchess.logic;

import java.util.Objects;

public final class Square implements Comparable<Square> {

    private final byte rank, file;

    /**
     * Returns the rank number in range from 0 to 7 (inclusive).
     *
     * @return the rank number
     */
    public byte rank() {
        return rank;
    }

    /**
     * Returns the file index in range from 0 to 7 (inclusive), e.g. a &rarr; 0, b &rarr; 1, &hellip;, h &rarr; 7.
     *
     * @return the file index
     */
    public byte file() {
        return file;
    }

    @Override
    public int hashCode() {
        // Every unique square has to have unique hash code
        return 8 * rank + file;
    }

    /**
     * Returns same result as {@link #hashCode()} method, but casted to {@code byte}.
     *
     * @return hash code casted to {@code byte}
     */
    public byte byteHashCode() {
        return (byte) hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        // Return false if obj is not a Square object or it is null (instanceof operator returns false if obj is null,
        // too)
        if (!(obj instanceof Square))
            return false;

        // We have checked that obj is definitely a Square, we can cast it now:
        Square square = (Square) obj;

        // Are both ranks and both files equal?
        return this.file == square.file && this.rank == square.rank;
    }

    @Override
    public String toString() {
        // Reproduce standard notation of a chessboard square
        // Create StringBuilder with capacity of 2 characters
        StringBuilder stringBuilder = new StringBuilder(2);

        // Append file and rank
        stringBuilder.append((char) (file + 'a'));
        stringBuilder.append((char) (rank + '1'));

        // Return string representation of the square
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(final Square square) {
        // Simply compare hash codes
        return Byte.compare(this.byteHashCode(), square.byteHashCode());
    }

    /**
     * Creates a {@link Square} object from a {@link String} containing a letter from {@code 'a'} to {@code 'h'}
     * followed by a number from {@code '1'} to {@code '8'}
     *
     * @param square the character sequence as described above
     *
     * @throws IllegalArgumentException if illegal input is given
     */
    public Square(final String square) {
        // Require non-null string
        Objects.requireNonNull(square, "Received null as parameter");
        // Require non-empty string
        if (square.isBlank())
            throw new IllegalArgumentException("Received blank string as parameter");

        // Normalize string first, e.g. trim redundant whitespaces around the string using trim() method and convert it
        // to a lower-case string
        String normalized = square.trim().toLowerCase();

        // Every square a1, a2, ..., h7, h8 has exactly two characters
        if (normalized.length() != 2)
            throw new IllegalArgumentException("Invalid square notation: '" + square + "'");

        // First character is the file letter, from a to h
        // Convert file like 'a' -> 0, 'b' -> 1, ..., 'h' -> 7
        byte file = (byte) (normalized.charAt(0) - 'a');

        // Second character is rank number, from 1 to 8
        // Convert to integral value and start by zero: '1' -> 0, '2' -> 1, ..., '8' -> 7
        byte rank = (byte) (normalized.charAt(1) - '1');

        // But are we sure, that string is correct? We can check file and rank values:
        if (rank < 0 || rank > 7 || file < 0 || file > 7)
            throw new IllegalArgumentException("Invalid square notation: '" + square + "'");

        // Now we are sure that data are correct. Let's save them into the instance we're trying to create.
        this.rank = rank;
        this.file = file;
    }

    /**
     * Creates square from given rank and file index in range from 0 to 7 (inclusive).
     *
     * @param rank rank number
     * @param file file number
     *
     * @throws IllegalArgumentException if rank or file index is out of range
     */
    public Square(final byte rank, final byte file) {
        // check file and rank values:
        if (rank < 0 || rank > 7 || file < 0 || file > 7)
            throw new IllegalArgumentException("Rank or file out of range");

        // Now we are sure that data are correct. Let's save them into the instance we're trying to create.
        this.rank = rank;
        this.file = file;
    }

    /**
     * Creates square from given hash code
     *
     * @param hashCode hash code of given square
     *
     * @throws IllegalArgumentException if invalid hash code is given
     */
    public Square(final int hashCode) {
        if (hashCode < 0 || hashCode > 63)
            throw new IllegalArgumentException("Received invalid hash code of square");

        this.rank = (byte) (hashCode / 8);
        this.file = (byte) (hashCode % 8);
    }
}
