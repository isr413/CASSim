package com.seat.sim.client.negotiation;

public class ParamsBuilder {

  private double deadline = Double.POSITIVE_INFINITY;
  private double decommitmentPenaltyRate = 0.;
  private double earliestStartTime = 0.;
  private double earlyFinishRewardRate = 1.;
  private double negotiationDeadline = Double.POSITIVE_INFINITY;
  private double negotiationDuration = Double.POSITIVE_INFINITY;
  private double negotiationStartTime = 0.;
  private double regularReward = 1.;
  private double rewardFalloffRate = 0.;
  private double successDeclineRate = 0.;
  private double successProbability = 1.;

  public NegotiationParams build() {
    return new NegotiationParams(
        this.earliestStartTime,
        this.deadline,
        this.regularReward,
        this.earlyFinishRewardRate,
        this.rewardFalloffRate,
        this.decommitmentPenaltyRate,
        this.negotiationDuration,
        this.negotiationStartTime,
        this.negotiationDeadline,
        this.successProbability,
        this.successDeclineRate
      );
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

  public void setDeadline(double deadline) {
    this.deadline = deadline;
  }

  public void setDecommitmentPenaltyRate(double penaltyRate) {
    this.decommitmentPenaltyRate = penaltyRate;
  }

  public void setEarliestStartTime(double startTime) {
    this.earliestStartTime = startTime;
  }

  public void setEarlyFinishRewardRate(double rewardRate) {
    this.earlyFinishRewardRate = rewardRate;
  }

  public void setNegotiationDeadline(double deadline) {
    this.negotiationDeadline = deadline;
  }

  public void setNegotiationDuration(double duration) {
    this.negotiationDuration = duration;
  }

  public void setNegotiationStartTime(double startTime) {
    this.negotiationStartTime = startTime;
  }

  public void setRegularReward(double reward) {
    this.regularReward = reward;
  }

  public void setRewardFalloffRate(double falloffRate) {
    this.rewardFalloffRate = falloffRate;
  }

  public void setSuccessDeclineRate(double declineRate) {
    this.successDeclineRate = declineRate;
  }

  public void setSuccessProbability(double probability) {
    this.successProbability = probability;
  }

  public String toString() {
    return String.format("ParamsBuilder(earliestStartTime=%.2f, deadline=%.2f, regularReward=%.2f, earlyFinishRewardRate=%.2f, rewardFalloffRate=%.2f, decommitmentPenaltyRate=%.2f, negotiationDuration=%.2f, negotiationStartTime=%.2f, negotiationDeadline=%.2f, successProbability=%.2f, successDeclineRate=%.2f",
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
