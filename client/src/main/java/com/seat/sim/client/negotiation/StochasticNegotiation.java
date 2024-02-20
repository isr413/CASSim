package com.seat.sim.client.negotiation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.seat.sim.common.util.Random;
import com.seat.sim.common.util.Range;

public class StochasticNegotiation implements NegotiationManager {

  private static final double E_START_TIME = 0;
  private static final double NEG_DEADLINE = 1;
  private static final double NEG_DURATION = 1;
  private static final double NEG_START_TIME = 0;

  private double deadline;
  private Range dRewardRate;
  private Range dSuccessRate;
  private Range eDeadline;
  private Range eRewardRate;
  private Range limit;
  private Range penalty;
  private Range pSuccess;
  private Range reward;
  private Random rng;

  private Map<String, List<Proposal>> negotiations;
  private Map<String, List<Proposal>> proposals;

  public StochasticNegotiation(Range eDeadline, double deadline, Range reward, Range eRewardRate, Range dRewardRate,
      Range pSuccess, Range dSuccessRate, Range penalty, Range limit, Random rng) {
    this.eDeadline = eDeadline;
    this.deadline = deadline;
    this.reward = reward;
    this.eRewardRate = eRewardRate;
    this.dRewardRate = dRewardRate;
    this.pSuccess = pSuccess;
    this.dSuccessRate = dSuccessRate;
    this.penalty = penalty;
    this.limit = limit;
    this.rng = rng;
    this.proposals = new HashMap<>();
    this.negotiations = new HashMap<>();
  }

  private Proposal generateProposal() {
    double eDeadlineSample = eDeadline.sample(this.rng);
    double deadlineSample = Range.Inclusive(eDeadlineSample, this.deadline).sample(this.rng);
    return new Proposal(
        StochasticNegotiation.NEG_START_TIME,
        StochasticNegotiation.NEG_DEADLINE,
        StochasticNegotiation.NEG_DURATION,
        StochasticNegotiation.E_START_TIME,
        eDeadlineSample,
        deadlineSample,
        this.reward.sample(this.rng),
        this.eRewardRate.sample(this.rng),
        this.dRewardRate.sample(this.rng),
        this.pSuccess.sample(this.rng),
        this.dSuccessRate.sample(this.rng),
        this.penalty.sample(this.rng)
      );
  }

  private String getKey(String senderID, String receiverID) {
    return String.format("%s::%s", senderID, receiverID);
  }

  public void addProposal(String remoteID, Proposal proposal) {
    if (!this.proposals.containsKey(remoteID)) {
      this.proposals.put(remoteID, new LinkedList<>());
    }
    this.proposals.get(remoteID).add(proposal);
  }

  public void close() {
    this.negotiations = new HashMap<>();
  }

  public Optional<Proposal> getNextProposal(String senderID, String receiverID) {
    if (!this.hasNextProposal(senderID, receiverID)) {
      return Optional.empty();
    }
    String key = this.getKey(senderID, receiverID);
    if (!this.negotiations.containsKey(key)) {
      this.negotiations.put(key, new LinkedList<>());
    }
    Proposal proposal = this.getProposals(senderID).get(this.negotiations.get(key).size());
    this.negotiations.get(key).add(proposal);
    return Optional.of(proposal);
  }

  public List<Proposal> getProposals(String remoteID) {
    if (!this.proposals.containsKey(remoteID)) {
      this.proposals.put(remoteID, new LinkedList<>());
      for (int i = 0; i < (int) this.limit.sample(this.rng); i++) {
        this.addProposal(remoteID, this.generateProposal());
      }
    }
    return this.proposals.get(remoteID);
  }

  public boolean hasNextProposal(String senderID, String receiverID) {
    if (!this.hasProposals(senderID)) {
      return false;
    }
    String key = this.getKey(senderID, receiverID);
    return !this.negotiations.containsKey(key) &&
        this.negotiations.get(key).size() < this.proposals.get(senderID).size();
  }

  public boolean hasProposals(String remoteID) {
    return !this.getProposals(remoteID).isEmpty();
  }

  public void reset() {
    this.proposals = new HashMap<>();
    this.negotiations = new HashMap<>();
  }
}
