package md.jgames.jchess.logic;

public class ChessboardPlayerConfiguration {
	public static final int PLAYER_HUMAN = 0,
			PLAYER_COMPUTER = 1;
	
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
	
	public ChessboardPlayerConfiguration(int w, int b, int esl) {
		if((w != PLAYER_HUMAN && w != PLAYER_COMPUTER) ||
				(b != PLAYER_HUMAN && b != PLAYER_COMPUTER) ||
				(esl < 0 || esl > 20))
			throw new IllegalArgumentException("Passed invalid values");
		this.whitePlayerConfiguration = w;
		this.blackPlayerConfiguration = b;
		this.engineSkillLevel = esl;
	}
}
