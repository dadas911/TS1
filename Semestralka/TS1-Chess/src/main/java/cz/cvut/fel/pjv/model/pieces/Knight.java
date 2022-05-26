package cz.cvut.fel.pjv.model.pieces;

import cz.cvut.fel.pjv.model.game.Coordinates;

import java.util.Vector;

public class Knight extends Piece{

    public Knight(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
        this.type = "Knight";
    }

    @Override
    public String getPieceName()
    {
        if(isWhite)
        {
            return "whiteKnight";
        }

        return "blackKnight";
    }

    @Override
    public boolean isValidMove(int start_x, int start_y, int dest_x, int dest_y) {

        // Starting position is the same as destination
        if((start_x == dest_x) && (start_y == dest_y))
        {
            return false;
        }

        // X value differs by 1, Y value by 2
        boolean first_move = (Math.abs(start_x - dest_x) == 1) && (Math.abs(start_y - dest_y) == 2);
        // X value differs by 2, Y value by 1
        boolean second_move = (Math.abs(start_x - dest_x) == 2) && (Math.abs(start_y - dest_y) == 1);

        return first_move || second_move;
    }

    @Override
    public Vector<Coordinates> getAllValidPath(int start_x, int start_y, int dest_x, int dest_y) {
        return new Vector<Coordinates>(0);
    }
}
