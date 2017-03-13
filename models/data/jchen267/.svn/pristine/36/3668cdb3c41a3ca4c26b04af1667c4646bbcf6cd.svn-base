package com.chess.board;

import com.chess.Utility.Coordinate;
import com.chess.pieces.*;
import com.chess.users.User;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

/**
 * Created by jiayu on 2/5/2017.
 **/

public class Board {
    private static final Coordinate COORDI_EATEN = new Coordinate(-1, -1);
    public static int DEFAULT_BOARD_LENGTH = 8;
    private int width;
    private int height;
    private Tile[][] tileGrid;
    private HashMap<String, Coordinate> usernameToKingCoordi = new HashMap<String, Coordinate>();
    private User[] users = new User[2];
    // record piece moved, the origin and destination of the move
    private Stack<Pair<Piece, Coordinate[]>> movesMade = new Stack<Pair<Piece, Coordinate[]>>();
    private Stack<Pair<Piece, Coordinate[]>> movesWithdrawn = new Stack<Pair<Piece, Coordinate[]>>();

    /**
     * default constructor for board
     */
    public Board() {
        this.width = DEFAULT_BOARD_LENGTH;
        this.height = DEFAULT_BOARD_LENGTH;
        setUpTile();
        this.users[0] = new User("BlackUnNamed");
        this.users[1] = new User("WhiteUnNamed");

        setUpPieces(this.users[0], this.users[1], false);
    }

    /**
     * initialize set up of tiles for board according to board dimensions
     */
    private void setUpTile() {
        this.tileGrid = new Tile[this.width][this.height];
        Tile.color colorToAssign = Tile.color.BLACK;
        for (int i = 0; i < width; ++i) {
            colorToAssign = colorToAssign == Tile.color.BLACK ? Tile.color.WHITE : Tile.color.BLACK;
            for (int j = 0; j < height; ++j) {
                tileGrid[i][j] = new Tile(i, j, colorToAssign);
                colorToAssign = colorToAssign == Tile.color.BLACK ? Tile.color.WHITE : Tile.color.BLACK;
            }
        }
    }

    /**
     * initialize set up all pieces for both users according to conventional rule
     *
     * @param userBlack user with black pieces
     * @param userWhite user with white pieces
     */
    private void setUpPieces(User userBlack, User userWhite, boolean useCustomPieces) {
        User[] users = {userBlack, userWhite};

        for (int i = 0; i < users.length; ++i) {
            User user = users[i];
            int rowNum = i == 0 ? 1 : this.height - 2;
            // set up pieces on rows farther from the edges
            for (int j = 0; j < this.width; ++j) {
                if (j == 1 && useCustomPieces) {
                    this.setUpPiece(new Flyer(user, rowNum, j), rowNum, j);
                } else if (j == this.width - 2 && useCustomPieces) {
                    this.setUpPiece(new Spy(user, rowNum, j), rowNum, j);
                } else {
                    this.setUpPiece(new Pawn(user, rowNum, j), rowNum, j);
                }
            }
            // set up pieces on rows at the edges
            rowNum = i == 0 ? 0 : this.height - 1;
            int colNum = 0;
            this.setUpPiece(new Rook(user, rowNum, colNum), rowNum, colNum++);
            this.setUpPiece(new Knight(user, rowNum, colNum), rowNum, colNum++);
            this.setUpPiece(new Bishop(user, rowNum, colNum), rowNum, colNum++);
            this.setUpPiece(new Queen(user, rowNum, colNum), rowNum, colNum++);
            this.setUpPiece(new King(user, rowNum, colNum), rowNum, colNum);
            this.usernameToKingCoordi.put(user.getName(), new Coordinate(rowNum, colNum++));

            this.setUpPiece(new Bishop(user, rowNum, colNum), rowNum, colNum++);
            this.setUpPiece(new Knight(user, rowNum, colNum), rowNum, colNum++);
            this.setUpPiece(new Rook(user, rowNum, colNum), rowNum, colNum);
        }

        updateLegalMovesForAllPieces();
    }

