package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.model.board.Board;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GameTest {

    Board board;

    @BeforeEach
    void setUp()
    {
        board = new Board();
        board.initializeStandardLayout();
    }

    @Test
    public void processTest_foolsMate_whiteWins()
    {
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getLastMove());
        boolean isValidMove;

        // White pawn to e4
        isValidMove = board.makeMove(6, 4, 4, 4);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(6, 4));
        Assertions.assertEquals("Pawn", board.getPiece(4, 4).getType());

        // Black pawn to f6
        isValidMove = board.makeMove(1, 5, 2, 5);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(1, 5));
        Assertions.assertEquals("Pawn", board.getPiece(2, 5).getType());

        // White knight to c3
        isValidMove = board.makeMove(7, 1, 5, 2);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(7, 1));
        Assertions.assertEquals("Knight", board.getPiece(5, 2).getType());

        // Black pawn to g5
        isValidMove = board.makeMove(1, 6, 3, 6);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(1, 6));
        Assertions.assertEquals("Pawn", board.getPiece(3, 6).getType());

        // White Queen to h5 -> checkmate -> white wins
        isValidMove = board.makeMove(7, 3, 3, 7);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertTrue(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(7, 3));
        Assertions.assertEquals("Queen", board.getPiece(3, 7).getType());
    }

    @Test
    public void processTest_whitePlays_rightCastlingMove()
    {
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getLastMove());
        boolean isValidMove;

        // White pawn to e4
        isValidMove = board.makeMove(6, 4, 4, 4);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(6, 4));
        Assertions.assertEquals("Pawn", board.getPiece(4, 4).getType());

        // Black pawn to e5
        isValidMove = board.makeMove(1, 4, 3, 4);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(1, 4));
        Assertions.assertEquals("Pawn", board.getPiece(3, 4).getType());

        // White bishop to e2 (making space for castling move)
        isValidMove = board.makeMove(7, 5, 6, 4);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(7, 5));
        Assertions.assertEquals("Bishop", board.getPiece(6, 4).getType());

        // Black knight to c6
        isValidMove = board.makeMove(0, 1, 2, 2);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(0, 1));
        Assertions.assertEquals("Knight", board.getPiece(2, 2).getType());

        // White knight to f3
        isValidMove = board.makeMove(7, 6, 5, 5);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(7, 6));
        Assertions.assertEquals("Knight", board.getPiece(5, 5).getType());

        // Black knight to f6
        isValidMove = board.makeMove(0, 6, 2, 5);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(0, 6));
        Assertions.assertEquals("Knight", board.getPiece(2, 5).getType());

        // White right castling move
        boolean isCastlingMove = board.isCastlingMove(7, 4, 7, 7);
        isValidMove = board.makeMove(7, 4, 7, 7);
        Assertions.assertTrue(isCastlingMove);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(7, 4));
        Assertions.assertNull(board.getPiece(7, 7));
        Assertions.assertEquals("Rook", board.getPiece(7, 5).getType());
        Assertions.assertEquals("King", board.getPiece(7, 6).getType());
    }

    @Test
    public void processTest_whiteAttacks_enPassantBlackPawn()
    {
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getLastMove());
        boolean isValidMove;

        // White pawn to e4
        isValidMove = board.makeMove(6, 4, 4, 4);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(6, 4));
        Assertions.assertEquals("Pawn", board.getPiece(4, 4).getType());

        // Black pawn to e6
        isValidMove = board.makeMove(1, 4, 2, 4);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(1, 4));
        Assertions.assertEquals("Pawn", board.getPiece(2, 4).getType());

        // White pawn to e5
        isValidMove = board.makeMove(4, 4, 3, 4);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(4, 4));
        Assertions.assertEquals("Pawn", board.getPiece(3, 4).getType());

        // Black pawn to d5
        isValidMove = board.makeMove(1, 3, 3, 3);
        Assertions.assertTrue(isValidMove);
        // Check if en passant can be done
        Assertions.assertTrue(board.getLastMove().getCanEnPassant());
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(1, 3));
        Assertions.assertEquals("Pawn", board.getPiece(3, 3).getType());

        // White pawn attacks d6, takes black pawn on d5
        isValidMove = board.makeMove(3, 4, 2, 3);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getPiece(3, 4));
        Assertions.assertNull(board.getPiece(3, 3));
        Assertions.assertEquals("Pawn", board.getPiece(2, 3).getType());
    }

    @Test
    public void processTest_whiteChecksBlackKing_blackBlockAndCapture()
    {
        Assertions.assertFalse(board.isCheckMate(true));
        Assertions.assertFalse(board.isCheckMate(false));
        Assertions.assertNull(board.getLastMove());
        boolean isValidMove;

        // White pawn to c4
        isValidMove = board.makeMove(6, 2, 4, 2);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isKingChecked(true));
        Assertions.assertFalse(board.isKingChecked(false));
        Assertions.assertNull(board.getPiece(6, 2));
        Assertions.assertEquals("Pawn", board.getPiece(4, 2).getType());

        // Black pawn to d6
        isValidMove = board.makeMove(1, 3, 2, 3);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isKingChecked(true));
        Assertions.assertFalse(board.isKingChecked(false));
        Assertions.assertNull(board.getPiece(1, 3));
        Assertions.assertEquals("Pawn", board.getPiece(2, 3).getType());

        // White Queen to a4 -> Check -> Black side can block
        isValidMove = board.makeMove(7, 3, 4, 0);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isKingChecked(true));
        Assertions.assertTrue(board.isKingChecked(false));
        // Check if check can be blocked
        Assertions.assertTrue(board.canBlockAttackingPiece(false));
        Assertions.assertNull(board.getPiece(7, 3));
        Assertions.assertEquals("Queen", board.getPiece(4, 0).getType());

        // Black bishop to d7 -> blocking queen -> no check
        isValidMove = board.makeMove(0, 2, 1, 3);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isKingChecked(true));
        Assertions.assertFalse(board.isKingChecked(false));
        Assertions.assertNull(board.getPiece(0, 2));
        Assertions.assertEquals("Bishop", board.getPiece(1, 3).getType());

        // White Queen to d6 -> Check -> Black side can capture
        isValidMove = board.makeMove(4, 0, 1, 3);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isKingChecked(true));
        Assertions.assertTrue(board.isKingChecked(false));
        // Check if check can be blocked or captured
        Assertions.assertFalse(board.canBlockAttackingPiece(false));
        Assertions.assertTrue(board.canCaptureAttackingPiece(false));
        Assertions.assertNull(board.getPiece(4, 0));
        Assertions.assertEquals("Queen", board.getPiece(1, 3).getType());

        // Black king to d7 -> captures white queen -> no check
        isValidMove = board.makeMove(0, 4, 1, 3);
        Assertions.assertTrue(isValidMove);
        Assertions.assertFalse(board.isKingChecked(true));
        Assertions.assertFalse(board.isKingChecked(false));
        Assertions.assertNull(board.getPiece(0, 4));
        Assertions.assertEquals("King", board.getPiece(1, 3).getType());
    }
}
