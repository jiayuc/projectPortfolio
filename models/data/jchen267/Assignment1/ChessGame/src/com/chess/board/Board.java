package com.chess.board;

import com.chess.pieces.*;
import com.chess.users.User;

import java.util.HashMap;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Board {

    private int width;
    private int height;

    private Tile[][] tileGrid;
    private HashMap<String, Coordinate> username2KingCoordi;

    public Board() {
        int defaultSize = 8;
        this.width = defaultSize;
        this.height = defaultSize;
        setUpTile();
        User userA = new User("UnNamedA");
        User userB = new User("UnNamedB");
        setUpPieces(userA, userB);
    }

    public Board(int width, int height, User userA, User userB) {
        this.width = width;
        this.height = height;
        setUpTile();
        setUpPieces(userA, userB);
    }

    /***
     * init tiles for board according to board size
     */
    private void setUpTile() {
        this.tileGrid = new Tile[this.width][this.height];
        Tile.color colorToAssign = Tile.color.BLACK;
        for (int i=0; i<width; ++i) {
            for (int j=0; j<height; ++j) {
                tileGrid[i][j] = new Tile(i, j, colorToAssign);
                colorToAssign = colorToAssign == Tile.color.BLACK ? Tile.color.WHITE : Tile.color.BLACK;
            }
        }
    }

    /***
     * set up all pieces for users according to convention
     * @param userA
     * @param userB
     */
    private void setUpPieces(User userA, User userB) {
        User[] users = {userA, userB};

        for (int i=0; i<users.length; ++i) {
            User user = users[i];
            int rowNum = i==0 ? 1 : this.height-2;
            final int PAWN_CT = 8;
            for (int j=0; j<PAWN_CT; ++j) {
                this.movePiece(new Pawn(user, rowNum, j), rowNum, j);
            }

            rowNum = i==0 ? 0 : this.height-1;
            int colNum = 0;
            this.movePiece(new Rook(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Knight(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Bishop(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Queen(user, rowNum, colNum), rowNum, colNum++);

            this.movePiece(new King(user,rowNum, colNum), rowNum, colNum);
            username2KingCoordi.put(user.getName(), new Coordinate(rowNum, colNum++));

            this.movePiece(new Bishop(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Knight(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Rook(user, rowNum, colNum), rowNum, colNum);
        }

        // update legal moves for all pieces
        for (int i=0; i<this.tileGrid.length; ++i) {
            for (int j=0; j<this.tileGrid[i].length; ++j) {
                Piece piece = this.tileGrid[i][j].getPiece();
                if (piece == null) continue;
                piece.updateLegalMoves(this);
            }
        }

    }

    /**
     * perform an update on this.tileGrid to record corresponding piece move
     * @param pieceToMove
     * @param x
     * @param y
     */
    public void movePiece(Piece pieceToMove, int x, int y) {
        // TODO: shouldn't need to, but check in bound for now
        if (x<0 || x>=this.height || y<0 || y>=this.width) {
            System.out.println("Error: attempt to move piece " + pieceToMove.getPieceType() + " to out-of-bound destination " + x + ", " + y);
            return;
        }
        Coordinate srcCoordi = pieceToMove.getCoordinate();
        // remove old piece
        Tile srcTile = this.tileGrid[srcCoordi.getx()][srcCoordi.gety()];
        srcTile.setPiece(null);
        // if dest has piece, alert piece has been eaten
        Tile destTile = this.tileGrid[x][y];
        if (destTile.getPiece() != null) {
            Piece pieceEaten = destTile.pieceOnTile;
            System.out.println("Announcement:" + pieceEaten.getOwner() +  "'s " + pieceEaten.getOwner().getName() + "is eaten :(");
        }
        destTile.setPiece(pieceToMove);
        pieceToMove.setCoordinate(x, y);
    }

    /***
     * return entire 2d array of tiles
     * @return
     */
    public Tile[][] getTileGrid() {
        return this.tileGrid;
    }

    public Tile getTile(int x, int y) {
        if (isCoordinateInBound(x, y)) {
            return this.tileGrid[x][y];
        } else {
            return null;
        }
    }

    /***
     * Check whether coordinate on board is in bound and has no piece of the ownerToMove
     * @param ownerToMove
     * @param x
     * @param y
     * @return
     */
    public boolean isCoordinateValid(User ownerToMove, int x, int y) {
        if (this.isCoordinateInBound(x, y)) {
            Piece piece = this.getTile(x, y).getPiece();
            if ( piece == null || piece.getOwner() != ownerToMove) {
                return true;
            }
        }
        return false;
    }

    /***
     * check if given location is out of bound for the board
     * @param x
     * @param y
     * @return
     */
    public boolean isCoordinateInBound(int x, int y) {
        return x>=0 && x<this.height && y>=0 && y<this.width;
    }

    /***
     * check if game is over by 2 conditions: either no moves or checkmate for nextMoveMaker
     * @param nextMoveMaker
     * @return
     */
    public boolean isGameOver(User nextMoveMaker) {
        return isCheckMate() || noPossibleMove(nextMoveMaker);
    }

    /***
     * check if no move for nextMoveMaker to save the checked King
     * @param nextMoveMaker
     * @return
     */
    public boolean isCheckMate(User nextMoveMaker) {
        if (!isKingChecked(nextMoveMaker)) return  false;
        // no move to avoid being check
        for (int i=0; i<this.height; ++i) {
            for (int j=0; j<this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece.getOwner() != nextMoveMaker) continue;
                // TODO: bfs to traverse through all possible next movements
            }
        }

    }

    /***
     * return true when no possible move for nextMoveMaker, doesn't check for checkMate
     * @param nextMoveMaker
     * @return
     */
    public boolean noPossibleMove(User nextMoveMaker) {
        for (int i=0; i<this.height; ++i) {
            for (int j=0; j<this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece.getOwner() != nextMoveMaker) continue;
                if (!piece.getLegalMoves().isEmpty()) return false;
            }
        }
        return true;
    }

    /***
     * checj if nextMoveMaker's king is checked
     * @param nextMoveMaker
     * @return
     */
    public boolean isKingChecked(User nextMoveMaker) {
        Coordinate targetKingCoordi = this.username2KingCoordi.get(nextMoveMaker.getName());

        for (int i=0; i<this.height; ++i) {
            for (int j=0; j<this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece.getOwner() == nextMoveMaker) continue;
                if (piece.getLegalMoves().contains(targetKingCoordi)) return true;
            }
        }
        return false;
    }
}
