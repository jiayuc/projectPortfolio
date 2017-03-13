package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Tile
import com.chess.pieces.King
import com.chess.users.User
import org.junit.Test

/**
 * Created by jiayu on 2/5/2017.
 */
class TileTest extends GroovyTestCase {
    void setUp() {
        super.setUp()

    }

    @Test
    /**
     * test tile constructor
     * and the tile should be empty and piece should be null when first initialized
     */
    void testSetAndGetPiece() {
        Coordinate coordi = new Coordinate(2, 3)
        Tile tile = new Tile(coordi.getx(), coordi.gety(), Tile.color.BLACK)
        assertEquals(coordi, tile.getCoordi())
        assertTrue(tile.getColor() == Tile.color.BLACK)
        assertTrue(tile.isTileFree())
        assertNull(tile.getPiece())
        // set piece and check if can retreive that piece
        King kPiece = new King(new User(), coordi.getx(), coordi.gety())
        tile.setPiece(kPiece)
        assertEquals(kPiece, tile.getPiece())
    }

}
