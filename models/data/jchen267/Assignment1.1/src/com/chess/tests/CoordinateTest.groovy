package com.chess.tests

import com.chess.Utility.Coordinate

/**
 * Created by jiayu on 2/6/2017.
 */
class CoordinateTest extends GroovyTestCase {
    void testEquals() {
        Coordinate coordiA = new Coordinate(2, 4)
        Coordinate coordiB = new Coordinate(2, 4)
        assertTrue(coordiA == coordiB)
        coordiA.setxy(0, 5)
        assertTrue(coordiA != coordiB)
    }
}
