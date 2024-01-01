package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

import mancala.GameNotOverException;
import mancala.InvalidMoveException;
import mancala.MancalaGame;
import mancala.PitNotFoundException;
import mancala.Player;
import mancala.Saver;
import mancala.UserProfile;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.Box;

public class GraphicalUI extends JFrame {

    private JPanel gameContainer;
    private JMenuBar menuBar;
    private JButton[][] pits;
    private JButton player1StoreButton,player2StoreButton;
    JLabel player1Label,player2Label;
    JPanel player1Store,player2Store;
    private MancalaGame game;
    private JFileChooser fileChooser;
    private JLabel turnLabel; 
    Player player1 = new Player("Player 1");
    Player player2 = new Player("Player 2");
    private JLabel player1RecordLabel,player2RecordLabel; 
    private Saver saver = new Saver();

    public GraphicalUI() {
        this("Welcome to Mancala!");
    }

    public GraphicalUI(String title) {
        super();
        setUp(title);

        fileChooser = new JFileChooser(); 
        turnLabel = new JLabel();
        player1RecordLabel = new JLabel();
        player2RecordLabel = new JLabel();
        player1Label = new JLabel();
        player2Label = new JLabel();
        player1Store = new JPanel();
        player2Store = new JPanel();
        
        Object[] options = {"Kalah Rules", "Ayo Rules"};
        int n = JOptionPane.showOptionDialog(this,
            "Hey there, welcome to Mancala! To get started which rule set would you like to use for your Mancala Game?",
            "Choose Rule Set",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            -1);
        if (n == -1) {
            System.exit(0);
        } else if (n == JOptionPane.YES_OPTION) {
            game = new MancalaGame(1);
        } else if (n == JOptionPane.NO_OPTION) {
            game = new MancalaGame(2);
        }
        game.setPlayers(player1, player2);
        game.setCurrentPlayer(player1); 
        offerProfileOptions();
    
        setupGameContainer();
        add(gameContainer, BorderLayout.CENTER);
        add(makeButtonPanel(), BorderLayout.EAST);
        makeMenu();
        setJMenuBar(menuBar);

        updateRecordLabels();
        JPanel recordPanel = new JPanel();
        recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
 
        recordPanel.add(player1RecordLabel);
        recordPanel.add(Box.createVerticalStrut(20)); 
        recordPanel.add(player2RecordLabel);
        gameContainer.add(recordPanel, BorderLayout.SOUTH);

        pack();
        setSize(800, 350); 
        setLocationRelativeTo(null); 
    }    

    private void setUp(String title) {
        this.setTitle(title);
        gameContainer = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public void setupGameContainer() {
        gameContainer.add(makeMancalaBoard());
        gameContainer.setPreferredSize(new Dimension(600, 200)); 
    }

    private JPanel makeButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        return buttonPanel;
    }

