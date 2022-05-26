package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.board.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * View of the whole game.
 * Creates whole GUI - board, tiles, icons etc...
 * Contains
 * <ul>
 *     <li>Main window - Board</li>
 *     <li>Menu</li>
 *     <li>Scoreboard</li>
 * </ul>
 */
public class ChessGUI {
    // Main frame and chess board
    private final JFrame window;
    private Board chessBoard;

    // Path to icons
    private String path = "src/main/java/cz/cvut/fel/pjv/icons/";

    // Board
    ChessBoardPanel board;

    // Menu
    JMenuBar menu;

    // Scoreboard
    JPanel scoreBoard;

    // Labels
    private JLabel whitePlayerNameLabel;
    private JLabel blackPlayerNameLabel;
    private JLabel whitePlayerScoreLabel;
    private JLabel blackPlayerScoreLabel;
    private JLabel whitePlayerTimeLabel;
    private JLabel blackPlayerTimeLabel;

    // Buttons
    private JButton resetButton;
    private JButton forfeitWhitePlayerButton;
    private JButton forfeitBlackPlayerButton;
    private JButton saveGameButton;
    private JButton loadGameButton;

    // Parameters variables
    private final int window_width = 600;
    private final int window_length = 700;
    private final int board_width = 400;
    private final int board_length = 350;

    public ChessGUI(Board chessBoard) {
        this.chessBoard = chessBoard;

        // Initialize frame and its parameters
        this.window = new JFrame("Chess - David Jansa");
        this.window.setLayout(new BorderLayout());
        this.window.setSize(window_width, window_length);
        this.window.setLayout(null);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setLocationRelativeTo(null);
        this.window.setResizable(false);

        // Create scoreboard + add to JFrame
        scoreBoard = new JPanel(new GridLayout(0, 6));
        scoreBoard.setBounds(0, 25, 600, 25);
        window.add(scoreBoard);

        // Create labels
        whitePlayerNameLabel = new JLabel("White Player ", SwingConstants.RIGHT);
        whitePlayerScoreLabel = new JLabel("0", SwingConstants.CENTER);
        whitePlayerTimeLabel = new JLabel("00:00", SwingConstants.LEFT);
        blackPlayerNameLabel = new JLabel("Black Player ", SwingConstants.RIGHT);
        blackPlayerScoreLabel = new JLabel("0", SwingConstants.CENTER);
        blackPlayerTimeLabel = new JLabel("00:00", SwingConstants.LEFT);

        // Adding labels to scoreboard
        scoreBoard.add(whitePlayerNameLabel);
        scoreBoard.add(whitePlayerScoreLabel);
        scoreBoard.add(whitePlayerTimeLabel);
        scoreBoard.add(blackPlayerNameLabel);
        scoreBoard.add(blackPlayerScoreLabel);
        scoreBoard.add(blackPlayerTimeLabel);

        // Create menu + add to JFrame
        menu = new JMenuBar();
        menu.setBounds(0, 0, 600, 25);
        window.add(menu);

        // Initialize buttons
        resetButton = new JButton("Reset Game");
        forfeitWhitePlayerButton = new JButton("Forfeit - White");
        forfeitBlackPlayerButton = new JButton("Forfeit - Black");
        loadGameButton = new JButton("Load Game");
        saveGameButton = new JButton("Save Game");

        // Add buttons to menu
        menu.add(resetButton);
        menu.add(forfeitWhitePlayerButton);
        menu.add(forfeitBlackPlayerButton);
        menu.add(loadGameButton);
        menu.add(saveGameButton);

        // Initialize chess board
        this.board = new ChessBoardPanel();
        this.window.add(this.board);
        this.board.setBounds(0, 50, 600, 600);

        // Set visible
        this.window.setVisible(true);
        this.board.revalidate();
    }

    /**
     * Class representing tile in chess board
     */
    public class ChessBoardTile extends JButton
    {
        // Coordination of tile
        private int x;
        private int y;
        String pieceName;

        // Constructor
        public ChessBoardTile(int x, int y)
        {
            super();
            this.x = x;
            this.y = y;
            // Set preferred dimension
            this.setPreferredSize(new Dimension(10, 10));
            // No margin between tiles
            Insets margin = new Insets(0, 0, 0, 0);
            this.setMargin(margin);
            // Assign color to tile
            this.choosePieceIcon(chessBoard, x, y);
            validate();
        }

        public int getX()
        {
            return this.x;
        }

        public int getY()
        {
            return this.y;
        }

        public String getPieceName()
        {
            return this.pieceName;
        }

