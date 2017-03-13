package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.pieces.Spy
import com.chess.users.User
import org.junit.Before
import org.junit.Test

/**
 * Created by jiayu on 2/12/2017.
 */
class SpyTest extends GroovyTestCase {
    private Board board
    private int defaultSize = 8
    private User[] users = new User[2]
    private Spy[] spies = new Spy[2]

    @Before
    /**
     * set up before the test
     */
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA")
        users[1] = new User("TesterB")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], true)
        int[] rowNums = [1, this.defaultSize - 2]

        spies[0] = board.getTile(1, 6).getPiece()
        spies[1] = board.getTile(6, 6).getPiece()
    }

    @Test
    /**
     * test GetPieceType works correctly
     */
    void testGetPieceType() {
        for (int i = 0; i < this.spies.length; ++i) {
            assertEquals(PieceType.Spy, spies[i].getPieceType())
        }
    }

    @Test
    /**
     * test GetOwner works correctly
     */
    void testGetOwner() {
        for (int i = 0; i < this.spies.length; ++i) {
            User userCurr = this.spies[i].getOwner()
            assertEquals(this.users[i], userCurr)
        }
    }

    @Test
    /**
     * test UpdateLegalMoves work correctly in various situations
     */
    void testUpdateLegalMoves() {
        for (int i = 0; i < this.spies.length; ++i) {
            Piece spy = this.spies[i]
            Coordinate coordi = spy.getCoordinate()
            int oldX = coordi.getx()
            int oldY = coordi.gety()
            int moveXDir = oldX == 1 ? 1 : -1
            // in initial setting, spy can move forward one or two step
            assertEquals(2, spy.getLegalMoves().size())
            Set<Coordinate> moves = spy.getLegalMoves()
            assertTrue(moves.contains(new Coordinate(oldX + moveXDir, oldY)))
            assertTrue(moves.contains(new Coordinate(oldX + moveXDir * 2, oldY)))
        }
    }

    @Test
    /**
     * test move method on piece work correctly in various situations
     */
    void testMove() {
        Piece spy = this.spies[0]
        Coordinate coordi = spy.getCoordinate()
        int oldX = coordi.getx()
        int oldY = coordi.gety()
        int moveXDir = oldX == 1 ? 1 : -1
        // in initial setting, spy can move forward one or two step
        // can take two step initially
        assertEquals(0, spy.move(this.board, oldX + moveXDir * 2, oldY))
        // cannot take two steps afterwards
        assertEquals(1, spy.move(this.board, oldX + moveXDir * 2, oldY))

        checkMovementsAt66(spy)
    }

    /**
     * helper to check legal moves at position (6, 6) is correct
     * @param piece piece we check legal moves for
     */
    void checkMovementsAt66(Spy spy) {
        List<Coordinate> coords = new Coordinate[0]
        coords.add(new Coordinate(6, 5))
        coords.add(new Coordinate(7, 5))
        coords.add(new Coordinate(7, 6))
        coords.add(new Coordinate(7, 7))
        coords.add(new Coordinate(6, 7))

        // should be able to move to 5 positions at position 6, 6
        for (int i = 0; i < coords.size(); ++i) {
            // set up the pieces for testing condition
            this.board.movePiece(spy, 6, 6, true)
            spy.updateLegalMoves(this.board)
            assertEquals(0, spy.move(this.board, coords.get(i).getx(), coords.get(i).gety()))
        }
    }
}
