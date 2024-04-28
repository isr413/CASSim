package com.seat.sim.client.negotiation;

import java.util.List;
import java.util.Optional;

import com.seat.sim.common.util.Random;
import com.seat.sim.common.util.Range;

public class StochasticNegotiation extends DynamicNegotiation {

  private static final double E_START_TIME = 0;
  private static final double NEG_DEADLINE = 1;
  private static final double NEG_DURATION = 1;
  private static final double NEG_START_TIME = 0;

  private double deadline;
  private Optional<Range> eDeadline;
  private Optional<Range> eRewardBonus;
  private Optional<Range> eSuccess;
  private Range limit;
  private Range penalty;
  private Range pSuccess;
  private Range reward;
  private Random rng;

  public StochasticNegotiation(Optional<Range> eDeadline, double deadline, Range reward,
      Optional<Range> eRewardBonus, Range pSuccess, Optional<Range> eSuccess,
      Range penalty, Range limit, Random rng) {
    this.eDeadline = (eDeadline != null) ? eDeadline : Optional.empty();
    this.deadline = deadline;
    this.reward = reward;
    this.eRewardBonus = (eRewardBonus != null) ? eRewardBonus : Optional.empty();
    this.pSuccess = pSuccess;
    this.eSuccess = (eSuccess != null) ? eSuccess : Optional.empty();
    this.penalty = penalty;
    this.limit = limit;
    this.rng = rng;
  }

  private Proposal generateProposal() {
    double eDeadlineSample = (this.eDeadline.isPresent()) ? this.eDeadline.get().sample(this.rng) : 0.;
    double deadlineSample = Range.Inclusive(eDeadlineSample, this.deadline).sample(this.rng);
    return new Proposal(
        StochasticNegotiation.NEG_START_TIME,
        StochasticNegotiation.NEG_DEADLINE,
        StochasticNegotiation.NEG_DURATION,
        StochasticNegotiation.E_START_TIME,
        eDeadlineSample,
        deadlineSample,
        this.reward.uniform(this.rng),
        (this.eRewardBonus.isPresent()) ? this.eRewardBonus.get().uniform(this.rng) : 0.,
        (this.eSuccess.isPresent()) ? this.eSuccess.get().uniform(this.rng) : 0.,
        this.pSuccess.uniform(this.rng),
        this.penalty.uniform(this.rng)
      );
  }

  private void ensureProposals(String remoteID) {
    if (!this.proposals.containsKey(remoteID)) {
      for (int i = 0; i < (int) this.limit.sample(this.rng); i++) {
        this.addProposal(remoteID, this.generateProposal());
      }
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
