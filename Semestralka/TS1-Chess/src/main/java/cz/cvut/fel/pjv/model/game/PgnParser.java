package cz.cvut.fel.pjv.model.game;

import cz.cvut.fel.pjv.model.board.Board;
import cz.cvut.fel.pjv.model.pieces.Pawn;
import cz.cvut.fel.pjv.model.pieces.Piece;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.lang.*;

public class PgnParser {
    private Board board;
    private String moveText;
    private String headerText;
    private String completeText;
    int counter = 1;
    HashMap<Integer, String> columnMap;
    HashMap<Integer, String> rowMap;
    HashMap<String, String> pieceMap;

    HashMap<Character, Integer> columnMapReverse;
    HashMap<Character, Integer> rowMapReverse;
    HashMap<Character, String> pieceTypeMap;

    public PgnParser(Board board)
    {
        moveText = "";
        headerText = "";
        this.board = board;

        columnMap = new HashMap<>();
        columnMap.put(0, "a");
        columnMap.put(1, "b");
        columnMap.put(2, "c");
        columnMap.put(3, "d");
        columnMap.put(4, "e");
        columnMap.put(5, "f");
        columnMap.put(6, "g");
        columnMap.put(7, "h");

        rowMap = new HashMap<>();
        rowMap.put(0, "8");
        rowMap.put(1, "7");
        rowMap.put(2, "6");
        rowMap.put(3, "5");
        rowMap.put(4, "4");
        rowMap.put(5, "3");
        rowMap.put(6, "2");
        rowMap.put(7, "1");

        pieceMap = new HashMap<>();
        pieceMap.put("whitePawn", "");
        pieceMap.put("blackPawn", "");
        pieceMap.put("whiteKnight", "N");
        pieceMap.put("blackKnight", "N");
        pieceMap.put("whiteBishop", "B");
        pieceMap.put("blackBishop", "B");
        pieceMap.put("whiteRook", "R");
        pieceMap.put("blackRook", "R");
        pieceMap.put("whiteQueen", "Q");
        pieceMap.put("blackQueen", "Q");
        pieceMap.put("whiteKing", "K");
        pieceMap.put("blackKing", "K");

        columnMapReverse = new HashMap<>();
        columnMapReverse.put('a', 0);
        columnMapReverse.put('b', 1);
        columnMapReverse.put('c', 2);
        columnMapReverse.put('d', 3);
        columnMapReverse.put('e', 4);
        columnMapReverse.put('f', 5);
        columnMapReverse.put('g', 6);
        columnMapReverse.put('h', 7);

        rowMapReverse = new HashMap<>();
        rowMapReverse.put('8', 0);
        rowMapReverse.put('7', 1);
        rowMapReverse.put('6', 2);
        rowMapReverse.put('5', 3);
        rowMapReverse.put('4', 4);
        rowMapReverse.put('3', 5);
        rowMapReverse.put('2', 6);
        rowMapReverse.put('1', 7);

        pieceTypeMap = new HashMap<>();
        pieceTypeMap.put('B', "Bishop");
        pieceTypeMap.put('N', "Knight");
        pieceTypeMap.put('R', "Rook");
        pieceTypeMap.put('Q', "Queen");
        pieceTypeMap.put('K', "King");
    }

    public void saveGame(String fileName, String whiteName, String blackName) throws IOException {
        // Create folder if doesn't exist
        File directory = new File("savedGames");
        if(!directory.exists())
        {
            directory.mkdirs();
        }

        String path = "savedGames\\" + fileName +".pgn";
        // Creating file
        File newFile = new File(path);
        newFile.createNewFile();

        this.setHeader(whiteName, blackName);
        String result = this.getResult();

        // Writing into file
        FileWriter myWriter = new FileWriter(path);
        myWriter.write(result);
        myWriter.close();
    }

    public String getResult()
    {
        completeText = headerText + moveText;
        System.out.println(this.completeText);
        return completeText;
    }

    public void setHeader(String whiteName, String blackName)
    {
        headerText = "[Event \"PJV\"]\n[Site \"?\"]\n[Date \"?\"]\n[Round \"?\"]\n[White \"" + whiteName + "\"]\n[Black \"" + blackName + "\"]\n[Result \"?\"]\n\n";
    }

