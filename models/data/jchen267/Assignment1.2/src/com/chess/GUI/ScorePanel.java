package com.chess.GUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by jiayu on 2/19/2017.
 */
public class ScorePanel extends JPanel {
    private static final Color DEFAULT_COLOR = Color.gray;
    protected PlayerPanel[] PlayerPanels = new PlayerPanel[2];
    protected GameControlPanel gameControlPanel;

    /**
     * Constructore for ScorePanel
     */
    public ScorePanel() {
        super(new BorderLayout());
        this.setBackground(DEFAULT_COLOR);
        this.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        int width = 100;
        this.PlayerPanels[0] = new PlayerPanel("BlackPlayer");
        this.PlayerPanels[1] = new PlayerPanel("WhitePlayer");
        this.add(this.PlayerPanels[0].panel, BorderLayout.NORTH);
        this.add(this.PlayerPanels[1].panel, BorderLayout.SOUTH);

        this.gameControlPanel = new GameControlPanel();
        this.add(this.gameControlPanel.panel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(170, 80));
    }

    /**
     * inner class PlayerPanel, where user information are displayed
     * and each user is given a button to forfeit at anytime
     */
    protected class PlayerPanel {
        protected JPanel panel = new JPanel();
        protected JLabel scoreLabel = new JLabel("", JLabel.CENTER);
        protected JLabel name;
        protected JButton forfeit = new JButton("forfeit");
        private int score = 0;

        /**
         * Constructor or PlayPanel
         * @param playerName the user provided username
         */
        public PlayerPanel(String playerName) {

            this.panel.setLayout(new GridLayout(4, 1));
            panel.setSize(300, 300);
            panel.setBackground(Color.white);
            // add name labels
            name = new JLabel("Player: " + playerName, JLabel.CENTER);
            this.panel.add(name);
            // add score labels
            panel.add(scoreLabel);
            this.setScore();
            // add buttons
            JPanel panelButtons = new JPanel();
            panelButtons.setLayout(new GridLayout(1, 2));
            forfeit.setBorder(BorderFactory.createLineBorder(Color.white));
            panelButtons.add(forfeit);
            panel.add(panelButtons);
        }

        /**
         * private method to internally set score on the score board
         */
        private void setScore() {
            this.scoreLabel.setText("Score: " + this.score);
        }

        /**
         * public method used to increment score on the board
         */
        public void incrementScore() {
            ++this.score;
            this.setScore();
        }
    }

    /**
     * inner class, creates the game control panel
     * where user can perform redo and undo
     */
    protected class GameControlPanel {
        protected JPanel panel = new JPanel();
        protected JButton undoButton = new JButton("<< undo");
        protected JButton redoButton = new JButton("redo >>");

        /**
         * constructor for GameControlPanel
         */
        public GameControlPanel() {
            this.panel.setLayout(new GridLayout(0, 1));
            this.panel.setSize(150, 50);
            this.undoButton.setBackground(Color.white);
            this.redoButton.setBackground(Color.white);
            this.panel.add(undoButton);
            this.panel.add(redoButton);
        }
    }
}
