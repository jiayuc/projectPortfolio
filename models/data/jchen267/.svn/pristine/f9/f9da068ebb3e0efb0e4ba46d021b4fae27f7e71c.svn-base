package com.chess;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.board.Tile;
import com.chess.pieces.Piece;
import com.chess.users.User;

import java.util.Scanner;

/**
 * Created by jiayu on 2/12/2017.
 */
public class Game {
    private Board board;
    private int defaultSize = 8;
    private User[] users = new User[2];

    public Game() {
        System.out.println("== Welcome to Chess in console mode! ==");
        users[0] = new User("BlackTester");
        users[1] = new User("WhiteTester");
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], false);
        User firstMoveMaker = users[0];
        runGame(firstMoveMaker);
    }

    /**
     * Run game in console mode, taking control to make both users to make legal moves taking turns
     *
     * @param nextMoveMaker user that should make the next move
     */
    private void runGame(User nextMoveMaker) {
        while (!this.board.isKingCaptured(nextMoveMaker)) {
            this.board.printBoard();
            System.out.println("Next!" + nextMoveMaker.getName() + "'s turn...");

            // 1) locate piece to move
            Piece pieceToMove = getPieceToMove(nextMoveMaker);
            System.out.println("---" + nextMoveMaker + "has chosen to move " + pieceToMove.getPieceType() + ". Legal moves are: ");
            pieceToMove.printLegalMoves();

            // 2) get new location for the pieceToMove
            Coordinate targetCoordi = getLocationFromUser().getCoordi();
            // while move is illegal, keep looping
            while (pieceToMove.move(this.board, targetCoordi.getx(), targetCoordi.gety()) == 1) {
                System.out.print("Invalid target tile! Pick again. Reject reason: ");
                targetCoordi = getLocationFromUser().getCoordi();
            }
            // 3) switch turn for users
            System.out.println("===" + nextMoveMaker + "moved. ");
            nextMoveMaker = nextMoveMaker == users[0] ? users[1] : users[0];
        }
    }

    /**
     * Get piece of the nextMoveMaker that user intends to move,
     * by prompting for input at console
     *
     * @param nextMoveMaker user that should make the next move
     * @return piece that the user intends to move
     */
    private Piece getPieceToMove(User nextMoveMaker) {
        Tile tile = getLocationFromUser();
        Piece pieceToMove = tile == null ? null : tile.getPiece();

        while (pieceToMove == null || pieceToMove.getOwner() != nextMoveMaker ||
                pieceToMove.getLegalMoves().isEmpty()) {
            System.out.println("Invalid source tile! Rejection reason: ");

            if (pieceToMove == null) {
                System.out.print("No piece on target tile\n");
            } else if (pieceToMove.getOwner() != nextMoveMaker) {
                System.out.print("Not your piece\n");
            } else {
                System.out.print(pieceToMove.getPieceType() + " No move possible for this piece\n");
            }

            tile = getLocationFromUser();
            pieceToMove = tile == null ? null : tile.getPiece();
        }
        return pieceToMove;
    }

    /**
     * Prompt user to input a coordinate, return the tile at that coordinate
     *
     * @return tile at the coordinate that specified by the user
     */
    private Tile getLocationFromUser() {
        // Reading from System.in
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter row index of the piece: ");
        int rowIdx = reader.nextInt();
        System.out.println("Enter col index of the piece: ");
        int colIdx = reader.nextInt();
        return this.board.getTile(rowIdx, colIdx);
    }

}
