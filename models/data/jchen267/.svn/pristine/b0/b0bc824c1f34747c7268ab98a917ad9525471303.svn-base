package com.chess.tests

import com.chess.board.Board
import com.chess.Utility.Coordinate
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.users.User
import org.junit.Before
import org.junit.Test

/**
 * Created by jiayu on 2/6/2017.
 */
class RookTest extends GroovyTestCase {
    private Board board
    private int defaultSize = 8
    private User[] users = new User[2]
    @Before
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA")
        users[1] = new User("TesterB")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1])
    }
    @Test
    void testUpdateLegalMoves() {
        Piece rook = board.getTile(0, 0).getPiece()
        assertEquals(PieceType.Rook, rook.getPieceType())
        assertEquals(0, rook.getLegalMoves().size())

        this.board.movePiece(rook, 4, 4)
        rook.updateLegalMoves(board)
        checkLegalMovesAt44(rook)
    }

    @Test
    void testMove() {
        Piece rook = board.getTile(0, 0).getPiece()
        int testCt = 10
        for (int i=0; i<testCt; ++i) {
            int randX = (-1) + (int)(Math.random() * this.defaultSize)
            int randY = (-1) + (int)(Math.random() * this.defaultSize)
            // Rook should fail to make any move, meaning return 1 as error code
            assertEquals(1, rook.move(this.board, randX, randY))
        }
    }

    boolean checkLegalMovesAt44(Piece piece) {
        Set<Coordinate> legalMoves = piece.getLegalMoves()
        assertEquals(11, legalMoves.size())
        int currX = piece.coordinate.getx()
        int currY = piece.coordinate.gety()

        // check moves on the same row
        for (int coli=0; coli<this.defaultSize; ++coli) {
            if (coli == currY) continue
            assertTrue(legalMoves.contains(new Coordinate(currX, coli)))
        }
        // check moves on the same col
        for (int rowi=2; rowi<this.defaultSize-1; ++rowi) {
            if (rowi == currY) continue
            assertTrue(legalMoves.contains(new Coordinate(rowi, currY)))
        }
    }
}
