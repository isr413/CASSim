package com.seat.sim.client;

import com.seat.sim.client.core.AppClient;
import com.seat.sim.client.core.ClientException;
import com.seat.sim.client.sandbox.BmpExample;
import com.seat.sim.client.sandbox.Default;
import com.seat.sim.client.sandbox.GlobalTaskQueue;
import com.seat.sim.client.sandbox.QuadTree;
import com.seat.sim.client.sandbox.RandomFixed;
import com.seat.sim.client.sandbox.RandomWalk;
import com.seat.sim.common.core.Application;
import com.seat.sim.common.util.ArgsParser;

public class App {
  private static final String DELAY_ARG = "-d";
  private static final String DISPLAY_ARG = "--display";
  private static final String HEIGHT_ARG = "-height";
  private static final String HOST_ARG = "-h";
  private static final String ID_ARG = "-id";
  private static final String PORT_ARG = "-p";
  private static final String SEED_ARG = "-seed";
  private static final String THREAD_ARG = "-j";
  private static final String WIDTH_ARG = "-width";

  private static Application getApplication(String scenarioID, ArgsParser args) throws ClientException {
    int threadID = (args.hasParam(App.THREAD_ARG)) ? args.getInt(App.THREAD_ARG) : 0;
    long seed = (args.hasParam(App.SEED_ARG)) ? args.getLong(App.SEED_ARG) : 0;
    try {
      if (scenarioID.equals("Default")) {
        return new Default(args, threadID, seed);
      }
      if (scenarioID.equals("BmpExample")) {
        return new BmpExample(args);
      }
      if (scenarioID.equals("GlobalTaskQueue")) {
        return new GlobalTaskQueue(args, threadID, seed);
      }
      if (scenarioID.equals("QuadTree")) {
        return new QuadTree(args, threadID, seed);
      }
      if (scenarioID.equals("RandomFixed")) {
        return new RandomFixed(args, threadID, seed);
      }
      if (scenarioID.equals("RandomWalk")) {
        return new RandomWalk(args, threadID, seed);
      }
    } catch (Exception e) {
      throw new ClientException(e.getMessage());
    }
    throw new ClientException(String.format("Unrecognized application ID <%s>", scenarioID));
  }

  public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
    if (!parser.hasParam(App.ID_ARG)) {
      throw new ClientException("No application ID has been provided");
    }
    String scenarioID = parser.getString(App.ID_ARG);
    Application app = App.getApplication(scenarioID, parser);
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
    if (parser.hasParam(App.WIDTH_ARG) && parser.hasParam(App.HEIGHT_ARG)) {
      client.setPanelDims(parser.getInt(App.WIDTH_ARG), parser.getInt(App.HEIGHT_ARG));
    }
    if (parser.hasParam(App.DELAY_ARG)) {
      client.run(parser.hasParam(App.DISPLAY_ARG), parser.getDouble(App.DELAY_ARG));
    } else {
      client.run(parser.hasParam(App.DISPLAY_ARG));
    }
  }
}
