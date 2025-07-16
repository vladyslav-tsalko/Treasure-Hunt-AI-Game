package client.map;

public class EmptyTerrainException extends RuntimeException {
    public EmptyTerrainException() {
        super("Unexpected empty terrain!");
    }
}
