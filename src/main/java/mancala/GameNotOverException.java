package mancala;

public class GameNotOverException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public GameNotOverException() {
        super("Game not over!");
    }

    public GameNotOverException(final String message) {
        super(message);
    }
}