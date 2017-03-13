package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.users.User
import org.junit.Test

/**
 * Created by jiayu on 2/6/2017.
 */
class KingTest extends GroovyTestCase {
    private Board board
    private int defaultSize = 8
    private User[] users = new User[2]

    KingTest() {
        super.setUp()
        users[0] = new User("TesterA")
        users[1] = new User("TesterB")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], false)
    }

    @Test
    /**
     * test if king updates legal moves correctly
     */
    void temp() {
        setUp()
        assertEquals(true, true)
    }

    @Test
    /**
     * test if king updates legal moves correctly
     */
    void testUpdateLegalMoves() {
        Piece kingA = board.getTile(0, 4).getPiece()
        assertEquals(PieceType.King, kingA.getPieceType())
        assertEquals(0, kingA.getLegalMoves().size())

        this.board.movePiece(kingA, 4, 4, true)
        kingA.updateLegalMoves(board)
        checkLegalMovesAt44(kingA)
    }

    @Test
    /**
     * test if king updates legal moves automatically after it moves
     */
    void testMove() {
        Piece kingA = board.getTile(0, 4).getPiece()
        assertEquals(PieceType.King, kingA.getPieceType())
        assertEquals(0, kingA.getLegalMoves().size())

        this.board.movePiece(kingA, 4, 3, true)
        kingA.updateLegalMoves(board)

        assertEquals(0, kingA.move(this.board, 4, 4))
        checkLegalMovesAt44(kingA)
    }

    /**
     * check if the legalMoves are calculated correctly when king is in coordinate (4, 4)
     * @param piece
     * @return
     */
    void checkLegalMovesAt44(Piece piece) {
        Set<Coordinate> legalMoves = piece.getLegalMoves()
        assertTrue(legalMoves.contains(new Coordinate(5, 3)))
        assertTrue(legalMoves.contains(new Coordinate(5, 4)))
        assertTrue(legalMoves.contains(new Coordinate(4, 5)))
        assertTrue(legalMoves.contains(new Coordinate(5, 5)))
        assertTrue(legalMoves.contains(new Coordinate(5, 3)))
    }
}
