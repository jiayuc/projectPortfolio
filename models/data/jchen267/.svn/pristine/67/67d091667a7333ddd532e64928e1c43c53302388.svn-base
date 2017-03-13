package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Bishop extends Piece {
    /**
     * Default constructor
     */
    public Bishop() {
        super();
    }

    /**
     * Constructor that sets customized owner and position for the piece
     *
     * @param owner user to set as the owner
     * @param x     x coordinate of the new position
     * @param y     y coordinate of the new position
     */
    public Bishop(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Bishop);

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
        int[] directions = {-1, 1};

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        for (int i : directions) {
            for (int j : directions) {
                for (int step = 1; step < 8; ++step) {
                    int nextX = currX + i * step;
                    int nextY = currY + j * step;
                    // if position already invalid, try next direction
                    if (board.isCoordinateValidForUser(this.getOwner(), nextX, nextY) == false) break;
                    legalMoves.add(new Coordinate(nextX, nextY));
                    // if passed any piece(enemy piece), try next direction
                    if (board.getTile(nextX, nextY).getPiece() != null) break;
                }
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
        Bishop obj = new Bishop(this.getOwner(), coordi.getx(), coordi.gety());
        obj.legalMoves = new HashSet<Coordinate>(this.getLegalMoves());
        return obj;
    }
}
