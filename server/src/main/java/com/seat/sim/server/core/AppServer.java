package com.seat.sim.server.core;

import java.util.Map;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.net.JsonSocket;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.intent.IntentionType;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.server.scenario.Scenario;

public class AppServer {

  public static final String SERVER_ID = "__SERVER__";

  private JsonSocket socket;

  public AppServer() throws CommonException {
    this.socket = JsonSocket.Server();
  }

  public AppServer(int port) throws CommonException {
    this.socket = JsonSocket.Server(port);
  }

  public void run() throws CommonException, ServerException, SimException {
    Debugger.logger.info("Running server ...");

    while (true) {
      Debugger.logger.info("Waiting for scenario config ...");
      ScenarioConfig config = this.socket.getScenarioConfigBlocking();
      Debugger.logger.state(String.format("Received scenario config <%s>", config.getScenarioID()));

      Scenario scenario = new Scenario(config);
      Debugger.logger.info(String.format("Running scenario <%s> ...", scenario.getScenarioID()));

      Snapshot snap = scenario.getSnapshot();
      Debugger.logger.info(String.format("Sending initial snap <%s> ...", snap.getHash()));
      this.socket.sendSnapshot(snap);
      Debugger.logger.state(String.format("Snap <%s> sent", snap.getHash()));

      while (true) {
        double time = scenario.getTime() + scenario.getStepSize();

        Debugger.logger.info("Waiting for intention(s) ...");
        Map<String, IntentionSet> intentions = this.socket.getIntentionsBlocking();
        Debugger.logger.state("Received intention(s)");

        if (intentions.containsKey(AppServer.SERVER_ID)) {
          Debugger.logger.info("Updating server ...");
          IntentionSet serverIntentions = intentions.get(AppServer.SERVER_ID);
          if (serverIntentions.hasIntentionWithType(IntentionType.DONE) ||
              serverIntentions.hasIntentionWithType(IntentionType.STOP) ||
              serverIntentions.hasIntentionWithType(IntentionType.SHUTDOWN) ||
              serverIntentions.hasIntentionWithType(IntentionType.DEACTIVATE)) {
            Debugger.logger.state("Shutting server down");
            break;
          }
          continue;
        }

        Debugger.logger.info(String.format("Updating scenario <%s> to time=%.2f ...", scenario.getScenarioID(), time));
        if (scenario.getMissionLength() < time) {
          scenario.update(intentions, scenario.getMissionLength() + scenario.getStepSize() - time);
        } else {
          scenario.update(intentions, scenario.getStepSize());
        }

        snap = scenario.getSnapshot();
        Debugger.logger.info(String.format("Sending snap <%s> ...", snap.getHash()));
        this.socket.sendSnapshot(snap);
        Debugger.logger.state(String.format("Snap <%s> sent", snap.getHash()));

        if (scenario.isDone()) {
          break;
        }
      }

      Debugger.logger.state(String.format("Scenario <%s> is done", scenario.getScenarioID()));
    }
  }
}
