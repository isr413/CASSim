package com.seat.sim.client.negotiation;

import java.util.Optional;

public class Proposal implements Comparable<Proposal> {

  private double deadline;
  private double decommitmentPenalty;
  private double earliestDeadline;
  private double earliestStartTime;
  private double earlyFinishReward;
  private double earlySuccessProbability;
  private Optional<Integer> label;
  private double negotiationDeadline;
  private double negotiationDuration;
  private double negotiationStartTime;
  private double regularReward;
  private double successProbability;

  public Proposal(double negStartTime, double negDeadline, double negDuration, double eStartTime, double eDeadline,
      double deadline, double reward, double eRewardBonus, double eSuccess, double pSuccess, double penalty) {
    this(Optional.empty(), negStartTime, negDeadline, negDuration, eStartTime, eDeadline, deadline, reward,
        eRewardBonus, eSuccess, pSuccess, penalty);
  }

  public Proposal(Optional<Integer> label, double negStartTime, double negDeadline, double negDuration,
      double eStartTime, double eDeadline, double deadline, double reward, double eRewardBonus, double eSuccess,
      double pSuccess, double penalty) {
    this.label = (label != null) ? label : Optional.empty();
    this.negotiationStartTime = negStartTime;
    this.negotiationDeadline = negDeadline;
    this.negotiationDuration = negDuration;
    this.earliestStartTime = eStartTime;
    this.earliestDeadline = eDeadline;
    this.deadline = deadline;
    this.regularReward = reward;
    this.earlyFinishReward = eRewardBonus;
    this.earlySuccessProbability = eSuccess;
    this.successProbability = pSuccess;
    this.decommitmentPenalty = penalty;
  }

  public Proposal clone() {
    return this.clone(this.earlySuccessProbability, this.successProbability);
  }

  public Proposal clone(double pSucc) {
    return this.clone(this.earlySuccessProbability, pSucc);
  }

  public Proposal clone(double eSucc, double pSucc) {
    return new Proposal(label, negotiationStartTime, negotiationDeadline, negotiationDuration, earliestStartTime,
        earliestDeadline, deadline, regularReward, earlyFinishReward, eSucc, pSucc, decommitmentPenalty);
  }

  public int compareTo(Proposal q) {
    return Double.compare(this.getCumulativeReward(), q.getCumulativeReward());
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Proposal) {
      return this.equals((Proposal) o);
    }
    return false;
  }

  public boolean equals(Proposal p) {
    if (this.hasLabel() && p.hasLabel()) {
      return this.getLabel() == p.getLabel();
    }
    return this == p;
  }

  public double getCumulativeReward() {
    return (this.earlyFinishReward + this.regularReward) * this.earlySuccessProbability +
        this.regularReward * (1. - this.earlySuccessProbability) * this.successProbability;
  }

  public double getDeadline() {
    return this.deadline;
  }

  public double getDecommitmentPenalty() {
    return this.decommitmentPenalty;
  }

  public double getEarliestDeadline() {
    return this.earliestDeadline;
  }

  public double getEarliestStartTime() {
    return this.earliestStartTime;
  }

  public double getEarlyRewardBonus() {
    return this.earlyFinishReward;
  }

  public double getEarlySuccessLikelihood() {
    return this.earlySuccessProbability;
  }

  public int getLabel() {
    return this.label.get();
  }

  public double getNegotiationDeadline() {
    return this.negotiationDeadline;
  }

  public double getNegotiationDuration() {
    return this.negotiationDuration;
  }

  public double getNegotiationStartTime() {
    return this.negotiationStartTime;
  }

  public double getReward() {
    return this.regularReward;
  }

  public double getSuccessLikelihood() {
    return this.successProbability;
  }

  public boolean hasLabel() {
    return this.label.isPresent();
  }

  public Proposal update() {
    return new Proposal(
        this.label,
        Math.max(this.negotiationStartTime - 1, 0),
        Math.max(this.negotiationDeadline - 1, 0),
        Math.max(this.negotiationDuration - 1, 0),
        Math.max(this.earliestStartTime - 1, 0),
        Math.max(this.earliestDeadline - 1, 0),
        Math.max(this.deadline - 1, 0),
        this.regularReward,
        this.earlyFinishReward,
        this.earlySuccessProbability,
        this.successProbability,
        this.decommitmentPenalty
      );
  }
}
