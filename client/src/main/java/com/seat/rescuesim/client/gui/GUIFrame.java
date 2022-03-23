package com.seat.rescuesim.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.remote.RemoteState;

public class GUIFrame extends JFrame {

    private GUIPanel panel;

    public GUIFrame() {
        this("App");
    }

    public GUIFrame(String title) {
        this(title, 1280, 720);
    }

    public GUIFrame(String title, int width, int height) {
        this.panel = new GUIPanel();
        add(this.panel);
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void displaySnap(Snapshot snap) {
        ArrayList<Point> points = new ArrayList<>();
        for (RemoteState state : snap.getState()) {
            points.add(new Point((int) state.getLocation().getX(), (int) state.getLocation().getY()));
        }
        this.panel.paintPoints(points);
    }

    static class GUIPanel extends JPanel implements ActionListener {

        private ArrayList<Point> points = new ArrayList<>();

        private void drawPoints(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(Color.blue);
            for (Point p : this.points) {
                g2d.drawLine(p.x, p.y, p.x, p.y);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawPoints(g);
        }

        public void paintPoints(ArrayList<Point> points) {
            this.points = points;
            repaint();
        }

    }

    static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

}
