package com.seat.rescuesim.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.scenario.Snapshot;

public class GUIFrame extends JFrame {

    protected static final int DEFAULT_PANEL_WIDTH = 1280;
    protected static final int DEFAULT_PANEL_HEIGHT = 720;
    protected static final String DEFAULT_TITLE = "App";

    private GUIPanel panel;

    public GUIFrame(int mapWidth, int mapHeight, int zoneSize) {
        this(GUIFrame.DEFAULT_TITLE, mapWidth, mapHeight, zoneSize);
    }

    public GUIFrame(String title, int mapWidth, int mapHeight, int zoneSize) {
        this(title, GUIFrame.DEFAULT_PANEL_WIDTH, GUIFrame.DEFAULT_PANEL_HEIGHT, mapWidth, mapHeight, zoneSize);
    }

    public GUIFrame(int width, int height, int mapWidth, int mapHeight, int zoneSize) {
        this(GUIFrame.DEFAULT_TITLE, width, height, mapWidth, mapHeight, zoneSize);
    }

    public GUIFrame(String title, int width, int height, int mapWidth, int mapHeight, int zoneSize) {
        this.panel = new GUIPanel(width, height, mapWidth, mapHeight, zoneSize);
        add(this.panel);
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void displaySnap(Snapshot snap) {
        ArrayList<Point> points = new ArrayList<>();
        for (RemoteState state : snap.getState()) {
            points.add(Point.Colored((int) state.getLocation().getX(), (int) state.getLocation().getY(),
                state.getTeam()));
        }
        this.panel.paintPoints(points);
    }

    static class GUIPanel extends JPanel implements ActionListener {

        protected static final Color DEFAULT_GRID_COLOR = Color.LIGHT_GRAY;
        protected static final Color DEFAULT_MAP_BOUNDS_COLOR = Color.DARK_GRAY;

        private int height;
        private int mapHeight;
        private int mapWidth;
        private ArrayList<Point> points = new ArrayList<>();
        private int width;
        private int zoneSize;

        public GUIPanel(int width, int height, int mapWidth, int mapHeight, int zoneSize) {
            this.width = width;
            this.height = height;
            this.mapWidth = mapWidth;
            this.mapHeight = mapHeight;
            this.zoneSize = zoneSize;
        }

        private void drawDisplay(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            this.drawGridLines(g2d);
            this.drawMapBounds(g2d);
            this.drawPoints(g2d);
        }

        private void drawPoints(Graphics2D g2d) {
            for (Point p : this.points) {
                g2d.setPaint(p.color);
                g2d.drawLine(this.getMapX() + p.x, this.getMapY() + p.y, this.getMapX() + p.x, this.getMapY() + p.y);
            }
        }

        private void drawGridLines(Graphics2D g2d) {
            g2d.setPaint(GUIPanel.DEFAULT_GRID_COLOR);
            for (int i = this.getMapX() + this.zoneSize; i < this.getMapX() + this.mapWidth; i += this.zoneSize) {
                g2d.drawLine(i, this.getMapY(), i, this.getMapY() + this.mapHeight);
            }
            for (int j = this.getMapY() + this.zoneSize; j < this.getMapY() + this.mapHeight; j += this.zoneSize) {
                g2d.drawLine(this.getMapX(), j, this.getMapX() + this.mapWidth, j);
            }
        }

        private void drawMapBounds(Graphics2D g2d) {
            g2d.setPaint(GUIPanel.DEFAULT_MAP_BOUNDS_COLOR);
            g2d.drawRect(this.getMapX(), this.getMapY(), this.mapWidth, this.mapHeight);
        }

        private int getMapX() {
            return this.getCenterX() - (this.mapWidth / 2);
        }

        private int getMapY() {
            return this.getCenterY() - (this.mapHeight / 2);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }

        public int getCenterX() {
            return this.width / 2;
        }

        public int getCenterY() {
            return this.height / 2;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawDisplay(g);
        }

        public void paintPoints(ArrayList<Point> points) {
            this.points = points;
            repaint();
        }

    }

    static class Point {
        public int x;
        public int y;
        public Color color;

        public static Point Colored(int x, int y, TeamColor team) {
            switch (team) {
                case BLUE: return new Point(x, y, Color.BLUE);
                case CYAN: return new Point(x, y, Color.CYAN);
                case DARK_GRAY: return new Point(x, y, Color.DARK_GRAY);
                case GRAY: return new Point(x, y, Color.GRAY);
                case GREEN: return new Point(x, y, Color.GREEN);
                case LIGHT_GRAY: return new Point(x, y, Color.LIGHT_GRAY);
                case MAGENTA: return new Point(x, y, Color.MAGENTA);
                case ORANGE: return new Point(x, y, Color.ORANGE);
                case PINK: return new Point(x, y, Color.PINK);
                case RED: return new Point(x, y, Color.RED);
                case YELLOW: return new Point(x, y, Color.YELLOW);
                default: return new Point(x, y, Color.BLACK);
            }
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(int x, int y, Color color) {
            this(x, y);
            this.color = color;
        }

    }

}
