package com.chess.pieces

import com.chess.board.Board
import com.chess.users.User

/**
 * Created by jiayu on 2/6/2017.
 */
class RookTest extends GroovyTestCase {
    private Board board;
    private int defaultSize = 8;
    private User[] users = new User[2];

    void setUp() {
        super.setUp()
        users[0] = new User("TesterA");
        users[1] = new User("TesterB");
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1]);
    }

    void testUpdateLegalMoves() {
        Piece rook = board.getTile(0, 0).getPiece();
        assertEquals(PieceType.Rook, rook.getPieceType());
        assertEquals(0, rook.getLegalMoves().size());

        this.board.movePiece(rook, 4, 4);
        rook.updateLegalMoves(board);
        rook.printLegalMoves();
        checkLegalMovesAt44(rook);
    }

    void testMove() {

    }

    boolean checkLegalMovesAt44(Piece piece) {
        Set<Coordinate> legalMoves = piece.getLegalMoves();
        assertEquals(10, legalMoves.size());
        int currX = piece.coordinate.getx();
        int currY = piece.coordinate.gety();

        // check moves on the same row
        for (int coli=0; coli<this.defaultSize; ++coli) {
            if (coli == currY) continue;
            assertTrue(legalMoves.contains(new Coordinate(currX, coli)));
        }
        // check moves on the same col
        for (int rowi=2; rowi<this.defaultSize-2; ++rowi) {
            if (rowi == currY) continue;
            assertTrue(legalMoves.contains(new Coordinate(rowi, currY)));
        }
    }
}