    public void insertMove(Move move, boolean isEnPassant, boolean isCastlingMove)
    {
        //System.out.println("Parsed: " + move + ", " + isEnPassant + ", " + isCastlingMove);
        // White player played move -> add number of current move into string
        if(move.getStartPiece().getIsWhite())
        {
            moveText += counter + ". ";
            counter++;
        }

        // Castling move -> special rule
        if(isCastlingMove)
        {
            // King side (small) castling move
            if(move.getStartY() < move.getDestY())
            {
                moveText += "0-0";
            }
            // Queen side (big) castling move
            else
            {
                moveText += "0-0-0";
            }
        }
        // Not castling move
        else
        {

            // Add Piece name (except for pawn)
            moveText += pieceMap.get(move.getStartPiece().getPieceName());

            // Check if there is piece (same Class) that can reach this position
            Vector<Coordinates> friendlyPieces = board.getListOfThreats(move.getDestX(), move.getDestY(), move.getStartPiece().getIsWhite());
            for (Coordinates friendlyPiece : friendlyPieces) {
                int curr_x = friendlyPiece.getX();
                int curr_y = friendlyPiece.getY();
                if (board.getPiece(curr_x, curr_y).getPieceName().equals(move.getStartPiece().getPieceName())
                        && (curr_x != move.getStartX() || curr_y != move.getStartY()))
                {
                    // Pieces are on the same column (file) -> add number
                    if(curr_y == move.getStartY())
                    {
                        moveText += rowMap.get(move.getStartX());
                    }
                    // Pieces not on same column -> add letter
                    else
                    {
                        moveText += columnMap.get(move.getStartY());
                    }
                    break;
                }
            }

            // If this is attacking move -> add x: Bc6 -> Bxc6
            if(move.getDestPiece() != null && move.getDestPiece().getIsWhite() != move.getStartPiece().getIsWhite())
            {
                // Pawn is attacking -> write his starting column
                if(move.getStartPiece() instanceof Pawn)
                {
                    moveText += columnMap.get(move.getStartY());
                }
                moveText += "x";
            }

            // Add destination field
            moveText += columnMap.get(move.getDestY()) + rowMap.get(move.getDestX());

            // Promote move
            if((move.getStartPiece() instanceof Pawn) && ((move.getStartPiece().getIsWhite() && move.getDestX() == 0) ||
                    !move.getStartPiece().getIsWhite() && move.getDestX() == 7))
            {
                moveText += ("=Q");
            }
        }

        // Common part
        // Enemy king is checkmate -> add "#" + score
        board.setPiece(move.getDestX(), move.getDestY(), move.getStartPiece());
        if(board.isCheckMate(!move.getStartPiece().getIsWhite()))
        {
            moveText += "# ";
            if(move.getStartPiece().getIsWhite())
            {
                moveText += "1-0";
            }
            else
            {
                moveText += "0-1";
            }
        }
        else if(board.isStaleMate(!move.getStartPiece().getIsWhite()))
        {
            moveText += "1/2-1/2";
        }
        // Enemy king is checked -> add "+"
        else if(board.isKingChecked(!move.getStartPiece().getIsWhite()))
        {
            moveText += "+";
        }

        board.remove(move.getDestX(), move.getDestY());

        // Add space after move
        moveText += " ";
    }

    public boolean loadGame(String game) throws IOException {
        String pathString = "gamesToLoad\\" + game + ".pgn";
        Path path = Path.of(pathString);
        String chessText = Files.readString(path);

        String[] splitArray = chessText.split("\n\n");
        //System.out.println("Header: " + splitArray[0]);
        boolean isWhitesTurn = getMoves(splitArray[1]);

        return isWhitesTurn;
    }

    public boolean getMoves(String movesString)
    {
        List<Move> generatedMoves = new ArrayList<>();
        //System.out.println("Moves: " + movesString);
        String[] moves = movesString.split(" ");
        Move currMove;
        boolean isWhitesTurn = false;

        // Going through all moves
        for(int i = 0; i < moves.length; i++)
        {
            boolean isCheck = false;
            boolean isCheckMate = false;
            // If move contain "." skip it
            if(moves[i].contains(".") || moves[i].contains("*"))
            {
                continue;
            }
            isWhitesTurn = !isWhitesTurn;

            // If there is any move "extension" (# or +) -> remove it and set variable
            if(moves[i].contains("#"))
            {
                moves[i] = moves[i].replace("#", "");
                isCheckMate = true;
            }
            if(moves[i].contains("+"))
            {
                moves[i] = moves[i].replace("+", "");
                isCheck = true;
            }

            if(moves[i].contains("x"))
            {
                currMove = generateAttackMove(moves[i], isWhitesTurn);
            }
            else if(moves[i].contains("0-0") || moves[i].contains("O-O"))
            {
                currMove = generateCastlingMove(moves[i], isWhitesTurn);
            }
            else
            {
                currMove = generateSimpleMove(moves[i], isWhitesTurn);
            }

            board.makeMove(currMove.getStartX(), currMove.getStartY(), currMove.getDestX(), currMove.getDestY());

            if(isCheckMate)
            {
                System.out.println("Parser: CheckMate!");
                break;
            }

        }

        return !isWhitesTurn;
    }

