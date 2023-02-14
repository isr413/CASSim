package com.seat.sim.common.util;

import java.util.List;

public class TimeSeries<T> {

  private List<T> data;
  private List<Double> time;

  public TimeSeries(List<Double> time, List<T> data) {
    this.time = time;
    this.data = data;
  }

  public List<T> getData() {
    return this.data;
  }

  public List<Double> getTime() {
    return this.time;
  }

  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("Time,Data\n");
    for (int i = 0; i < this.time.size(); i++) {
      if (this.data.get(i).getClass().isPrimitive()) {
        str.append(String.format("%.4f,%s\n", this.time.get(i), this.data.get(i).toString()));
      } else {
        str.append(String.format("%.4f,\"%s\"\n", this.time.get(i), this.data.get(i).toString()));
      }
    }
    return str.toString();
  }
}