    /**
     * simply set up piece on target tile behind the scene,
     * not regular moves made by users, but are behind the scene
     *
     * @param pieceToMove piece to set up
     * @param x           x coordinate of the new position
     * @param y           y coordinate of the new position
     */
    protected void setUpPiece(Piece pieceToMove, int x, int y) {
        // (-1, -1) is special coordinate for piece captured
        if (x == -1 && y == -1) {
            pieceToMove.setCoordinate(x, y);
            return;
        }
        // if dest has piece, alert error must occur
        Tile destTile = this.getTile(x, y);
        assert (destTile != null);
        assert (destTile.getPiece() == null);

        destTile.setPiece(pieceToMove);
        pieceToMove.setCoordinate(x, y);
    }

    /**
     * update legal moves for all pieces
     */
    private void updateLegalMovesForAllPieces() {
        // update legal moves for all pieces
        for (int i = 0; i < this.tileGrid.length; ++i) {
            for (int j = 0; j < this.tileGrid[i].length; ++j) {
                Piece piece = this.tileGrid[i][j].getPiece();
                if (piece == null) continue;
                piece.updateLegalMoves(this);
            }
        }
    }

    /**
     * Get tile at the specified location on board
     *
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

    /**
     * check if given location is out of bound for the board
     *
     * @param x x coordinate of the position to check
     * @param y y coordinate of the position to check
     * @return boolean to indicate whether provided location is in bound of the board
     */
    public boolean isCoordinateInBound(int x, int y) {
        return x >= 0 && x < this.height && y >= 0 && y < this.width;
    }


    /**
     * constructor for board that takes params for customization
     *
     * @param width           number of tiles at each row
     * @param height          number of tiles at each column
     * @param userBlack       user with black pieces
     * @param userWhite       user with white pieces
     * @param useCustomPieces
     */
    public Board(int width, int height, User userBlack, User userWhite, boolean useCustomPieces) {
        this.width = width;
        this.height = height;
        setUpTile();
        this.users[0] = userBlack;
        this.users[1] = userWhite;
        setUpPieces(this.users[0], this.users[1], useCustomPieces);
    }

