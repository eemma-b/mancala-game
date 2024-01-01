package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable {
    
    private static final long serialVersionUID = 1L;
    private int pitStones;

    // Initializes a new pit.
    public Pit() {
        this.pitStones = 0;
    }

    // Sets the number of stones in the pit.
    public void setStoneCount(final int stoneCount) {
        this.pitStones = stoneCount;
    }

    // Gets the number of stones in the pit.
    @Override
    public int getStoneCount() {
        return this.pitStones;
    }
    
    // Adds a stone to the pit.
    @Override
    public void addStone() {
        this.pitStones = this.pitStones + 1;
    }

    // Removes and returns the stones from the pit.
    @Override
    public int removeStones() {
        final int removedStone = this.pitStones;
        this.pitStones = 0;
        return removedStone;
    }

    @Override
    public String toString() {
        return "Pit Stones In Pit: " + pitStones;
    }

    @Override
    public void addStones(final int numToAdd) {
        this.pitStones += numToAdd;
    }
}