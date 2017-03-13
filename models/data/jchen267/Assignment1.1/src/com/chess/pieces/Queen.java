package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Queen extends Piece{
    /***
     * Default constructor
     */
    protected Queen() {
        super();
    }

    /***
     * Constructor that sets customized owner and position for the piece
     * @param owner user to set as the owner
     * @param x x coordinate of the new position
     * @param y y coordinate of the new position
     */
    public Queen(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Queen);

    }

    /***
     * Update legal moves for the piece on the provided board
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
                // try to move forward on this direction
                for (int step=1; step<8; ++step) {
                    int nextX = currX + i*step;
                    int nextY = currY + j*step;

                    if (!(board.isCoordinateValidForUser(this.getOwner(), nextX , nextY))) {
                        break;
                    }
                    legalMoves.add(new Coordinate(nextX, nextY));
                    // break if went pass any piece
                    if (board.getTile(nextX, nextY).getPiece() != null) {
                        break;
                    }
                }

            }
        }
        this.legalMoves = legalMoves;
    }

}

