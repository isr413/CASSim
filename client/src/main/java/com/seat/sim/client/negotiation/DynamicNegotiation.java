package com.seat.sim.client.negotiation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DynamicNegotiation implements NegotiationManager {

  protected Map<String, List<Contract>> contracts;
  protected Map<String, Contract> pairwise;
  protected Map<String, List<Proposal>> proposals;

  public DynamicNegotiation() {
    this.proposals = new HashMap<>();
    this.contracts = new HashMap<>();
    this.pairwise = new HashMap<>();
  }

  protected String getKey(String requesterID, String providerID) {
    return String.format("%s::%s", requesterID, providerID);
  }

  public Contract acceptProposal(String requesterID, String providerID, Proposal proposal) {
    Contract contract = new Contract(requesterID, providerID, proposal);
    if (!this.contracts.containsKey(requesterID)) {
      this.contracts.put(requesterID, new LinkedList<>());
    }
    if (!this.contracts.containsKey(providerID)) {
      this.contracts.put(providerID, new LinkedList<>());
    }
    this.contracts.get(requesterID).add(contract);
    this.contracts.get(providerID).add(contract);
    this.pairwise.put(this.getKey(requesterID, providerID), contract);
    return contract;
  }

  public void addProposal(String remoteID, Proposal proposal) {
    if (!this.proposals.containsKey(remoteID)) {
      this.proposals.put(remoteID, new ArrayList<>());
    }
    this.proposals.get(remoteID).add(proposal);
  }

  public Optional<Contract> getContract(String requesterID, String providerID) {
    if (!this.hasContract(requesterID, providerID)) {
      return Optional.empty();
    }
    return Optional.of(this.pairwise.get(this.getKey(requesterID, providerID)));
  }

  public List<Contract> getContracts(String remoteID) {
    return (this.contracts.containsKey(remoteID)) ? this.contracts.get(remoteID) : List.of();
  }

  public List<Proposal> getProposals(String remoteID) {
    return (this.proposals.containsKey(remoteID)) ? this.proposals.get(remoteID) : List.of();
  }

  public boolean hasContract(String requesterID, String providerID) {
    return this.pairwise.containsKey(this.getKey(requesterID, providerID));
  }

  public boolean hasContracts(String remoteID) {
    return this.contracts.containsKey(remoteID) && !this.contracts.get(remoteID).isEmpty();
  }

  public boolean hasProposals(String remoteID) {
    return this.proposals.containsKey(remoteID) && !this.proposals.get(remoteID).isEmpty();
  }

  public void reset() {
    this.proposals = new HashMap<>();
    this.contracts = new HashMap<>();
    this.pairwise = new HashMap<>();
  }

  public void terminateContract(String requesterID, String providerID) {
    if (!this.hasContract(requesterID, providerID)) {
      return;
    }
    Optional<Contract> contract = this.getContract(requesterID, providerID);
    this.contracts.get(requesterID).remove(contract.get());
    this.contracts.get(providerID).remove(contract.get());
    this.pairwise.remove(this.getKey(requesterID, providerID));
  }

}
