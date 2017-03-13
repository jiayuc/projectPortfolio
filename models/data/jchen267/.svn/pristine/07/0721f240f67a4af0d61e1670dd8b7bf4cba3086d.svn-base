/**
 * Created by jiayu on 2/12/2017.
 */
package com.chess.GUI;

import com.chess.board.Board;
import com.chess.board.Tile;
import com.chess.Utility.Coordinate;
import com.chess.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class BoardGUI {
    private final JFrame chessFrame;
    private final BoardPanel boardPanel;
    private final Board chessBoard;

    /***
     * Constants for the settings
     */
    private static Dimension FRAME_DIMENSION = new Dimension(500, 500);
    private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static String LOCAL_IMG_PATH = "C:\\Users\\jiayu\\IdeaProjects\\ChessGame\\src\\com\\resrc\\";

    /***
     * Constructor for table, init settings
     */
    public BoardGUI() {
        this.chessFrame = new JFrame("Jiayu's Chess Game");
        this.chessFrame.setLayout(new BorderLayout());
        // add menuBar
        JMenuBar tableMenuBar = createMenuBar();
        this.chessFrame.setJMenuBar(tableMenuBar);
        this.chessFrame.setSize(FRAME_DIMENSION);
        // create board and board panel, add to chessFrame
        this.chessBoard = new Board();
        this.boardPanel = new BoardPanel();
        this.chessFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.chessFrame.setVisible(true);
    }

    /***
     * Create menuBar, add menus to it
     * @return menuBar created
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        final JMenu cmdMenu = new JMenu("start");
        menuBar.add(cmdMenu);
        return menuBar;
    }


    private class BoardPanel extends JPanel {
        List<TilePanel> boardTiles;

        /***
         * Constructor of the boardPanel
         */
        BoardPanel() {
            super(new GridLayout(Board.DEFAULT_BOARD_LENGTH, Board.DEFAULT_BOARD_LENGTH));
            int tileCtPerSide = Board.DEFAULT_BOARD_LENGTH;
            this.boardTiles = new ArrayList<>();

            for (int i=0; i < tileCtPerSide; ++i) {
                for (int j = 0; j < tileCtPerSide; ++j) {
                    TilePanel tilePanel = new TilePanel(new Coordinate(i, j));
                    this.boardTiles.add(tilePanel);
                    // add to JPanel
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }

    private class TilePanel extends JPanel {
        private Coordinate coordi;

        /***
         * Constructor of the TilePanel, taking coordinate as the position to set
         * @param coordi coordinate of the tilePanel
         */
        TilePanel (Coordinate coordi) {
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

        /***
         * Add corresponding img for each chess piece on its tile on the window
         * @param board board the piece is on
         * @throws IOException in case of file IO error
         */
        private void addTilePieceImg(Board board) throws IOException {
            this.removeAll();
            if (!board.getTile(coordi.getx(), coordi.gety()).isTileFree()) {
                // get the corresponding path to the image
                String pieceIconPath = LOCAL_IMG_PATH;
                Piece piece = board.getTile(coordi.getx(), coordi.gety()).getPiece();
                String filename = pieceIconPath
                                  + piece.getOwner().getName().substring(0, 1)
                                  + "-" + piece.getPieceType().name().toLowerCase()+ ".png";
                // read in the image and adds to the tile
                final BufferedImage img = ImageIO.read(new File(filename));
                this.add(new JLabel(new ImageIcon(img)));

            }
        }

        /***
         * Set tile colors on the window
         */
        private void setTileColor() {
            Tile tile = chessBoard.getTile(coordi.getx(), coordi.gety());
            Color backgroundColor = tile.getColor() == Tile.color.BLACK ?
                                                       Color.ORANGE : Color.WHITE;
            setBackground(backgroundColor);
        }
    }
}
