package md.jgames.jchess.logic;

public final class ChessPlayerConfiguration {

    private final ChessPlayerType whitePlayer, blackPlayer;
    private final int engineSkillLevel;

    public ChessPlayerType whitePlayer() {
        return whitePlayer;
    }
    public ChessPlayerType blackPlayer() {
        return blackPlayer;
    }
    public int engineSkillLevel() {
        return engineSkillLevel;
    }

    public ChessPlayerConfiguration(final ChessPlayerType white, final ChessPlayerType black, final int engineSkillLevel) {
        // Is engine's skill level in range between 0 and 20 (inclusive)?
        if (engineSkillLevel < 0 || engineSkillLevel > 20)
            throw new IllegalArgumentException("Invalid engine skill level");

        // Computer must play with a human, not another computer
        if (white == ChessPlayerType.COMPUTER && black == ChessPlayerType.COMPUTER)
            throw new IllegalArgumentException("Both players cannot be computers at once");

        // Assign values
        this.whitePlayer = white;
        this.blackPlayer = black;
        this.engineSkillLevel = engineSkillLevel;
    }
}
