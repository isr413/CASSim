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
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class GUIGridFrame extends JFrame {

  protected static final int DEFAULT_PANEL_WIDTH = 1280;
  protected static final int DEFAULT_PANEL_HEIGHT = 720;
  protected static final String DEFAULT_TITLE = "App";

  private GUIGridPanel panel;

  public GUIGridFrame(Grid grid) {
    this(grid.getWidth(), grid.getHeight(), grid.getZoneSize());
  }

  public GUIGridFrame(int gridWidth, int gridHeight, int zoneSize) {
    this(GUIGridFrame.DEFAULT_TITLE, gridWidth, gridHeight, zoneSize);
  }

  public GUIGridFrame(String title, Grid grid) {
    this(title, grid.getWidth(), grid.getHeight(), grid.getZoneSize());
  }

  public GUIGridFrame(String title, int gridWidth, int gridHeight, int zoneSize) {
    this(title, GUIGridFrame.DEFAULT_PANEL_WIDTH, GUIGridFrame.DEFAULT_PANEL_HEIGHT, gridWidth, gridHeight, zoneSize);
  }

  public GUIGridFrame(int panelWidth, int panelHeight, Grid grid) {
    this(panelWidth, panelHeight, grid.getWidth(), grid.getHeight(), grid.getZoneSize());
  }

  public GUIGridFrame(int panelWidth, int panelHeight, int gridWidth, int gridHeight, int zoneSize) {
    this(GUIGridFrame.DEFAULT_TITLE, panelWidth, panelHeight, gridWidth, gridHeight, zoneSize);
  }

  public GUIGridFrame(String title, int panelWidth, int panelHeight, Grid grid) {
    this(title, panelWidth, panelHeight, grid.getWidth(), grid.getHeight(), grid.getZoneSize());
  }

  public GUIGridFrame(String title, int panelWidth, int panelHeight, int gridWidth, int gridHeight, int zoneSize) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    if (screenSize.getWidth() < panelWidth || screenSize.getHeight() < panelHeight) {
      panelWidth = (int) screenSize.getWidth();
      panelHeight = (int) screenSize.getHeight();
    }
    this.panel = new GUIGridPanel(panelWidth, panelHeight, gridWidth, gridHeight, zoneSize);
    add(this.panel);
    setTitle(title);
    setSize(panelWidth, panelHeight);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void displaySnap(Snapshot snap) {
    ArrayList<Point> points = new ArrayList<>();
    for (RemoteState remoteState : snap.getRemoteStates()) {
      if (!(remoteState.isEnabled() && remoteState.hasLocation())) {
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

    protected static final Color DEFAULT_GRID_COLOR = Color.LIGHT_GRAY;
    protected static final Color DEFAULT_MAP_BOUNDS_COLOR = Color.DARK_GRAY;
    protected static final int DEFAULT_POINT_SIZE = 4;

    private int gridHeight;
    private int gridWidth;
    private int height;
    private ArrayList<Point> points;
    private int width;
    private int zoneSize;

    public GUIGridPanel(int width, int height, int gridWidth, int gridHeight, int zoneSize) {
      this.width = width;
      this.height = height;
      this.gridWidth = gridWidth;
      this.gridHeight = gridHeight;
      this.zoneSize = zoneSize;
      this.points = new ArrayList<>();
    }

    private void drawDisplay(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      this.drawGridLines(g2d);
      this.drawGridBounds(g2d);
      this.drawPoints(g2d);
    }

    private void drawPoints(Graphics2D g2d) {
      for (Point p : this.points) {
        g2d.setPaint(p.color);
        g2d.fillOval(
          this.getGridBoundsX() + p.x - GUIGridPanel.DEFAULT_POINT_SIZE/2,
          this.getGridBoundsY() + p.y - GUIGridPanel.DEFAULT_POINT_SIZE/2,
          GUIGridPanel.DEFAULT_POINT_SIZE,
          GUIGridPanel.DEFAULT_POINT_SIZE
        );
      }
    }

    private void drawGridLines(Graphics2D g2d) {
      g2d.setPaint(GUIGridPanel.DEFAULT_GRID_COLOR);
      for (int i = this.getGridBoundsX() + this.zoneSize; i < this.getGridBoundsX() + this.gridWidth;
          i += this.zoneSize) {
        g2d.drawLine(i, this.getGridBoundsY(), i, this.getGridBoundsY() + this.gridHeight);
      }
      for (int j = this.getGridBoundsY() + this.zoneSize; j < this.getGridBoundsY() + this.gridHeight;
          j += this.zoneSize) {
        g2d.drawLine(this.getGridBoundsX(), j, this.getGridBoundsX() + this.gridWidth, j);
      }
    }

    private void drawGridBounds(Graphics2D g2d) {
      g2d.setPaint(GUIGridPanel.DEFAULT_MAP_BOUNDS_COLOR);
      g2d.drawRect(this.getGridBoundsX(), this.getGridBoundsY(), this.gridWidth, this.gridHeight);
    }

    private int getGridBoundsX() {
      return this.getCenterX() - (int) Math.round(this.gridWidth / 2.0);
    }

    private int getGridBoundsY() {
      return this.getCenterY() - (int) Math.round(this.gridHeight / 2.0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      repaint();
    }

    public int getCenterX() {
      return (int) Math.round(this.width / 2.0);
    }

    public int getCenterY() {
      return (int) Math.round(this.height / 2.0);
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
