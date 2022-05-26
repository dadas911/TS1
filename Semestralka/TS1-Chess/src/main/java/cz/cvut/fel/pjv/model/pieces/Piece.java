package cz.cvut.fel.pjv.model.pieces;

import cz.cvut.fel.pjv.model.game.Coordinates;

import java.util.Vector;

/**
 * Universal piece, contains functions used across all pieces
 */
public abstract class Piece {
    int x;
    int y;
    boolean isWhite;
    String type;

    public Piece(int x, int y, boolean isWhite)
    {
        this.x = x;
        this.y = y;
        this.isWhite = isWhite;
    }

    public boolean getIsWhite()
    {
        return this.isWhite;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void move(int x, int y)
    {
        this.setX(x);
        this.setY(y);
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
       this.type = type;
    }

    public abstract String getPieceName();

    /**
     * Checks if the entered move is valid for the certain piece
     * @param start_x
     * @param start_y
     * @param dest_x
     * @param dest_y
     * @return
     */
    public abstract boolean isValidMove(int start_x, int start_y, int dest_x, int dest_y);

    /**
     * Get whole path from starting spot to destination.
     * @param start_x
     * @param start_y
     * @param dest_x
     * @param dest_y
     * @return
     */
    public abstract Vector<Coordinates> getAllValidPath(int start_x, int start_y, int dest_x, int dest_y);
}

