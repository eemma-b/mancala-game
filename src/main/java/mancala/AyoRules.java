package mancala;

public class AyoRules extends GameRules {
    
    private static final long serialVersionUID = 1L;
    private static final int PLAYER_ONE = 1;
    private static final int WRAP = 12;
    private static final int STORE = 6;

    public AyoRules() {
        super();
        resetBoard();
        setPlayer(1);
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
        if (startPit < 1 || startPit > 12) {
            throw new InvalidMoveException();
        }
        int initialStones;
        if (playerNum == PLAYER_ONE) {
            initialStones = this.getDataStructure().getStoreCount(1);
            this.setPlayer(1);
        } else {
            initialStones = this.getDataStructure().getStoreCount(2);
            this.setPlayer(2);
        }
        distributeStones(startPit);

        int finalStones;
        if (playerNum == PLAYER_ONE) {
            finalStones = this.getDataStructure().getStoreCount(1);
        } else {
            finalStones = this.getDataStructure().getStoreCount(2);
        }
        return finalStones - initialStones;
    }

    @Override
    /* package */ int distributeStones(final int startPit) {
        int stones = getDataStructure().removeStones(startPit);
        int stonesAdded = 0;
        int currentPit = startPit;
        getDataStructure().setIterator(startPit, getPlayer(), true);

        while (stones > 0) {
            final Countable pit = getDataStructure().next();
            currentPit++;
            
            if (stones == 1 && (currentPit == 7 && getPlayer() == 1 || currentPit == 13 && getPlayer() == 2)) {
                pit.addStone();
                stonesAdded++;
                break;
            }
        
            if (stones > 1 || currentPit != startPit) {
                pit.addStone();
                stonesAdded++;
                stones--;
            }
        
            if (stones == 0 && pit.getStoneCount() > 1 && currentPit != startPit && currentPit != 7 && currentPit != 14) {
                stones = pit.removeStones();
            }
        
            if (currentPit > WRAP) {
                currentPit = 1;
            }
        
            if (stones == 0 && pit.getStoneCount() == 1 && currentPit != startPit) {
                captureStones(currentPit);
            }
        }
        return stonesAdded;
    }

    @Override
    /* package */ int captureStones(final int stoppingPoint) {
        int capturedStones = 0;
        final int oppositePit = 13 - stoppingPoint;

        if (this.getDataStructure().getNumStones(stoppingPoint) == 1 && this.getDataStructure().getNumStones(oppositePit) > 0) {
            capturedStones += this.getDataStructure().removeStones(oppositePit);

            int storeNum;
            if (stoppingPoint <= STORE) {
                storeNum = 1;
            } else {
                storeNum = 2;
            }
            this.getDataStructure().addToStore(storeNum, capturedStones);
        }
        return capturedStones;
    }
}