package com.seat.sim.client.sandbox.recon.scenarios;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.negotiation.StochasticNegotiation;
import com.seat.sim.client.sandbox.recon.remote.IntelManager;
import com.seat.sim.client.sandbox.recon.remote.ReconScenario;
import com.seat.sim.client.sandbox.rescue.util.QuadTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ReconDHelpful extends ReconScenario {

  private Map<String, Result> cache = new HashMap<>();

  public ReconDHelpful(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ReconDHelpful",                      // scenarioID
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
        Range.Inclusive(1., 1., 0.),
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
      String droneIDs = (receiverID.contains(state.getRemoteID())) ? receiverID : senderID;
      List<RemoteState> states = Arrays.stream(droneIDs.split("::"))
          .map(droneID -> snap.getRemoteStateWithID(droneID))
          .collect(Collectors.toList());
      double expLoss = super.tasks
        .get()
        .predict(
            snap,
            states,
            proposal.getEarliestDeadline(),
            proposal.getEarlySuccessLikelihood(),
            proposal.getDeadline(),
            Optional.of(mass)
          );
      this.cache.put(state.getRemoteID(), new Result(snap.getTime(), expLoss));
    }
    double points = (receiverID.contains(state.getRemoteID()))
        ? ((IntelManager) super.getManager().getAssetManager()).getPointAssignment(senderID)
        : ((IntelManager) super.getManager().getAssetManager()).getPointAssignment(receiverID);
    return Double.compare(rankProposal(proposal) * points / 1800, this.cache.get(state.getRemoteID()).loss) >= 0;
  }

  @Override
  public double rankProposal(Proposal p) {
    return (p.getEarlyRewardBonus() + p.getReward()) * p.getEarlySuccessLikelihood() +
        p.getReward() * (1. - p.getEarlySuccessLikelihood()) * p.getSuccessLikelihood();
  }

  static record Result(double time, double loss) {}
}
