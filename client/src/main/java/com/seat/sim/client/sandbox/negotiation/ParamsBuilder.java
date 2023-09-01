package com.seat.sim.client.sandbox.negotiation;

public class ParamsBuilder {

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
        this.successProbability
      );
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

  public void setSuccessProbability(double probability) {
    this.successProbability = probability;
  }
}
