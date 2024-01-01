package mancala;

public class KalahRules extends GameRules {

    private static final long serialVersionUID = 1L;
    private static final int PLAYER1PITEND = 6;
    private static final int PLAYER2PITEND = 12;
    private static final int PLAYERONE = 1;

    public KalahRules() {
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
        if (playerNum == PLAYERONE) {
            initialStones = this.getDataStructure().getStoreCount(1);
            this.setPlayer(1);
        } else {
            initialStones = this.getDataStructure().getStoreCount(2);
            this.setPlayer(2);
        }

        distributeStones(startPit);

        int finalStones;
        if (playerNum == PLAYERONE) {
            finalStones = this.getDataStructure().getStoreCount(1);
        } else {
            finalStones = this.getDataStructure().getStoreCount(2);
        }
        return finalStones - initialStones;
    }

    @Override
    /* package */int distributeStones(final int startPit) {
        int stonesAdded = 0;
        int currentPit = startPit;
        int stones = this.getDataStructure().removeStones(startPit);

        while (stones > 0) {
            if (shouldAddStoneToStore(stones, currentPit, startPit)) {
                addStoneToStore(startPit, currentPit);
                stonesAdded++;
                stones--;
                if (stones == 0) {
                    setFreeTurn(true);
                    break;
                }
            }
            currentPit = currentPit % 12 + 1; 

            if (shouldCaptureStones(stones, currentPit, startPit)) {
                captureStones(currentPit);
                addLastStoneToStore(currentPit);
                stones--;
            } else {
                this.getDataStructure().addStones(currentPit, 1);
                stones--;
                stonesAdded++;
            }
        }
        return stonesAdded;
    }

    @Override
    /* package */ int captureStones(final int stoppingPoint) {
        int capturedStones = this.getDataStructure().removeStones(stoppingPoint);
        final int oppositePit = 13 - stoppingPoint;
        capturedStones += this.getDataStructure().removeStones(oppositePit);
    
        int storeNum;
        if (stoppingPoint <= PLAYER1PITEND) {
            storeNum = 1;
        } else {
            storeNum = 2;
        }
        this.getDataStructure().addToStore(storeNum, capturedStones);
        return capturedStones;
    }    
    
    protected boolean shouldAddStoneToStore(final int stones, final int currentPit, final int startingPoint) {
        return stones > 0 && (currentPit == 6 && startingPoint <= 6 || currentPit == 12 && startingPoint > 6);
    }

    protected void addStoneToStore(final int startingPoint, final int currentPit) {
        if (currentPit == PLAYER1PITEND) {
            this.getDataStructure().addToStore(1, 1);
        } else if (currentPit == PLAYER2PITEND) {
            this.getDataStructure().addToStore(2, 1);
        }
    }    

    protected boolean shouldCaptureStones(final int stones, final int currentPit, final int startingPoint) {
        return stones == 1 && this.getDataStructure().getNumStones(currentPit) == 0 
            && (currentPit <= 6 && startingPoint <= 6 || currentPit > 6 && startingPoint > 6);
    }

    protected void addLastStoneToStore(final int currentPit) {
        if (currentPit <= PLAYER1PITEND) {
            this.getDataStructure().addToStore(1, 1);
        } else {
            this.getDataStructure().addToStore(2, 1);
        }
    }
}