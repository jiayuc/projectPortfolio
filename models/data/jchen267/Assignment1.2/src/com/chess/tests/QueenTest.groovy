package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.pieces.Queen
import com.chess.users.User
import org.junit.Before
import org.junit.Test

/**
 * Created by jiayu on 2/12/2017.
 */
class QueenTest extends GroovyTestCase {
    private Board board;
    private int defaultSize = 8;
    private User[] users = new User[2];
    private Queen[] queens = new Queen[2];

    @Before
    /**
     * set up before the test
     */
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA");
        users[1] = new User("TesterB");
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], false);
        queens[0] = board.getTile(0, 3).getPiece();
        queens[1] = board.getTile(7, 3).getPiece();
    }

    @Test
    /**
     * test GetPieceType works correctly
     */
    void testGetPieceType() {
        assertEquals(PieceType.Queen, queens[0].getPieceType());
        assertEquals(PieceType.Queen, queens[1].getPieceType());
    }

    @Test
    /**
     * test UpdateLegalMoves work correctly in various situations
     */
    void testUpdateLegalMoves() {
        for (int i = 0; i < this.queens.size(); ++i) {
            // in initial setting, queen cannot move
            assertEquals(0, queens[i].getLegalMoves().size());
            Coordinate coordi = queens[i].getCoordinate();
            int oldX = coordi.getx();
            int oldY = coordi.gety();

            this.board.movePiece(queens[i], 4, 4, true);
            queens[i].updateLegalMoves(board);
            checkLegalMovesAt44(queens[i]);
            this.board.movePiece(queens[i], oldX, oldY, true);
        }
    }

    /**
     * helper to check legal moves at position (4, 4) is correct
     * @param piece piece we check legal moves for
     */
    void checkLegalMovesAt44(Piece piece) {
        assertEquals(19, piece.getLegalMoves().size());
        Set<Coordinate> moves = piece.getLegalMoves();
        // check moves on the same row
        int currX = piece.getCoordinate().getx();
        int currY = piece.getCoordinate().gety();
        for (int coli = 0; coli < this.defaultSize; ++coli) {
            if (coli == currY) continue;
            assertTrue(moves.contains(new Coordinate(currX, coli)));
        }
        // check moves on the same col
        for (int rowi = 2; rowi < this.defaultSize - 2; ++rowi) {
            // can't be at myself or the other queen
            if (rowi == currY) continue;
            assertTrue(moves.contains(new Coordinate(rowi, currY)));
        }
        // check piece on diagonal
        assertTrue(moves.contains(new Coordinate(3, 3)));
        assertTrue(moves.contains(new Coordinate(2, 2)));
        assertTrue(moves.contains(new Coordinate(3, 5)));
        assertTrue(moves.contains(new Coordinate(2, 6)));
        assertTrue(moves.contains(new Coordinate(5, 3)));
        assertTrue(moves.contains(new Coordinate(5, 5)));
        // check eat enemy pieces

        if (this.board.getTile(1, 4).getPiece().getOwner() == piece.getOwner()) {
            // if moved queen from lower-indexed rows
            Piece enemyPiece = this.board.getTile(6, 4).getPiece();
            assertTrue(moves.contains(enemyPiece.getCoordinate()));
            enemyPiece = this.board.getTile(6, 2).getPiece();
            assertTrue(moves.contains(enemyPiece.getCoordinate()));
            enemyPiece = this.board.getTile(6, 6).getPiece();
            assertTrue(moves.contains(enemyPiece.getCoordinate()));
        } else {
            // if moved queen from upper-indexed rows
            Piece enemyPiece = this.board.getTile(1, 4).getPiece();
            assertTrue(moves.contains(enemyPiece.getCoordinate()));
            enemyPiece = this.board.getTile(1, 1).getPiece();
            assertTrue(moves.contains(enemyPiece.getCoordinate()));
            enemyPiece = this.board.getTile(1, 7).getPiece();
            assertTrue(moves.contains(enemyPiece.getCoordinate()));

        }
    }

}
