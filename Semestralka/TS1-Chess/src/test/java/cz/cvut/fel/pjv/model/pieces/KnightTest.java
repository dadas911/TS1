package cz.cvut.fel.pjv.model.pieces;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KnightTest {
    Knight knight;
    static String warningMessage = "Validation of move was not correct";

    @BeforeEach
    void setUp()
    {
        knight = new Knight(0, 0, true);
    }

    @Test
    public void isValidMove_correctMove_trueReturned()
    {
        boolean result = knight.isValidMove(0, 0, 2, 1);
        Assertions.assertTrue(result, warningMessage);
    }

    @Test
    public void isValidMove_knightTypeMove_falseReturned()
    {
        boolean result = knight.isValidMove(0, 0, 2, 0);
        Assertions.assertFalse(result, warningMessage);
    }
}
