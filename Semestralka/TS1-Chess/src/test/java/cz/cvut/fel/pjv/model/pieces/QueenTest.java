package cz.cvut.fel.pjv.model.pieces;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueenTest {

    Queen queen;
    static String warningMessage = "Validation of move was not correct";

    @BeforeEach
    void setUp()
    {
        queen = new Queen(0, 0, true);
    }

    @Test
    public void isValidMove_diagonalMove_trueReturned()
    {
        boolean result = queen.isValidMove(0, 0, 4, 4);
        Assertions.assertTrue(result, warningMessage);
    }

    @Test
    public void isValidMove_knightTypeMove_falseReturned()
    {
        boolean result = queen.isValidMove(0, 0, 2, 1);
        Assertions.assertFalse(result, warningMessage);
    }

    @Test
    public void isValidMove_samePosition_falseReturned()
    {
        boolean result = queen.isValidMove(0, 0, 0, 0);
        Assertions.assertFalse(result, warningMessage);
    }
}
