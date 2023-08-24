package com.seat.sim.client.sandbox.negotiation;

public class NegotiationParams {

  private double deadline;
  private double decommitmentPenaltyRate;
  private double earliestStartTime;
  private double earlyFinishRewardRate;
  private double negotiationDeadline;
  private double negotiationDuration;
  private double negotiationStartTime;
  private double regularReward;
  private double rewardFalloffRate;
  private double successProbability;

  public NegotiationParams(double eStart, double deadline, double reward, double eReward,
      double falloff, double penalty, double duration, double start, double finish,
      double successRate) {

    this.earliestStartTime = eStart;
    this.deadline = deadline;
    this.regularReward = reward;
    this.earlyFinishRewardRate = eReward;
    this.rewardFalloffRate = falloff;
    this.decommitmentPenaltyRate = penalty;
    this.negotiationDuration = duration;
    this.negotiationStartTime = start;
    this.negotiationDeadline = finish;
    this.successProbability = successRate;
  }

  public double getDeadline() {
    return this.deadline;
  }

  public double getDecommitmentPenaltyRate() {
    return this.decommitmentPenaltyRate;
  }

  public double getEarliestStartTime() {
    return this.earliestStartTime;
  }

  public double getEarlyFinishRewardRate() {
    return this.earlyFinishRewardRate;
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

  public double getRegularReward() {
    return this.regularReward;
  }

  public double getRewardFalloffRate() {
    return this.rewardFalloffRate;
  }

  public double getSuccessProbability() {
    return this.successProbability;
  }
}
