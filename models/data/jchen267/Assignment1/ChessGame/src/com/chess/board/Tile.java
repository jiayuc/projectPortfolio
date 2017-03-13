package com.chess.board;

import com.chess.pieces.Coordinate;
import com.chess.pieces.Piece;

/**
 * Created by jiayu on 2/4/2017.
 */
public class Tile {
    private boolean isFree;
    private Coordinate coordi;
    // two types of tiles with different color
    public enum color {
        BLACK,
        WHITE
    }
    private color tileColor;
    Piece pieceOnTile;

    /***
     * Construct tile with given location and tile color
     * @param x
     * @param y
     * @param tileColor
     */
    Tile(int x, int y, color tileColor) {
        this.isFree = true;
        this.coordi = new Coordinate(x, y);
        this.tileColor = tileColor;
        this.pieceOnTile = null;
    }

    public boolean isTileFree() {
        return isFree;
    }

    public Coordinate getCoordi() {
        return this.coordi;
    }

    public Piece getPiece() {
        return pieceOnTile;
    }

    /***
     * place pieceToSet on the tile
     * @param pieceToSet
     */
    public void setPiece(Piece pieceToSet) {
        this.pieceOnTile = pieceToSet;
    }

    public color getColor() {
        return this.tileColor;
    }
}