    /**
     * Restore the board into initial state
     *
     * @param useCustomPieces boolean to indicate whether set up the pieces using customized pieces
     */
    public void restore(boolean useCustomPieces) {
        this.movesMade.clear();
        this.movesWithdrawn.clear();
        // clear all pieces on tiles
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                this.getTile(i, j).setPiece(null);
            }
        }
        setUpPieces(this.users[0], this.users[1], useCustomPieces);
    }

    /**
     * perform an update on this.tileGrid to record corresponding piece move
     * and also to update possible moves for all other pieces
     * when a piece moves, it calls this function to inform board of its movement
     *
     * @param pieceToMove               piece that just moved
     * @param x                         x coordinate of the new position
     * @param y                         y coordinate of the new position
     * @param shouldClearWithdrawnMoves boolean to control whether withdrawnmoves should be clear
     */
    public void movePiece(Piece pieceToMove, int x, int y, boolean shouldClearWithdrawnMoves) {
        Coordinate originCoordi = pieceToMove.getCoordinate();

        // remove old piece
        Tile srcTile = this.tileGrid[originCoordi.getx()][originCoordi.gety()];
        assert (srcTile != null);
        srcTile.setPiece(null);
        // if dest has piece, alert piece is captured
        Tile destTile = this.getTile(x, y);
        assert (destTile != null);
        if (destTile.getPiece() != null) {
            Piece pieceCaptured = destTile.pieceOnTile;
            // move captured piece to special coordinate and record this move
            this.recordMove(pieceCaptured, pieceCaptured.getCoordinate(), COORDI_EATEN);
            this.setUpPiece(pieceCaptured, COORDI_EATEN.getx(), COORDI_EATEN.gety());
            System.out.println("Announcement:" + pieceCaptured.getOwner().getName() + "'s " + pieceCaptured.getPieceType() + " is captured :(");
            this.updateKingCoordi(pieceCaptured);
        }

        // record then perform the move
        this.recordMove(pieceToMove, originCoordi, new Coordinate(x, y));
        // place piece to destination tile
        destTile.setPiece(pieceToMove);
        pieceToMove.setCoordinate(x, y);

        // take care of special cases
        this.updateKingCoordi(pieceToMove);
        if (pieceToMove.getPieceType() == PieceType.Pawn)
            ((Pawn) pieceToMove).markStateMoved();
        updateLegalMovesForAllPieces();
        // empty movesWithdrawn
        if (shouldClearWithdrawnMoves)
            this.movesWithdrawn.clear();
    }

    /**
     * update the record of king's coordinates
     *
     * @param piece a newly moved piece to check for whether it's a king
     */
    private void updateKingCoordi(Piece piece) {
        // record king's new location
        if (piece.getPieceType() == PieceType.King) {
            String ownerName = piece.getOwner().getName();
            this.usernameToKingCoordi.replace(ownerName, piece.getCoordinate().clone());
        }
    }

    /**
     * record moving of a piece to the stack movesMade
     *
     * @param pieceToMove  piece that made the movement
     * @param originCoordi coordinate of the original position
     * @param destCoordi   coordinate of the destination
     */
    private void recordMove(Piece pieceToMove, Coordinate originCoordi, Coordinate destCoordi) {
        Coordinate[] coordis = new Coordinate[2];
        coordis[0] = originCoordi.clone();
        coordis[1] = destCoordi.clone();
        this.movesMade.add(new Pair<Piece, Coordinate[]>(pieceToMove.clone(), coordis));
    }

    /**
     * Withdraw the last move on the board, save this operation onto stacks
     *
     * @return return 0 on success, 1 on failure
     */
    public int withdrawLastMove() {
        if (this.movesMade.empty()) {
            System.out.println("moves made empty");
            return 1;
        }

        // withdraw move on board
        Pair lastMove = this.movesMade.pop();
        Piece pieceToMove = ((Piece) lastMove.getKey());
        Coordinate originCoordi = ((Coordinate[]) lastMove.getValue())[0];
        Coordinate destCoordi = ((Coordinate[]) lastMove.getValue())[1];

        // remove curr piece unless it was captured
        if (!destCoordi.equals(COORDI_EATEN)) {
            this.getTile(destCoordi.getx(), destCoordi.gety()).setPiece(null);
        }
        // restore old piece to original location
        this.setUpPiece(pieceToMove, originCoordi.getx(), originCoordi.gety());
        // record the withdraw on stack
        this.movesWithdrawn.add(lastMove);

        // update moves
        this.updateLegalMovesForAllPieces();
        // check if withdrawn move above caused a captured piece that needs to be restored too
        if (this.isEatingMoveNext(this.movesMade)) {
            this.withdrawLastMove();
        }
        return 0;
    }

    /**
     * determine if the next move recorded on top of given stack is about a piece being captured
     *
     * @param moves a stack that records a series of moves
     * @return boolean to indicate whether next move recorded is about a piece being captured
     */
    private boolean isEatingMoveNext(Stack<Pair<Piece, Coordinate[]>> moves) {
        if (moves.empty()) return false;
        Pair<Piece, Coordinate[]> move = moves.peek();
        Coordinate destCoordi = move.getValue()[1];
        return destCoordi.equals(COORDI_EATEN);
    }

    /**
     * Redo the last withdrawn move on the board, save this operation onto stacks
     *
     * @return return 0 on success, 1 on failure
     */
    public int redoWithdrawnMove() {
        if (this.movesWithdrawn.empty()) return 1;
        // if nextMove is about eating piece,
        // perform another redoWithdrawnMove to fully restore
        boolean shouldRedoAgain = this.isEatingMoveNext(this.movesWithdrawn);

        // redo withdrawnMove on board
        Pair<Piece, Coordinate[]> withdrawnMove = this.movesWithdrawn.pop();
        Coordinate originCoordi = withdrawnMove.getValue()[0];
        Coordinate destCoordi = withdrawnMove.getValue()[1];

        // remove old piece at original position
        Tile originTile = this.getTile(originCoordi.getx(), originCoordi.gety());
        assert (originTile != null);
        originTile.setPiece(null);
        // place new piece at dest position
        this.setUpPiece(withdrawnMove.getKey(), destCoordi.getx(), destCoordi.gety());
        this.recordMove(withdrawnMove.getKey(), originCoordi, destCoordi);
        this.printMovesMade();

        // update moves
        this.updateLegalMovesForAllPieces();

        if (shouldRedoAgain)
            redoWithdrawnMove();
        return 0;
    }

    /**
     * Get entire 2D array of Tiles on the board
     *
     * @return 2D array of Tiles on the board
     */
    public Tile[][] getTileGrid() {
        return this.tileGrid;
    }

    /**
     * Check whether coordinate on board is in bound and has no piece of the userToCheck
     *
     * @param userToCheck user that intents to move to the provided position
     * @param x           x coordinate of the position to check
     * @param y           y coordinate of the position to check
     * @return boolean to indicate whether coordinate is valid for userToCheck's pieces to go
     */
    public boolean isCoordinateValidForUser(User userToCheck, int x, int y) {
        if (this.isCoordinateInBound(x, y)) {
            Piece piece = this.getTile(x, y).getPiece();
            if (piece == null || piece.getOwner() != userToCheck) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if no move for nextMoveMaker to save the checked King
     *
     * @param nextMoveMaker user that should make the next move
     * @return boolean to indicate whether nextMoveMaker encounters checkmate
     */
    public boolean isCheckMate(User nextMoveMaker) {
        if (!isKingChecked(nextMoveMaker)) return false;
        // no move to avoid being check
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {

                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null || piece.getOwner() != nextMoveMaker) continue;
                // traverse through all possible next movements
                // return can escape return false
                if (canAvoidCheckMate(this.getTile(i, j))) return false;
            }
        }
        System.err.println("Announcement: checkmate towards " + nextMoveMaker.getName());
        return true;
    }

    /**
     * stalemate happens when a player's King is safe on a square
     * AND the King is the only Piece that player could move,
     * BUT is unable to as it would place that King in "Check".
     *
     * @param nextMoveMaker user that should make the next move
     * @return boolean to indicate whether nextMoveMaker encounters checkmate
     */
    public boolean isStaleMate(User nextMoveMaker) {
        // assume not in checkmate
//        if (this.isCheckMate(nextMoveMaker)) return false;
        // from previous check in isGameOver
        // checks if no moves available except for the king
        if (!noPossibleMove(nextMoveMaker, false)) return false;
        // no move to for king to avoid being in checkmate next
        Coordinate coordiKing = this.usernameToKingCoordi.get(nextMoveMaker.getName());
        return !canAvoidCheckMate(this.getTile(coordiKing.getx(), coordiKing.gety()));
    }

    /**
     * Check if this piece can move so that its owner will not be in checkMate
     *
     * @param tileUnderpiece tile where the piece that attempts to move is on
     * @return boolean to indicate whether the owner can avoid to be in checkMate
     * by the movement of this piece
     */
    private boolean canAvoidCheckMate(Tile tileUnderpiece) {
        Piece piece = tileUnderpiece.getPiece();
        Set<Coordinate> moves = piece.getLegalMoves();
        boolean canAvoidCheckMate;

        for (Coordinate move : moves) {
            piece = tileUnderpiece.getPiece();
            // try to take the move
            this.movePiece(piece, move.getx(), move.gety(), false);
            canAvoidCheckMate = isKingChecked(piece.getOwner()) == false;

            if (canAvoidCheckMate) {
                System.err.println("Can aoivd checked king!" + piece.getPieceType() + " move to " + move.getx() + "," + move.gety());
            }
            // restore board and move history
            System.err.println("cannot AvoidCheckMate");
            this.printBoard();
            this.withdrawLastMove();
            this.movesWithdrawn.pop();
            if (canAvoidCheckMate) return true;
        }
        return false;
    }

    /**
     * check if nextMoveMaker's king is checked
     *
     * @param nextMoveMaker user that should make the next move
     * @return boolean to indicate whether nextMoveMaker's king is captured
     */
    public boolean isKingChecked(User nextMoveMaker) {
        Coordinate targetKingCoordi = this.usernameToKingCoordi.get(nextMoveMaker.getName());
        // check if any enemy piece can capture this king
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null || piece.getOwner() == nextMoveMaker) continue;
                if (piece.getLegalMoves().contains(targetKingCoordi)) return true;
            }
        }
        return false;
    }

    /**
     * return true when no possible move for nextMoveMaker, doesn't check for checkMate
     *
     * @param nextMoveMaker    user that should make the next move
     * @param checkMoveForKing if we should check for possible moves for nextMoveMaker's king
     * @return boolean to indicate whether there's no possible move for any piece of the nextMoveMaker
     */
    public boolean noPossibleMove(User nextMoveMaker, boolean checkMoveForKing) {
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null || piece.getOwner() != nextMoveMaker) continue;
                if (piece.getPieceType() == PieceType.King && !checkMoveForKing) continue;
                if (!piece.getLegalMoves().isEmpty()) return false;
            }
        }
        return true;
    }

    /**
     * Helper to print the board for visual aid
     */
    public void printBoard() {
        for (int i = -1; i < this.height; ++i) {
            if (i != -1) System.out.print(i + " ");
            else System.out.print("  ");
            for (int j = 0; j < this.width; ++j) {
                if (i == -1) {
                    System.out.print(" " + j);
                    continue;
                }
                System.out.print('|');
                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null) System.out.print(' ');
                else {
                    if (piece.getOwner().getName() == this.users[0].getName())
                        System.out.print(piece.getPieceType().name().charAt(0));
                    else
                        System.out.print(piece.getPieceType().name().toLowerCase().charAt(0));
                }
            }
            System.out.print('\n');
        }
        printMovesMade();
        printMovesWithdrawn();
    }

    /**
     * helper to print history of all moved made by users
     */
    public void printMovesMade() {
        System.out.println("printMovesMade--");
        Stack<Pair<Piece, Coordinate[]>> cp = new Stack<>();
        Pair<Piece, Coordinate[]> move;
        while (!this.movesMade.empty()) {
            move = this.movesMade.pop();
            cp.add(move);
            System.out.println("Piece " + move.getKey() +
                    " moved from (" + move.getValue()[0].getx() + ", " + move.getValue()[0].gety() + ")"
                    + "to (" + move.getValue()[1].getx() + ", " + move.getValue()[1].gety() + ")");
        }

        while (!cp.empty()) {
            this.movesMade.add(cp.pop());
        }
    }

    /**
     * helper to print history of moved withdrawn by users
     */
    public void printMovesWithdrawn() {
        System.out.println("printMovesWithdrawn--");
        Stack<Pair<Piece, Coordinate[]>> cp = new Stack<>();
        Pair<Piece, Coordinate[]> move;
        while (!this.movesWithdrawn.empty()) {
            move = this.movesWithdrawn.pop();
            cp.add(move);
            System.out.println("Piece " + move.getKey() +
                    " moved from (" + move.getValue()[0].getx() + ", " + move.getValue()[0].gety() + ")"
                    + "to (" + move.getValue()[1].getx() + ", " + move.getValue()[1].gety() + ")");
        }

        while (!cp.empty()) {
            this.movesWithdrawn.add(cp.pop());
        }
    }


    /**
     * get users
     *
     * @return array of users
     */
    public User[] getUsers() {
        return this.users;
    }

    /**
     * get the width of the board
     *
     * @return width of the board
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * get the height of the board
     *
     * @return the height of the board
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * determine whether the nextMoveMaker's king is captured
     *
     * @param nextMoveMaker the user to make the next move
     * @return boolean to indicate whether the nextMoveMaker's king is captured
     */
    public boolean isKingCaptured(User nextMoveMaker) {
        Coordinate kingCoordi = this.usernameToKingCoordi.get(nextMoveMaker.getName());
        if (kingCoordi.equals(COORDI_EATEN) == false) {
            System.out.println("King at position " + kingCoordi.getx() + " " + kingCoordi.gety());
        }
        return kingCoordi.equals(COORDI_EATEN);
    }

    /**
     * get the coordinate of the user's king
     *
     * @param user user that we want to check the king location for
     * @return coordinate of the user's king
     */
    public Coordinate getKingCoordi(User user) {
        return this.usernameToKingCoordi.get(user.getName());
    }

    /**
     * check whether the given coordinate is not a checked position for kingOwner's king
     *
     * @param kingOwner user whose king we are checking safety for
     * @param coordiX   x coordinate of the position we check
     * @param coordiY   y coordinate of the position we check
     * @return boolean to indicate whether the given coordinate is not a checked position for kingOwner's king
     */
    public boolean isMoveSafeForKing(User kingOwner, int coordiX, int coordiY) {
        Coordinate targetKingCoordi = new Coordinate(coordiX, coordiY);
        // check if any enemy piece can capture this king
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                Piece piece = this.getTile(i, j).getPiece();
                if (piece == null || piece.getOwner() == kingOwner) continue;
                if (piece.getLegalMoves().contains(targetKingCoordi)) return false;
            }
        }
        return true;
    }
}
