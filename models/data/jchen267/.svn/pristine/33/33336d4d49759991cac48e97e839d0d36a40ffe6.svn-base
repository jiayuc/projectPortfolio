package com.chess.pieces;

import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Knight extends Piece{
    protected Knight() {
        super();
    }

    public Knight(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Knight);

    }
    @Override
    public void updateLegalMoves(Board board) {
        Coordinate currCoordi = this.getCoordinate();
        int currX = currCoordi.getx();
        int currY = currCoordi.gety();
        int[] directions = {-1, 0, 1};

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        for (int i : directions) {
            for (int j : directions) {
                if (board.isCoordinateValid(this.getOwner(), currX+i, currY+j)) {
                    legalMoves.add(new Coordinate(currX+i, currY+j));
                }
            }
        }
        this.legalMoves = legalMoves;
    }
}
