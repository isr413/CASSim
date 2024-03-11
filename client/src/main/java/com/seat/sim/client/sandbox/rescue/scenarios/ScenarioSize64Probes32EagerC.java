package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.negotiation.StochasticNegotiation;
import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.QuadTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes32EagerC extends RescueScenario {

  public ScenarioSize64Probes32EagerC(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32EagerC",       // scenarioID
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
        Range.Inclusive(2, 3, 1),
        this.getRng()
      ));
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new QuadTaskManager(this));
  }

  @Override
  public boolean isAcceptable(Snapshot snap, RemoteState state, String senderID, String receiverID,
      Proposal proposal) {
    double expectedLoss = super.tasks
      .get()
      .predict(
          snap,
          state,
          proposal.getEarliestDeadline(),
          proposal.getEarlySuccessLikelihood(),
          proposal.getDeadline()
        );
    double expectedReward = (proposal.getEarlyRewardBonus() + proposal.getReward()) *
        proposal.getEarlySuccessLikelihood() + proposal.getReward() *
        (1. - proposal.getEarlySuccessLikelihood()) * proposal.getSuccessLikelihood();
    return Double.compare(expectedReward, expectedLoss) >= 0;
  }
}
