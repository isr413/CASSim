package com.seat.sim.client.negotiation;

import java.util.Set;

public interface Negotiator {

  public boolean accept(Negotiator sender, Negotiator receiver, String service, Set<String> privileges,
      NegotiationParams negParams);

  public boolean ask(Negotiator receiver, Negotiator provider, String serviceTag);

  default public boolean equals(Negotiator np) {
    return this.getID().equals(np.getID());
  }

  public boolean failure(Negotiator sender, Negotiator receiver);

  public String getID();

  public boolean leave(Negotiator sender, Negotiator receiver);

  public boolean noMoreProposals(Negotiator sender, Negotiator receiver, String service);

  public void notify(Set<Negotiator> providers);

  public boolean propose(Negotiator receiver, Negotiator provider, String service,
      Set<String> privileges, NegotiationParams negParams);

  public void setPartner(Negotiator partner);
}
