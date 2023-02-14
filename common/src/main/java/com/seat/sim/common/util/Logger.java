package com.seat.sim.common.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger implements Closeable {

  private String filename;
  private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
  private PrintWriter writer;

  public static TimeSeries<String> Parse(String filename) throws IOException {
    List<Double> time = new ArrayList<>();
    List<String> data = new ArrayList<>();
    Files
        .lines(Path.of(filename))
        .forEach(line -> {
          String[] split = line.trim().split("\\] ");
          time.add(Double.parseDouble(split[0].trim().split(" ")[1].trim()));
          if (split.length > 2) {
            StringBuilder str = new StringBuilder();
            str.append(split[1]);
            for (int i = 2; i < split.length; i++) {
              str.append("] ");
              str.append(split[i]);
            }
            data.add(str.toString().trim());
          } else {
            data.add(split[1].trim());
          }
        });
    return new TimeSeries<>(time, data);
  }

  public Logger(String scenarioID) throws IOException {
    this(scenarioID, false);
  }

  public Logger(String scenarioID, boolean append) throws IOException {
    this.filename = String.format("%s_%s.log", scenarioID, new SimpleDateFormat("yyyyMMddHHmmssZ").format(new Date()));
    this.writer = new PrintWriter(new FileWriter(new File(filename), append), true);
  }

  private String getDatestamp() {
    return this.formatter.format(new Date());
  }

  public void close() throws IOException {
    this.writer.close();
  }

  public String getFilename() {
    return this.filename;
  }

  public void log(double simTime, String msg) {
    this.writer.println(String.format("[%s %.4f] %s", this.getDatestamp(), simTime, msg));
  }
}
