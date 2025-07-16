package client.map;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import client.player.Move;

public record Position(int x, int y) implements Comparable<Position> {
	static public Position getUninitializedPosition() {
		return new Position(-1, -1);
	}

	public boolean isInit(){
		return !this.equals(Position.getUninitializedPosition());
	}

	public Position() {
		this(0,0);
	}
	
	public Position(Position position) {
		this(position.x(), position.y());
	}
	
	public Set<Position> getAdjacentPositions(){
		Set<Position> adjacentPositions = new HashSet<>();
		for (int i = -1; i < 2; i++) {
        	for (int j = -1; j < 2; j++) {
        		if(i == 0 && j == 0) continue;
                adjacentPositions.add(new Position(this.x() + i, this.y() + j));
        	}
        }
		return adjacentPositions;
	}
	
	public int getListIndex(int MAP_WIDTH) {
		return this.x() + this.y()*MAP_WIDTH;
	}
	
	public Position getNextPosition(Move move) {
        return switch (move) {
            case UP -> new Position(this.x, this.y - 1);
            case DOWN -> new Position(this.x, this.y + 1);
            case LEFT -> new Position(this.x - 1, this.y);
            case RIGHT -> new Position(this.x + 1, this.y);
        };
	}
	
	@Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

	@Override
	public int compareTo(Position pos) {
		int compareRow = Integer.compare(this.y, pos.y);
		return compareRow != 0 ? compareRow: Integer.compare(this.x, pos.x);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return x == position.x && y == position.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
