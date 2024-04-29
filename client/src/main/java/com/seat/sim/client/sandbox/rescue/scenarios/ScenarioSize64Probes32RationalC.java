package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

import com.seat.sim.client.negotiation.Contract;
import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.ParetoNegotiation;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.QuadTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes32RationalC extends RescueScenario {

  private static final int P_COUNT = 3;

  private Map<String, Result> cache = new HashMap<>();

  public ScenarioSize64Probes32RationalC(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32RationalC",    // scenarioID
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

  private Optional<Proposal> getEntry(String remoteID, Proposal p) {
    int idx = this.negotiations.get().getProposals(remoteID).indexOf(p);
    if (idx == -1) {
      return Optional.empty();
    }
    Proposal q = this.negotiations.get().getProposals(remoteID).get(idx);
    double eSucc = q.getEarlySuccessLikelihood() * p.getEarlySuccessLikelihood();
    double pSucc = q.getSuccessLikelihood() * p.getSuccessLikelihood();
    return Optional.of(q.clone(eSucc, pSucc));
  }

  @Override
  public Optional<NegotiationManager> getNegotiations() {
    return Optional.of(new ParetoNegotiation(
        Range.Inclusive(1, 9, 1.),
        Range.Inclusive(1., 1., 0.),
        Range.Inclusive(0., 1., 0.05),
        ScenarioSize64Probes32RationalC.P_COUNT,
        this.getRng(),
        (s) -> this.getManager().getDroneManager().hasDrone(s)
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
  public Optional<Contract> negotiate(Snapshot snap, RemoteState state, String senderID, String receiverID) {
    if (!this.hasNegotiations()) {
      return Optional.empty();
    }

    List<List<Proposal>> requesterBuckets = new ArrayList<>(ScenarioSize64Probes32RationalC.P_COUNT + 1);
    List<List<Proposal>> providerBuckets = new ArrayList<>(ScenarioSize64Probes32RationalC.P_COUNT + 1);
    for (int i = 0; i <= ScenarioSize64Probes32RationalC.P_COUNT; i++) {
      requesterBuckets.add(new ArrayList<>());
      providerBuckets.add(new ArrayList<>());
    }

    for (Proposal p : this.negotiations.get().getProposals(senderID)) {
      requesterBuckets.get(Integer.bitCount(p.getLabel())).add(p);
    }
    for (Proposal p : this.negotiations.get().getProposals(receiverID)) {
      providerBuckets.get(Integer.bitCount(p.getLabel())).add(p);
    }

    List<Proposal> requesterProposals = new ArrayList<>();
    for (List<Proposal> ps : requesterBuckets) {
      Collections.sort(ps);
      requesterProposals.add(ps.get(0));
    }
    List<Proposal> providerProposals = new ArrayList<>();
    for (List<Proposal> ps : providerBuckets) {
      Collections.sort(ps);
      providerProposals.add(ps.get(0));
    }

    PriorityQueue<Proposal> requesterQueue = new PriorityQueue<>();
    PriorityQueue<Proposal> providerQueue = new PriorityQueue<>();
    for (Proposal p : providerProposals) {
      requesterQueue.add(this.getEntry(senderID, p).get());
    }
    for (Proposal p : requesterProposals) {
      providerQueue.add(this.getEntry(receiverID, p).get());
    }

    while (!requesterQueue.isEmpty() && !providerQueue.isEmpty()) {
      Proposal p = requesterQueue.poll();
      int idx = this.negotiations.get().getProposals(receiverID).indexOf(p);
      if (idx >= 0) {
        Proposal q = this.negotiations.get().getProposals(receiverID).get(idx);
        double u1 = providerQueue.peek().getReward() * providerQueue.peek().getSuccessLikelihood();
        double u2 = q.getReward() * p.getSuccessLikelihood();
        if (Double.compare(u1, u2) <= 0) {
          if (this.isAcceptable(snap, state, senderID, receiverID, p)) {
            return Optional.of(this.negotiations.get().acceptProposal(senderID, receiverID, p));
          }
        }
      }
      if (requesterQueue.isEmpty()) {
        break;
      }
      p = providerQueue.poll();
      idx = this.negotiations.get().getProposals(senderID).indexOf(p);
      if (idx >= 0) {
        Proposal q = this.negotiations.get().getProposals(senderID).get(idx);
        double u1 = requesterQueue.peek().getReward() * requesterQueue.peek().getSuccessLikelihood();
        double u2 = q.getReward() * p.getSuccessLikelihood();
        if (Double.compare(u1, u2) <= 0) {
          if (this.isAcceptable(snap, state, senderID, receiverID, p)) {
            return Optional.of(this.negotiations.get().acceptProposal(senderID, receiverID, p));
          }
        }
      }
    }

    if (requesterQueue.isEmpty()) {
      while (!providerQueue.isEmpty()) {
        Proposal p = providerQueue.poll();
        if (this.isAcceptable(snap, state, senderID, receiverID, p)) {
          return Optional.of(this.negotiations.get().acceptProposal(senderID, receiverID, p));
        }
      }
    }
    if (providerQueue.isEmpty()) {
      while (!requesterQueue.isEmpty()) {
        Proposal p = requesterQueue.poll();
        if (this.isAcceptable(snap, state, senderID, receiverID, p)) {
          return Optional.of(this.negotiations.get().acceptProposal(senderID, receiverID, p));
        }
      }
    }

    return Optional.empty();
  }

  @Override
  public double rankProposal(Proposal p) {
    return p.getCumulativeReward();
  }

  static record Entry(double util, Proposal prop) implements Comparable<Entry> {

    public int compareTo(Entry e) {
      return -Double.compare(this.util, e.util);
    }
  }

  static record Result(double time, double loss) {}
}
