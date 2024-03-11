package com.seat.sim.client;

import com.seat.sim.client.core.AppClient;
import com.seat.sim.client.core.ClientException;
import com.seat.sim.client.sandbox.rescue.scenarios.*;
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
      if (scenarioID.equals("ScenarioSize64Probes32BestWalkA")) {
        return new ScenarioSize64Probes32BestWalkA(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32BestWalkB")) {
        return new ScenarioSize64Probes32BestWalkB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32BestWalkC")) {
        return new ScenarioSize64Probes32BestWalkC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32EagerC")) {
        return new ScenarioSize64Probes32EagerC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32GenerousC")) {
        return new ScenarioSize64Probes32GenerousC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32GlobalExploreA")) {
        return new ScenarioSize64Probes32GlobalExploreA(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32GlobalExploreB")) {
        return new ScenarioSize64Probes32GlobalExploreB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32GlobalExploreC")) {
        return new ScenarioSize64Probes32GlobalExploreC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32HelpfulC")) {
        return new ScenarioSize64Probes32HelpfulC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32QuadSmgA")) {
        return new ScenarioSize64Probes32QuadSmgA(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32QuadSmgB")) {
        return new ScenarioSize64Probes32QuadSmgB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32QuadSmgC")) {
        return new ScenarioSize64Probes32QuadSmgC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32QuadSmgD")) {
        return new ScenarioSize64Probes32QuadSmgD(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32QuadSmgE")) {
        return new ScenarioSize64Probes32QuadSmgE(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes1RandomWalkA") ||
          scenarioID.equals("Default")) {
        return new ScenarioSize64Probes1RandomWalkA(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes1RandomWalkB")) {
        return new ScenarioSize64Probes1RandomWalkB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes1RandomWalkC")) {
        return new ScenarioSize64Probes1RandomWalkC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32RandomWalkA")) {
        return new ScenarioSize64Probes32RandomWalkA(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32RandomWalkB")) {
        return new ScenarioSize64Probes32RandomWalkB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32RandomWalkC")) {
        return new ScenarioSize64Probes32RandomWalkC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32SelfishC")) {
        return new ScenarioSize64Probes32SelfishC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32SmgWalkA")) {
        return new ScenarioSize64Probes32SmgWalkA(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32SmgWalkB")) {
        return new ScenarioSize64Probes32SmgWalkB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32SmgWalkC")) {
        return new ScenarioSize64Probes32SmgWalkC(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32StochasticWalkB")) {
        return new ScenarioSize64Probes32StochasticWalkB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32WeightedWalkA")) {
        return new ScenarioSize64Probes32WeightedWalkA(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32WeightedWalkB")) {
        return new ScenarioSize64Probes32WeightedWalkB(args, threadID, seed);
      }
      if (scenarioID.equals("ScenarioSize64Probes32WeightedWalkC")) {
        return new ScenarioSize64Probes32WeightedWalkC(args, threadID, seed);
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
