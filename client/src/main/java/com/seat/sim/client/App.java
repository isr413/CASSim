package com.seat.sim.client;

import com.seat.sim.client.core.AppClient;
import com.seat.sim.client.core.ClientException;
import com.seat.sim.client.sandbox.ACSOS;
import com.seat.sim.common.core.Application;
import com.seat.sim.common.util.ArgsParser;

public class App {
  private static final String DELAY_ARG = "-d";
  private static final String DISPLAY_ARG = "--display";
  private static final String HOST_ARG = "-h";
  private static final String ID_ARG = "-id";
  private static final String PORT_ARG = "-p";

  private static Application getApplication(String scenarioID, String[] args) throws ClientException {
    if (scenarioID.equals("ACSOS")) {
      return new ACSOS(args);
    }
    throw new ClientException(String.format("Unrecognized application ID <%s>", scenarioID));
  }

  public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
    if (!parser.hasParam(App.ID_ARG)) {
      throw new ClientException("No application ID has been provided");
    }
    String scenarioID = parser.getString(App.ID_ARG);
    Application app = App.getApplication(scenarioID, args);
    AppClient client;
    if (parser.hasParam(App.HOST_ARG) && parser.hasParam(App.PORT_ARG)) {
      client = new AppClient(app, parser.getString(App.HOST_ARG), parser.getInt(App.PORT_ARG));
    } else if (parser.hasParam(App.HOST_ARG)) {
      client = new AppClient(app, parser.getString(App.HOST_ARG));
    } else if (parser.hasParam(App.PORT_ARG)) {
      client = new AppClient(app, parser.getInt(App.PORT_ARG));
    } else {
      client = new AppClient(app);
    }
    if (parser.hasParam(App.DELAY_ARG)) {
      client.run(parser.hasParam(App.DISPLAY_ARG), parser.getDouble(App.DELAY_ARG));
    } else {
      client.run(parser.hasParam(App.DISPLAY_ARG));
    }
  }
}
