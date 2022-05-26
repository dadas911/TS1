package cz.cvut.fel.pjv.model.player;

import cz.cvut.fel.pjv.model.board.Board;
import cz.cvut.fel.pjv.model.game.Move;

/**
 * Abstract class for 2 types of players - HumanPlayer & ComputerPlayer
 * Contains name and color of a user.
 */
public abstract class Player {

    String name;
    boolean isWhiteSide;
    boolean isChecked;

    public Player(String name, boolean isWhiteSide)
    {
        this.name = name;
        this.isWhiteSide = isWhiteSide;
    }

    public void setIsWhite(boolean isWhiteSide)
    {
        this.isWhiteSide = isWhiteSide;
    }

    public boolean getIsWhite()
    {
        return this.isWhiteSide;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public abstract Move generateValidMove(Board board);
}
