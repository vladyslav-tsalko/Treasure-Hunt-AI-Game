package client.server.player_state;

public class PlayerServerStatus {
	private boolean isTreasureCollected;
	private PlayerActState playerActState;
	
	public PlayerServerStatus() {
		this.isTreasureCollected = false;
		this.playerActState = PlayerActState.MUST_WAIT;
	}
	
	public PlayerServerStatus(boolean isTreasureFound, PlayerActState playerActState) {
		this.isTreasureCollected = isTreasureFound;
		this.playerActState = playerActState;
	}
	
	public void update(PlayerServerStatus playerServerStatus) {
		this.isTreasureCollected = playerServerStatus.isTreasureCollected;
		this.playerActState = playerServerStatus.playerActState;
	}

	public boolean isTreasureCollected() {
		return this.isTreasureCollected;
	}

	public boolean mustWait() {
		return (this.playerActState == PlayerActState.MUST_WAIT);
	}
	
	public boolean isLost() {
		return (this.playerActState == PlayerActState.LOST);
	}
	
	public boolean isWon() {
		return (this.playerActState == PlayerActState.WON);
	}

	public boolean isGameOver() {
		return (isWon() || isLost());
	}
}
