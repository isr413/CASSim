package com.seat.sim.common.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.seat.sim.common.math.Vector;

public class Range implements Iterable<Double> {

  public static Range Exclusive(double end) {
    return Range.Exclusive(0., end, 1.);
  }

  public static Range Exclusive(double start, double end) {
    return Range.Exclusive(start, end, 1.);
  }

  public static Range Exclusive(double start, double end, double step) {
    return new Range(start, end, step, false);
  }

  public static Range Inclusive(double end) {
    return Range.Inclusive(0., end, 1.);
  }

  public static Range Inclusive(double start, double end) {
    return Range.Inclusive(start, end, 1.);
  }

  public static Range Inclusive(double start, double end, double step) {
    return new Range(start, end, step, true);
  }

  private double end;
  private double start;
  private double step;

  private Range(double start, double end, double step, boolean inclusive) {
    this.start = start;
    this.end = end + ((inclusive) ? step : 0.);
    this.step = step;
  }

  public Range(Range range) {
    this.start = range.start;
    this.end = range.end;
    this.step = range.step;
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

  public Iterator<Double> iterator() {
    return new RangeIterator(this);
  }

  public int points() {
    return (int) Math.ceil((this.end - this.start) / this.step);
  }

  public double sample() {
    return this.sample(new Random());
  }

  public double sample(Random rng) {
    return this.getStart() + this.getStep() * rng.getRandomNumber(this.points());
  }

  public String toString() {
    return String.format("[%.2f:%.2f:%.2f]", this.start, this.end, this.step);
  }

  private static class RangeIterator implements Iterator<Double> {

    private double cursor;
    private Range range;

    public RangeIterator(Range range) {
      this.range = range;
      this.cursor = range.start;
    }

    public boolean hasNext() {
      return this.cursor >= this.range.end || Vector.near(this.cursor, this.range.end);
    }

    public Double next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      }
      double point = this.cursor;
      this.cursor += this.range.step;
      return point;
    }
  }
}