    private JPanel makeMancalaBoard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        JPanel pitPanel = new JPanel(new GridLayout(2, 6));
        pits = new JButton[2][6];
    
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                int pitIndex;
                if (row == 0) {
                    pitIndex = 12 - col; // Reverse the order for the top row
                } else {
                    pitIndex = col + 1; // Keep the order for the bottom row
                }
                pits[row][col] = new JButton(); 
                pits[row][col].setActionCommand(Integer.toString(pitIndex));
                pits[row][col].addActionListener(e -> {
                    pitClicked(pitIndex);
                });
                JLabel pitLabel = new JLabel("Pit " + pitIndex, SwingConstants.CENTER);
                JPanel pitContainer = new JPanel(new BorderLayout());
                pitContainer.setPreferredSize(new Dimension(85, 85));
                pitContainer.add(pitLabel, BorderLayout.NORTH);
                pitContainer.add(pits[row][col], BorderLayout.CENTER);
                pitPanel.add(pitContainer);
            }
        }
    
        player1Store = new JPanel(new BorderLayout());
        player1Store.setPreferredSize(new Dimension(120, 160)); 
        String player1Name = player1.getName();
        if (player1Name.length() > 15) {
            player1Name = player1Name.substring(0, 15) + "<br>" + player1Name.substring(15);
        }
        player1Label = new JLabel("<html>" + player1Name + "'s Store</html>", SwingConstants.CENTER);
        player1StoreButton = new JButton("0"); 
        player1Store.add(player1Label, BorderLayout.NORTH);
        player1Store.add(player1StoreButton, BorderLayout.CENTER);

        player2Store = new JPanel(new BorderLayout());
        player2Store.setPreferredSize(new Dimension(120, 160));
        String player2Name = player2.getName();
        if (player2Name.length() > 15) {
            player2Name = player2Name.substring(0, 15) + "<br>" + player2Name.substring(15);
        }
        player2Label = new JLabel("<html>" + player2Name + "'s Store</html>", SwingConstants.CENTER);
        player2StoreButton = new JButton("0"); 
        player2Store.add(player2Label, BorderLayout.NORTH);
        player2Store.add(player2StoreButton, BorderLayout.CENTER);

        panel.add(pitPanel, BorderLayout.CENTER);
        panel.add(player1Store, BorderLayout.EAST);
        panel.add(player2Store, BorderLayout.WEST);

        updatePitCounts();
        updateStoreCounts();
        return panel;
    }    

    private void pitClicked(int pitIndex) {
        try {
            try {
                if (game.getNumStones(pitIndex) == 0) {
                    JOptionPane.showMessageDialog(null, "The selected pit is empty. Please select a different pit.");
                    return;
                }
            } catch (HeadlessException | PitNotFoundException e) {
                e.printStackTrace();
            }
            game.move(pitIndex);           
            if (game.isBonus()) {
                JOptionPane.showMessageDialog(null, "Bonus move! You get another turn!");
            }
  
            checkGameState();
            updateView();
            updateStoreCounts(); 
            updatePitCounts();
            updateTurnLabel();
        } catch (InvalidMoveException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Move!");
        }
    }
    
    private void updatePitCounts() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                int pitIndex;
                if (row == 0) {
                    pitIndex = 12 - col; 
                } else {
                    pitIndex = col + 1; 
                }
                int pitCount;
                try {
                    pitCount = game.getNumStones(pitIndex);
                } catch (PitNotFoundException e) {
                    pitCount = 0; 
                }
                pits[row][col].setText(Integer.toString(pitCount));
            }
        }
    }

    private void updateStoreCounts() {
        int player1StoreCount = game.getPlayer1().getStoreCount();
        int player2StoreCount = game.getPlayer2().getStoreCount();
        player1StoreButton.setText(Integer.toString(player1StoreCount));
        player2StoreButton.setText(Integer.toString(player2StoreCount));
        }

    protected void updateView() {
        for (int i = 1; i <= 12; i++) {
            try {
                int stonesInPit = game.getNumStones(i);
                pits[(i - 1) / 6][(i - 1) % 6].setText(Integer.toString(stonesInPit));
            } catch (PitNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Pit not found!");
            }
        }
    }    
    
    protected void newGame() {
        game.startNewGame();
        game.setPlayers(player1, player2);
        game.setCurrentPlayer(player1);
        updateTurnLabel();
        updatePitCounts();
        updateStoreCounts();
        updateView();
        updateRecordLabels();
    }

    private void checkGameState() {
        if (game.isGameOver()) {
             updateStoreCounts();
            updatePitCounts();
            Player winner;
            try {
                winner = game.getWinner();
                if (winner == null) {
                    JOptionPane.showMessageDialog(null, "Game Over, it's a tie!");
                } else {
                    JOptionPane.showMessageDialog(null, winner.getName() + " wins!");
                    if (winner == player1) {
                        if (game.getRuleSet() == 1) {
                            player1.getUserProfile().setKalahWins(player1.getUserProfile().getKalahWins() + 1);
                            player1.getUserProfile().setKalahGamesPlayed(player1.getUserProfile().getKalahGamesPlayed() + 1);
                        } else {
                            player1.getUserProfile().setAyoWins(player1.getUserProfile().getAyoWins() + 1);
                            player1.getUserProfile().setAyoGamesPlayed(player1.getUserProfile().getAyoGamesPlayed() + 1);
                        }
                    } else {
                        if (game.getRuleSet() == 1) {
                            player2.getUserProfile().setKalahWins(player2.getUserProfile().getKalahWins() + 1);
                            player2.getUserProfile().setKalahGamesPlayed(player2.getUserProfile().getKalahGamesPlayed() + 1);
                        } else {
                            player2.getUserProfile().setAyoWins(player2.getUserProfile().getAyoWins() + 1);
                            player2.getUserProfile().setAyoGamesPlayed(player2.getUserProfile().getAyoGamesPlayed() + 1);
                        }
                    }
                }
                updateRecordLabels();
    
                int response = JOptionPane.showConfirmDialog(null, "Game Over. Would you like to play again?", "Play Again?", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    newGame();  
                }
            } catch (GameNotOverException e) {
                JOptionPane.showMessageDialog(null, "Game is not yet over!");
            }
        }
    }

    private void updateRecordLabels() {
        UserProfile player1Profile = player1.getUserProfile();
        UserProfile player2Profile = player2.getUserProfile();
        player1RecordLabel.setText("<html><div style='text-align: center;'>" 
        + player1Profile.getName() + "<br/>Kalah Wins - " 
        + player1Profile.getKalahWins() 
        + " , Ayo Wins - " + player1Profile.getAyoWins() 
        + " , Kalah Games Played - " + player1Profile.getKalahGamesPlayed() 
        + " , Ayo Games Played - " + player1Profile.getAyoGamesPlayed() + "</div></html>");
        player2RecordLabel.setText("<html><div style='text-align: center;'>" 
        + player2Profile.getName() + "<br/>Kalah Wins - " 
        + player2Profile.getKalahWins() 
        + " , Ayo Wins - " + player2Profile.getAyoWins() 
        + " , Kalah Games Played - " + player2Profile.getKalahGamesPlayed() 
        + " , Ayo Games Played - " + player2Profile.getAyoGamesPlayed() + "</div></html>");
    }    

    private void offerProfileOptions() {
        Object[] profileOptions = {"Load Player Profile", "New Player Profile"};
        int profileChoice = JOptionPane.showOptionDialog(this,
        "<html><div style='text-align: center;'>Would you like to load or save a new player profile?<br/>(All game and user files will be saved in an 'assets' folder.<br/>If you do not have one, one will be created for you)</div></html>",
            "Player Profile",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            profileOptions,
            -1);
    
        if (profileChoice == JOptionPane.YES_OPTION) {
            loadUserProfile(); 
        } else if (profileChoice == JOptionPane.NO_OPTION) {
            saveUserProfile(); 
        }
    }

    private void makeMenu() {
        menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game Options");
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> newGame());
        
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener(e -> saveGame());
        
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener(e -> loadGame());
        
        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        menuBar.add(gameMenu);
    
        JMenu userMenu = new JMenu("User Options");
        
        JMenuItem saveProfileItem = new JMenuItem("Save Profile");
        saveProfileItem.addActionListener(e -> saveUserProfile());
        
        JMenuItem loadProfileItem = new JMenuItem("Load Profile");
        loadProfileItem.addActionListener(e -> loadUserProfile());
        
        userMenu.add(saveProfileItem);
        userMenu.add(loadProfileItem);
        menuBar.add(userMenu);
    
        JMenuItem emptyItem = new JMenuItem("");
        emptyItem.setEnabled(false);
        emptyItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, emptyItem.getPreferredSize().height));
    
        menuBar.add(emptyItem);
    
        turnLabel = new JLabel("Current turn: " + game.getCurrentPlayer().getName());
        menuBar.add(turnLabel);
    
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> confirmQuitGame());
    
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(quitButton);
        
        setJMenuBar(menuBar);
    }
    
    public void updateTurnLabel() {
        turnLabel.setText("Current turn: " + game.getCurrentPlayer().getName());
    }

    private void confirmQuitGame() {
        Object[] options = {"Save Profile", "Quit Without Saving"};
        int quitChoice = JOptionPane.showOptionDialog(
            this,
            "Would you like to save your player profile before quitting?",
            "Quit Game",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            -1);
    
        if (quitChoice == JOptionPane.YES_OPTION) {
            saveUserProfile(); 
            this.dispose();
        } else if (quitChoice == JOptionPane.NO_OPTION) {
            this.dispose();
        }
    }    

    protected void saveGame() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                saver.saveObject(game, selectedFile.getName());
                JOptionPane.showMessageDialog(null, "Game Saved!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected void loadGame() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                game = (MancalaGame) saver.loadObject(selectedFile.getName());
                JOptionPane.showMessageDialog(null, "Game Loaded!");
                updatePitCounts();
                updateStoreCounts();
                updateTurnLabel();
                updateRecordLabels();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected void saveUserProfile() {
        Object[] options = {"Player 2", "Player 1"};
        int playerChoice = JOptionPane.showOptionDialog(this,
            "Which player do you want to save? Player 1 or Player 2?",
            "Choose Player",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            -1);
    
        if (playerChoice == JOptionPane.YES_OPTION || playerChoice == JOptionPane.NO_OPTION) {
            int returnValue = fileChooser.showSaveDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    if (playerChoice == JOptionPane.YES_OPTION) {
                        player2.setName(selectedFile.getName());
                        saver.saveObject(player2.getUserProfile(), selectedFile.getName());
                        player2Label.setText(player2.getName() + "'s Store"); 
                        updateRecordLabels();
                        updateTurnLabel();
                    } else {
                        player1.setName(selectedFile.getName());
                        saver.saveObject(player1.getUserProfile(), selectedFile.getName());
                        player1Label.setText(player1.getName() + "'s Store"); 
                        updateRecordLabels();
                        updateTurnLabel();
                    }
                    JOptionPane.showMessageDialog(null, "User Profile Saved!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }    
    
    protected void loadUserProfile() {
        Object[] options = {"Player 2", "Player 1"};
        int playerChoice = JOptionPane.showOptionDialog(this,
            "Which player do you want to load? Player 1 or Player 2?",
            "Choose Player",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            -1);
    
        if (playerChoice == JOptionPane.YES_OPTION || playerChoice == JOptionPane.NO_OPTION) {
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    UserProfile userProfile = (UserProfile) saver.loadObject(selectedFile.getName());
                    if (playerChoice == JOptionPane.YES_OPTION) {
                        player2.setName(userProfile.getName());
                        player2.getUserProfile().setKalahWins(userProfile.getKalahWins());
                        player2.getUserProfile().setAyoWins(userProfile.getAyoWins());
                        player2.getUserProfile().setKalahGamesPlayed(userProfile.getKalahGamesPlayed());
                        player2.getUserProfile().setAyoGamesPlayed(userProfile.getAyoGamesPlayed());
                        player2Label.setText(player2.getName() + "'s Store");  
                        updateRecordLabels();
                        updateTurnLabel();
                    } else {
                        player1.setName(userProfile.getName());
                        player1.getUserProfile().setKalahWins(userProfile.getKalahWins());
                        player1.getUserProfile().setAyoWins(userProfile.getAyoWins());
                        player1.getUserProfile().setKalahGamesPlayed(userProfile.getKalahGamesPlayed());
                        player1.getUserProfile().setAyoGamesPlayed(userProfile.getAyoGamesPlayed());
                        player1Label.setText(player1.getName() + "'s Store"); 
                        updateRecordLabels();
                        updateTurnLabel();
                    }
                    JOptionPane.showMessageDialog(null, "User Profile Loaded!");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        GraphicalUI graphicalUI = new GraphicalUI();
        graphicalUI.setVisible(true);
    }
}