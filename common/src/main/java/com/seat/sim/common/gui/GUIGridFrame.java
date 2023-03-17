package com.seat.sim.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.ZoneType;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class GUIGridFrame extends JFrame {

  private static final int DEFAULT_PANEL_WIDTH = 1280;
  private static final int DEFAULT_PANEL_HEIGHT = 720;
  private static final String DEFAULT_TITLE = "CASSim";

  private GUIGridPanel panel;

  public GUIGridFrame(Grid grid) {
    this(GUIGridFrame.DEFAULT_TITLE, grid);
  }

  public GUIGridFrame(String title, Grid grid) {
    this(title, GUIGridFrame.DEFAULT_PANEL_WIDTH, GUIGridFrame.DEFAULT_PANEL_HEIGHT, grid);
  }

  public GUIGridFrame(int panelWidth, int panelHeight, Grid grid) {
    this(GUIGridFrame.DEFAULT_TITLE, panelWidth, panelHeight, grid);
  }

  public GUIGridFrame(String title, int panelWidth, int panelHeight, Grid grid) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    if (screenSize.getWidth() < panelWidth || screenSize.getHeight() < panelHeight) {
      panelWidth = (int) screenSize.getWidth();
      panelHeight = (int) screenSize.getHeight();
    }
    this.panel = new GUIGridPanel(panelWidth, panelHeight, grid);
    this.add(this.panel);
    this.setTitle(title);
    this.setSize(panelWidth, panelHeight);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void displaySnap(Snapshot snap) {
    ArrayList<Point> points = new ArrayList<>();
    for (RemoteState remoteState : snap.getRemoteStates()) {
      if (remoteState.isDone() || !remoteState.isEnabled() || !remoteState.hasLocation()) {
        continue;
      }
      points.add(Point.Colored(
        (int) Math.round(remoteState.getLocation().getX()),
        (int) Math.round(remoteState.getLocation().getY()),
        remoteState.getTeam())
      );
    }
    this.panel.paintPoints(points);
  }

  static class GUIGridPanel extends JPanel implements ActionListener {

    private static final Color DEFAULT_GRID_COLOR = Color.DARK_GRAY;
    private static final Color DEFAULT_GRID_LINES_COLOR = Color.LIGHT_GRAY;
    private static final int DEFAULT_POINT_SIZE = 4;

    private Grid grid;
    private int height;
    private ArrayList<Point> points;
    private int width;

    public GUIGridPanel(int width, int height, Grid grid) {
      this.width = width;
      this.height = height;
      this.grid = grid;
      this.points = new ArrayList<>();
    }

    private void drawDisplay(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      this.drawGrid(g2d);
      this.drawPoints(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
      this.drawGridBackground(g2d);
      this.drawGridLines(g2d);
    }

    private void drawGridBackground(Graphics2D g2d) {
      g2d.setPaint(GUIGridPanel.DEFAULT_GRID_COLOR);
      g2d.drawRect(this.getGridBoundsX(), this.getGridBoundsY(), this.grid.getWidth(), this.grid.getHeight());
      if (!this.grid.hasZones()) {
        return;
      }
      for (int y = 0; y < this.grid.getHeightInZones(); y++) {
        for (int x = 0; x < this.grid.getWidthInZones(); x++) {
          if (!this.grid.getZone(y, x).hasZoneType(ZoneType.BLOCKED)) {
            continue;
          }
          g2d.fillRect(
              this.getGridBoundsX() + x * this.grid.getZoneSize(),
              this.getGridBoundsY() + y * this.grid.getZoneSize(),
              this.grid.getZoneSize(),
              this.grid.getZoneSize()
            );
        }
      }
    }

    private void drawGridLines(Graphics2D g2d) {
      if (this.grid.getZoneSize() < GUIGridPanel.DEFAULT_POINT_SIZE + 4) {
        return;
      }
      g2d.setPaint(GUIGridPanel.DEFAULT_GRID_LINES_COLOR);
      for (int i = this.getGridBoundsX() + this.grid.getZoneSize();
           i < this.getGridBoundsX() + this.grid.getWidth();
           i += this.grid.getZoneSize()) {
        g2d.drawLine(i, this.getGridBoundsY(), i, this.getGridBoundsY() + this.grid.getHeight());
      }
      for (int j = this.getGridBoundsY() + this.grid.getZoneSize();
           j < this.getGridBoundsY() + this.grid.getHeight();
           j += this.grid.getZoneSize()) {
        g2d.drawLine(this.getGridBoundsX(), j, this.getGridBoundsX() + this.grid.getWidth(), j);
      }
    }

    private void drawPoints(Graphics2D g2d) {
      for (Point p : this.points) {
        g2d.setPaint(p.color);
        g2d.fillOval(
          this.getGridBoundsX() + p.x - GUIGridPanel.DEFAULT_POINT_SIZE / 2,
          this.getGridBoundsY() + p.y - GUIGridPanel.DEFAULT_POINT_SIZE / 2,
          GUIGridPanel.DEFAULT_POINT_SIZE,
          GUIGridPanel.DEFAULT_POINT_SIZE
        );
      }
    }

    private int getGridBoundsX() {
      return this.getCenterX() - (int) Math.round(this.grid.getWidth() / 2.);
    }

    private int getGridBoundsY() {
      return this.getCenterY() - (int) Math.round(this.grid.getHeight() / 2.);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      repaint();
    }

    public int getCenterX() {
      return (int) Math.round(this.width / 2.0);
    }

    public int getCenterY() {
      return (int) Math.round(this.height / 2.0) - 60;
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
        case GRAY: return new Point(x, y, Color.GRAY);
        case GREEN: return new Point(x, y, Color.GREEN);
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
