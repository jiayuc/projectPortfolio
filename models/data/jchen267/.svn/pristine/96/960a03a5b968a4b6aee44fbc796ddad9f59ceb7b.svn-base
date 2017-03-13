package com.chess.pieces

import com.chess.board.Board
import com.chess.users.User
import org.junit.Test

/**
 * Created by jiayu on 2/6/2017.
 */
class KingTest extends GroovyTestCase {
    private Board board;
    private int defaultSize = 8;
    private User[] users = new User[2];

    void setUp() {
        super.setUp()
        users[0] = new User("TesterA");
        users[1] = new User("TesterB");
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1]);
    }

    @Test
    /***
     * test if king updates legal moves correctly
     */
    void testUpdateLegalMoves() {
        Piece kingA = board.getTile(0, 4).getPiece();
        assertEquals(PieceType.King, kingA.getPieceType());
        assertEquals(0, kingA.getLegalMoves().size());

        this.board.movePiece(kingA, 4, 4)
        kingA.updateLegalMoves(board);
        checkLegalMovesAt44(kingA);
    }

    @Test
    /***
     * test if king updates legal moves automatically after it moves
     */
    void testMove() {
        Piece kingA = board.getTile(0, 4).getPiece();
        assertEquals(PieceType.King, kingA.getPieceType());
        assertEquals(0, kingA.getLegalMoves().size());

        this.board.movePiece(kingA, 4, 3)
        kingA.updateLegalMoves(board);

        assertEquals(0, kingA.move(this.board, 4, 4));
        checkLegalMovesAt44(kingA);
    }

    /***
     * check if the legalMoves are calculated correctly when king is in coordinate (4, 4)
     * @param piece
     * @return
     */
    boolean checkLegalMovesAt44(Piece piece) {
        Set<Coordinate> legalMoves = piece.getLegalMoves();
        assertEquals(8, legalMoves.size());

        assertTrue(legalMoves.contains(new Coordinate(5, 3)));
        assertTrue(legalMoves.contains(new Coordinate(3, 4)));
        assertTrue(legalMoves.contains(new Coordinate(5, 4)));
        assertTrue(legalMoves.contains(new Coordinate(4, 3)));
        assertTrue(legalMoves.contains(new Coordinate(4, 5)));
        assertTrue(legalMoves.contains(new Coordinate(3, 3)));
        assertTrue(legalMoves.contains(new Coordinate(5, 5)));
        assertTrue(legalMoves.contains(new Coordinate(3, 5)));
        assertTrue(legalMoves.contains(new Coordinate(5, 3)));
    }
}
