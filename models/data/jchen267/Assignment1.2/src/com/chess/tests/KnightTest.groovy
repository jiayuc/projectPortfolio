package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.pieces.Knight
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.users.User
import org.junit.Before
import org.junit.Test

/**
 * Created by jiayu on 2/12/2017.
 */
class KnightTest extends GroovyTestCase {
    private Board board
    private int defaultSize = 8
    private User[] users = new User[2]
    private Knight[] knights = new Knight[4]

    @Before
    /**
     * set up before the test
     */
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA")
        users[1] = new User("TesterB")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], false)
        knights[0] = board.getTile(0, 1).getPiece()
        knights[1] = board.getTile(0, 6).getPiece()
        knights[2] = board.getTile(7, 1).getPiece()
        knights[3] = board.getTile(7, 6).getPiece()
    }

    @Test
    /**
     * test GetPieceType works correctly
     */
    void testGetPieceType() {
        for (int i = 0; i < this.knights.size(); ++i)
            assertEquals(PieceType.Knight, knights[i].getPieceType())
    }

    @Test
    /**
     * test UpdateLegalMoves work correctly in various situations
     */
    void testUpdateLegalMoves() {
        for (int i = 0; i < this.knights.size(); ++i) {
            // in initial setting, queen cannot move
            assertEquals(2, knights[i].getLegalMoves().size())
            Coordinate coordi = knights[i].getCoordinate()
            int oldX = coordi.getx()
            int oldY = coordi.gety()

            this.board.movePiece(knights[i], 4, 4, true)
            knights[i].updateLegalMoves(board)
            checkLegalMovesAt44(knights[i])
            this.board.movePiece(knights[i], oldX, oldY, true)
        }
    }

    /**
     * helper to check legal moves at position (4, 4) is correct
     * @param piece piece we check legal moves for
     */
    void checkLegalMovesAt44(Piece piece) {
        Set<Coordinate> moves = piece.getLegalMoves()
        // check moves in common for knight of each user
        assertTrue(moves.contains(new Coordinate(2, 3)))
        assertTrue(moves.contains(new Coordinate(2, 5)))
        assertTrue(moves.contains(new Coordinate(3, 2)))
        assertTrue(moves.contains(new Coordinate(5, 2)))
        assertTrue(moves.contains(new Coordinate(3, 6)))
        assertTrue(moves.contains(new Coordinate(5, 6)))
        // check eat enemy pieces

        if (this.board.getTile(6, 3).getPiece().getOwner() != piece.getOwner()) {
            // if moved queen from lower-indexed rows
            Piece enemyPiece = this.board.getTile(6, 3).getPiece()
            assertTrue(moves.contains(enemyPiece.getCoordinate()))
            enemyPiece = this.board.getTile(6, 5).getPiece()
            assertTrue(moves.contains(enemyPiece.getCoordinate()))
            assertEquals(8, piece.getLegalMoves().size())
        } else {
            assertEquals(6, piece.getLegalMoves().size())
        }
    }

}
