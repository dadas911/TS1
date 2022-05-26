package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.board.Board;
import cz.cvut.fel.pjv.model.pieces.*;

public class ConsoleView {

    // Print board on console
    // P - pawn
    // R - rook
    // N - knight
    // B - bishop
    // K - king
    // Q - queen
    // Uppercase - white, lowercase - black
    public void displayBoard(Board board)
    {
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                Piece piece = board.getPiece(x, y);
                if(piece instanceof Pawn)
                {
                    if(piece.getIsWhite())
                    {
                        System.out.print("P");
                    }
                    else
                    {
                        System.out.print("p");
                    }
                }
                else if(piece instanceof Rook)
                {
                    if(piece.getIsWhite())
                    {
                        System.out.print("R");
                    }
                    else
                    {
                        System.out.print("r");
                    }
                }
                else if(piece instanceof Knight)
                {
                    if(piece.getIsWhite())
                    {
                        System.out.print("N");
                    }
                    else
                    {
                        System.out.print("n");
                    }
                }
                else if(piece instanceof Bishop)
                {
                    if(piece.getIsWhite())
                    {
                        System.out.print("B");
                    }
                    else
                    {
                        System.out.print("b");
                    }
                }
                else if(piece instanceof King)
                {
                    if(piece.getIsWhite())
                    {
                        System.out.print("K");
                    }
                    else
                    {
                        System.out.print("k");
                    }
                }
                else if(piece instanceof Queen)
                {
                    if(piece.getIsWhite())
                    {
                        System.out.print("Q");
                    }
                    else
                    {
                        System.out.print("q");
                    }
                }
                else
                {
                    System.out.print("-");
                }

                System.out.print(" ");
            }

            System.out.println();
        }
    }
}
