package com.seat.sim.client.negotiation;

import java.util.List;
import java.util.Optional;

import com.seat.sim.common.scenario.Snapshot;

public interface NegotiationManager {

  Contract acceptProposal(String requesterID, String providerID, Proposal proposal);

  void addProposal(String remoteID, Proposal proposal);

  default void close() {}

  default void init() {}

  Optional<Contract> getContract(String requesterID, String providerID);

  List<Contract> getContracts(String remoteID);

  List<Proposal> getProposals(String remoteID);

  boolean hasContract(String requesterID, String providerID);

  boolean hasContracts(String remoteID);

  boolean hasProposals(String remoteID);

  default void reset() {}

  void terminateContract(String requesterID, String providerID);

  default void update(Snapshot snap) {}
}
