package cz.cvut.fel.pjv.model.pieces;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PawnTest {

    Pawn pawn;
    static String warningMessage = "Validation of move was not correct";

    @BeforeEach
    void setUp() {
        pawn = new Pawn(6, 4, true);
    }

    @Test
    public void isValidMove_oneTileJump_trueReturned() {
        boolean result = pawn.isValidMove(6, 4, 5, 4);
        Assertions.assertTrue(result, warningMessage);
    }

    @Test
    public void isValidMove_twoTileJump_trueReturned() {
        boolean result = pawn.isValidMove(6, 4, 4, 4);
        Assertions.assertTrue(result, warningMessage);
    }

    @Test
    public void isValidMove_threeTileJump_trueReturned()
    {
        boolean result = pawn.isValidMove(6, 4, 3, 4);
        Assertions.assertFalse(result, warningMessage);
    }
}
