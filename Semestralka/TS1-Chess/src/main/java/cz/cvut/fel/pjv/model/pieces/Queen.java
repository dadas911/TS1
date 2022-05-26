package cz.cvut.fel.pjv.model.pieces;

import cz.cvut.fel.pjv.model.game.Coordinates;

import java.util.Vector;

public class Queen extends Piece{

    public Queen(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
        this.type = "Queen";
    }

    @Override
    public String getPieceName()
    {
        if(isWhite)
        {
            return "whiteQueen";
        }

        return "blackQueen";
    }

    @Override
    public boolean isValidMove(int start_x, int start_y, int dest_x, int dest_y) {

        // Starting position is the same as destination
        if((start_x == dest_x) && (start_y == dest_y))
        {
            return false;
        }

        // Moving on the same lines (as Rook)
        boolean move_on_x = (start_x == dest_x);
        boolean move_on_y = (start_y == dest_y);

        // Moving diagonally (as Bishop)
        double offset_negative = start_x - start_y;
        double offset_positive = start_x + start_y;

        boolean diagonal_positive = (dest_x == dest_y + offset_negative);
        boolean diagonal_negative = (dest_x == -dest_y + offset_positive);

        return move_on_x || move_on_y || diagonal_negative || diagonal_positive ;
    }

    @Override
    public Vector<Coordinates> getAllValidPath(int start_x, int start_y, int dest_x, int dest_y) {
        Vector<Coordinates> result = new Vector<>(0);
        int x_steps = Math.abs(start_x - dest_x);
        int y_steps = Math.abs(start_y - dest_y);
        Coordinates coord;

        if(start_x == dest_x)
        {
            for(int i = 1; i < y_steps; i++)
            {
                if(start_y < dest_y)
                {
                    coord = new Coordinates(start_x, start_y + i);
                }
                else
                {
                    coord = new Coordinates(start_x, start_y - i);
                }
                result.add(coord);
            }
        }
        else if(start_y == dest_y)
        {
            for(int i = 1; i < x_steps; i++)
            {
                if(start_x < dest_x)
                {
                    coord = new Coordinates(start_x + i, start_y);
                }
                else
                {
                    coord = new Coordinates(start_x - i, start_y);
                }
                result.add(coord);
            }
        }
        else if(start_x < dest_x && start_y < dest_y)
        {
            for(int i = 1; i < x_steps; i++)
            {
                coord = new Coordinates(start_x + i, start_y + i);
                result.add(coord);
            }
        }
        else if(start_x > dest_x && start_y < dest_y)
        {
            for(int i = 1; i < x_steps; i++)
            {
                coord = new Coordinates(start_x - i, start_y + i);
                result.add(coord);
            }
        }
        else if(start_x < dest_x && start_y > dest_y)
        {
            for(int i = 1; i < x_steps; i++)
            {
                coord = new Coordinates(start_x + i, start_y - i);
                result.add(coord);
            }
        }
        else
        {
            for(int i = 1; i < x_steps; i++)
            {
                coord = new Coordinates(start_x - i, start_y - i);
                result.add(coord);
            }
        }

        return result;
    }
}
