package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.pieces.Flyer
import com.chess.pieces.PieceType
import com.chess.users.User
import org.junit.Test

/**
 * Created by jiayu on 2/12/2017.
 */
class FlyerTest extends GroovyTestCase {
    private Board board
    private int defaultSize = 8
    private User[] users = new User[2]
    // first dim corresponds to user1, second dim corresponds to this user's fliers
    private Flyer[] fliers = new Flyer[2]

    /**
     * set up before the test
     */
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA")
        users[1] = new User("TesterB")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], true)
        int[] rowNums = [1, this.defaultSize - 2]
        fliers[0] = this.board.getTile(1, 1).getPiece()
        fliers[1] = this.board.getTile(6, 1).getPiece()
    }

    @Test
    /**
     * test GetPieceType works correctly
     */
    void testGetPieceType() {
        for (int i = 0; i < this.fliers.length; ++i) {
            assertEquals(PieceType.Flyer, fliers[i].getPieceType())
        }
    }

    @Test
    /**
     * test GetOwner works correctly
     */
    void testGetOwner() {
        for (int i = 0; i < this.fliers.length; ++i) {
            User userCurr = this.fliers[i].getOwner()
            assertEquals(this.users[i], userCurr)
        }
    }

    @Test
    /**
     * test UpdateLegalMoves work correctly in various situations
     */
    void testUpdateLegalMoves() {
        assertEquals(1, this.fliers[0].getLegalMoves().size())
        Set<Coordinate> moves = this.fliers[0].getLegalMoves()
        assertTrue(moves.contains(new Coordinate(7, 1)))

        assertEquals(1, this.fliers[1].getLegalMoves().size())
        moves = this.fliers[1].getLegalMoves()
        assertTrue(moves.contains(new Coordinate(0, 1)))
    }

    @Test
    /**
     * test Move method works correctly for this piece
     */
    void testMove() {
        this.fliers[0].move(this.board, 7, 1)
        Set<Coordinate> moves = this.fliers[0].getLegalMoves()

        int currY = fliers[0].getCoordinate().gety()
        for (int i = 1; i < 6; ++i)
            assertTrue(moves.contains(new Coordinate(i, currY)))
        assertTrue(moves.contains(new Coordinate(7, 3)))
        assertEquals(6, moves.size())
    }

}
