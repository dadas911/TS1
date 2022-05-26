package cz.cvut.fel.pjv.model.board;

import cz.cvut.fel.pjv.controller.ChessController;
import cz.cvut.fel.pjv.model.game.Coordinates;
import cz.cvut.fel.pjv.model.game.Move;
import cz.cvut.fel.pjv.model.game.PgnParser;
import cz.cvut.fel.pjv.model.pieces.*;
import cz.cvut.fel.pjv.model.player.Player;

import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Class that represent chess board.
 * Most of the chess rules are written in this class.
 * It provides:
 * <ul>
 *     <li>Initialize board into standard layout</li>
 *     <li>Moving with pieces</li>
 *     <li>Removing pieces</li>
 *     <li>Checking if the played move was valid in every way</li>
 *     <li>Checking for "Check", "Checkmate" or "Stalemate"</li>
 * </ul>
 */
public class Board {
    Piece[][] board;
    private Coordinates whiteKingCoordinates;
    private Coordinates blackKingCoordinates;
    private List<Move> playedMoves;
    PgnParser parser;

    private static final Logger LOGGER = Logger.getLogger(ChessController.class.getName());

    public Board()
    {
        board = new Piece[8][8];
        playedMoves = new ArrayList<>();
        parser = new PgnParser(this);
    }

    /**
     * Put all pieces in standard layout
     */
    public void initializeStandardLayout()
    {
        // Black (upper) side
        // First row
        board[0][0] = new Rook(0, 0, false);
        board[0][1] = new Knight(0, 1, false);
        board[0][2] = new Bishop(0, 2, false);
        board[0][3] = new Queen(0, 3, false);
        board[0][4] = new King(0, 4, false);
        board[0][5] = new Bishop(0, 5, false);
        board[0][6] = new Knight(0, 6, false);
        board[0][7] = new Rook(0, 7, false);

        blackKingCoordinates = new Coordinates(0,4);

        // Second row
        for(int i = 0; i < 8; i++)
        {
            board[1][i] = new Pawn(1, i, false);
        }

        // White (lower) side
        // First row
        board[7][0] = new Rook(7, 0, true);
        board[7][1] = new Knight(7, 1, true);
        board[7][2] = new Bishop(7, 2, true);
        board[7][3] = new Queen(7, 3, true);
        board[7][4] = new King(7, 4, true);
        board[7][5] = new Bishop(7, 5, true);
        board[7][6] = new Knight(7, 6, true);
        board[7][7] = new Rook(7, 7, true);

        whiteKingCoordinates = new Coordinates(7,4);

        // Second row
        for(int i = 0; i < 8; i++)
        {
            board[6][i] = new Pawn(6, i, true);
        }
    }

    public void initializeCustomizedLayout()
    {
        // Create board with layout, which is customized by player before game
    }

    /**
     * Set piece on given coordinates
     * @param x
     * @param y
     * @param piece
     */
    public void setPiece(int x, int y, Piece piece)
    {
        if(isNotOutOfRange(x, y))
        {
            board[x][y] = piece;
            if(piece instanceof King)
            {
                Coordinates kingsCoord = new Coordinates(x, y);
                if(piece.getIsWhite())
                {
                    whiteKingCoordinates = kingsCoord;
                }
                else
                {
                    blackKingCoordinates = kingsCoord;
                }
            }
        }
    }

    /**
     * Get piece from given coordinates
     * @param x
     * @param y
     * @return
     */
    public Piece getPiece(int x, int y)
    {
        if(isNotOutOfRange(x, y))
        {
            return this.board[x][y];
        }

        return null;
    }

    public Coordinates getWhiteKingCoordinates()
    {
        return this.whiteKingCoordinates;
    }

    public void setWhiteKingCoordinates(Coordinates coord)
    {
        this.whiteKingCoordinates = coord;
    }

    public Coordinates getBlackKingCoordinates()
    {
        return this.blackKingCoordinates;
    }

    public void setBlackKingCoordinates(Coordinates coord)
    {
        this.blackKingCoordinates = coord;
    }

    /**
     * Removes Piece from given coordinates
     * @param x
     * @param y
     */
    public void remove(int x, int y)
    {
        this.board[x][y] = null;
    }

