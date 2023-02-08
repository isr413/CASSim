package com.seat.sim.server;

import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.server.core.AppServer;

public class App {
  private static final String PORT_ARG = "-p";

  public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
    AppServer server = (parser.hasParam(App.PORT_ARG)) ?
      new AppServer(parser.getInt(App.PORT_ARG)):
      new AppServer();
    server.run();
  }
}