    public Move generateCastlingMove(String move, boolean isWhitesTurn)
    {
        int start_x = 0;
        int start_y = 0;
        int dest_x = 0;
        int dest_y = 0;

        // Determine king's coordinates by color
        if(isWhitesTurn)
        {
            start_x = 7;
            start_y = 4;
            // Small castling move -> right rook
            if(move.length() == 3)
            {
                dest_x = 7;
                dest_y = 7;
            }
            // Big castling move -> left rook
            else
            {
                dest_x = 7;
                dest_y = 0;
            }
        }
        else
        {
            start_x = 0;
            start_y = 4;

            // Small castling move -> right rook
            if(move.length() == 3)
            {
                dest_x = 0;
                dest_y = 7;
            }
            // Big castling move -> left rook
            else
            {
                dest_x = 0;
                dest_y = 0;
            }
        }

        Move currMove = new Move(start_x, start_y, dest_x, dest_y, board.getPiece(start_x, start_y), board.getPiece(dest_x, dest_y));

        return currMove;
    }

    public Move generateAttackMove(String move, boolean isWhitesTurn)
    {
        String pieceType;

        int start_x = 0;
        int start_y = 0;
        int dest_x = 0;
        int dest_y = 0;

        Move currMove = null;

        // Getting type of piece
        if(Character.isUpperCase(move.charAt(0)))
        {
            pieceType = pieceTypeMap.get(move.charAt(0));
        }
        else
        {
            pieceType = "Pawn";
        }

        // Attacking with pawn
        if(pieceType.equals("Pawn"))
        {
            // IDK if there can be more types
            dest_x = rowMapReverse.get(move.charAt(3));
            dest_y = columnMapReverse.get(move.charAt(2));

            start_y = columnMapReverse.get(move.charAt(0));

            // Generate list of friendly pieces that can reach this position
            Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
            // Select the one with correct type
            for(int i = 0; i < friendlyPieces.size(); i++)
            {
                int curr_x = friendlyPieces.get(i).getX();
                int curr_y = friendlyPieces.get(i).getY();
                if(board.getPiece(curr_x, curr_y).getType().equals(pieceType) && curr_y == start_y)
                {
                    start_x = curr_x;
                    start_y = curr_y;
                    break;
                }
            }
        }
        // Attacking with something else
        else
        {
            // Normal attack move - only one possible attacker
            if(move.length() == 4)
            {
                dest_x = rowMapReverse.get(move.charAt(3));
                dest_y = columnMapReverse.get(move.charAt(2));

                // Generate list of friendly pieces that can reach this position
                Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
                // Select the one with correct type
                for(int i = 0; i < friendlyPieces.size(); i++)
                {
                    int curr_x = friendlyPieces.get(i).getX();
                    int curr_y = friendlyPieces.get(i).getY();
                    if(board.getPiece(curr_x, curr_y).getType().equals(pieceType))
                    {
                        start_x = curr_x;
                        start_y = curr_y;
                        break;
                    }
                }
            }
            // There are more potential attackers
            else
            {
                dest_x = rowMapReverse.get(move.charAt(4));
                dest_y = columnMapReverse.get(move.charAt(3));

                // Two pieces of same type that can reach target exists -> check by column (letter)
                if(!Character.isDigit(move.charAt(1)))
                {
                    int columnCondition = columnMapReverse.get(move.charAt(1));
                    Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
                    // Select the one with correct type
                    for(int i = 0; i < friendlyPieces.size(); i++)
                    {
                        int curr_x = friendlyPieces.get(i).getX();
                        int curr_y = friendlyPieces.get(i).getY();
                        if((board.getPiece(curr_x, curr_y).getType().equals(pieceType)) && curr_y == columnCondition)
                        {
                            start_x = curr_x;
                            start_y = curr_y;
                            break;
                        }
                    }
                }
                // Two pieces of same type that can reach target exists && they are on the same column -> check by row
                else
                {
                    int rowCondition = rowMapReverse.get(move.charAt(1));

                    Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
                    // Select the one with correct type
                    for(int i = 0; i < friendlyPieces.size(); i++)
                    {
                        int curr_x = friendlyPieces.get(i).getX();
                        int curr_y = friendlyPieces.get(i).getY();
                        if(board.getPiece(curr_x, curr_y).getType().equals(pieceType) && curr_x == rowCondition)
                        {
                            start_x = curr_x;
                            start_y = curr_y;
                            break;
                        }
                    }
                }
            }

        }

        currMove = new Move(start_x, start_y, dest_x, dest_y, board.getPiece(start_x, start_y), board.getPiece(dest_x, dest_y));

        return currMove;
    }

