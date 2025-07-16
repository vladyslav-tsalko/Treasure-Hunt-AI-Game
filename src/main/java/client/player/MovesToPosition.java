package client.player;

import java.util.Objects;

import client.map.Position;

class MovesToPosition {
    private final Position startPosition;
    private final Move move;
    private int count;

    public MovesToPosition(Position startPosition, Move move, int count) {
        this.startPosition = startPosition;
        this.move = move;
        this.count = count;
    }

    public MovesToPosition() {
        this(null, null, 0);
    }

    Move getMove() {
        count--;
        if (count < 0) {
            throw new RuntimeException("Count can't be negative");
        }
        return move;
    }
    
    Position getStartPosition() {
    	return startPosition;
    }

    Position getEndPosition() {
        return startPosition.getNextPosition(move);
    }

    boolean isEmpty(){
        return count == 0;
    }

    int size(){
        return count;
    }

    String getMoves(){
        StringBuilder moves = new StringBuilder("[");
        for(int i = 0; i < count; i++){
            if(i > 0){
                moves.append(", ");
            }
            moves.append(move);
        }
        moves.append("]");
        return moves.toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(startPosition, move, count);
    }
    
    @Override
    public String toString() {
    	return (startPosition + " -> " + startPosition.getNextPosition(move) + ": " + getMoves() + ";");
    }
}