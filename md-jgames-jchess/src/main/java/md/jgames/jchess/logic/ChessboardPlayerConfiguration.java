package md.jgames.jchess.logic;

public final class ChessboardPlayerConfiguration {
	public static final int PLAYER_HUMAN = 0;
	public static final int PLAYER_COMPUTER = 1;
	
	private final int whitePlayerConfiguration, blackPlayerConfiguration;
	private final int engineSkillLevel;
	
	public int getWhitePlayerConfiguration() {
		return whitePlayerConfiguration;
	}
	public int getBlackPlayerConfiguration() {
		return blackPlayerConfiguration;
	}
	public int getEngineSkillLevel() {
		return engineSkillLevel;
	}
	
	public ChessboardPlayerConfiguration(final int whiteConfig, final int blackConfig, final int engineSkillLevel) {
		if ((whiteConfig != PLAYER_HUMAN && whiteConfig != PLAYER_COMPUTER) || (blackConfig != PLAYER_HUMAN && blackConfig != PLAYER_COMPUTER) || (engineSkillLevel < 0 || engineSkillLevel > 20))
			throw new IllegalArgumentException("Passed invalid values");
		if (whiteConfig == PLAYER_COMPUTER && blackConfig == PLAYER_COMPUTER)
			throw new IllegalArgumentException("Both players cannot be computers at once");
		this.whitePlayerConfiguration = whiteConfig;
		this.blackPlayerConfiguration = blackConfig;
		this.engineSkillLevel = engineSkillLevel;
	}
}
