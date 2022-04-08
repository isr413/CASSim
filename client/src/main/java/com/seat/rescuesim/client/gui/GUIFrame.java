package com.seat.rescuesim.client.gui;

import java.awt.BasicStroke;
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

    private GUIPanel panel;

    public GUIFrame() {
        this("App");
    }

    public GUIFrame(String title) {
        this(title, 1280, 720);
    }

    public GUIFrame(String title, int width, int height) {
        this.panel = new GUIPanel(width, height);
        add(this.panel);
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void displaySnap(Snapshot snap) {
        this.displaySnap(snap, this.panel.getWidth(), this.panel.getHeight());
    }

    public void displaySnap(Snapshot snap, int width, int height) {
        ArrayList<Point> points = new ArrayList<>();
        for (RemoteState state : snap.getState()) {
            points.add(Point.Colored((int) state.getLocation().getX(), (int) state.getLocation().getY(),
                state.getTeam()));
        }
        this.panel.paintPoints(points, width, height);
    }

    static class GUIPanel extends JPanel implements ActionListener {

        private int height;
        private double heightScale;
        private ArrayList<Point> points = new ArrayList<>();
        private int width;
        private double widthScale;

        public GUIPanel(int width, int height) {
            this.width = width;
            this.height = height;
        }

        private void drawPoints(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(5));
            for (Point p : this.points) {
                int x = (int) Math.round(p.x * this.widthScale);
                int y = (int) Math.round(p.y * this.heightScale);
                g2d.setPaint(p.color);
                g2d.drawLine(x, y, x, y);
            }
            g2d.setPaint(Color.BLACK);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
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
            drawPoints(g);
        }

        public void paintPoints(ArrayList<Point> points, int width, int height) {
            this.points = points;
            this.widthScale = ((float) this.width) / width;
            this.heightScale = ((float) this.height * 0.95) / height;
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
