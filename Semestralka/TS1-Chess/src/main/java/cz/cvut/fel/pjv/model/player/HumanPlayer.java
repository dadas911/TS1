package cz.cvut.fel.pjv.model.player;

import cz.cvut.fel.pjv.model.board.Board;
import cz.cvut.fel.pjv.model.game.Move;
import cz.cvut.fel.pjv.model.pieces.Piece;

/**
 * Class for human player, doesn't vary from abstract class
 */
public class HumanPlayer extends Player{

    public HumanPlayer(String name, boolean isWhiteSide) {
        super(name, isWhiteSide);
    }

    @Override
    public Move generateValidMove(Board board) {
        return null;
    }


}
