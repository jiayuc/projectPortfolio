package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Knight extends Piece{
    /***
     * Default constructor
     */
    protected Knight() {
        super();
    }

    /***
     * Constructor that sets customized owner and position for the piece
     * @param owner user to set as the owner
     * @param x x coordinate of the new position
     * @param y y coordinate of the new position
     */
    public Knight(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Knight);

    }

    /***
     * Update legal moves for the piece on the provided board
     * @param board board that the piece is on
     */
    @Override
    public void updateLegalMoves(Board board) {
        Coordinate currCoordi = this.getCoordinate();
        int[] directions = {-1, 0, 1};

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        for (int i : directions) {
            for (int j : directions) {
                // make sure one and only one direction is 0
                if ((i!=0 && j!=0) || (i==0 && j==0)) {
                    continue;
                }
                // first: move straight for one step
                int currX = currCoordi.getx() + i;
                int currY = currCoordi.gety() + j;
                // next: move diagonally for one step away from original pos
                for (int z=0; z < directions.length; ++z) {
                    if (directions[z] == 0) continue;
                    int dirX = i!=0 ? i : directions[z];
                    int dirY = j!=0 ? j : directions[z];

                    int nextX = currX + dirX;
                    int nextY = currY + dirY;

                    if (board.isCoordinateValidForUser(this.getOwner(), nextX , nextY)) {
                        legalMoves.add(new Coordinate(nextX, nextY));
                    }
                }

            }
        }
        this.legalMoves = legalMoves;
    }
}
