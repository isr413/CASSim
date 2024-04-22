package com.seat.sim.client.negotiation;

public class Proposal {

  private double deadline;
  private double decommitmentPenalty;
  private double earliestDeadline;
  private double earliestStartTime;
  private double earlyFinishReward;
  private double earlySuccessProbability;
  private double negotiationDeadline;
  private double negotiationDuration;
  private double negotiationStartTime;
  private double regularReward;
  private double successProbability;

  public Proposal(double negStartTime, double negDeadline, double negDuration, double eStartTime, double eDeadline,
      double deadline, double reward, double eRewardBonus, double eSuccess, double pSuccess, double penalty) {
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
    return new Proposal(negotiationStartTime, negotiationDeadline, negotiationDuration, earliestStartTime,
        earliestDeadline, deadline, regularReward, earlyFinishReward, earlySuccessProbability,
        successProbability, decommitmentPenalty);
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

  public Proposal update() {
    return new Proposal(
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
