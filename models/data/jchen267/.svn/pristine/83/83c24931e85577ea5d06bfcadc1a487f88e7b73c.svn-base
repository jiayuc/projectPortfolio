package com.chess.pieces;

import com.chess.board.Board;
import com.chess.users.User;

import java.util.Set;

/**
 * Created by jiayu on 2/4/2017.
 */
public abstract class Piece {
    private Coordinate coordinate;
    private User owner;
    protected Set<Coordinate> legalMoves;
    private PieceType pieceType;

    public Piece(){
        Coordinate coordinateToSet = new Coordinate(0, 0);
        this.coordinate = coordinateToSet;
    }

    public Piece(User owner, int x, int y){
        Coordinate coordinateToSet = new Coordinate(x, y);
        this.coordinate = coordinateToSet;
        this.owner = owner;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    /***
     * attempt to set coordinate for piece, return 0 on success
     * @param x
     * @param y
     * @return
     */
    public int setCoordinate(int x, int y) {
//      if (coordinateToSet) is valid return 0
        this.coordinate.setxy(x, y);
        return 0;
    }

    public User getOwner() {
        return this.owner;
    }

    /***
     * return type of the piece, such as King, Queen
     * @return
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;

    }

    /***
     * given a board with set up, calculate and update the legal moves for this piece
     * @param board
     */
    public abstract void updateLegalMoves(Board board);

    /***
     * helper function used to inspect all legal moves
     */
    public void printLegalMoves() {
        if (this.legalMoves.isEmpty()) {
            System.out.println("No legal moves");
            return;
        }
        for (Coordinate coordinate : this.legalMoves) {
            System.out.println(coordinate.getx() + " , " + coordinate.gety());
        }
    }

    /***
     * return legal moves as a set of coordinates
     * @param board
     * @return
     */
    public Set<Coordinate> getLegalMoves(Board board) {
        return this.legalMoves;
    }

    /***
     * try to move piece on board, decide if move is allowed by checking against the pre-calculated list of legalMoves
     * return 0 if move is legal and preceded
     */
    public int move(Board board, int x, int y) {
        // check if move is in legalMoves
        if (this.legalMoves.contains(new Coordinate(x, y))) {
            board.movePiece(this, x, y);
            this.updateLegalMoves(board);
            this.getOwner().setTurn(false);
            return 0;
        }
        return 1;
    }

}