    /**
     * Determine if given coordinates are out off
     * @param x
     * @param y
     * @return
     */
    public boolean isNotOutOfRange(int x, int y)
    {
        return (x>=0)&&(x<8)&&(y>=0)&&(y<8);
    }

    /**
     * Determine if the given spot is empty
     * @param x
     * @param y
     * @return
     */
    public boolean isEmpty(int x, int y)
    {
        return this.board[x][y] == null;
    }

    /**
     * Get all pieces of certain color
     * @param isWhitePieces
     * @return
     */
    public Vector<Coordinates> getAllPieces(boolean isWhitePieces)
    {
        Vector<Coordinates> result = new Vector<>(0);

        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                if(!isEmpty(x, y) && getPiece(x, y).getIsWhite() == isWhitePieces)
                {
                    result.add(new Coordinates(x, y));
                }
            }
        }

        return result;
    }

    public List<Move> getPlayedMoves()
    {
        return playedMoves;
    }

    public void clearPlayedMoves()
    {
        playedMoves.clear();
    }

    public void printPlayedMoves()
    {
        for (Move playedMove : playedMoves) {
            System.out.println(playedMove);
        }
    }

    public Move getLastMove()
    {
        if(!playedMoves.isEmpty())
        {
            return playedMoves.get(playedMoves.size() - 1);
        }

        return null;
    }

    /**
     * Makes move from one spot to another and check all rules
     * @param start_x
     * @param start_y
     * @param dest_x
     * @param dest_y
     * @return
     */
    public boolean makeMove(int start_x, int start_y, int dest_x, int dest_y)
    {
        if(canMakeMoveWithoutExposingKing(start_x, start_y, dest_x, dest_y))
        {
            Move currMove;

            if(isCastlingMove(start_x, start_y, dest_x, dest_y))
            {
                boolean isLeftCastlingMove;

                Piece king = getPiece(start_x, start_y);
                Piece rook = getPiece(dest_x, dest_y);

                // Needed condition for checking type, else error
                // If moving with King or Rook -> change hasMoved variable to true
                if(king instanceof King)
                {
                    ((King) king).setHasMoved(true);
                }
                if(rook instanceof Rook)
                {
                    ((Rook) rook).setHasMoved(true);
                }

                currMove = new Move(start_x, start_y, dest_x, dest_y, king, rook);

                parser.insertMove(currMove, false, true);

                // Determine the direction (big or small castling move)
                if(start_y < dest_y)
                {
                    isLeftCastlingMove = false;
                }
                else
                {
                    isLeftCastlingMove = true;
                }

                // Big (left) castling move
                if(isLeftCastlingMove)
                {
                    setPiece(start_x, start_y - 2, king);
                    setPiece(dest_x, dest_y + 3, rook);
                    remove(start_x, start_y);
                    remove(dest_x, dest_y);
                }
                // Small (right) castling move
                else
                {
                    setPiece(start_x, start_y + 2, king);
                    setPiece(dest_x, dest_y - 2, rook);
                    remove(start_x, start_y);
                    remove(dest_x, dest_y);
                }

                LOGGER.info("Castling move executed.");
            }
            // Not castling move
            else
            {
                Piece currPiece = getPiece(start_x, start_y);
                Piece remPiece = getPiece(dest_x, dest_y);


                // If moving with King or Rook -> change hasMoved variable to true
                if(currPiece instanceof King)
                {
                    ((King) currPiece).setHasMoved(true);
                }
                else if(currPiece instanceof Rook)
                {
                    ((Rook) currPiece).setHasMoved(true);
                }

                currMove = new Move(start_x, start_y, dest_x, dest_y, currPiece, remPiece);

                parser.insertMove(currMove, isEnPassant(start_x, start_y, dest_x, dest_y), false);

                if(isEnPassant(start_x, start_y, dest_x, dest_y))
                {
                    remove(start_x, dest_y);
                    LOGGER.info("En Passant move executed.");
                }
                remove(start_x, start_y);

                setPiece(dest_x, dest_y, currPiece);
            }

            playedMoves.add(currMove);

            return true;
        }

        return false;
    }

    /**
     * Determine if there is any obstacles on path
     * @param start_x
     * @param start_y
     * @param dest_x
     * @param dest_y
     * @return
     */
    public boolean noObstacleOnPath(int start_x, int start_y, int dest_x, int dest_y)
    {
        Piece currPiece = getPiece(start_x, start_y);
        Vector<Coordinates> path = currPiece.getAllValidPath(start_x, start_y, dest_x, dest_y);

        if(!path.isEmpty())
        {
            int x, y;
            // Go through every step of the path
            for(int i = 0; i < path.size(); i++)
            {
                x = path.get(i).getX();
                y = path.get(i).getY();
                // Piece is in the way -> return false, path is blocked
                if(board[x][y] != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isEnPassant(int start_x, int start_y, int dest_x, int dest_y)
    {
        if(getLastMove() != null)
        {
            if(getLastMove().getCanEnPassant())
            {
                if(Math.abs(getLastMove().getDestY() - start_y) == 1 && getLastMove().getDestX() == start_x && (getPiece(start_x, start_y) instanceof Pawn))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCastlingMove(int start_x, int start_y, int dest_x, int dest_y)
    {
        // Coordinates of the first step made by King (cannot be in danger),
        // second (destination) position is checked by canMoveWithoutExposingKing method
        int step_x, step_y;

        Piece king = getPiece(start_x, start_y);
        Piece rook = getPiece(dest_x, dest_y);

        // Player is moving with King and clicking on Rook + they are same color
        // + pieces haven't been moved yet
        if((king instanceof King) && (rook instanceof Rook) && (king.getIsWhite() == rook.getIsWhite())
        && !((King) king).getHasMoved() && !((Rook) rook).getHasMoved())
        {
            // Determine if the castling move is right (small) or left (big)
            if(start_y < dest_y)
            {
                step_x = start_x;
                step_y = start_y + 1;
            }
            else
            {
                step_x = start_x;
                step_y = start_y - 1;
            }

            // No obstacles between king and rook

            if(noObstacleOnPath(start_x, start_y, dest_x, dest_y))
            {
                // "Move" with king to first position to check if he is safe
                setPiece(step_x, step_y, king);
                // "Re moving" king to original position (global variable kins position is changed without this)
                setPiece(start_x, start_y, king);

                // King is not in danger -> castling move can be done
                if(!isInDanger(step_x, step_y))
                {
                    remove(step_x, step_y);
                    return true;
                }
            }
            remove(step_x, step_y);

        }
        return false;
    }

    // Determine if piece can move from current spot to destination
    public boolean canMakeMove(int start_x, int start_y, int dest_x, int dest_y)
    {
        // Coordinates are not out of range
        if(this.isNotOutOfRange(dest_x, dest_y))
        {
            Piece currPiece = getPiece(start_x, start_y);
            if(currPiece != null)
            {
                // If spot is empty or there is enemy piece
                if(isEmpty(dest_x, dest_y) || (currPiece.getIsWhite() != getPiece(dest_x, dest_y).getIsWhite()))
                {
                    // Check if it is valid move in piece method
                    if(currPiece.isValidMove(start_x, start_y, dest_x, dest_y))
                    {
                        // Special pawn rule
                        if(currPiece instanceof Pawn)
                        {
                            // Check en passant
                            if(isEnPassant(start_x, start_y, dest_x, dest_y))
                            {
                                return true;
                            }

                            // Vertical pawn move (spot needs to be empty)
                            if(isEmpty(dest_x, dest_y) && start_y == dest_y && noObstacleOnPath(start_x, start_y, dest_x, dest_y))
                            {
                                return true;
                            }
                            // Diagonal move, only possible when attacking enemy piece, else return false
                            else return !isEmpty(dest_x, dest_y) && start_y != dest_y;
                        }
                        // Piece is not pawn && there is no obstacle on way
                        if(noObstacleOnPath(start_x, start_y, dest_x, dest_y))
                        {
                            return true;
                        }
                    }
                }
                else if(isCastlingMove(start_x, start_y, dest_x, dest_y))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns list of coordinates of pieces which can reach entered position
     * @param x
     * @param y
     * @param isWhite
     * @return
     */
    public Vector<Coordinates> getListOfThreats(int x, int y, boolean isWhite)
    {
        Vector<Coordinates> result = new Vector<>(0);

        for(int curr_x = 0; curr_x < 8; curr_x++)
        {
            for(int curr_y = 0; curr_y < 8; curr_y++)
            {
                // Put every piece that can move to entered position in the result list
                if(!isEmpty(curr_x, curr_y))
                {
                    if((getPiece(curr_x, curr_y).getIsWhite() == isWhite) && canMakeMove(curr_x, curr_y, x, y))
                    {
                        result.add(new Coordinates(curr_x, curr_y));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Determine if current position can be reached by any enemy piece on board
     * @param x
     * @param y
     * @return
     */
    public boolean isInDanger(int x, int y)
    {
        Piece currPiece = getPiece(x, y);

        for(int curr_x = 0; curr_x < 8; curr_x++)
        {
            for(int curr_y = 0; curr_y < 8; curr_y++)
            {
                // Check if enemy piece that can move to entered spot exists -> if yes, return true
                if(!isEmpty(curr_x, curr_y))
                {
                    if((getPiece(curr_x, curr_y).getIsWhite() != currPiece.getIsWhite()) && canMakeMove(curr_x, curr_y, x, y))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check if king is checked, based on entered "color"
     * @param isWhiteKing
     * @return
     */
    public boolean isKingChecked(boolean isWhiteKing)
    {
        int x, y;
        if(isWhiteKing)
        {
            x = getWhiteKingCoordinates().getX();
            y = getWhiteKingCoordinates().getY();
        }
        else
        {
            x = getBlackKingCoordinates().getX();
            y = getBlackKingCoordinates().getY();
        }

        return isInDanger(x, y);
    }

    /**
     * Can make move without endangering own king
     * @param start_x
     * @param start_y
     * @param dest_x
     * @param dest_y
     * @return
     */
    public boolean canMakeMoveWithoutExposingKing(int start_x, int start_y, int dest_x, int dest_y)
    {
        boolean result = true;

        // If this move is valid without checking own king
        if(canMakeMove(start_x, start_y, dest_x, dest_y))
        {
            Piece currPiece = getPiece(start_x, start_y);
            Piece remPiece = getPiece(dest_x, dest_y);

            // Try to make move
            remove(start_x, start_y);
            setPiece(dest_x, dest_y, currPiece);

            // Now check if friendly King is checked
            if(isKingChecked(currPiece.getIsWhite()))
            {
                result = false;
            }

            // Put pieces back to original places
            setPiece(start_x, start_y, currPiece);
            setPiece(dest_x, dest_y, remPiece);

            return result;
        }

        return false;
    }

    // Determines if King can escape "Check"
    public boolean canKingEscape(boolean isWhiteKing)
    {
        // Get coordinates of checked king
        int x, y;
        if(isWhiteKing)
        {
            x = getWhiteKingCoordinates().getX();
            y = getWhiteKingCoordinates().getY();
        }
        else
        {
            x = getBlackKingCoordinates().getX();
            y = getBlackKingCoordinates().getY();
        }

        // Try all different combination of moving the king
        return canMakeMoveWithoutExposingKing(x, y, x+1, y) || canMakeMoveWithoutExposingKing(x, y, x-1, y)
                || canMakeMoveWithoutExposingKing(x, y, x, y+1) || canMakeMoveWithoutExposingKing(x, y, x, y-1)
                || canMakeMoveWithoutExposingKing(x, y, x+1, y+1) || canMakeMoveWithoutExposingKing(x, y,x+1, y-1)
                || canMakeMoveWithoutExposingKing(x, y, x-1, y+1) || canMakeMoveWithoutExposingKing(x, y,x-1, y-1);
    }

    public boolean canCaptureAttackingPiece(boolean isWhiteKing)
    {
        // Get coordinates of checked king
        int x, y;
        if(isWhiteKing)
        {
            x = getWhiteKingCoordinates().getX();
            y = getWhiteKingCoordinates().getY();
        }
        else
        {
            x = getBlackKingCoordinates().getX();
            y = getBlackKingCoordinates().getY();
        }

        // Get list of all enemy pieces that can attack king directly
        Vector<Coordinates> threats = getListOfThreats(x, y, !isWhiteKing);

        for(int i = 0; i < threats.size(); i++)
        {
            int threat_x = threats.get(i).getX();
            int threat_y = threats.get(i).getY();

            // Get list of all friendly pieces that can capture this threat
            Vector<Coordinates> saviors = getListOfThreats(threat_x, threat_y, isWhiteKing);
            // Check if there is any possible savior
            if(saviors.size() != 0)
            {
                // Now check every savior if they can capture threat without putting king in danger
                for(int j = 0; i < saviors.size(); j++)
                {
                    int saviour_x = saviors.get(j).getX();
                    int saviour_y = saviors.get(j).getY();

                    // They can save king without putting him in another danger
                    if(canMakeMoveWithoutExposingKing(saviour_x, saviour_y, threat_x, threat_y))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Determine if attacking piece can be blocked by friendly piece (isWhiteKing = friendly color)
    public boolean canBlockAttackingPiece(boolean isWhiteKing)
    {
        // Get coordinates of checked king
        int x, y;
        if(isWhiteKing)
        {
            x = getWhiteKingCoordinates().getX();
            y = getWhiteKingCoordinates().getY();
        }
        else
        {
            x = getBlackKingCoordinates().getX();
            y = getBlackKingCoordinates().getY();
        }

        Vector<Coordinates> threats = getListOfThreats(x, y, !isWhiteKing);

        // Player can block only one direction of attack -> if size is more than one -> false
        if(threats.size() == 1)
        {
            int threat_x = threats.get(0).getX();
            int threat_y = threats.get(0).getY();

            // Get list of path from threat to king
            Vector<Coordinates> attackingPath = getPiece(threat_x, threat_y).getAllValidPath(threat_x, threat_y, x, y);
            if(attackingPath.size() > 0)
            {
                // Go through path and searched for possible saviour
                for (Coordinates coordinates : attackingPath) {
                    int curr_x = coordinates.getX();
                    int curr_y = coordinates.getY();

                    // Generate list of saviours -> pieces that can get to path
                    Vector<Coordinates> saviours = getListOfThreats(curr_x, curr_y, isWhiteKing);

                    for (Coordinates saviour : saviours) {
                        int saviour_x = saviour.getX();
                        int saviour_y = saviour.getY();

                        // Friendly piece can move without endangering king
                        if (canMakeMoveWithoutExposingKing(saviour_x, saviour_y, curr_x, curr_y)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check if checkmate occurred
     * @param isWhiteKing
     * @return
     */
    public boolean isCheckMate(boolean isWhiteKing)
    {
        // If king is checked -> check every possible option to save him
        if(isKingChecked(isWhiteKing))
        {
            return !(canKingEscape(isWhiteKing) || canCaptureAttackingPiece(isWhiteKing) || canBlockAttackingPiece(isWhiteKing));
        }

        return false;
    }

    public boolean isStaleMate(boolean isWhiteKing)
    {
        // Check for checkmate
        if(!isCheckMate(isWhiteKing))
        {
            // Go through whole board and check if some piece can move
            for(int x = 0; x < 8; x++)
            {
                for(int y = 0; y < 8; y++)
                {
                    Piece currPiece = getPiece(x, y);
                    if(currPiece != null && (currPiece.getIsWhite() == isWhiteKing))
                    {
                        for(int curr_x = 0; curr_x < 8; curr_x++)
                        {
                            for(int curr_y = 0; curr_y < 8; curr_y++)
                            {
                                // If there exist piece that can move somewhere -> false
                                if(canMakeMoveWithoutExposingKing(x, y, curr_x, curr_y))
                                {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Function checks if pawn should be promoted
     */
    public void checkPromotion()
    {
        // Black (upper) end of board -> if there is white pawn, promote it to queen
        for(int y = 0; y < 8; y++)
        {
            Piece currPiece = getPiece(0, y);
            if((currPiece instanceof Pawn) && currPiece.getIsWhite())
            {
                remove(0, y);
                setPiece(0, y, new Queen(0, y, true));
            }
        }

        // White (lower) end of board -> if there is black pawn, promote it to queen
        for(int y = 0; y < 8; y++)
        {
            Piece currPiece = getPiece(7, y);
            if((currPiece instanceof Pawn) && !currPiece.getIsWhite())
            {
                remove(7, y);
                setPiece(7, y, new Queen(7, y, false));
            }
        }
    }

    public void saveGame(String fileName, String whiteName, String blackName) throws IOException {
        parser.saveGame(fileName, whiteName, blackName);
    }
}
