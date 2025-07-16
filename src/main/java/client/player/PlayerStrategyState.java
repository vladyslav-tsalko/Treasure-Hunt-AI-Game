package client.player;

public enum PlayerStrategyState {
	EXPLORING_OWN_HALFMAP,
	GOING_TO_TREASURE,
	GOING_TO_ENEMY_HALFMAP,
	EXPLORING_ENEMY_HALFMAP,
	GOING_TO_FORT;
	
	public boolean isOnOwnHalfMap() {
		return (this == EXPLORING_OWN_HALFMAP || this == GOING_TO_TREASURE);
	}
	
	public boolean isOnEnemyHalfMap() {
		return (this == EXPLORING_ENEMY_HALFMAP || this == GOING_TO_FORT);
	}

	public boolean isExploringEnemyHalfMap() {
		return this == PlayerStrategyState.EXPLORING_ENEMY_HALFMAP;
	}

	public boolean isExploringOwnHalfMap() {
		return this == PlayerStrategyState.EXPLORING_OWN_HALFMAP;
	}

	public boolean isExploring(){
		return this.isExploringEnemyHalfMap() || this.isExploringOwnHalfMap();
	}
}
