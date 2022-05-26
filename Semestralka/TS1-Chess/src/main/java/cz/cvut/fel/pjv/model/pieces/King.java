package cz.cvut.fel.pjv.model.pieces;

import cz.cvut.fel.pjv.model.game.Coordinates;

import java.util.Vector;

public class King extends Piece{

    private boolean hasMoved;

    public King(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
        hasMoved = false;
        this.type = "King";
    }

    @Override
    public String getPieceName()
    {
        if(isWhite)
        {
            return "whiteKing";
        }

        return "blackKing";
    }

    public boolean getHasMoved()
    {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean isValidMove(int start_x, int start_y, int dest_x, int dest_y) {

        // Starting position is the same as destination
        if((start_x == dest_x) && (start_y == dest_y))
        {
            return false;
        }

        return Math.abs(start_x - dest_x) <= 1 && Math.abs(start_y - dest_y) <= 1;
    }

    @Override
    public Vector<Coordinates> getAllValidPath(int start_x, int start_y, int dest_x, int dest_y) {
        Vector<Coordinates> result = new Vector<>(0);
        Coordinates coord;

        if(start_x == dest_x)
        {
            for(int i = 1; i < Math.abs(start_y - dest_y); i++)
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
        else
        {
            for(int i = 1; i < Math.abs(start_x - dest_x); i++)
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

        return result;
    }
}
