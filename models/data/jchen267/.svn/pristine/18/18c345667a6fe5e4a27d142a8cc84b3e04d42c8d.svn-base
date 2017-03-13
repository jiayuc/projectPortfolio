package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class King extends Piece {
    /**
     * Default constructor
     */
    protected King() {
        super();
    }

    /**
     * Constructor that sets customized owner and position for the piece
     *
     * @param owner user to set as the owner
     * @param x     x coordinate of the new position
     * @param y     y coordinate of the new position
     */
    public King(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.King);
    }

    /**
     * Update legal moves for the piece on the provided board
     *
     * @param board board that the piece is on
     */
    @Override
    public void updateLegalMoves(Board board) {
        Coordinate currCoordi = this.getCoordinate();
        int currX = currCoordi.getx();
        int currY = currCoordi.gety();

        int[] directions = {-1, 0, 1};

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        for (int i : directions) {
            for (int j : directions) {
                if (!board.isCoordinateValidForUser(this.getOwner(), currX + i, currY + j)) continue;
                // if such move makes itself in check, invalid
                if (!board.isMoveSafeForKing(this.getOwner(), currX + i, currY + j)) continue;
                legalMoves.add(new Coordinate(currX + i, currY + j));
            }
        }
        this.legalMoves = legalMoves;
    }

    @Override
    /**
     * Perform deep copy of itself
     * @return a deep copy of itself
     */
    public Piece clone() {
        Coordinate coordi = this.getCoordinate();
        King obj = new King(this.getOwner(), coordi.getx(), coordi.gety());
        obj.legalMoves = new HashSet<Coordinate>(this.getLegalMoves());
        return obj;
    }
}
