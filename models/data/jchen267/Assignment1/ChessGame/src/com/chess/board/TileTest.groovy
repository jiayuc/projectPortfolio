package com.chess.board

import org.junit.Test

/**
 * Created by jiayu on 2/5/2017.
 */
class TileTest extends GroovyTestCase {
    void setUp() {
        super.setUp();

    }

    /**
     * test tile constructor
     * and the tile should be empty and piece should be null when first initialized
     */
    @Test
    void testGetPiece(){
        Tile tile = new Tile(1, 1, Tile.color.BLACK);
        assertTrue(tile.getColor() == Tile.color.BLACK);
        assertTrue(tile.isTileFree());
        assertNull(tile.getPiece());
    }

}
