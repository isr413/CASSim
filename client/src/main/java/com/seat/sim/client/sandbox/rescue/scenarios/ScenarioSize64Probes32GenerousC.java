package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.negotiation.Contract;
import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.ParetoNegotiation;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.QuadTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes32GenerousC extends RescueScenario {

  private static final int P_COUNT = 3;

  public ScenarioSize64Probes32GenerousC(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32GenerousC",    // scenarioID
        0,                                    // base count
        32,                                   // drone count
        1024,                                 // victim count
        0,                                    // cooldown
        Range.Inclusive(0.125, 0.125, 0.125),     // alpha
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
    return Optional.of(new ParetoNegotiation(
        Range.Inclusive(1, 9, 1.),
        Range.Inclusive(1., 1., 0.),
        Range.Inclusive(0., 1., 0.05),
        ScenarioSize64Probes32GenerousC.P_COUNT,
        this.getRng(),
        (s) -> this.getManager().getDroneManager().hasDrone(s)
      ));
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new QuadTaskManager(this));
  }

  @Override
  public Optional<Contract> negotiate(Snapshot snap, RemoteState state, String senderID, String receiverID) {
    if (!this.hasNegotiations()) {
      return Optional.empty();
    }

    for (Proposal p : this.negotiations.get().getProposals(senderID)) {
      int idx = this.negotiations.get().getProposals(receiverID).indexOf(p);
      if (idx == -1) continue;
      Proposal q = this.negotiations.get().getProposals(receiverID).get(idx);
      double eSucc = p.getEarlySuccessLikelihood() * q.getEarlySuccessLikelihood();
      double pSucc = p.getSuccessLikelihood() * q.getSuccessLikelihood();
      return Optional.of(this.negotiations.get().acceptProposal(senderID, receiverID, q.clone(eSucc, pSucc)));
    }

    return Optional.empty();
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
