package mancala;

import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable {

    private static final long serialVersionUID = 1L;
    private MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    private boolean freeTurn = false;

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
   /* package */MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    /* package */boolean isSideEmpty(final int pitNum) {
       //Indicates whether one side of the board is empty.
        boolean isEmpty = true;
        int start;
        int end;
        if (pitNum >= 1 && pitNum <= 6) {
            start = 1;
            end = 6;
        } else {
            start = 7;
            end = 12;
        }
        for (int i = start; i <= end; i++) {
            if (gameBoard.getNumStones(i) > 0) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;    
    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    protected void setPlayer(final int playerNum) {
        currentPlayer = playerNum;
    }

    /**
     * Gets the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    protected int getPlayer() {
        return currentPlayer;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    /* package */abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    /* package */abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        final Store playerOneStore = new Store();
        final Store playerTwoStore = new Store();
        
        playerOneStore.setOwner(one);
        playerTwoStore.setOwner(two);
        
        one.setStore(playerOneStore);
        two.setStore(playerTwoStore);

        gameBoard.setStore(playerOneStore, 1);
        gameBoard.setStore(playerTwoStore, 2);
        /* make a new store in this method, set the owner
         then use the setStore(store,playerNum) method of the data structure*/
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }


    /**
     * @param free The free turn variable
     */
    public void setFreeTurn(final boolean free) {
        this.freeTurn = free;
    }


    /**
     * @return Returns if free turn is true or false
     */
    public boolean isFreeTurn() {
        return freeTurn;
    }

    @Override
    public String toString() {
        final StringBuilder theString = new StringBuilder();
        theString.append("Current Player: ").append(currentPlayer).append("\n");
        theString.append("Game Board: ").append("\n");
        for (int i = 1; i <= 12; i++) {
            theString.append("Pit ").append(i).append(": ").append(gameBoard.getNumStones(i)).append(" stones\n");
        }
        theString.append("Player 1 Store: ").append(gameBoard.getStoreCount(1)).append(" stones\n");
        theString.append("Player 2 Store: ").append(gameBoard.getStoreCount(2)).append(" stones\n");
        return theString.toString();
    }
}