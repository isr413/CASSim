package com.seat.sim.client.sandbox.negotiation;

import java.util.Set;

public interface NegotiationProtocol {

  public boolean accept(String sender, String receiver, String service, Set<String> privileges,
      NegotiationParams negParams);

  public boolean ask(String receiver, String provider, String service);

  public boolean failure(String sender, String receiver);

  public boolean leave(String sender, String receiver);

  public boolean noMoreProposals(String sender, String receiver, String service);

  public boolean propose(String receive, String provider, String service, Set<String> privileges,
      NegotiationParams negParams);
}
