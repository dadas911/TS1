package cz.cvut.fel.pjv.model.game;

/**
 * Class that works like a structure for saving coordinates.
 * Stores x and y value.
 */
public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y)
    {
        this.x = x;
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

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
