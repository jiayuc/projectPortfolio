package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/11/2017.
 * Flyer:
 * can move to any tile vertically or horizontally,
 * provided that there's one and only one piece as "stepping stone" in the way
 * (between current location and target location)
 */

public class Flyer extends Piece {
    /**
     * Default constructor
     */
    public Flyer() {
        super();
    }

    /**
     * Constructor that sets customized owner and position for the piece
     *
     * @param owner user to set as the owner
     * @param x     x coordinate of the new position
     * @param y     y coordinate of the new position
     */
    public Flyer(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Flyer);
    }

    /**
     * Init legal moves for fliers, taking care of the special case of its different rule of first move
     */
    private void initFlyerLegalMoves() {
        Coordinate coordi = this.getCoordinate();
        int oldX = coordi.getx();
        int oldY = coordi.gety();
        int moveXDir = oldX == 1 ? 1 : -1;

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        legalMoves.add(new Coordinate(oldX + moveXDir, oldY));
        legalMoves.add(new Coordinate(oldX + moveXDir * 2, oldY));
        this.legalMoves = legalMoves;
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

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();

        int[] directions = {-1, 0, 1};
        for (int i : directions) {
            for (int j : directions) {
                // make sure one and only one direction is 0
                if ((i != 0 && j != 0) || (i == 0 && j == 0))
                    continue;
                boolean foundSteppingStone = false;
                for (int step = 1; step < 8; ++step) {
                    int nextX = currX + i * step;
                    int nextY = currY + j * step;
                    // check next direction if target position is already out of bound
                    if (board.getTile(nextX, nextY) == null) break;

                    // if already has stepping stone, add target position and check for piece on target position
                    Piece piece = board.getTile(nextX, nextY).getPiece();
                    if (foundSteppingStone) {
                        if (piece == null) {
                            legalMoves.add(new Coordinate(nextX, nextY));
                            continue;
                        }

                        if (piece.getOwner() != this.getOwner()) {
                            // capture enemy piece
                            legalMoves.add(new Coordinate(nextX, nextY));
                        }
                        break;
                    }
                    // no stepping stone yet
                    foundSteppingStone = piece != null;
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
        Flyer obj = new Flyer(this.getOwner(), coordi.getx(), coordi.gety());
        obj.legalMoves = new HashSet<Coordinate>(this.getLegalMoves());
        return obj;
    }
}
