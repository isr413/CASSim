package com.seat.sim.client.negotiation;

import java.util.List;
import java.util.Optional;

import com.seat.sim.common.scenario.Snapshot;

public interface NegotiationManager {

    void addProposal(String remoteID, Proposal proposal);

    default void close() {}

    default void init() {}

    Optional<Proposal> getNextProposal(String senderID, String receiverID);

    List<Proposal> getProposals(String remoteID);

    boolean hasNextProposal(String senderID, String receiverID);

    boolean hasProposals(String remoteID);

    default void reset() {}

    default void update(Snapshot snap) {}
}
