package com.chess.board;

import com.chess.Utility.Coordinate;
import com.chess.pieces.*;
import com.chess.users.User;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by jiayu on 2/5/2017.
 **/

public class Board {
    public static int DEFAULT_BOARD_LENGTH = 8;

    private int width;
    private int height;

    private Tile[][] tileGrid;
    private HashMap<String, Coordinate> usernameToKingCoordi =  new HashMap<String, Coordinate>();

    /***
     * default constructor for board
     */
    public Board() {
        this.width = DEFAULT_BOARD_LENGTH;
        this.height = DEFAULT_BOARD_LENGTH;
        setUpTile();
        User userBlack = new User("BlackUnNamed");
        User userWhite = new User("WhiteUnNamed");
        setUpPieces(userBlack, userWhite);
    }

    /***
     * constructor for board that takes params for customization
     * @param width number of tiles at each row
     * @param height number of tiles at each column
     * @param userBlack user with black pieces
     * @param userWhite user with white pieces
     */
    public Board(int width, int height, User userBlack, User userWhite) {
        this.width = width;
        this.height = height;
        setUpTile();
        setUpPieces(userBlack, userWhite);
    }

    /***
     * set up tiles for board according to board dimensions
     */
    private void setUpTile() {
        this.tileGrid = new Tile[this.width][this.height];
        Tile.color colorToAssign = Tile.color.BLACK;
        for (int i=0; i<width; ++i) {
            colorToAssign = colorToAssign == Tile.color.BLACK ? Tile.color.WHITE : Tile.color.BLACK;
            for (int j=0; j<height; ++j) {
                tileGrid[i][j] = new Tile(i, j, colorToAssign);
                colorToAssign = colorToAssign == Tile.color.BLACK ? Tile.color.WHITE : Tile.color.BLACK;
            }
        }
    }

