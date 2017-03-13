package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Pawn extends Piece{
    // use 1/-1 to record forward direction for pawn
    private int forwardDir;
    private boolean isFirstMove;

    /***
     * Default constructor
     */
    public Pawn() {
        super();
    }

    /***
     * Constructor that sets customized owner and position for the piece
     * @param owner user to set as the owner
     * @param x x coordinate of the new position
     * @param y y coordinate of the new position
     */
    public Pawn(User owner, int x, int y) {
        super(owner, x, y);
        super.setPieceType(PieceType.Pawn);
        this.isFirstMove = true;
        this.forwardDir = this.getCoordinate().getx() == 1? 1 : -1;
    }

    /***
     * Update legal moves for the piece on the provided board
     * @param board board that the piece is on
     */
    @Override
    public void updateLegalMoves(Board board) {
        if (this.isFirstMove == true) {
            this.initPawnLegalMoves();
            return;
        }
        Coordinate currCoordi = this.getCoordinate();
        int currX = currCoordi.getx();
        int currY = currCoordi.gety();

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        // try one step forward
        int nextX = currX + this.forwardDir;
        int nextY = currY;
        if ((board.isCoordinateValidForUser(this.getOwner(), nextX , nextY))) {
            legalMoves.add(new Coordinate(nextX, nextY));
        }
        // try diagonal to capture
        int[] directions = {-1, 1};
        for (int dirY : directions) {
            int dirX = this.forwardDir;
            nextX = currX + dirX;
            nextY = currY + dirY;
            if ((board.isCoordinateValidForUser(this.getOwner(), nextX , nextY))) {
                // allow diagonal move if it leads to capture enemy piece
                if (board.getTile(nextX, nextY).getPiece() != null) {
                    legalMoves.add(new Coordinate(nextX, nextY));
                }
            }
        }
        this.legalMoves = legalMoves;
    }

    /***
     * Init legal moves for pawns, taking care of the special case of its different rule of first move
     */
    private void initPawnLegalMoves() {
        Coordinate coordi = this.getCoordinate();
        int oldX = coordi.getx();
        int oldY = coordi.gety();
        int moveXDir = oldX == 1? 1 : -1;

        Set<Coordinate> legalMoves = new HashSet<Coordinate>();
        legalMoves.add(new Coordinate(oldX + moveXDir, oldY));
        legalMoves.add(new Coordinate(oldX + moveXDir*2, oldY));
        this.legalMoves = legalMoves;
    }

    /***
     *  override method defined in piece, to toggle 'isFirstMove' off
     * @param board board where the piece in on
     * @param x  x coordinate of the new position
     * @param y y coordinate of the new position
     * @return return 0 if move is legal and preceded
     */
    @Override
    public int move(Board board, int x, int y) {
        // check if move is in legalMoves
        this.isFirstMove = false;
        if (this.legalMoves.contains(new Coordinate(x, y))) {
            board.movePiece(this, x, y);
            this.updateLegalMoves(board);
            return 0;
        }
        return 1;
    }

    /***
     * restore pawn to initial state where it hasn't moved
     */
    public void restoreToInitState() {
        this.isFirstMove = true;
    }

    /***
     * restore pawn to initial state where it hasn't moved
     */
    public void printMoveDir() {
        System.out.println("Forwards dir: " + this.forwardDir);
    }

}
