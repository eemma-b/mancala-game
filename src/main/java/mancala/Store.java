package mancala;

import java.io.Serializable;

public class Store implements Serializable, Countable{

    private static final long serialVersionUID = 1L;
    private Player storeOwner = null;
    private int stoneTotal;

    // Initializes a new store.
    /* package */ Store() {
        this.stoneTotal = 0;
    }

    // Sets the number of stones in the store.
    protected void setTotalStones(final int stoneCount) {
        this.stoneTotal = stoneCount;
    }    

    // Sets the owner of the store.
    protected void setOwner(final Player player) {
        this.storeOwner = player;
    }

    // Gets the owner of the store.
    protected Player getOwner() {
        return this.storeOwner;
    }

    // Gets the total number of stones in the store.
    protected int getTotalStones() { 
        return this.stoneTotal;
    }

    // Adds stones to the store.
    @Override
    public void addStones(final int amount) {
        for (int i = 0; i < amount; i++) {
            stoneTotal++;
        }
    }

    // Empties the store and returns the number of stones that were in it.
    protected int emptyStore() {
        final int beforeEmptyStones = this.stoneTotal;
        this.stoneTotal = 0;
        return beforeEmptyStones;
    }

    @Override
    public String toString() {
        return "Store Owner: " + storeOwner.getName() + "\nTotal Stones In Store: " + stoneTotal;
    }

    @Override
    public void addStone() {
        this.stoneTotal++;
    }

    @Override
    public int getStoneCount() {
        return this.stoneTotal;
    }

    @Override
    public int removeStones() {
        final int stones = this.stoneTotal;
        this.stoneTotal = 0;
        return stones;
    }
}