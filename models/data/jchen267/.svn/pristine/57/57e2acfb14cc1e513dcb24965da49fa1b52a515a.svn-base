package com.chess.tests

import com.chess.Utility.Coordinate
import com.chess.board.Board
import com.chess.pieces.Bishop
import com.chess.pieces.Piece
import com.chess.pieces.PieceType
import com.chess.users.User
import org.junit.Before
import org.junit.Test

/**
 * Created by jiayu on 2/12/2017.
 */
class BishopTest extends GroovyTestCase {
    private Board board;
    private int defaultSize = 8;
    private User[] users = new User[2];
    private Bishop[] bishops = new Bishop[4];

    @Before
    /**
     * set up for the test
     */
    void setUp() {
        super.setUp()
        users[0] = new User("TesterA");
        users[1] = new User("TesterB");
        this.board = new Board(this.defaultSize, this.defaultSize, this.users[0], this.users[1], false);
        bishops[0] = board.getTile(0, 2).getPiece();
        bishops[1] = board.getTile(0, 5).getPiece();
        bishops[2] = board.getTile(7, 2).getPiece();
        bishops[3] = board.getTile(7, 5).getPiece();
    }

    /**
     * test GetPieceType work correctly
     */
    @Test
    void testGetPieceType() {
        for (int i = 0; i < this.bishops.size(); ++i) {
            assertEquals(PieceType.Bishop, bishops[i].getPieceType());
        }
    }

    /**
     * test UpdateLegalMoves work correctly
     */
    @Test
    void testUpdateLegalMoves() {
        for (int i = 0; i < this.bishops.size(); ++i) {
            // in initial setting, bishop cannot move
            assertEquals(0, bishops[i].getLegalMoves().size());
            Coordinate coordi = bishops[i].getCoordinate();
            int oldX = coordi.getx();
            int oldY = coordi.gety();
            this.board.movePiece(bishops[i], 4, 4, true);
            bishops[i].updateLegalMoves(this.board);
            checkLegalMovesAt44(bishops[i]);

            // restore the location to prepare for the next testing
            this.board.movePiece(bishops[i], oldX, oldY, true);
            bishops[i].updateLegalMoves(this.board);
        }
    }

    /**
     * helper to check legal moves at position (4, 4) is correct
     * @param piece piece we check legal moves for
     */
    void checkLegalMovesAt44(Piece piece) {
        assertEquals(8, piece.getLegalMoves().size());
    }
}
