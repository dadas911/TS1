package cz.cvut.fel.pjv.model.board;

import cz.cvut.fel.pjv.model.game.Coordinates;
import cz.cvut.fel.pjv.model.pieces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BoardTest {

    Board board;
    static String warningMessageObstacle = "Wrong detection of obstacle";
    static String warningMessageEscape = "Wrong detection king escape";


    @BeforeEach
    void setUp()
    {
        board = new Board();
    }

    @Test
    public void noObstacleOnPath_noObstacle_trueReturned()
    {
        board.setPiece(0, 0, new Queen(0, 0, true));
        board.setPiece(0, 4, new Queen(0, 4, false));
        boolean result = board.noObstacleOnPath(0, 0, 7, 7);
        Assertions.assertTrue(result, warningMessageObstacle);
    }

    @Test
    public void noObstacleOnPath_oneObstacle_falseReturned()
    {
        board.setPiece(0, 0, new Queen(0, 0, true));
        board.setPiece(0, 4, new Queen(0, 4, false));
        boolean result = board.noObstacleOnPath(0, 0, 0, 7);
        Assertions.assertFalse(result, warningMessageObstacle);
    }

    @Test
    public void canKingEscape_cannotEscape_falseReturned()
    {
        board.setPiece(0, 0, new King(0, 0, false));
        board.setBlackKingCoordinates(new Coordinates(0, 0));
        board.setPiece(7,0, new Rook(7, 0, true));
        board.setPiece(7,1, new Rook(7, 0, true));
        boolean result = board.canKingEscape(false);
        Assertions.assertFalse(result, warningMessageEscape);
    }

    @Test
    public void canKingEscape_canEscape_trueReturned()
    {
        board.setPiece(0, 0, new King(0, 0, false));
        board.setBlackKingCoordinates(new Coordinates(0, 0));
        board.setPiece(7,0, new Rook(7, 0, true));
        boolean result = board.canKingEscape(false);
        Assertions.assertTrue(result, warningMessageEscape);
    }

    // ---------- MOCKITO ----------
    @Test
    public void isNotOutOfRange_outOfRangeButValid_trueReturned()
    {
        board = Mockito.mock(Board.class);
        Mockito.when(board.isNotOutOfRange(-444, -222)).thenReturn(true);
        Assertions.assertTrue(board.isNotOutOfRange(-444, -222));
    }

    @Test
    public void isNotOutOfRange_validMoveButOutOfRange_falseReturned()
    {
        board = Mockito.mock(Board.class);
        Mockito.when(board.isNotOutOfRange(2, 2)).thenReturn(false);
        Assertions.assertFalse(board.isNotOutOfRange(2, 2));
    }

    @Test
    public void verify_setPieceInInitializingBoard_invokedEightTimes_trueReturned()
    {
        board = Mockito.mock(Board.class);
        board.setPiece(0, 0, new Pawn(0, 0, true));
        board.setPiece(0, 1, new Pawn(0, 0, true));
        board.setPiece(0, 2, new Pawn(0, 0, true));
        board.setPiece(0, 3, new Pawn(0, 0, true));
        board.setPiece(0, 4, new Pawn(0, 0, true));
        board.setPiece(0, 5, new Pawn(0, 0, true));
        board.setPiece(0, 6, new Pawn(0, 0, true));
        board.setPiece(0, 7, new Pawn(0, 0, true));
        verify(board, times(8)).setPiece(anyInt(), anyInt(), any());
    }

    @Test
    public void getPiece_getPiecesWithSameType_trueReturned()
    {
        board = Mockito.mock(Board.class);
        Mockito.when(board.getPiece(0, 0)).thenReturn(new Queen(0, 0, true));
        Mockito.when(board.getPiece(7, 7)).thenReturn(new Queen(7, 7, false));
        Piece whiteQueen = board.getPiece(0, 0);
        Piece blackQueen = board.getPiece(7, 7);
        Assertions.assertEquals(whiteQueen.getType(), blackQueen.getType());
    }

    @Test
    public void getPiece_getPawnCanJumpTwoTiles_trueReturned()
    {
        board = Mockito.mock(Board.class);
        Mockito.when(board.getPiece(6, 4)).thenReturn(new Pawn(6, 4, true));
        Piece pawn = board.getPiece(6, 4);
        Assertions.assertTrue(pawn.isValidMove(6, 4, 4, 4));
    }
}
