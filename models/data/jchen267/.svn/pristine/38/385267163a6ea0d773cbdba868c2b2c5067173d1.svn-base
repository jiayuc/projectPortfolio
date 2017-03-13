package com.chess.board;

import com.chess.Utility.Coordinate;
import com.chess.pieces.Piece;

/**
 * Created by jiayu on 2/4/2017.
 */
public class Tile {
    // two types of tiles with different color
    public enum color {
        BLACK,
        WHITE
    }
    private color tileColor;
    private Coordinate coordi;
    Piece pieceOnTile;

    /***
     * Construct tile with given location and tile color
     * @param x x coordinate of the position to check
     * @param y y coordinate of the position to check
     * @param tileColor color to assign to the tile
     */
    Tile(int x, int y, color tileColor) {
        this.coordi = new Coordinate(x, y);
        this.tileColor = tileColor;
        this.pieceOnTile = null;
    }

    /***
     * Determine whether tile has no piece on it
     * @return boolean to indicate whether tile is free of any piece
     */
    public boolean isTileFree() {
        return this.getPiece() == null;
    }

    /***
     * Get the coordinate of the tile
     * @return Coordinate of the tile
     */
    public Coordinate getCoordi() {
        return this.coordi;
    }

    /***
     * Get the piece on the tile
     * @return piece on the tile
     */
    public Piece getPiece() {
        return pieceOnTile;
    }

    /***
     * Place pieceToSet on the tile
     * @param pieceToSet piece that we want to place on the tile
     */
    public void setPiece(Piece pieceToSet) {
        this.pieceOnTile = pieceToSet;
    }

    /***
     * Get the color of the tile
     * @return color of the tile
     */
    public color getColor() {
        return this.tileColor;
    }
}



