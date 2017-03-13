package com.chess.pieces;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.users.User;

import java.util.Set;

/**
 * Created by jiayu on 2/4/2017.
 */
public abstract class Piece {
    private PieceType pieceType;
    private User owner;
    private Coordinate coordinate;
    protected Set<Coordinate> legalMoves;

    /***
     * Default constructor for the piece, setting at position (0, 0)
     */
    public Piece(){
        Coordinate coordinateToSet = new Coordinate(0, 0);
        this.coordinate = coordinateToSet;
    }

    /***
     * Constructor that set the piece to have the provided user and position
     * @param owner user to set as the owner of the piece
     * @param x x coordinate of the position to set
     * @param y x coordinate of the position to set
     */
    public Piece(User owner, int x, int y){
        Coordinate coordinateToSet = new Coordinate(x, y);
        this.coordinate = coordinateToSet;
        this.owner = owner;
    }

    /***
     * Get the coordinate of the piece
     * @return coordinate of the piece
     */
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    /***
     * attempt to set coordinate for piece, return 0 on success
     * @param x x coordinate of the position to set
     * @param y x coordinate of the position to set
     */
    public void setCoordinate(int x, int y) {
        this.coordinate.setxy(x, y);
    }

    /***
     * Get the owner of the piece
     * @return user who owns the piece
     */
    public User getOwner() {
        return this.owner;
    }

    /***
     * return type of the piece, such as King, Queen
     * @return PieceType of the piece
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /***
     * Set the PieceType for the piece
     * @param pieceType the PieceType to set for the piece
     */
    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;

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
     * given a board with pieces, calculate and update the legal moves for this piece
     * @param board board that the piece is on
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
     * try to move piece on board,
     * decide if move is allowed by checking against the pre-calculated list of legalMoves
     * @param board board where the piece in on
     * @param x  x coordinate of the new position
     * @param y y coordinate of the new position
     * @return
     */
    public int move(Board board, int x, int y) {
        // check if move is in legalMoves
        if (this.legalMoves.contains(new Coordinate(x, y))) {
            board.movePiece(this, x, y);
            this.updateLegalMoves(board);
            //TODO change the other turn also
            return 0;
        }
        return 1;
    }

}
