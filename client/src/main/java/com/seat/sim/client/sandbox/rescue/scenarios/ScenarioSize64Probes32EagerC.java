package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.negotiation.StochasticNegotiation;
import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.QuadTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes32EagerC extends RescueScenario {

  private Map<String, Result> cache = new HashMap<>();

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
        Optional.empty(),
        9,
        Range.Inclusive(1., 1., 0.),
        Optional.empty(),
        Range.Inclusive(0., 1., 0.05),
        Optional.empty(),
        Range.Inclusive(0., 1., 0.05),
        Range.Inclusive(2., 3., 1.),
        this.getRng()
      ));
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new QuadTaskManager(this));
  }

  @Override
  public boolean isAcceptable(Snapshot snap, RemoteState state, String senderID, String receiverID,
      Proposal proposal) {
    if (!this.cache.containsKey(state.getRemoteID()) ||
        !Vector.near(snap.getTime(), this.cache.get(state.getRemoteID()).time)) {
      double mass = super.getManager().getDroneManager().getDetectedAssets().size();
      double expLoss = super.tasks
        .get()
        .predict(
            snap,
            state,
            proposal.getEarliestDeadline(),
            proposal.getEarlySuccessLikelihood(),
            proposal.getDeadline(),
            Optional.of(mass)
          );
      this.cache.put(state.getRemoteID(), new Result(snap.getTime(), expLoss));
    }
    return Double.compare(rankProposal(proposal), this.cache.get(state.getRemoteID()).loss) >= 0;
  }

  @Override
  public double rankProposal(Proposal p) {
    return p.getCumulativeReward();
  }

  static record Result(double time, double loss) {}
}
