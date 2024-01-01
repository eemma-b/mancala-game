package mancala;

public class PitNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public PitNotFoundException() {
        super("Pit not found!");
    }

    public PitNotFoundException(final String message) {
        super(message);
    }
}