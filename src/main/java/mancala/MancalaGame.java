package mancala;

import java.io.Serializable;

public class MancalaGame implements Serializable {

    private static final long serialVersionUID = 1L;
    private GameRules theBoard;
    private Player currentPlayer = null;
    private static final int KALAH = 1;
    private static final int AYO = 2;
    private boolean bonusMove = false;
    private Player playerOne;
    private Player playerTwo;
    private int ruleSetSetting = 0;

    public MancalaGame() {
        this(1);
    }

    /**
     * @param rules Either 1 or 2, 1 for Kalah and 2 for Ayoayo
     */
    public MancalaGame(final int rules) {
        if (rules == KALAH) {
            theBoard = new KalahRules();
            ruleSetSetting = 1;
        } else if (rules == AYO) {
            theBoard = new AyoRules();
            ruleSetSetting = 2;
        }
    }

    /**
     * @param ruleSetSetting Setting what the rule set is, used as a flag in constructor 
     */
    public void setRuleSet(final int ruleSetSetting){
        this.ruleSetSetting = ruleSetSetting;
    }

    /**
     * @return Retruns rule set to use in GUI for the winners wins and games played for that rule set 
     */
    public int getRuleSet(){
        return ruleSetSetting;
    }

    /**
     * @param player Current player 
     */
    public void setCurrentPlayer(final Player player) {
        this.currentPlayer = player;
    }

    /**
     * @return Returns the current player 
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * @param onePlayer Player one
     * @param twoPlayer Player two
     */
    public void setPlayers(final Player onePlayer, final Player twoPlayer) {
        theBoard.registerPlayers(onePlayer, twoPlayer);
        playerOne = onePlayer;
        playerTwo = twoPlayer;
        currentPlayer = onePlayer;
    }

    /**
     * @return Returns player one
     */
    public Player getPlayer1() {
        return playerOne;
    }

    /**
     * @return Returns player two
     */
    public Player getPlayer2() {
        return playerTwo;
    }

    /**
     * @return Returns the game board of type GameRules
     */
    public GameRules getBoard() {
        return this.theBoard;
    }

    /**
     * @param pitNum Pit number
     * @return Returns the number of stones in the pit
     * @throws PitNotFoundException
     */
    public int getNumStones(final int pitNum) throws PitNotFoundException {
        if (pitNum < 1 || pitNum > 12) {
            throw new PitNotFoundException();
        }
        return theBoard.getNumStones(pitNum);
    }

    /**
     * @param player The player 
     * @return Returns the store count of the current player
     * @throws NoSuchPlayerException
     */
    public int getStoreCount(final Player player) throws NoSuchPlayerException {
        if (player.equals(currentPlayer)) {
            return player.getStoreCount();
        } else {
            throw new NoSuchPlayerException();
        }
    }

    /**
     * @return Returns the Player winner 
     * @throws GameNotOverException
     */
    public Player getWinner() throws GameNotOverException {
        if (!isGameOver()) {
            throw new GameNotOverException();
        }
        final int player1StoreCount = playerOne.getStoreCount();
        final int player2StoreCount = playerTwo.getStoreCount();
        
        Player winner; 
        if (player1StoreCount > player2StoreCount) {
            winner = playerOne;
        } else if (player2StoreCount > player1StoreCount) {
            winner = playerTwo; 
        } else {
            winner = null;
        }
        return winner;
    }

    /**
     * @return Returns true if game over, false if not over
     */
    public boolean isGameOver() {
        final boolean gameOver = theBoard.isSideEmpty(1) || theBoard.isSideEmpty(7);
        if (gameOver) {
            distributeRemainingStones();
        }
        return gameOver;
    }
    

    private void distributeRemainingStones() {
        if (theBoard.isSideEmpty(1)) {
            for (int i = 7; i <= 12; i++) {
                final int stones = theBoard.getDataStructure().removeStones(i);
                theBoard.getDataStructure().addToStore(2, stones);
            }
        } else if (theBoard.isSideEmpty(7)) {
            for (int i = 1; i <= 6; i++) {
                final int stones = theBoard.getDataStructure().removeStones(i);
                theBoard.getDataStructure().addToStore(1, stones);
            }
        }
    }    

    /**
     * Method for switching the players turn if a free turn happens
     */
    public void switchPlayersTurn() {
        if (theBoard.isFreeTurn()) {
            setBonus(true);
        } else {
            if (currentPlayer.equals(playerOne)) {
                currentPlayer = playerTwo;
            } else {
                currentPlayer = playerOne;
            }
            setBonus(false);
        }
        theBoard.setFreeTurn(false);
    }
    
    /**
     * @param bonusMove Takes the true or false bonus move 
     */
    public void setBonus(final boolean bonusMove) {
        this.bonusMove = bonusMove;
    }

    /**
     * @return Returns true if theres a bonus move, false if not
     */
    public boolean isBonus() {
        return this.bonusMove;
    }

    /**
     * @param startPit The starting pit
     * @return Returns the total stones total number of stones remaining in the players side pits
     * @throws InvalidMoveException
     */
    public int move(final int startPit) throws InvalidMoveException {
        if (startPit < 1 || startPit > 12) {
            throw new InvalidMoveException();
        }
        int playerNum;
        if (currentPlayer.equals(playerOne) && startPit >= 1 && startPit <= 6) { 
            playerNum = 1;
        } else if (currentPlayer.equals(playerTwo) && startPit >= 7 && startPit <= 12) { 
            playerNum = 2;
        } else {
            throw new InvalidMoveException();
        }
        theBoard.moveStones(startPit, playerNum);   
        switchPlayersTurn();
            
        int totalStones = 0;
        for (int i = 1; i <= 12; i++) {
            totalStones += theBoard.getNumStones(i);
        }
        
        if (isGameOver()) {
            return totalStones;
        } else {
            return totalStones - theBoard.getNumStones(startPit);
        }
    }

    /**
     * Starts a new game by resetting the board
     */
    public void startNewGame() {
        theBoard.resetBoard();
    }

    @Override
    public String toString() {
        String result = "";
        result += "Current Player: " + currentPlayer.getName() + "\n";
        result += "Game Board: " + theBoard.toString() + "\n";
        result += "Player: " + playerOne.getName() + ", Store Count: " + playerOne.getStoreCount() + "\n";
        result += "Player: " + playerTwo.getName() + ", Store Count: " + playerTwo.getStoreCount() + "\n";
        return result;
    }
}