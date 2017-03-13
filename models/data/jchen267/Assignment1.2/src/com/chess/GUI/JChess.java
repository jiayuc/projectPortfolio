package com.chess.GUI;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.board.Tile;
import com.chess.users.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/**
 * Created by jiayu on 2/12/2017.
 */
public class JChess {
    private Board board;
    private BoardGUI boardGUI;
    private User[] users = new User[2];
    private String[] usernames;
    private int nextMoveMakerIdx = 0;

    public JChess() {
        this.board = new Board();
        this.users = this.board.getUsers();
        this.boardGUI = new BoardGUI(this.board);

        this.usernames = this.getUsernames();
        System.out.println(usernames[0] + usernames[1]);
        addListenerToButtons();
        addListenerToTiles();
        addListenerToRestartCmd();

        this.restart();
    }

    /**
     * Add listener to restart cmd
     */
    private void addListenerToRestartCmd() {
        this.boardGUI.addListenerToRestartCmd(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Fresh game :)");
                restart();
            }
        });
    }

    /**
     * add listener to buttons on player panel
     */
    private void addListenerToButtons() {
        this.boardGUI.addListenerToUndoButton(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int status = board.withdrawLastMove();
                if (status == 0) {
                    nextMoveMakerIdx = flip(nextMoveMakerIdx);
                    updateGUIView();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.boardGUI.addListenerToRedoButton(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int status = board.redoWithdrawnMove();
                if (status == 0) {
                    nextMoveMakerIdx = flip(nextMoveMakerIdx);
                    updateGUIView();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.boardGUI.addListenerToForfeitButtonBlack(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                User forfeitUser = users[0];
                announceResult(forfeitUser);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.boardGUI.addListenerToForfeitButtonWhite(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                User forfeitUser = users[1];
                announceResult(forfeitUser);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * add listener to all tiles
     */
    private void addListenerToTiles() {
        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                this.boardGUI.addListenerToTile(i, j, new tileMouseListener(i, j));
            }
        }
    }

    /**
     * Run the game loop
     */
    private void runGame() {
        // flip turn
        this.nextMoveMakerIdx = this.nextMoveMakerIdx == 0 ? 1 : 0;
        User nextMoveMaker = this.users[this.nextMoveMakerIdx];
        if (!this.isGameOver(nextMoveMaker)) {
            this.board.printBoard();
            System.out.println("Next!" + nextMoveMaker.getName() + "'s turn...");
        } else {
            announceResult(null);
        }
    }

    /**
     * determine if game is forced to over w/o forfeit
     *
     * @param nextMoveMaker user who should make th next move
     * @return boolean indicate whether game is forced to be over by chess rule
     */
    private boolean isGameOver(User nextMoveMaker) {
        return this.board.isKingCaptured(nextMoveMaker) ||
                this.board.isCheckMate(nextMoveMaker) ||
                this.board.isStaleMate(nextMoveMaker);
    }

    /**
     * announce result for the ended game
     *
     * @param forfeitUser user who forfeit if applicable
     */
    private void announceResult(User forfeitUser) {
        User nextMoveMaker = this.users[nextMoveMakerIdx];
        String msg = "Game over:　";
        if (forfeitUser != null) {
            msg += "User " + forfeitUser.getName() + "forfeit";
            int forfeitUserIdx = forfeitUser == users[0] ? 0 : 1;
            this.incrementScore(forfeitUserIdx);
        } else if (this.board.isCheckMate(nextMoveMaker)) {
            msg += "User " + nextMoveMaker.getName() + " lost in checkmate";
            this.incrementScore(this.flip(nextMoveMakerIdx));
        } else if (this.board.isStaleMate(nextMoveMaker)) {
            msg += "User " + nextMoveMaker.getName() + " in stalemate";
        } else if (this.board.isKingCaptured(nextMoveMaker)) {
            msg = "User " + nextMoveMaker.getName() + " lost king";
            this.incrementScore(this.flip(nextMoveMakerIdx));
        } else {
            msg += "But why? Report bug to developer :(";
        }

        JOptionPane.showMessageDialog(null, msg);
        this.restart();
    }

    /**
     * increment score for the user
     *
     * @param userIdx idx of the user who wins
     */
    private void incrementScore(int userIdx) {
        this.boardGUI.incrementScore(userIdx);
    }

    /**
     * flip the turns
     *
     * @param userIdx idx of user
     * @return
     */
    private int flip(int userIdx) {
        return userIdx == 0 ? 1 : 0;
    }

    /**
     * restart the game
     */
    private void restart() {
        this.setFirstMoveMaker();
        this.board.restore(this.getGameType());
        this.updateGUIView();
    }

    /**
     * prompts and asks for the first movemaker
     */
    private void setFirstMoveMaker() {
        // texts on button
        String[] choices = {this.usernames[0], this.usernames[1], "Quit"};

        int response = JOptionPane.showOptionDialog(
                null
                , "New game: who makes first move?"
                , "New Game"
                , JOptionPane.YES_NO_OPTION
                , JOptionPane.PLAIN_MESSAGE
                , null
                , choices
                , "init val"
        );

        System.out.println(response);
        if (response == -1 || response == 2) {
            System.exit(0);
            return;
        } else {
            this.nextMoveMakerIdx = response; // change to 0-indexed
        }

    }

    private boolean getGameType() {
        int dialogResult = JOptionPane.showConfirmDialog(
                null,
                "Try Jiayu's chess pieces?",
                "Game Type",
                JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }

    /**
     * update view on GUI
     */
    private void updateGUIView() {
        // mark king checked if applicable
        Set<Coordinate> coordis = new HashSet<>();
        for (User user : this.users) {
            boolean shouldMarkAsChecked = this.board.isKingChecked(user);
            if (shouldMarkAsChecked) {
                System.err.println("King in check: " + user.getName());
                coordis.add(this.board.getKingCoordi(user));
            }
            this.boardGUI.markKingInCheck(coordis);
        }
        // update GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    boardGUI.boardPanel.drawBoard(board);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * prompt users for usernames
     *
     * @return array of Strings containing usernames
     */
    private String[] getUsernames() {
        // create pop up window
        JTextField Black = new JTextField();
        JTextField White = new JTextField();
        Object[] message = {
                "Black user name:", Black,
                "White user name:", White
        };
        int option = JOptionPane.showConfirmDialog(this.boardGUI.getFrame(), message, "User name", JOptionPane.OK_CANCEL_OPTION);

        String[] usernames = new String[2];
        if (option == JOptionPane.OK_OPTION) {
            // get strings from Jtextfield and set 2 player
            String blackName = Black.getText();
            String whiteName = White.getText();
            // try again if inputs are invalid
            if (whiteName.isEmpty() || blackName.isEmpty() || whiteName.compareTo(blackName) == 0) {
                System.err.println("invalid usernames, try again");
                return getUsernames();
            }
            usernames[0] = blackName;
            usernames[1] = whiteName;
        }

        return usernames;
    }

    /**
     * override mouse clicking listeners
     */
    private class tileMouseListener implements MouseListener {
        private int coordiX;
        private int coordiY;

        public tileMouseListener(int x, int y) {
            this.coordiX = x;
            this.coordiY = y;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isRightMouseButton(e)) {
                // cancel selection on piece
                boardGUI.clearTileSelection();

            } else if (isLeftMouseButton(e)) {
                Tile clickedTile = board.getTile(this.coordiX, this.coordiY);
                if (boardGUI.sourceTile == null) {
                    // if selecting free tile, or not selecting nextMoveMaker's piece, ignore
                    if (clickedTile.isTileFree()
                            || clickedTile.getPiece().getOwner() != users[nextMoveMakerIdx])
                        return;
                    // fresh valid selection
                    boardGUI.sourceTile = clickedTile;
                    boardGUI.selectedPiece = boardGUI.sourceTile.getPiece();

                } else {
                    // has previous selection on
                    boardGUI.destTile = clickedTile;
                    // try to perform the desired move
                    Coordinate destCoordi = boardGUI.destTile.getCoordi();
                    int ret = boardGUI.selectedPiece.move(board, destCoordi.getx(), destCoordi.gety());
                    if (ret == 1) {
                        System.out.println("Invalid attempt to move!!");
                    } else {
                        System.out.println("Moved :)");
                        runGame();
                    }
                    boardGUI.clearTileSelection();
                }
            }

            updateGUIView();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
