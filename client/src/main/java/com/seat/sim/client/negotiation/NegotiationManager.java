package com.seat.sim.client.negotiation;

import java.util.List;
import java.util.Optional;

import com.seat.sim.common.scenario.Snapshot;

public interface NegotiationManager {

  Contract acceptProposal(String senderID, String receiverID, Proposal proposal);

  void addProposal(String remoteID, Proposal proposal);

  default void close() {}

  default void init() {}

  Optional<Contract> getContract(String senderID, String receiverID);

  List<Contract> getContracts(String remoteID);

  Optional<Proposal> getNextProposal(String senderID, String receiverID);

  List<Proposal> getProposals(String remoteID);

  boolean hasContract(String senderID, String receiverID);

  boolean hasContracts(String remoteID);

  boolean hasNextProposal(String senderID, String receiverID);

  boolean hasProposals(String remoteID);

  default void reset() {}

  void terminateContract(String senderID, String receiverID);

  default void update(Snapshot snap) {}
}
