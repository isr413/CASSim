package com.seat.sim.client.negotiation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.seat.sim.common.util.Random;
import com.seat.sim.common.util.Range;

public class ParetoNegotiation extends DynamicNegotiation {

  private static final double E_DEADLINE = 0.;
  private static final double E_REWARD = 0.;
  private static final double E_START_TIME = 0.;
  private static final double E_SUCCESS = 0.;
  private static final double NEG_DEADLINE = 1.;
  private static final double NEG_DURATION = 1.;
  private static final double NEG_START_TIME = 0.;
  private static final double PENALTY = 0.;

  private Range deadline;
  private Function<String, Boolean> matcher;
  private int pCount;
  private Range pSuccess;
  private Range reward;
  private Random rng;

  public ParetoNegotiation(Range deadline, Range reward, Range pSuccess, int pCount, Random rng,
      Function<String, Boolean> providerMatcher) {
    this.deadline = deadline;
    this.reward = reward;
    this.pSuccess = pSuccess;
    this.pCount = pCount;
    this.rng = rng;
    this.matcher = providerMatcher;
  }

  private Proposal generateProposal(int label, double reward, double pSuccess) {
    return new Proposal(
        Optional.of(label),
        ParetoNegotiation.NEG_START_TIME,
        ParetoNegotiation.NEG_DEADLINE,
        ParetoNegotiation.NEG_DURATION,
        ParetoNegotiation.E_START_TIME,
        ParetoNegotiation.E_DEADLINE,
        this.deadline.sample(this.rng),
        reward,
        ParetoNegotiation.E_REWARD,
        ParetoNegotiation.E_SUCCESS,
        pSuccess,
        ParetoNegotiation.PENALTY
      );
  }

  private void ensureProposals(String remoteID) {
    if (this.proposals.containsKey(remoteID)) {
      return;
    }
    if (this.matcher.apply(remoteID)) {
      this.ensureProviderProposals(remoteID);
    } else {
      this.ensureRequesterProposals(remoteID);
    }
    Collections.sort(this.getProposals(remoteID));
  }

  private void ensureProviderProposals(String providerID) {
    int n = (int) Math.pow(this.pCount, 2);

    double[] util = new double[n];
    double[] succ = new double[n];

    for (int i = 1; i < n; i++) {
      double minUtil = this.reward.getStart();
      double minSucc = this.pSuccess.getStart();
      for (int j = 1; j < n; j++) {
        if (i == j || (j & i) != j) {
          continue;
        }
        minUtil = Math.max(minUtil, util[j]);
        minSucc = Math.max(minSucc, succ[j]);
      }

      double maxUtil = this.reward.getEnd();
      double maxSucc = this.pSuccess.getEnd();
      for (int j = 1; j < i; j++) {
        if ((i & j) != i) {
          continue;
        }
        maxUtil = Math.min(maxUtil, util[j]);
        maxSucc = Math.min(maxSucc, succ[j]);
      }

      util[i] = Range.Inclusive(minUtil, maxUtil, this.reward.getStep()).sample(this.rng);
      succ[i] = Range.Inclusive(minSucc, maxSucc, this.pSuccess.getStep()).sample(this.rng);
    }

    double minUtil = 1.;
    double minSucc = 1.;
    for (int i = 1; i < n; i++) {
      minUtil = Math.min(minUtil, util[i]);
      minSucc = Math.min(minSucc, succ[i]);
    }

    util[0] = Range.Inclusive(this.reward.getStart(), minUtil, this.reward.getStep()).sample(this.rng);
    succ[0] = Range.Inclusive(this.pSuccess.getStart(), minSucc, this.pSuccess.getStep()).sample(this.rng);

    for (int i = 0; i < n; i++) {
      this.addProposal(providerID, this.generateProposal(i, util[i], succ[i]));
    }
  }

  private void ensureRequesterProposals(String requesterID) {
    int n = (int) Math.pow(this.pCount, 2);

    double[] util = new double[n];
    double[] succ = new double[n];

    for (int i = 1; i < n; i++) {
      double minUtil = this.reward.getStart();
      double minSucc = this.pSuccess.getStart();
      for (int j = 1; j < n; j++) {
        if (i == j || (j & i) != i) {
          continue;
        }
        minUtil = Math.max(minUtil, util[j]);
        minSucc = Math.max(minSucc, succ[j]);
      }

      double maxUtil = this.reward.getEnd();
      double maxSucc = this.pSuccess.getEnd();
      for (int j = 1; j < i; j++) {
        if ((i & j) != j) {
          continue;
        }
        maxUtil = Math.min(maxUtil, util[j]);
        maxSucc = Math.min(maxSucc, succ[j]);
      }

      util[i] = Range.Inclusive(minUtil, maxUtil, this.reward.getStep()).sample(this.rng);
      succ[i] = Range.Inclusive(minSucc, maxSucc, this.pSuccess.getStep()).sample(this.rng);
    }

    double maxUtil = 1.;
    double maxSucc = 1.;
    for (int i = 1; i < n; i++) {
      maxUtil = Math.min(maxUtil, util[i]);
      maxSucc = Math.min(maxSucc, succ[i]);
    }

    util[0] = Range.Inclusive(maxUtil, this.reward.getEnd(), this.reward.getStep()).sample(this.rng);
    succ[0] = Range.Inclusive(maxSucc, this.pSuccess.getEnd(), this.pSuccess.getStep()).sample(this.rng);

    for (int i = 0; i < n; i++) {
      this.addProposal(requesterID, this.generateProposal(i, util[i], succ[i]));
    }
  }

  @Override
  public List<Proposal> getProposals(String remoteID) {
    this.ensureProposals(remoteID);
    return super.getProposals(remoteID);
  }

  @Override
  public boolean hasProposals(String remoteID) {
    this.ensureProposals(remoteID);
    return super.hasProposals(remoteID);
  }
}
