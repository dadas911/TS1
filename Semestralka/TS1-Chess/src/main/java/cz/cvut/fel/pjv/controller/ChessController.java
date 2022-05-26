package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.board.Board;
import cz.cvut.fel.pjv.model.game.Move;
import cz.cvut.fel.pjv.model.game.MyTimer;
import cz.cvut.fel.pjv.model.game.PgnParser;
import cz.cvut.fel.pjv.model.pieces.Piece;
import cz.cvut.fel.pjv.model.player.ComputerPlayer;
import cz.cvut.fel.pjv.model.player.HumanPlayer;
import cz.cvut.fel.pjv.model.player.Player;
import cz.cvut.fel.pjv.view.ChessGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Logger;

import static javax.swing.SwingUtilities.isLeftMouseButton;

/**
 * Main controller of the game
 * Handles:
 * <ul>
 *     <li>Game loop</li>
 *     <li>Proper turns</li>
 *     <li>Attaching functions to actionListeners</li>
 *     <li>Proper piece movement</li>
 *     <li>Connects View(GUI) with Model(Board, Player etc...)</li>
 *     <li>Getting names from user</li>
 * </ul>
 */
public class ChessController {

    private Board board;
    private ChessGUI view;

    private Player whitePlayer;
    private Player blackPlayer;
    private Player currPlayer;

    private ChessGUI.ChessBoardTile startTile;
    private ChessGUI.ChessBoardTile destTile;

    private Piece startPiece;
    private Piece destPiece;

    private int start_x;
    private int start_y;
    private int dest_x;
    private int dest_y;

    private int white_score;
    private int black_score;

    private final int board_length = 8;
    private final int board_width = 8;

    private String fileName = "game";

    private MyTimer timer;

    private PgnParser parser;

    private static final Logger LOGGER = Logger.getLogger(ChessController.class.getName());

