package com.seat.sim.common.util;

import com.seat.sim.common.math.Vector;

public class Range {

  private double end;
  private double start;
  private double step;

  public Range(double end) {
    this(0., 1., end, false);
  }

  public Range(double end, boolean inclusive) {
    this(0., 1., end, inclusive);
  }

  public Range(double start, double end) {
    this(start, 1., end, false);
  }

  public Range(double start, double end, boolean inclusive) {
    this(start, 1., end, inclusive);
  }

  public Range(double start, double step, double end) {
    this(start, step, end, false);
  }

  public Range(double start, double step, double end, boolean inclusive) {
    this.start = start;
    this.step = step;
    this.end = end + ((inclusive) ? step : 0.);
  }

  public Range(Range range) {
    this.start = range.start;
    this.step = range.step;
    this.end = range.end;
  }

  public double getEnd() {
    return this.end;
  }

  public double getStart() {
    return this.start;
  }

  public double getStep() {
    return this.step;
  }

  public boolean isDone() {
    return this.start >= this.end || Vector.near(this.start, this.end);
  }

  public int points() {
    return (int) Math.ceil((this.end - this.start) / this.step);
  }

  public String toString() {
    return String.format("(%.2f:%.2f:%.2f)", this.start, this.step, this.end);
  }

  public void update() {
    if (this.isDone()) {
      return;
    }
    this.start += this.step;
  }
}
