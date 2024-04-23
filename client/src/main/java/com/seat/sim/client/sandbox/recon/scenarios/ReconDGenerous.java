package com.seat.sim.client.sandbox.recon.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.negotiation.StochasticNegotiation;
import com.seat.sim.client.sandbox.recon.remote.ReconScenario;
import com.seat.sim.client.sandbox.rescue.util.QuadTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ReconDGenerous extends ReconScenario {

  public ReconDGenerous(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ReconDGenerous",                     // scenarioID
        0,                                    // base count
        200,                                  // drone count
        50,                                   // intel count
        15,                                   // popup count
        300,                                  // adv count
        1800,                                 // intel points
        Range.Inclusive(600, 1200),           // popup time
        0,                                    // cooldown
        0.875,                                // alpha
        0.875,                                // beta
        0.875,                                // gamma
        40,                                   // trials
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
        10,
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
    return true;
  }

  @Override
  public double rankProposal(Proposal p) {
    return 1.;
  }
}
