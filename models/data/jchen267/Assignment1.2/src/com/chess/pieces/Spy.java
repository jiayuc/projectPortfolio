package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jiayu on 2/11/2017.
 */
public class Spy extends Piece {
    // use 1/-1 to record forward direction for spy
    private int forwardDir;
    private boolean isFirstMove;

    /**
     * Default constructor
     */
    public Spy() {
        super();
    }

    /**
     * Constructor that sets customized owner and position for the piece
     *
     * @param owner user to set as the owner
     * @param x     x coordinate of the new position
     * @param y     y coordinate of the new position
     */
    public Spy(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Spy);
        this.isFirstMove = true;
        this.forwardDir = this.getCoordinate().getx() == 1 ? 1 : -1;
    }

    /**
     * override method defined in piece, to toggle 'isFirstMove' off
     *
     * @param board board where the piece in on
     * @param x     x coordinate of the new position
     * @param y     y coordinate of the new position
     * @return return 0 if move is legal and preceded
     */
    public int move(Board board, int x, int y) {
        // check if move is in legalMoves
        this.isFirstMove = false;
        if (this.legalMoves.contains(new Coordinate(x, y))) {
            board.movePiece(this, x, y, true);
            this.updateLegalMoves(board);
            return 0;
        }
        return 1;
    }

    /**
     * Update legal moves for the piece on the provided board
     *
     * @param board board that the piece is on
     */
    @Override
    public void updateLegalMoves(Board board) {
        if (this.isFirstMove == true) {
            this.initSpyLegalMoves();
            return;
        }
        Coordinate currCoordi = this.getCoordinate();
        int currX = currCoordi.getx();
        int currY = currCoordi.gety();

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        // try one step forward
        int nextX = currX + this.forwardDir;
        int nextY = currY;
        if ((board.isCoordinateValidForUser(this.getOwner(), nextX, nextY))) {
            legalMoves.add(new Coordinate(nextX, nextY));
        }
        // try diagonal to capture,
        // also try horizontally if spy is currently in enemy's half of the board
        int[] directionsY = {-1, 1};
        List<Integer> directionsX = new ArrayList<Integer>();
        directionsX.add(this.forwardDir);
        if (inEnemyCamp(this))
            directionsX.add(0);

        for (int dirX : directionsX) {
            for (int dirY : directionsY) {
                nextX = currX + dirX;
                nextY = currY + dirY;
                if ((board.isCoordinateValidForUser(this.getOwner(), nextX, nextY))) {
                    // allow move if it leads to capturing of  enemy piece
                    if (board.getTile(nextX, nextY).getPiece() != null) {
                        legalMoves.add(new Coordinate(nextX, nextY));
                    }
                }
            }
            this.legalMoves = legalMoves;
        }
    }

    /**
     * Init legal moves for spies, taking care of the special case of its different rule of first move
     */
    private void initSpyLegalMoves() {
        Coordinate coordi = this.getCoordinate();
        int oldX = coordi.getx();
        int oldY = coordi.gety();

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        legalMoves.add(new Coordinate(oldX + this.forwardDir, oldY));
        legalMoves.add(new Coordinate(oldX + this.forwardDir * 2, oldY));

        this.legalMoves = legalMoves;
    }

    /**
     * Determine if Spy is in the half of board where enemy pieces were set up initially
     *
     * @param spy piece to check
     * @return boolean to indicate whether spy is in the enemy camp
     */
    private boolean inEnemyCamp(Spy spy) {
        Coordinate coordi = spy.getCoordinate();
        // mid-point that separates the board into two halves by the row index
        int midPoint = 7 / 2;
        if (spy.forwardDir == 1)
            return coordi.getx() > midPoint;
        else
            return coordi.getx() < midPoint;
    }

    /**
     * restore spy to initial state where it hasn't moved
     */
    public void restoreToInitState() {
        this.isFirstMove = true;
    }

    /**
     * restore spy to initial state where it hasn't moved
     */
    public void printMoveDir() {
        System.out.println("Forwards dir: " + this.forwardDir);
    }

    @Override
    /**
     * Perform deep copy of itself
     * @return a deep copy of itself
     */
    public Piece clone() {
        Coordinate coordi = this.getCoordinate();
        Spy obj = new Spy(this.getOwner(), coordi.getx(), coordi.gety());
        obj.legalMoves = new HashSet<Coordinate>(this.getLegalMoves());
        // extra attributes of subclass
        obj.forwardDir = this.forwardDir;
        obj.isFirstMove = this.isFirstMove;
        return obj;
    }
}
