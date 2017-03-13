package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.users.User
import org.junit.Before
import org.junit.Test

/**
 * Created by jiayu on 2/12/2017.
 */
class PawnTest extends GroovyTestCase {
    private Board board
    private int defaultSize = 8
    private User[] users = new User[2]
    // first dim corresponds to user1, second dim corresponds to this user's pawns
    private Piece[][] pawns = new Piece[2][6]

    @Before
    /**
     * set up before the test
     */
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA")
        users[1] = new User("TesterB")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], false)
        int[] rowNums = [1, 6]

        for (int i = 0; i < rowNums.length; ++i) {
            int rowNum = rowNums[i]
            int colNum = 0
            for (int j = 0; j < this.defaultSize; ++j) {
                if (j == 1 || j == 6) continue
                pawns[i][colNum++] = board.getTile(rowNum, j).getPiece()
            }
        }
    }

    @Test
    /**
     * test GetPieceType works correctly
     */
    void testGetPieceType() {
        for (int i = 0; i < this.pawns.length; ++i) {
            for (int j = 0; j < this.pawns[i].length; ++j) {
                assertEquals(PieceType.Pawn, pawns[i][j].getPieceType())
            }
        }
    }

    @Test
    /**
     * test GetOwner works correctly
     */
    void testGetOwner() {
        for (int i = 0; i < this.pawns.length; ++i) {
            for (int j = 0; j < this.pawns[i].length; ++j) {
                User userCurr = this.pawns[i][j].getOwner()
                assertEquals(this.users[i], userCurr)
            }
        }
    }

    @Test
    /**
     * test UpdateLegalMoves work correctly in various situations
     */
    void testUpdateLegalMoves() {
        for (int i = 0; i < this.pawns.length; ++i) {
            for (int j = 0; j < this.pawns[i].size(); ++j) {
                Piece pawn = this.pawns[i][j]
                Coordinate coordi = pawn.getCoordinate()
                int oldX = coordi.getx()
                int oldY = coordi.gety()
                int moveXDir = oldX == 1 ? 1 : -1
                // in initial setting, pawn can move forward one or two step
                assertEquals(2, pawn.getLegalMoves().size())
                Set<Coordinate> moves = pawn.getLegalMoves()
                assertTrue(moves.contains(new Coordinate(oldX + moveXDir, oldY)))
                assertTrue(moves.contains(new Coordinate(oldX + moveXDir * 2, oldY)))
            }
        }
    }

    @Test
    /**
     * test Move method works correctly for this piece
     */
    void testMove() {
        for (int i = 0; i < this.pawns.length; ++i) {
            for (int j = 0; j < this.pawns[i].size(); ++j) {
                Piece pawn = this.pawns[i][j]
                pawn.restoreToInitState()
                Coordinate coordi = pawn.getCoordinate()
                int oldX = coordi.getx()
                int oldX_cp = oldX
                int oldY = coordi.gety()
                int oldY_cp = oldY
                int moveXDir = oldX == 1 ? 1 : -1
                // in initial setting, pawn can move forward one or two step
                // can take two step initially
                assertEquals(0, pawn.move(this.board, oldX + moveXDir * 2, oldY))
                // cannot take two steps afterwards
                assertEquals(1, pawn.move(this.board, oldX + moveXDir * 2, oldY))
                // test capture diagonally
                for (int step = 0; step < 2; ++step) {
                    assertEquals(1, pawn.getLegalMoves().size())
                    oldX = pawn.getCoordinate().getx()
                    assertEquals(0, pawn.move(this.board, oldX + moveXDir * 1, oldY))
                }
                int expectedMoveCt = (oldY == 0 || oldY == this.defaultSize - 1) ? 1 : 2
                this.board.printBoard()
                assertEquals(expectedMoveCt, pawn.getLegalMoves().size())
                pawn.restoreToInitState()
                this.board.movePiece(pawn, oldX_cp, oldY_cp, true)
            }
        }
    }

}
