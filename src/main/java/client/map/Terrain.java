package client.map;

public enum Terrain {
	EMPTY,
	GRASS,
	WATER,
	MOUNTAIN;
	
	private static final String ANSI_BROWN = "\u001B[33m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_RESET = "\u001B[0m";
	
	public boolean isAccessible() {
        return this != WATER;
    }
	
	@Override
    public String toString() {
        return switch (this) {
            case GRASS -> ANSI_GREEN + "  GRASS  " + ANSI_RESET;
            case WATER -> ANSI_BLUE + "  WATER  " + ANSI_RESET;
            case MOUNTAIN -> ANSI_BROWN + "  MOUNT  " + ANSI_RESET;
            case EMPTY -> throw new EmptyTerrainException();
        };
    }
}
