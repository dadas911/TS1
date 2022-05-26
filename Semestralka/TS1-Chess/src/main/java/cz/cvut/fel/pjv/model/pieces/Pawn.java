package cz.cvut.fel.pjv.model.pieces;

import cz.cvut.fel.pjv.model.game.Coordinates;

import java.util.Vector;

public class Pawn extends Piece {

    private final int starting_x;
    private final int starting_y;

    public Pawn(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
        this.starting_x = x;
        this.starting_y = y;
        this.type = "Pawn";
    }

    public int getStartingX()
    {
        return this.starting_x;
    }

    public int getStartingY()
    {
        return this.starting_y;
    }

    @Override
    public String getPieceName()
    {
        if(isWhite)
        {
            return "whitePawn";
        }

        return "blackPawn";
    }

    @Override
    public boolean isValidMove(int start_x, int start_y, int dest_x, int dest_y) {
        boolean move_by_one, move_by_two, move_diagonal;

        // Starting position is the same as destination
        if((start_x == dest_x) && (start_y == dest_y))
        {
            return false;
        }

        // White pawn
        if(this.getIsWhite())
        {
            move_by_one = ((start_x-1 == dest_x && start_y == dest_y));
            // Pawn is on starting tile -> can move by 2
            if(start_x == this.starting_x && start_y == this.starting_y)
            {
                move_by_two = ((start_x-2 == dest_x && start_y == dest_y));
            }
            else
            {
                move_by_two = false;
            }
            move_diagonal = (start_x-1 == dest_x && start_y-1 == dest_y) || (start_x-1 == dest_x && start_y+1 == dest_y);
        }
        // Black pawn
        else
        {
            move_by_one = ((start_x+1 == dest_x && start_y == dest_y));
            // Pawn is on starting tile -> can move by 2
            if(start_x == this.starting_x && start_y == this.starting_y)
            {
                move_by_two = ((start_x+2 == dest_x && start_y == dest_y));
            }
            else
            {
                move_by_two = false;
            }

            move_diagonal = (start_x+1 == dest_x && start_y-1 == dest_y) || (start_x+1 == dest_x && start_y+1 == dest_y);
        }

        return move_by_one || move_by_two || move_diagonal;
    }

    @Override
    public Vector<Coordinates> getAllValidPath(int start_x, int start_y, int dest_x, int dest_y) {
        Vector result = new Vector(0);

        for(int i = 1; i < Math.abs(start_x - dest_x); i++)
        {
            Coordinates coord;

            if(this.getIsWhite())
            {
                coord = new Coordinates(start_x - i, start_y);
            }
            else
            {
                coord = new Coordinates(start_x + i, start_y);
            }

            result.add(coord);
        }

        return result;
    }
}