    public Move generateSimpleMove(String move, boolean isWhitesTurn)
    {
        String pieceType;

        int start_x = 0;
        int start_y = 0;
        int dest_x = 0;
        int dest_y = 0;

        Move currMove = null;

        // Getting type of piece
        if(Character.isUpperCase(move.charAt(0)))
        {
            pieceType = pieceTypeMap.get(move.charAt(0));
        }
        else
        {
            pieceType = "Pawn";
        }

        // Making move using Pawn
        if(pieceType.equals("Pawn"))
        {
            // It is the most basic move, only dest position
            if(move.length() == 2)
            {
                dest_y = columnMapReverse.get(move.charAt(0));
                dest_x = rowMapReverse.get(move.charAt(1));

                // Generate list of friendly pieces that can reach this position
                Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
                // Select the one with correct type
                for(int i = 0; i < friendlyPieces.size(); i++)
                {
                    int curr_x = friendlyPieces.get(i).getX();
                    int curr_y = friendlyPieces.get(i).getY();
                    if(board.getPiece(curr_x, curr_y).getType().equals(pieceType))
                    {
                        start_x = curr_x;
                        start_y = curr_y;
                        break;
                    }
                }

            }
            else //?? IDK if this is possible
            {
                System.out.println("Pawn 3 length move");
            }
        }
        // Moved with other pieces
        else
        {
            // It is the most basic move, only piece type + dest position
            if(move.length() == 3)
            {
                dest_y = columnMapReverse.get(move.charAt(1));
                dest_x = rowMapReverse.get(move.charAt(2));

                Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
                // Select the one with correct type
                for(int i = 0; i < friendlyPieces.size(); i++)
                {
                    int curr_x = friendlyPieces.get(i).getX();
                    int curr_y = friendlyPieces.get(i).getY();
                    if(board.getPiece(curr_x, curr_y).getType().equals(pieceType))
                    {
                        start_x = curr_x;
                        start_y = curr_y;
                        break;
                    }
                }
            }
            // Complicated move - more same type pieces can reach target
            // or more same pieces can reach target + they are on the same column
            else
            {
                dest_y = columnMapReverse.get(move.charAt(2));
                dest_x = rowMapReverse.get(move.charAt(3));
                // Two pieces of same type that can reach target exists -> check by column (letter)
                if(!Character.isDigit(move.charAt(1)))
                {

                    int columnCondition = columnMapReverse.get(move.charAt(1));
                    Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
                    // Select the one with correct type
                    for(int i = 0; i < friendlyPieces.size(); i++)
                    {
                        int curr_x = friendlyPieces.get(i).getX();
                        int curr_y = friendlyPieces.get(i).getY();
                        if((board.getPiece(curr_x, curr_y).getType().equals(pieceType)) && curr_y == columnCondition)
                        {
                            start_x = curr_x;
                            start_y = curr_y;
                            break;
                        }
                    }
                }
                // Two pieces of same type that can reach target exists && they are on the same column -> check by row
                else
                {
                    int rowCondition = rowMapReverse.get(move.charAt(1));

                    Vector<Coordinates> friendlyPieces = board.getListOfThreats(dest_x, dest_y, isWhitesTurn);
                    // Select the one with correct type
                    for(int i = 0; i < friendlyPieces.size(); i++)
                    {
                        int curr_x = friendlyPieces.get(i).getX();
                        int curr_y = friendlyPieces.get(i).getY();
                        if(board.getPiece(curr_x, curr_y).getType().equals(pieceType) && curr_x == rowCondition)
                        {
                            start_x = curr_x;
                            start_y = curr_y;
                            break;
                        }
                    }
                }
            }
        }

        //System.out.println(pieceType + " moved from (" + start_x + ", " + start_y + ") to (" + dest_x + "," + dest_y + ")");
        currMove = new Move(start_x, start_y, dest_x, dest_y, board.getPiece(start_x, start_y), board.getPiece(dest_x, dest_y));

        return currMove;
    }
}
