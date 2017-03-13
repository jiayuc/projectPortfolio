package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.board.Tile
import com.chess.pieces.Pawn
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.users.User
import org.junit.Before
import org.junit.Test

import java.util.concurrent.ThreadLocalRandom

/**
 * Created by jiayu on 2/6/2017.
 */
class BoardTest extends GroovyTestCase {
    private Board board
    private int defaultSize = 8
    private User[] users = new User[2]

    @Before
    /**
     * set up for the test
     */
    void setUp() {
        super.setUp()
        users[0] = new User("BTester")
        users[1] = new User("WTester")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], false)
    }

    @Test
    /**
     * check if tiles and pieces are set up following the rule
     */
    void testPiecesSetUp() {
        this.board.printBoard()
        System.println(users[0].getName())
        // test tileGrid size
        Tile[][] tileGrid = board.getTileGrid()
        assertTrue(tileGrid.length == this.defaultSize)
        int randomIdx = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
        assertTrue(tileGrid[randomIdx].length == this.defaultSize)
        // test pieces
        int expectedPieceCt = 16
        for (User user : this.users) {
            int actualPieceCt = 0
            for (int i = 0; i < tileGrid.length; ++i) {
                for (int j = 0; j < tileGrid[i].length; ++j) {
                    Piece piece = tileGrid[i][j].getPiece()
                    if (piece == null) continue
                    if (piece.getOwner() == user) {
                        ++actualPieceCt
                    }
                }
            }
            assertEquals("Expect pieces count for user " + user.getName() + " to be ", expectedPieceCt, actualPieceCt)
        }
        this.board.printBoard()
    }

    @Test
    /**
     * test if we can get Tile with the right piece by providing its location
     */
    void testGetTile() {
        Tile tile = board.getTile(0, 2)
        assertTrue(tile != null)
        assertEquals(this.users[0].getName(), tile.getPiece().getOwner().getName())
        assertEquals(PieceType.Bishop, tile.getPiece().getPieceType())
    }

    @Test
    /**
     * test if IsCoordinateValid, can correctly judge validity of coordinates
     */
    void testIsCoordinateValid() {
        int testsCt = 3
        for (int i = 0; i < testsCt; ++i) {
            // test tiles on board that have no piece from the same user
            int randomX = ThreadLocalRandom.current().nextInt(2, this.defaultSize)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(true, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
            randomX = ThreadLocalRandom.current().nextInt(0, 2)
            // test tiles on board that have piece from the same user
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
        }

        for (int i = 0; i < testsCt; ++i) {
            int randomX = ThreadLocalRandom.current().nextInt(2, this.defaultSize)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(true, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
        }

        for (int i = 0; i < testsCt; ++i) {
            int randomX = ThreadLocalRandom.current().nextInt(-100, 0)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomY, randomX))
        }

        for (int i = 0; i < testsCt; ++i) {
            int randomX = ThreadLocalRandom.current().nextInt(this.defaultSize, 100)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomY, randomX))
        }

    }

    @Test
    /**
     * test WithdrawLastMove works correctly
     */
    void testWithdrawLastMove() {
        // 1. test withdraw single piece
        trySingleWithdraw()
        // 2. test withdrawn move that causes piece to be eaten
        tryMultiWithdraw()
        assertEquals(1, this.board.withdrawLastMove())
    }

    /**
     * helper to test single call on WithdrawLastMove works correctly
     */
    void trySingleWithdraw() {
        Piece pawn = board.getTile(1, 0).getPiece()
        assertEquals(PieceType.Pawn, pawn.getPieceType())

        Piece pawnCp = pawn.clone()
        // assert pawn moves successfully
        assertEquals(0, pawn.move(this.board, 3, 0))

        this.board.withdrawLastMove()

        Pawn pawnCurr = board.getTile(1, 0).getPiece()
        pawnCurr.printState()
        pawnCurr.printLegalMoves()

        assertEquals(pawnCp, pawnCurr)
    }

    /**
     * helper to test multiple calls WithdrawLastMove work correctly
     */
    void tryMultiWithdraw() {
        Pawn pawn = board.getTile(1, 0).getPiece()
        int moveCt = 0
        assertEquals(0, pawn.move(this.board, 3, 0))
        ++moveCt
        Pawn pawn2 = board.getTile(6, 1).getPiece()
        assertEquals(0, pawn2.move(this.board, 4, 1))
        ++moveCt
        this.board.printBoard()
        // pawn captures pawn2
        pawn2 = board.getTile(4, 1).getPiece()
        pawn2.printLegalMoves()
        assertEquals(0, pawn2.move(this.board, 3, 0))
        ++moveCt
        this.board.printBoard()
        while (moveCt--) {
            assertEquals(0, this.board.withdrawLastMove())
            this.board.printBoard()
        }
    }

    @Test
    /**
     * test RedoWithdrawnMove work correctly
     */
    void testRedoWithdrawnMove() {
        // 1. test redo single piece
        trySingleRedo()
        // 2. test withdrawn move that causes piece to be eaten
        tryMultiRedo()
    }

    /**
     * helper to test single call on RedoWithdrawnMove works correctly
     */
    void trySingleRedo() {
        Piece pawn = board.getTile(1, 0).getPiece()
        assertEquals(PieceType.Pawn, pawn.getPieceType())

        Piece pawnCp = pawn.clone()
        // assert pawn moves successfully
        assertEquals(0, pawn.move(this.board, 3, 0))
        // test undo, redo, undo sequentially
        this.board.withdrawLastMove()

        Pawn pawnCurr = board.getTile(1, 0).getPiece()
        board.printBoard()
        pawnCp.printLegalMoves()
        pawnCurr.printLegalMoves()
        pawnCurr.printState()
        assertEquals(pawnCp, pawnCurr)
        assertEquals(0, this.board.redoWithdrawnMove())
        assertEquals(pawnCurr.getCoordinate(), new Coordinate(3, 0))
        assertEquals(0, this.board.withdrawLastMove())

        // should not be able to redo move once new moves are made
        pawn = board.getTile(1, 0).getPiece()
        assertEquals(0, pawn.move(this.board, 3, 0))
        assertEquals(1, this.board.redoWithdrawnMove())
        // restore to init state
        assertEquals(0, this.board.withdrawLastMove())
    }

    /**
     * helper to test multiple calls on RedoWithdrawnMove work correctly
     */
    void tryMultiRedo() {
        // simulate regular moves by users
        Pawn pawn = board.getTile(1, 0).getPiece()
        int moveCt = 0
        assertEquals(0, pawn.move(this.board, 3, 0))
        ++moveCt
        Pawn pawn2 = board.getTile(6, 1).getPiece()
        assertEquals(0, pawn2.move(this.board, 4, 1))
        ++moveCt
        this.board.printBoard()
        // pawn eats pawn2
        pawn2 = board.getTile(4, 1).getPiece()
        pawn2.printLegalMoves()
        assertEquals(0, pawn2.move(this.board, 3, 0))
        ++moveCt

        // test  undo, redo, undo sequentially
        for (int i = 0; i < moveCt; ++i) {
            assertEquals(0, this.board.withdrawLastMove())
            this.board.printBoard()
        }
        System.out.println("redoing-----------")
        // perform redo
        for (int i = 0; i < moveCt; ++i) {
            assertEquals(0, this.board.redoWithdrawnMove())
            this.board.printBoard()
        }
        for (int i = 0; i < moveCt; ++i) {
            assertEquals(0, this.board.withdrawLastMove())
            this.board.printBoard()
        }
        // test pawn at initial state, can move 2 steps
        pawn = board.getTile(1, 0).getPiece()
        assertEquals(0, pawn.move(this.board, 3, 0))
        pawn2 = board.getTile(6, 1).getPiece()
        assertEquals(0, pawn2.move(this.board, 4, 1))
        assertEquals(0, this.board.withdrawLastMove())
        assertEquals(0, this.board.withdrawLastMove())
    }

    @Test
    /**
     * Test various cases for checkmate
     */
    void testCheckMate() {
        checkMateCase1()
        this.board.restore(false)
        checkMateCase2()
        this.board.restore(false)
    }

    /**
     * Checkmate case 1: towards white king
     */
    void checkMateCase1() {
        Piece pawnW1 = this.board.getTile(6, 5).getPiece()
        assertEquals(0, pawnW1.move(this.board, 5, 5))

        Piece pawnB1 = this.board.getTile(1, 4).getPiece()
        assertEquals(0, pawnB1.move(this.board, 3, 4))

        Piece pawnW2 = this.board.getTile(6, 6).getPiece()
        assertEquals(0, pawnW2.move(this.board, 4, 6))

        Piece queenB = this.board.getTile(0, 3).getPiece()
        assertEquals(PieceType.Queen, queenB.getPieceType())
        assertEquals(0, queenB.move(this.board, 4, 7))
        this.board.printBoard()
        Piece pawnW3 = this.board.getTile(6, 7).getPiece()
        pawnW3.printLegalMoves()
        assertTrue(this.board.isKingChecked(this.users[1]))
        assertTrue(this.board.isCheckMate(this.users[1]))
    }

    /**
     * Checkmate case 2: towards black king
     */
    void checkMateCase2() {
        this.board.printBoard()
        Piece pawnB1 = this.board.getTile(1, 4).getPiece()
        assertEquals(0, pawnB1.move(this.board, 3, 4))

        Piece knightW1 = this.board.getTile(7, 1).getPiece()
        assertEquals(0, knightW1.move(this.board, 5, 2))

        Piece pawnB2 = this.board.getTile(1, 6).getPiece()
        assertEquals(0, pawnB2.move(this.board, 3, 6))

        assertEquals(0, knightW1.move(this.board, 3, 3))

        Piece knightB1 = this.board.getTile(0, 6).getPiece()
        assertEquals(0, knightB1.move(this.board, 1, 4))
        assertEquals(0, knightW1.move(this.board, 2, 5))

        this.board.printBoard()
        knightW1.printLegalMoves()
        assertTrue(this.board.isKingChecked(this.users[0]))
        assertTrue(this.board.isCheckMate(this.users[0]))
    }
}