        public void setPieceName(String name)
        {
            this.pieceName = name;
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            this.setBorderPainted(false);
            this.setOpaque(true);
            int x = this.getX();
            int y = this.getY();

            if((x + y) % 2 == 1)
            {
                this.setBackground(new Color(50, 50, 70));
            }
            else
            {
                this.setBackground(new Color(255, 255, 255));
            }
        }

        /**
         * Function chooses the correct icon for every piece
         * @param board
         * @param x
         * @param y
         */
        public void choosePieceIcon(Board board, int x, int y)
        {
            // Notify layout manager to remove components
            this.removeAll();

            if(board.getPiece(x, y) == null)
            {
                this.pieceName = "null";
                this.setIcon(null);
            }
            else
            {
                String name = board.getPiece(x, y).getPieceName();
                File file = new File(path+name+".gif");
                try {
                    BufferedImage gif = ImageIO.read(file);
                    ImageIcon icon = new ImageIcon(gif);
                    this.pieceName = name;
                    this.setIcon(icon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ChessBoardPanel extends JPanel
    {
        ChessBoardTile[][] tiles = new ChessBoardTile[8][8];

        public ChessBoardPanel()
        {
            super(new GridLayout(9, 9));

            String letters = "abcdefgh ";
            String numbers = " 87654321";

            for(int row = 0; row < 9; row++)
            {
                for(int column = 0; column < 9; column++)
                {
                    if(row == 0)
                    {
                        JPanel letterColumn = new JPanel();
                        letterColumn.add(new JLabel(String.valueOf(letters.charAt(column))));
                        this.add(letterColumn);
                    }
                    else if(column == 8)
                    {

                        JPanel numberRow = new JPanel();
                        numberRow.add(new JLabel(String.valueOf(numbers.charAt(row)), SwingConstants.CENTER));
                        this.add(numberRow);
                    }
                    else
                    {
                        ChessBoardTile newTile = new ChessBoardTile(row-1, column);
                        this.tiles[row-1][column] = newTile;
                        this.add(newTile);
                        validate();
                    }
                }
            }

            this.setPreferredSize(new Dimension(400, 350));
            //printChessBoard();
            validate();
        }

        public ChessBoardTile getTile(int x, int y)
        {
            return tiles[x][y];
        }

        private void printChessBoard()
        {
            for(int row = 0; row < 8; row++)
            {
                for(int column = 0; column < 8; column++)
                {
                    System.out.println(this.tiles[row][column].getPieceName() + " ");
                }
                System.out.println("\n");
            }
        }
    }


    public ChessBoardPanel getChessBoardPanel()
    {
        return this.board;
    }

    public JFrame getWindow()
    {
        return this.window;
    }

    // UPDATE/SET/RESET FUNCTIONS
    // Set names in labels
    public void setWhitePlayerName(String name)
    {
        whitePlayerNameLabel.setText(name);
    }
    public String getWhitePlayerName()
    {
        return whitePlayerNameLabel.getText();
    }

    public void setBlackPlayerName(String name)
    {
        blackPlayerNameLabel.setText(name);
    }
    public String getBlackPlayerName()
    {
        return blackPlayerNameLabel.getText();
    }

    // Set score in labels
    public void setWhitePlayerScore(int score)
    {
        whitePlayerScoreLabel.setText(Integer.toString(score));
    }

    public void setBlackPlayerScore(int score)
    {
        blackPlayerScoreLabel.setText(Integer.toString(score));
    }

    // Set timers
    public void setWhitePlayerTime(String time)
    {
        whitePlayerTimeLabel.setText("(" + time + ")");
    }

    public void setBlackPlayerTime(String time)
    {
        blackPlayerTimeLabel.setText("(" + time + ")");
    }


    /**
     * Restarts and redraw whole view
     * @param chessBoard
     */
    public void reset(Board chessBoard)
    {
        this.chessBoard = chessBoard;
        this.window.remove(board);
        this.board = new ChessBoardPanel();
        this.window.add(board);
        this.board.setBounds(0, 50, 600, 600);
        this.board.revalidate();
        this.window.setVisible(true);
    }

    public void updateIcons(Board board)
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                getChessBoardPanel().getTile(i, j).choosePieceIcon(board, i, j);
            }
        }
    }

    public void resetListener(ActionListener e)
    {
        resetButton.addActionListener(e);
    }

    public void forfeitWhitePlayerListener(ActionListener e)
    {
        forfeitWhitePlayerButton.addActionListener(e);
    }

    public void forfeitBlackPlayerListener(ActionListener e)
    {
        forfeitBlackPlayerButton.addActionListener(e);
    }

    public void saveGameListener(ActionListener e){
        saveGameButton.addActionListener(e);
    }

    public void loadGameListener(ActionListener e)
    {
        loadGameButton.addActionListener(e);
    }
}
