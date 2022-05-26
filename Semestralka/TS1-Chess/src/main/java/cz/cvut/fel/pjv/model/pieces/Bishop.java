package cz.cvut.fel.pjv.model.pieces;

import cz.cvut.fel.pjv.model.game.Coordinates;

import java.util.Vector;

public class Bishop extends Piece{

    public Bishop(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
        this.type = "Bishop";
    }

    @Override
    public String getPieceName()
    {
        if(isWhite)
        {
            return "whiteBishop";
        }

        return "blackBishop";
    }


    @Override
    public boolean isValidMove(int start_x, int start_y, int dest_x, int dest_y) {

        // Starting position is the same as destination
        if((start_x == dest_x) && (start_y == dest_y))
        {
            return false;
        }


        double offset_negative = start_x - start_y;
        double offset_positive = start_x + start_y;

        boolean diagonal_positive = (dest_x == dest_y + offset_negative);
        boolean diagonal_negative = (dest_x == -dest_y + offset_positive);

        return diagonal_positive || diagonal_negative;
    }

    @Override
    public Vector<Coordinates> getAllValidPath(int start_x, int start_y, int dest_x, int dest_y) {
        Vector<Coordinates> result = new Vector<>(0);
        int steps = Math.abs(start_x - dest_x);
        Coordinates coord;

        if(start_x < dest_x && start_y < dest_y)
        {
            for(int i = 1; i < steps; i++)
            {
                coord = new Coordinates(start_x + i, start_y + i);
                result.add(coord);
            }
        }
        else if(start_x > dest_x && start_y < dest_y)
        {
            for(int i = 1; i < steps; i++)
            {
                coord = new Coordinates(start_x - i, start_y + i);
                result.add(coord);
            }
        }
        else if(start_x < dest_x && start_y > dest_y)
        {
            for(int i = 1; i < steps; i++)
            {
                coord = new Coordinates(start_x + i, start_y - i);
                result.add(coord);
            }
        }
        else
        {
            for(int i = 1; i < steps; i++)
            {
                coord = new Coordinates(start_x - i, start_y - i);
                result.add(coord);
            }
        }

        return result;
    }
}
