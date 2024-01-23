package com.seat.sim.client.negotiation;

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
  private double successDeclineRate;
  private double successProbability;

  public NegotiationParams() {
    this(0, Double.POSITIVE_INFINITY, 1, 1, 0, 0, Double.POSITIVE_INFINITY, 0, Double.POSITIVE_INFINITY, 1, 0);
  }

  public NegotiationParams(double eStart, double deadline, double reward, double eReward,
      double rFalloffRate, double penalty, double duration, double start, double finish,
      double successRate, double sDeclineRate) {

    this.earliestStartTime = eStart;
    this.deadline = deadline;
    this.regularReward = reward;
    this.earlyFinishRewardRate = eReward;
    this.rewardFalloffRate = rFalloffRate;
    this.decommitmentPenaltyRate = penalty;
    this.negotiationDuration = duration;
    this.negotiationStartTime = start;
    this.negotiationDeadline = finish;
    this.successProbability = successRate;
    this.successDeclineRate = sDeclineRate;
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

  public double getSuccessDeclineRate() {
    return this.successDeclineRate;
  }

  public double getSuccessProbability() {
    return this.successProbability;
  }

  public String toString() {
    return String.format("Params(earliestStartTime=%.2f, deadline=%.2f, regularReward=%.2f, earlyFinishRewardRate=%.2f, rewardFalloffRate=%.2f, decommitmentPenaltyRate=%.2f, negotiationDuration=%.2f, negotiationStartTime=%.2f, negotiationDeadline=%.2f, successProbability=%.2f, successDeclineRate=%.2f",
        this.getEarliestStartTime(),
        this.getDeadline(),
        this.getRegularReward(),
        this.getEarlyFinishRewardRate(),
        this.getRewardFalloffRate(),
        this.getDecommitmentPenaltyRate(),
        this.getNegotiationDuration(),
        this.getNegotiationStartTime(),
        this.getNegotiationDeadline(),
        this.getSuccessProbability(),
        this.getSuccessDeclineRate()
      );
  }
}
