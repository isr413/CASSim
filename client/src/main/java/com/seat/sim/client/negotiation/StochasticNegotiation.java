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
  private Range eDeadline;
  private Range eRewardBonus;
  private Range eSuccess;
  private Range limit;
  private Range penalty;
  private Range pSuccess;
  private Range reward;
  private Random rng;

  private Map<String, List<Contract>> contracts;
  private Map<String, List<Proposal>> negotiations;
  private Map<String, List<Proposal>> proposals;

  public StochasticNegotiation(Range eDeadline, double deadline, Range reward, Range eRewardBonus,
      Range pSuccess, Range eSuccess, Range penalty, Range limit, Random rng) {
    this.eDeadline = eDeadline;
    this.deadline = deadline;
    this.reward = reward;
    this.eRewardBonus = eRewardBonus;
    this.pSuccess = pSuccess;
    this.eSuccess = eSuccess;
    this.penalty = penalty;
    this.limit = limit;
    this.rng = rng;
    this.proposals = new HashMap<>();
    this.negotiations = new HashMap<>();
    this.contracts = new HashMap<>();
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
        this.eRewardBonus.sample(this.rng),
        this.eSuccess.sample(this.rng),
        this.pSuccess.sample(this.rng),
        this.penalty.sample(this.rng)
      );
  }

  private void ensureProposals(String remoteID) {
    if (!this.proposals.containsKey(remoteID)) {
      for (int i = 0; i < (int) this.limit.sample(this.rng); i++) {
        this.addProposal(remoteID, this.generateProposal());
      }
    }
  }

  private String getKey(String senderID, String receiverID) {
    return String.format("%s::%s", senderID, receiverID);
  }

  public Contract acceptProposal(String senderID, String receiverID, Proposal proposal) {
    Contract contract = new Contract(senderID, receiverID, proposal);
    if (!this.contracts.containsKey(senderID)) {
      this.contracts.put(senderID, new LinkedList<>());
    }
    if (!this.contracts.containsKey(receiverID)) {
      this.contracts.put(receiverID, new LinkedList<>());
    }
    this.contracts.get(senderID).add(contract);
    this.contracts.get(receiverID).add(contract);
    return contract;
  }

  public void addProposal(String remoteID, Proposal proposal) {
    if (!this.proposals.containsKey(remoteID)) {
      this.proposals.put(remoteID, new LinkedList<>());
    }
    this.proposals.get(remoteID).add(proposal);
  }

  public Optional<Contract> getContract(String senderID, String receiverID) {
    if (!this.hasContracts(senderID)) {
      return Optional.empty();
    }
    for (Contract contract : this.getContracts(senderID)) {
      if (contract.matchSignatures(senderID, receiverID)) {
        return Optional.of(contract);
      }
    }
    return Optional.empty();
  }

  public List<Contract> getContracts(String remoteID) {
    return (this.contracts.containsKey(remoteID)) ? this.contracts.get(remoteID) : List.of();
  }

  public Optional<Proposal> getNextProposal(String senderID, String receiverID) {
    this.ensureProposals(senderID);
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
    return (this.proposals.containsKey(remoteID)) ? this.proposals.get(remoteID) : List.of();
  }

  public boolean hasContract(String senderID, String receiverID) {
    return this.getContract(senderID, receiverID).isPresent();
  }

  public boolean hasContracts(String remoteID) {
    return this.contracts.containsKey(remoteID) && !this.contracts.get(remoteID).isEmpty();
  }

  public boolean hasNextProposal(String senderID, String receiverID) {
    this.ensureProposals(senderID);
    if (!this.hasProposals(senderID)) {
      return false;
    }
    String key = this.getKey(senderID, receiverID);
    return !this.negotiations.containsKey(key) ||
        this.negotiations.get(key).size() < this.proposals.get(senderID).size();
  }

  public boolean hasProposals(String remoteID) {
    this.ensureProposals(remoteID);
    return !this.getProposals(remoteID).isEmpty();
  }

  public void reset() {
    this.proposals = new HashMap<>();
    this.negotiations = new HashMap<>();
    this.contracts = new HashMap<>();
  }

  public void terminateContract(String senderID, String receiverID) {
    Optional<Contract> contract = this.getContract(senderID, receiverID);
    if (!contract.isPresent()) {
      return;
    }
    this.contracts.get(senderID).remove(contract.get());
    this.contracts.get(receiverID).remove(contract.get());
  }
}
