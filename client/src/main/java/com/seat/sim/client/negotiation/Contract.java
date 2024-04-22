package com.seat.sim.client.negotiation;

import com.seat.sim.common.math.Vector;

public class Contract implements Comparable<Contract> {

  private boolean originalContract;
  private Proposal proposal;
  private String receiverID;
  private String senderID;

  public Contract(String senderID, String receiverID, Proposal proposal) {
    this(senderID, receiverID, proposal, true);
  }

  private Contract(String senderID, String receiverID, Proposal proposal, boolean originalContract) {
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.proposal = proposal;
    this.originalContract = originalContract;
  }

  public Contract clone() {
    return new Contract(senderID, receiverID, proposal.clone(), false);
  }

  public int compareTo(Contract c) {
    if (Vector.near(this.proposal.getEarliestDeadline(), 0.) && Vector.near(c.proposal.getEarliestDeadline(), 0.)) {
      return Double.compare(this.proposal.getDeadline(), c.proposal.getDeadline());
    } else if (Vector.near(this.proposal.getEarliestDeadline(), 0.)) {
      return Double.compare(this.proposal.getDeadline(), c.proposal.getEarliestDeadline());
    } else if (Vector.near(c.proposal.getEarliestDeadline(), 0.)) {
      return Double.compare(this.proposal.getEarliestDeadline(), c.proposal.getDeadline());
    } else {
      return Double.compare(this.proposal.getEarliestDeadline(), c.proposal.getEarliestDeadline());
    }
  }

  public Proposal getProposal() {
    return this.proposal;
  }

  public String getReceiverID() {
    return this.receiverID;
  }

  public String getSenderID() {
    return this.senderID;
  }

  public boolean isOriginal() {
    return this.originalContract;
  }

  public boolean matchSignatures(String senderID, String receiverID) {
    return (this.senderID.equals(senderID) && this.receiverID.equals(receiverID)) ||
        (this.senderID.equals(receiverID) && this.receiverID.equals(senderID));
  }

  public Contract update() {
    return new Contract(this.senderID, this.receiverID, this.proposal.update());
  }
}
