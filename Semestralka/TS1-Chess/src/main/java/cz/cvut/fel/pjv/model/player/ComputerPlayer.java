package cz.cvut.fel.pjv.model.player;
import cz.cvut.fel.pjv.controller.ChessController;
import cz.cvut.fel.pjv.model.board.Board;
import cz.cvut.fel.pjv.model.game.Coordinates;
import cz.cvut.fel.pjv.model.game.Move;

import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Simplest AI that takes Board and returns valid move after generating random numbers every time.
 */
public class ComputerPlayer extends Player{
    private Random rand;
    private static final Logger LOGGER = Logger.getLogger(ChessController.class.getName());

    public ComputerPlayer(String name, boolean isWhiteSide) {
        super(name, isWhiteSide);
        rand = new Random();
    }

    /**
     * Generate random 4 numbers until it is valid move, then return it.
     * @param board
     * @return
     */
    public Move generateValidMove(Board board)
    {
        Vector<Coordinates> pieces = board.getAllPieces(this.isWhiteSide);
        int index = rand.nextInt(pieces.size());
        int start_x = pieces.get(index).getX();
        int start_y = pieces.get(index).getY();

        int dest_x = rand.nextInt(8);
        int dest_y = rand.nextInt(8);

        while(!board.canMakeMoveWithoutExposingKing(start_x, start_y, dest_x, dest_y))
        {
            index = rand.nextInt(pieces.size());

            start_x = pieces.get(index).getX();
            start_y = pieces.get(index).getY();

            dest_x = rand.nextInt(8);
            dest_y = rand.nextInt(8);
        }

        return new Move(start_x, start_y, dest_x, dest_y, board.getPiece(start_x, start_y), board.getPiece(dest_x, dest_y));
    }
}
