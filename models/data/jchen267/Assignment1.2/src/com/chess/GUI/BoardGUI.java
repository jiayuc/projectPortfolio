/**
 * Created by jiayu on 2/12/2017.
 */
package com.chess.GUI;

import com.chess.Utility.Coordinate;
import com.chess.board.Board;
import com.chess.board.Tile;
import com.chess.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BoardGUI {
    /**
     * Constants for the settings
     */
    private static Dimension FRAME_DIMENSION = new Dimension(650, 550);
    private static Dimension BOARD_PANEL_DIMENSION = new Dimension(350, 350);
    private static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static String LOCAL_IMG_PATH = "C:\\Users\\jiayu\\IdeaProjects\\ChessGame\\src\\com\\resrc\\";
    protected final BoardPanel boardPanel;
    private final JFrame chessFrame;
    private final Board chessBoard;
    protected Tile sourceTile = null;
    protected Tile destTile = null;
    protected Piece selectedPiece = null;
    protected Set<Coordinate> coordisMarkRed = new HashSet<>();
    private ScorePanel scorePanel;
    private JMenuItem startCmd;

    /**
     * Constructor for table, init settings
     */
    public BoardGUI(Board chessBoard) {
        this.chessFrame = new JFrame("Jiayu's Chess Game");
        this.chessFrame.setLayout(new BorderLayout());
        // add menuBar
        JMenuBar tableMenuBar = createMenuBar();
        this.chessFrame.setJMenuBar(tableMenuBar);
        this.chessFrame.setSize(FRAME_DIMENSION);
        // create board and board panel, add to chessFrame
        this.chessBoard = chessBoard;
        this.boardPanel = new BoardPanel();
        this.scorePanel = new ScorePanel();
        this.chessFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.chessFrame.add(this.scorePanel, BorderLayout.EAST);
        this.chessFrame.setVisible(true);
    }

    /**
     * Create menuBar, add menus to it
     *
     * @return menuBar created
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    }

    /**
     * Create menu, add menuItems to it
     *
     * @return menuBar created
     */
    private JMenu createFileMenu() {
        final JMenu cmdMenu = new JMenu("Game");
        this.startCmd = new JMenuItem("Restart");
        cmdMenu.add(startCmd);

        final JMenuItem exitCmd = new JMenuItem("Exit");
        exitCmd.addActionListener(e -> System.exit(0));
        cmdMenu.add(exitCmd);

        return cmdMenu;
    }

    public void addListenerToRestartCmd(ActionListener listener) {
        this.startCmd.addActionListener(listener);
    }

    public void addListenerToTile(int coordiX, int coordiY, MouseListener listener) {
        TilePanel[][] boardTiles = this.boardPanel.boardTiles;
        boardTiles[coordiX][coordiY].addMouseListener(listener);
    }

    /**
     * increment score for the user
     *
     * @param userIdx idx of the user to increment scor efor
     */
    public void incrementScore(int userIdx) {
        this.scorePanel.PlayerPanels[userIdx].incrementScore();
    }

    public void markKingInCheck(Set<Coordinate> coordis) {
        coordisMarkRed = coordis;
    }

    /**
     * get JFrame
     *
     * @return
     */
    public JFrame getFrame() {
        return this.chessFrame;
    }

    /**
     * Clear tile selections
     */
    protected void clearTileSelection() {
        sourceTile = null;
        destTile = null;
        selectedPiece = null;
    }

    /**
     * add listender to undo button
     *
     * @param listener MouseListener to add
     */
    public void addListenerToUndoButton(MouseListener listener) {
        System.out.println("addListenerToPanelButtons");
        JButton undoButton = this.scorePanel.gameControlPanel.undoButton;
        undoButton.addMouseListener(listener);
    }

    /**
     * add listener to redo button
     *
     * @param listener listener MouseListener to add
     */
    public void addListenerToRedoButton(MouseListener listener) {
        System.out.println("addListenerToRedoButton");
        JButton redoButton = this.scorePanel.gameControlPanel.redoButton;
        redoButton.addMouseListener(listener);
    }

    /**
     * add listner to black forfeit button
     *
     * @param listener listener MouseListener to add
     */
    public void addListenerToForfeitButtonBlack(MouseListener listener) {
        System.out.println("addListenerToForfeitButtonBlack");
        JButton forfeitButton = this.scorePanel.PlayerPanels[0].forfeit;
        forfeitButton.addMouseListener(listener);
    }

    /**
     * add listener to white forfeit button
     *
     * @param listener listener MouseListener to add
     */
    public void addListenerToForfeitButtonWhite(MouseListener listener) {
        System.out.println("addListenerToForfeitButtonWhite");
        JButton forfeitButton = this.scorePanel.PlayerPanels[1].forfeit;
        forfeitButton.addMouseListener(listener);
    }

    protected class BoardPanel extends JPanel {
        TilePanel[][] boardTiles = new TilePanel[chessBoard.getHeight()][chessBoard.getWidth()];

        /**
         * Constructor of the boardPanel
         */
        BoardPanel() {
            super(new GridLayout(Board.DEFAULT_BOARD_LENGTH, Board.DEFAULT_BOARD_LENGTH));
            int tileCtPerSide = Board.DEFAULT_BOARD_LENGTH;
            for (int i = 0; i < tileCtPerSide; ++i) {
                for (int j = 0; j < tileCtPerSide; ++j) {
                    TilePanel tilePanel = new TilePanel(new Coordinate(i, j));
                    this.boardTiles[i][j] = tilePanel;
                    // add to JPanel
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        /**
         * redraw the board
         *
         * @param board board to redraw
         */
        public void drawBoard(Board board) throws IOException {
            removeAll();
            for (int i = 0; i < chessBoard.getHeight(); ++i) {
                for (int j = 0; j < chessBoard.getWidth(); ++j) {
                    TilePanel tilePanel = this.boardTiles[i][j];
                    tilePanel.drawTile(board);
                    add(tilePanel);
                }
            }
            validate();
            repaint();
        }


    }

    private class TilePanel extends JPanel {
        private Coordinate coordi;

        /**
         * Constructor of the TilePanel, taking coordinate as the position to set
         *
         * @param coordi coordinate of the tilePanel
         */
        TilePanel(Coordinate coordi) {
            super(new GridBagLayout());
            this.coordi = coordi;
            setPreferredSize(TILE_PANEL_DIMENSION);
            setTileColor();
            try {
                addTilePieceImg(chessBoard);
            } catch (IOException e) {
                e.printStackTrace();
            }

            validate();
        }

        /**
         * Set tile colors on the window
         */
        private void setTileColor() {
            Tile tile = chessBoard.getTile(coordi.getx(), coordi.gety());
            Color backgroundColor = tile.getColor() == Tile.color.BLACK ?
                    Color.ORANGE : Color.WHITE;
            if (coordisMarkRed.contains(coordi))
                backgroundColor = Color.red;

            if (tile == sourceTile)
                backgroundColor = Color.lightGray;
            setBackground(backgroundColor);
        }

        /**
         * Add corresponding img for each chess piece on its tile on the window
         *
         * @param board board the piece is on
         * @throws IOException in case of file IO error
         */
        private void addTilePieceImg(Board board) throws IOException {
            this.removeAll();
            String filename = "";
            String frameFilename = "frame.png";
            boolean isLegalMove = selectedPiece != null && selectedPiece.getLegalMoves().contains(this.coordi);

            if (!board.getTile(coordi.getx(), coordi.gety()).isTileFree()) {
                // get the corresponding path to the image
                Piece piece = board.getTile(coordi.getx(), coordi.gety()).getPiece();
                filename = LOCAL_IMG_PATH + piece.getOwner().getName().substring(0, 1)
                        + "-" + piece.getPieceType().name().toLowerCase();
                filename += isLegalMove ? "-framed.png" : ".png";
            } else {
                filename += isLegalMove ? LOCAL_IMG_PATH + frameFilename : "";
            }
            // read in the image and adds to the tile
            if (!filename.isEmpty()) {
                BufferedImage img = ImageIO.read(new File(filename));
                this.add(new JLabel(new ImageIcon(img)));
            }

        }

        /**
         * draw tile on GUI
         *
         * @param board board where the tile is on
         */
        public void drawTile(Board board) throws IOException {
            setTileColor();
            addTilePieceImg(board);
            validate();
            repaint();
        }
    }

}
