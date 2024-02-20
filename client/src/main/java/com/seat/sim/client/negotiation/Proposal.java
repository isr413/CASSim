package com.seat.sim.client.negotiation;

public class Proposal {

  private double deadline;
  private double decommitmentPenalty;
  private double earliestDeadline;
  private double earliestStartTime;
  private double earlyFinishRewardRate;
  private double negotiationDeadline;
  private double negotiationDuration;
  private double negotiationStartTime;
  private double regularReward;
  private double rewardFalloffRate;
  private double successDeclineRate;
  private double successProbability;

  public Proposal(double negStartTime, double negDeadline, double negDuration, double eStartTime, double eDeadline,
      double deadline, double reward, double eRewardRate, double dRewardRate,
      double pSuccess, double dSuccessRate, double penalty) {
    this.negotiationStartTime = negStartTime;
    this.negotiationDeadline = negDeadline;
    this.negotiationDuration = negDuration;
    this.earliestStartTime = eStartTime;
    this.earliestDeadline = eDeadline;
    this.deadline = deadline;
    this.regularReward = reward;
    this.earlyFinishRewardRate = eRewardRate;
    this.rewardFalloffRate = dRewardRate;
    this.successProbability = pSuccess;
    this.successDeclineRate = dSuccessRate;
    this.decommitmentPenalty = penalty;
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

  public double getEarlyRewardBonusRate() {
    return this.earlyFinishRewardRate;
  }

  public double getEarliestStartTime() {
    return this.earliestStartTime;
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

  public double getRewardDeclineRate() {
    return this.rewardFalloffRate;
  }

  public double getSuccessDeclineRate() {
    return this.successDeclineRate;
  }

  public double getSuccessLikelihood() {
    return this.successProbability;
  }
}
