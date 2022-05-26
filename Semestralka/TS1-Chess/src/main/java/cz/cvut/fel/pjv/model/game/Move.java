package cz.cvut.fel.pjv.model.game;

import cz.cvut.fel.pjv.model.pieces.Pawn;
import cz.cvut.fel.pjv.model.pieces.Piece;

/**
 * Keeps information about valid move.
 * Store coordinates of start and destination tile
 */
public class Move {
    private final int start_x;
    private final int start_y;
    private final int dest_x;
    private final int dest_y;
    private final Piece startPiece;
    private final Piece destPiece;
    private boolean canEnPassant;

    public Move(int start_x, int start_y, int dest_x, int dest_y, Piece startPiece, Piece destPiece)
    {
        this.start_x = start_x;
        this.start_y = start_y;
        this.dest_x = dest_x;
        this.dest_y = dest_y;
        this.startPiece = startPiece;
        this.destPiece = destPiece;
        this.canEnPassant = false;

        if(startPiece instanceof Pawn)
        {
            int pawn_start_x = ((Pawn) startPiece).getStartingX();
            if(Math.abs(pawn_start_x - dest_x) == 2)
            {
                this.canEnPassant = true;
            }
        }
    }

    public int getStartX()
    {
        return this.start_x;
    }

    public int getStartY()
    {
        return this.start_y;
    }

    public int getDestX()
    {
        return this.dest_x;
    }

    public int getDestY()
    {
        return this.dest_y;
    }

    public Piece getStartPiece()
    {
        return this.startPiece;
    }

    public Piece getDestPiece()
    {
        return this.destPiece;
    }

    public boolean getCanEnPassant()
    {
        return this.canEnPassant;
    }

    @Override
    public String toString() {
        return "Moved " + startPiece.getPieceName() + " from (" + start_x + ", " + start_y +
                ") to (" + dest_x + ", " + dest_y + ")";
    }
}