    /**
     * Function starts whole program
     */
    public void start()
    {
        // Initialize board (with standard layout) and GUI
        board = new Board();
        board.initializeStandardLayout();
        view = new ChessGUI(board);

        parser = new PgnParser(board);

        white_score = 0;
        black_score = 0;

        // Players init
        getUsername();

        view.setWhitePlayerName(whitePlayer.getName());
        view.setBlackPlayerName(blackPlayer.getName());

        // White player has the first turn
        currPlayer = whitePlayer;

        timer = new MyTimer(300d, 300d, this, this.view);
        timer.start();

        initializeTileActions();
        LOGGER.info("Game has started!");

        // Create actionPerformed functions for each actionListener
        view.resetListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tie -> point for both
                white_score++;
                black_score++;
                view.setWhitePlayerScore(white_score);
                view.setBlackPlayerScore(black_score);
                JOptionPane.showMessageDialog(view.getWindow(), "Tied");
                reset();
            }
        });

        view.forfeitWhitePlayerListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forfeitWhitePlayer();
            }
        });

        view.forfeitBlackPlayerListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forfeitBlackPlayer();
            }
        });

        view.saveGameListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    board.saveGame(fileName, view.getWhitePlayerName(), view.getBlackPlayerName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        view.loadGameListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadGame();
                } catch (IOException ex) {
                    LOGGER.warning("You entered invalid file name");
                    reset();
                }
            }
        });
    }


    /**
     * Attach action to every tile. This function also implements game loop and controls proper turns
     */
    private void initializeTileActions()
    {
        for(int row = 0; row < board_length; row++)
        {
            for(int col = 0; col < board_width; col++)
            {
                // Save current tile in variable
                ChessGUI.ChessBoardTile currTile = view.getChessBoardPanel().getTile(row, col);
                int tile_x = currTile.getX();
                int tile_y = currTile.getY();

                currTile.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if(isLeftMouseButton(e))
                        {
                            // Current player is human -> do normal turn
                            if(currPlayer instanceof HumanPlayer)
                            {
                                // If startTile is empty -> set it + set current piece if there is any
                                if(startTile == null)
                                {
                                    startTile = view.getChessBoardPanel().getTile(tile_x, tile_y);
                                    Piece currPiece = board.getPiece(tile_x, tile_y);

                                    // User clicked on empty tile -> reset startTile
                                    if(currPiece == null)
                                    {
                                        startTile = null;
                                    }
                                    // User clicked on tile with piece -> check if colors are the same
                                    else
                                    {
                                        // Colors are not the same -> warning and reset startTile
                                        if(currPlayer.getIsWhite() != currPiece.getIsWhite())
                                        {
                                            if(currPlayer.getIsWhite())
                                            {
                                                JOptionPane.showMessageDialog(view.getWindow(), "White player's turn");
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(view.getWindow(), "Black player's turn");
                                            }
                                            startTile = null;
                                        }
                                    }
                                }
                                // startTile is not empty -> set destTile
                                else
                                {
                                    destTile = view.getChessBoardPanel().getTile(tile_x, tile_y);
                                    int curr_start_x = startTile.getX();
                                    int curr_start_y = startTile.getY();
                                    int curr_dest_x = destTile.getX();
                                    int curr_dest_y = destTile.getY();

                                    // Assign pieces
                                    startPiece = board.getPiece(curr_start_x, curr_start_y);
                                    destPiece = board.getPiece(curr_dest_x, curr_dest_y);

                                    // Save move
                                    start_x = curr_start_x;
                                    start_y = curr_start_y;
                                    dest_x = curr_dest_x;
                                    dest_y = curr_dest_y;

                                    // Make a move and saves the bool value of success
                                    boolean was_valid_move = board.makeMove(curr_start_x, curr_start_y, curr_dest_x, curr_dest_y);

                                    // User did valid move
                                    if(was_valid_move)
                                    {
                                        // reset tiles
                                        startTile = null;
                                        destTile = null;

                                        board.checkPromotion();

                                        // Update view (icons), because Board has already changed
                                        view.updateIcons(board);

                                        // Display view
                                        view.getWindow().setVisible(true);

                                        boolean currColor = !currPlayer.getIsWhite();

                                        // Check game conditions
                                        boolean isCheck = board.isKingChecked(currColor);
                                        boolean isCheckMate = board.isCheckMate(currColor);
                                        boolean isStaleMate = board.isStaleMate(currColor);

                                        // Change current player -> next turn
                                        if(currPlayer.getIsWhite())
                                        {
                                            currPlayer = blackPlayer;
                                        }
                                        else
                                        {
                                            currPlayer = whitePlayer;
                                        }

                                        timer.changeSide();

                                        // Check for "check" condition
                                        if(isCheck && !isCheckMate)
                                        {
                                            LOGGER.warning("Check!");
                                            JOptionPane.showMessageDialog(view.getWindow(), "Check!");
                                        }
                                        else if(isCheckMate)
                                        {
                                            LOGGER.warning("Checkmate! Game over.");

                                            JOptionPane.showMessageDialog(view.getWindow(), "CheckMate!");
                                            try {
                                                board.saveGame(fileName, view.getWhitePlayerName(), view.getBlackPlayerName());
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }

                                            // End game
                                            if(!currPlayer.getIsWhite())
                                            {
                                                white_score++;
                                                view.setWhitePlayerScore(white_score);
                                            }
                                            else
                                            {
                                                black_score++;
                                                view.setBlackPlayerScore(black_score);
                                            }
                                            reset();
                                        }
                                        else if(isStaleMate)
                                        {
                                            LOGGER.warning("Stalemate!");
                                            JOptionPane.showMessageDialog(view.getWindow(), "Stalemate!");
                                            white_score++;
                                            black_score++;
                                            view.setWhitePlayerScore(white_score);
                                            view.setBlackPlayerScore(black_score);
                                            reset();
                                        }
                                    }
                                    // User did invalid move
                                    else
                                    {
                                        LOGGER.warning("Invalid move!");
                                        JOptionPane.showMessageDialog(view.getWindow(), "Invalid move!");
                                        startPiece = null;
                                        destPiece = null;
                                        startTile = null;
                                        destTile = null;
                                    }
                                }
                            }
                            // Computer's turn
                            else
                            {
                                Move computerMove = currPlayer.generateValidMove(board);

                                int comp_start_x = computerMove.getStartX();
                                int comp_start_y = computerMove.getStartY();
                                int comp_dest_x = computerMove.getDestX();
                                int comp_dest_y = computerMove.getDestY();

                                board.makeMove(comp_start_x, comp_start_y, comp_dest_x, comp_dest_y);

                                board.checkPromotion();

                                // Update view (icons), because Board has already changed
                                view.updateIcons(board);

                                // Display view
                                view.getWindow().setVisible(true);

                                boolean currColor = !currPlayer.getIsWhite();

                                // Check game conditions
                                boolean isCheck = board.isKingChecked(currColor);
                                boolean isCheckMate = board.isCheckMate(currColor);
                                boolean isStaleMate = board.isStaleMate(currColor);

                                // Change current player -> next turn
                                if(currPlayer.getIsWhite())
                                {
                                    currPlayer = blackPlayer;
                                }
                                else
                                {
                                    currPlayer = whitePlayer;
                                }

                                timer.changeSide();

                                // Check for "check" condition
                                if(isCheck && !isCheckMate)
                                {
                                    LOGGER.warning("Check!");
                                    JOptionPane.showMessageDialog(view.getWindow(), "Check!");
                                }
                                else if(isCheckMate)
                                {
                                    LOGGER.warning("Checkmate! Game over.");
                                    JOptionPane.showMessageDialog(view.getWindow(), "CheckMate!");
                                    try {
                                        board.saveGame(fileName, view.getWhitePlayerName(), view.getBlackPlayerName());
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }

                                    // End game
                                    if(!currPlayer.getIsWhite())
                                    {
                                        white_score++;
                                        view.setWhitePlayerScore(white_score);
                                    }
                                    else
                                    {
                                        black_score++;
                                        view.setBlackPlayerScore(black_score);
                                    }
                                    reset();
                                }
                                else if(isStaleMate)
                                {
                                    LOGGER.warning("Stalemate!");
                                    JOptionPane.showMessageDialog(view.getWindow(), "Stalemate!");
                                    white_score++;
                                    black_score++;
                                    view.setWhitePlayerScore(white_score);
                                    view.setBlackPlayerScore(black_score);
                                    reset();
                                }
                            }

                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
        }
    }

    /**
     * Function gets usernames for both users.
     * If no name is entered, the player will be set to ComputerPlayer
     */
    public void getUsername()
    {
        JTextField whitePlayerField = new JTextField();
        JTextField blackPlayerField = new JTextField();
        JTextField fileNameField = new JTextField();

        Object[] displayedMessage = {"White player username:", whitePlayerField, "Black player username:", blackPlayerField, "File name:", fileNameField};

        int return_code = JOptionPane.showConfirmDialog(view.getWindow(), displayedMessage, "Username", JOptionPane.OK_CANCEL_OPTION);

        if(return_code == JOptionPane.OK_OPTION)
        {
            String whitePlayerName = whitePlayerField.getText();
            String blackPlayerName = blackPlayerField.getText();
            String fileName = fileNameField.getText();

            // White player's name exception
            if(whitePlayerName.equals(""))
            {
                whitePlayer = new ComputerPlayer("White - computer", true);
            }
            else
            {
                whitePlayer = new HumanPlayer(whitePlayerName, true);
            }

            // Black player's name exception
            if(blackPlayerName.equals(""))
            {
                blackPlayer = new ComputerPlayer("Black - computer", false);
            }
            else
            {
                blackPlayer = new HumanPlayer(blackPlayerName, false);
            }

            if(!fileName.equals(""))
            {
                this.fileName = fileName;
            }
        }
        else
        {
            whitePlayer = new HumanPlayer("White Player", true);
            blackPlayer = new HumanPlayer("Black Player", false);
        }
    }

    /**
     * Resets whole game with timer
     */
    private void loadGame() throws IOException {
        String file = "";

        JTextField loadGameField = new JTextField();

        Object[] displayedMessage = {"Name of file (without .pgn):", loadGameField};

        int return_code = JOptionPane.showConfirmDialog(view.getWindow(), displayedMessage, "Load File", JOptionPane.OK_CANCEL_OPTION);

        if(return_code == JOptionPane.OK_OPTION) {
            String text = loadGameField.getText();

            // White player's name exception
            if (!text.equals("")) {
               file = text;
            } else {
                LOGGER.warning("You need to enter valid file name");
            }
        }

        if(!file.equals(""))
        {
            startPiece = null;
            destPiece = null;
            timer.end = true;

            board = new Board();
            board.initializeStandardLayout();
            parser = new PgnParser(board);
            boolean isWhitesTurn = parser.loadGame(file);

            if(isWhitesTurn)
            {
                System.out.println("Whites turn");
                currPlayer = whitePlayer;
            }
            else
            {
                System.out.println("Blacks turn");
                currPlayer = blackPlayer;
            }

            view.reset(board);

            board.printPlayedMoves();

            timer = new MyTimer(300d, 300d, this, this.view);
            timer.start();

            initializeTileActions();
            LOGGER.info("Game has loaded");
        }
    }


    private void reset()
    {
        board.printPlayedMoves();
        board.clearPlayedMoves();
        board.printPlayedMoves();

        currPlayer = whitePlayer;
        startPiece = null;
        destPiece = null;
        timer.end = true;
        board = new Board();
        board.initializeStandardLayout();
        view.reset(board);

        timer = new MyTimer(300d, 300d, this, this.view);
        timer.start();

        initializeTileActions();
        LOGGER.info("Game has restarted!");
    }

    private void forfeitWhitePlayer()
    {
        LOGGER.info("White player has forfeited.");
        JOptionPane.showMessageDialog(view.getWindow(), "Black player is the winner!");
        black_score++;
        view.setBlackPlayerScore(black_score);
        reset();
    }

    private void forfeitBlackPlayer()
    {
        LOGGER.info("Black player has forfeited.");
        JOptionPane.showMessageDialog(view.getWindow(), "White player is the winner!");
        white_score++;
        view.setWhitePlayerScore(white_score);
        reset();
    }

    /**
     * Determines what to do if time runs out
     * @param isWhiteSide
     */
    public void timeIsOut(boolean isWhiteSide)
    {
        if(isWhiteSide)
        {
            JOptionPane.showMessageDialog(view.getWindow(), "Time is up - Black player is the winner");
            black_score++;
            view.setBlackPlayerScore(black_score);
        }
        else
        {
            JOptionPane.showMessageDialog(view.getWindow(), "Time is up - White player is the winner");
            white_score++;
            view.setWhitePlayerScore(white_score);
        }

        reset();
    }

}
