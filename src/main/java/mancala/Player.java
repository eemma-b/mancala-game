package mancala;

import java.io.Serializable;

public class Player implements Serializable{

    private static final long serialVersionUID = 1L;
    private Store playersStore;
    private String playerName;
    final private UserProfile userProfile; 

    // Initializes a new player.
    public Player() {
        this.playerName = " ";
        this.playersStore = new Store(); 
        this.userProfile = new UserProfile(this.playerName);
    }

    // Initializes a new player with a name.
    public Player(final String name) {
        this.playerName = name;
        this.playersStore = new Store(); 
        this.userProfile = new UserProfile(this.playerName); 
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
    // Sets the player's name.
    public void setName(final String name) {
        this.playerName = name;
        this.userProfile.setName(name); 
    }

    // Sets the player's store.
    protected void setStore(final Store store) {
        this.playersStore = store;
    }

    // Gets the name of the player.
    public String getName() {
        return playerName;
    }

    // Gets the player's store where they collect stones.
    protected Store getStore() {
        return playersStore;
    }
    
    // Gets the count of the number of stones in the player's store where they collect stones.
    public int getStoreCount() {
        return playersStore.getTotalStones();
    }

    @Override
    public String toString() {
        return "Player: " + playerName + "\nStore Count: " + getStoreCount();
    }
}
