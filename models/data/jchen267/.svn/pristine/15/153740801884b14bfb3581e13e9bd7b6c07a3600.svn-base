package com.chess.pieces;

import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Rook extends Piece{
    protected Rook() {
        super();
    }

    public Rook(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Rook);

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
                // make sure one and only one direction is 0
                if ((i!=0 && j!=0) || (i==0 && j==0))
                    continue;
                for (int step=1; step<8; ++step) {
                    int nextX = currX + i*step;
                    int nextY = currY + j*step;
                    if (!(board.isCoordinateValid(this.getOwner(), nextX , nextY))) {
                        System.out.println("Invalid pos: " + nextX + " , " +nextY);
                        System.out.println("Curr pos: " + currX + " , " + currY + " = to dir " + i + " , " + j);
                        break;
                    }
                    legalMoves.add(new Coordinate(nextX, nextY));
                    // break if passed any piece
                    if (board.getTile(nextX, nextY) != null) {
                        System.out.println(board.getTile(nextX, nextY).getPiece());
                        break;
                    }
                }
            }
        }
        this.legalMoves = legalMoves;
    }
}
