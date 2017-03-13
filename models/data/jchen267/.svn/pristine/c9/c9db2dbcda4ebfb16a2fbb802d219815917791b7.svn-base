package com.chess.tests

import com.chess.board.Board
import com.chess.board.Tile
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
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA")
        users[1] = new User("TesterB")
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1])
    }

    @Test
    /***
     * check if tiles and pieces are set up following the rule
     */
    void testPiecesSetUp() {
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
            for (int i=0; i<tileGrid.length; ++i) {
                for (int j=0; j<tileGrid[i].length; ++j) {
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
    /***
     * test if we can get Tile with the right piece by providing its location
     */
    void testGetTile() {
        Tile tile = board.getTile(0, 2)
        assertTrue(tile != null)
        assertEquals(this.users[0].getName(), tile.getPiece().getOwner().getName())
        assertEquals(PieceType.Bishop, tile.getPiece().getPieceType())
    }

    @Test
    /***
     * test if IsCoordinateValid, can correctly judge validity of coordinates
     */
    void testIsCoordinateValid() {
        int testsCt = 3
        for (int i=0; i<testsCt; ++i) {
            // test tiles on board that have no piece from the same user
            int randomX = ThreadLocalRandom.current().nextInt(2, this.defaultSize)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(true, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
            randomX = ThreadLocalRandom.current().nextInt(0, 2)
            // test tiles on board that have piece from the same user
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
        }

        for (int i=0; i<testsCt; ++i) {
            int randomX = ThreadLocalRandom.current().nextInt(2, this.defaultSize)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(true, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
        }

        for (int i=0; i<testsCt; ++i) {
            int randomX = ThreadLocalRandom.current().nextInt(-100, 0)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomY, randomX))
        }

        for (int i=0; i<testsCt; ++i) {
            int randomX = ThreadLocalRandom.current().nextInt(this.defaultSize, 100)
            int randomY = ThreadLocalRandom.current().nextInt(0, this.defaultSize)
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomX, randomY))
            assertEquals(false, board.isCoordinateValidForUser(this.users[0], randomY, randomX))
        }

    }

}
