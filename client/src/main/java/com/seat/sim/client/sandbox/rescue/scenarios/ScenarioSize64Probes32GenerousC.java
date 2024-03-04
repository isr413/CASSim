package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.negotiation.StochasticNegotiation;
import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.QuadTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes32GenerousC extends RescueScenario {

  public ScenarioSize64Probes32GenerousC(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32GenerousC",    // scenarioID
        0,                                    // base count
        32,                                   // drone count
        1024,                                 // victim count
        0,                                    // cooldown
        Range.Inclusive(0.0, 1.0, 0.125),     // alpha
        0.,                                   // beta
        Range.Inclusive(0.125, 1.0, 0.125),   // gamma
        10,                                   // trials
        threadID,                             // threadID
        4,                                    // threadCount
        seed,                                 // seed
        "seedsBC"                             // seedFile
      );
  }

  @Override
  public Optional<NegotiationManager> getNegotiations() {
    return Optional.of(new StochasticNegotiation(
        Range.Inclusive(0, 3, 1),
        9,
        Range.Inclusive(0., 1., 0.05),
        Range.Inclusive(0., .5, 0.05),
        Range.Inclusive(0., 1., 0.05),
        Range.Inclusive(0, .1, 0.01),
        Range.Inclusive(0, 1., 0.05),
        Range.Inclusive(1, 1),
        this.getRng()
      ));
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new QuadTaskManager(this));
  }

  @Override
  public boolean isAcceptable(String senderID, String receiverID, Proposal proposal) {
    return true;
  }
}
