package com.chess.pieces;

import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Bishop extends Piece{
    protected Bishop() {
        super();
    }

    public Bishop(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Bishop);

    }

    @Override
    public void updateLegalMoves(Board board) {
        Coordinate currCoordi = this.getCoordinate();
        int currX = currCoordi.getx();
        int currY = currCoordi.gety();
        int[] directions = {-1, 1};

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        for (int i : directions) {
            for (int j : directions) {
                for (int step=1; step<8; ++step) {
                    int nextX = currX + i*step;
                    int nextY = currY + i*step;
                    if (board.isCoordinateValid(this.getOwner(), nextX, nextY) == false) continue;
                    legalMoves.add(new Coordinate(nextX, nextY));
                    // break if passed any piece
                    if (board.getTile(nextX, nextY) != null) break;
                }
            }
        }
        this.legalMoves = legalMoves;
    }

}
