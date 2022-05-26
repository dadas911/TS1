package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.controller.ChessController;
import cz.cvut.fel.pjv.model.board.Board;
import cz.cvut.fel.pjv.model.game.Coordinates;
import cz.cvut.fel.pjv.model.game.PgnParser;
import cz.cvut.fel.pjv.model.pieces.Bishop;
import cz.cvut.fel.pjv.model.pieces.Pawn;
import cz.cvut.fel.pjv.model.pieces.Piece;
import cz.cvut.fel.pjv.model.pieces.Queen;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Main class where controller is initialized and game is started
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ChessController controller = new ChessController();
        controller.start();
    }


}
