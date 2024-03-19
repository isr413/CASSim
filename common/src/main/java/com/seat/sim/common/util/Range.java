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
  private boolean inclusive;
  private double start;
  private double step;

  private Range(double start, double end, double step, boolean inclusive) {
    this.start = start;
    this.end = end;
    this.step = step;
    this.inclusive = inclusive;
  }

  public Range(Range range) {
    this.start = range.start;
    this.end = range.end;
    this.step = range.step;
    this.inclusive = range.inclusive;
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

  public boolean isInclusive() {
    return this.inclusive;
  }

  public Iterator<Double> iterator() {
    return new RangeIterator(this);
  }

  public int points() {
    if (Vector.near(this.step, 0.)) {
      if (Vector.near(this.start, this.end)) {
        return ((this.inclusive) ? 1 : 0);
      }
      return ((this.inclusive) ? 2 : 1);
    }
    int length = (int) Math.ceil((this.end - this.start) / this.step - Vector.PRECISION);
    return length + ((this.inclusive) ? 1 : 0);
  }

  public double sample() {
    return this.sample(new Random());
  }

  public double sample(Random rng) {
    if (Vector.near(this.start, this.end)) {
      return this.start;
    }
    if (Vector.near(this.step, 0.)) {
      if (!this.inclusive) {
        return this.start;
      }
      return ((rng.getRandomNumber(2) == 0) ? this.start : this.end);
    }
    double choice = this.getStart() + this.getStep() * rng.getRandomNumber(this.points());
    return ((this.inclusive) ? Math.min(choice, this.end) : choice);
  }

  public String toString() {
    return String.format("[%.2f:%.2f:%.2f]", this.start, this.end, this.step);
  }

  public double uniform() {
    return this.uniform(new Random());
  }

  public double uniform(Random rng) {
    return rng.getRandomPoint(this.getStart(), this.getEnd());
  }

  private static class RangeIterator implements Iterator<Double> {

    private double cursor;
    private Range range;

    public RangeIterator(Range range) {
      this.range = range;
      this.cursor = range.start;
    }

    public boolean hasNext() {
      if (this.range.inclusive) {
        return this.cursor <= this.range.end || Vector.near(this.cursor, this.range.end);
      }
      return this.cursor < this.range.end && !Vector.near(this.cursor, this.range.end);
    }

    public Double next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      }
      double point = this.cursor;
      if (Vector.near(this.range.step, 0.)) {
        this.cursor = ((Vector.near(this.cursor, this.range.end)) ? this.range.end + 1 : this.range.end);
      } else {
        this.cursor += this.range.step;
      }
      return point;
    }
  }
}