    /***
     * set up all pieces for both users according to conventional rule
     * @param userBlack user with black pieces
     * @param userWhite user with white pieces
     */
    private void setUpPieces(User userBlack, User userWhite) {
        User[] users = {userBlack, userWhite};

        for (int i=0; i<users.length; ++i) {
            User user = users[i];
            int rowNum = i==0 ? 1 : this.height-2;
            // set up pieces on rows farther from the edges
            for (int j=0; j<this.width; ++j) {
                if ( j == 1) {
                    this.movePiece(new Flyer(user, rowNum, j), rowNum, j);
                } else if (j == this.width-2) {
                    this.movePiece(new Spy(user, rowNum, j), rowNum, j);
                } else {
                    this.movePiece(new Pawn(user, rowNum, j), rowNum, j);
                }
            }
            // set up pieces on rows at the edges
            rowNum = i==0 ? 0 : this.height-1;
            int colNum = 0;
            this.movePiece(new Rook(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Knight(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Bishop(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new Queen(user, rowNum, colNum), rowNum, colNum++);
            this.movePiece(new King(user,rowNum, colNum), rowNum, colNum);
            this.usernameToKingCoordi.put(user.getName(), new Coordinate(rowNum, colNum++));

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
     * when a piece moves, it calls this function to inform board of its movement
     * @param pieceToMove piece that just moved
     * @param x x coordinate of the new position
     * @param y y coordinate of the new position
     */
    public void movePiece(Piece pieceToMove, int x, int y) {
        // remove old piece
        Coordinate srcCoordi = pieceToMove.getCoordinate();
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
        // record king's new location
        if (pieceToMove.getPieceType() == PieceType.King) {
            String ownerName = pieceToMove.getOwner().getName();
            this.usernameToKingCoordi.replace(ownerName, pieceToMove.getCoordinate());
        }
    }

    /***
     * Get entire 2D array of Tiles on the board
     * @return 2D array of Tiles on the board
     */
    public Tile[][] getTileGrid() {
        return this.tileGrid;
    }

    /***
     * Get tile at the specified location on board
     * @param x x coordinate of the position to check
     * @param y y coordinate of the position to check
     * @return tile of the specified location
     */
    public Tile getTile(int x, int y) {
        if (isCoordinateInBound(x, y)) {
            return this.tileGrid[x][y];
        } else {
            return null;
        }
    }

    /***
     * Check whether coordinate on board is in bound and has no piece of the userToCheck
     * @param userToCheck user that intents to move to the provided position
     * @param x x coordinate of the position to check
     * @param y y coordinate of the position to check
     * @return boolean to indicate whether coordinate is valid for userToCheck's pieces to go
     */
    public boolean isCoordinateValidForUser(User userToCheck, int x, int y) {
        if (this.isCoordinateInBound(x, y)) {
            Piece piece = this.getTile(x, y).getPiece();
            if ( piece == null || piece.getOwner() != userToCheck) {
                return true;
            }
        }
        return false;
    }

    /***
     * check if given location is out of bound for the board
     * @param x x coordinate of the position to check
     * @param y y coordinate of the position to check
     * @return boolean to indicate whether provided location is in bound of the board
     */
    public boolean isCoordinateInBound(int x, int y) {
        return x>=0 && x<this.height && y>=0 && y<this.width;
    }

    /***
     * check if game is over by 2 conditions:
     * 1: no possible moves for any piece of nextMoveMaker
     * 2. checkmate for nextMoveMaker
     * @param nextMoveMaker user that should make the next move
     * @return boolean to indicate whether game is over
     */
    public boolean isGameOver(User nextMoveMaker) {
        return isCheckMate(nextMoveMaker) || noPossibleMove(nextMoveMaker);
    }

    /***
     * check if no move for nextMoveMaker to save the checked King
     * @param nextMoveMaker user that should make the next move
     * @return boolean to indicate whether nextMoveMaker encounters checkmate
     */
    public boolean isCheckMate(User nextMoveMaker) {
        if (!isKingChecked(nextMoveMaker)) return  false;
        // no move to avoid being check
        for (int i=0; i<this.height; ++i) {
            for (int j=0; j<this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece.getOwner() != nextMoveMaker) continue;
                // traverse through all possible next movements
                // return can escape return false
                if (canAvoidCheckMate(piece)) return false;
            }
        }
        return true;
    }

    /***
     * Check if this piece can move so that its owner can avoid being in checkMate
     * @param piece piece that attempts to move
     * @return boolean to indicate whether the owner can avoid being in checkMate
     *          by the movement of this piece
     */
    private boolean canAvoidCheckMate(Piece piece) {
        Set<Coordinate> moves = piece.getLegalMoves(this);
        for (Coordinate move : moves) {
            this.movePiece(piece, move.getx(), move.gety());
            piece.updateLegalMoves(this);
            if (isCheckMate(piece.getOwner()) == false) return true;
        }
        return false;
    }

    /***
     * check if nextMoveMaker's king is checked
     * @param nextMoveMaker user that should make the next move
     * @return boolean to indicate whether nextMoveMaker's king is captured
     */
    public boolean isKingChecked(User nextMoveMaker) {
        Coordinate targetKingCoordi = this.usernameToKingCoordi.get(nextMoveMaker.getName());
        // check if any enemy piece can capture this king
        for (int i=0; i<this.height; ++i) {
            for (int j=0; j<this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null || piece.getOwner() == nextMoveMaker) continue;
                if (piece.getLegalMoves(this).contains(targetKingCoordi)) return true;
            }
        }
        return false;
    }

    /***
     * return true when no possible move for nextMoveMaker, doesn't check for checkMate
     * @param nextMoveMaker user that should make the next move
     * @return boolean to indicate whether there's no possible move for any piece of the nextMoveMaker
     */
    public boolean noPossibleMove(User nextMoveMaker) {
        for (int i=0; i<this.height; ++i) {
            for (int j=0; j<this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null || piece.getOwner() != nextMoveMaker) continue;
                if (!piece.getLegalMoves(this).isEmpty()) return false;
            }
        }
        return true;
    }

    /***
     * Helper to print the board for visual aid
     */
    public void printBoard() {
        for (int i = -1; i<this.height; ++i) {
            if (i != -1) System.out.print(i+" ");
            else System.out.print("  ");
            for (int j=0; j<this.width; ++j) {
                if (i == -1) {
                    System.out.print(" "+j);
                    continue;
                }
                System.out.print('|');
                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null) System.out.print(' ');
                else System.out.print(piece.getPieceType().name().charAt(0));
            }
            System.out.print('\n');
        }
    }

}
