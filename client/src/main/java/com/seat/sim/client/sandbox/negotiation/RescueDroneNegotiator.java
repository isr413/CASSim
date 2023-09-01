package com.seat.sim.client.sandbox.negotiation;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class RescueDroneNegotiator implements Negotiator {

  private String droneID;
  private ParamsBuilder paramsBuilder;
  private Set<String> privilegeTags;
  private String serviceTag;
  private double steps;
  private double stepSize;
  private double time;
  private Function<Contract, Double> utility;

  public RescueDroneNegotiator(String droneID, Set<String> privilegeTags, String serviceTag,
      Function<Contract, Double> utility, ParamsBuilder params, double time, double stepSize) {
    this.droneID = droneID;
    this.privilegeTags = privilegeTags;
    this.serviceTag = serviceTag;
    this.utility = utility;
    this.paramsBuilder = params;
    this.time = time;
    this.stepSize = stepSize;
    this.steps = 0;
  }

  public boolean accept(Negotiator sender, Negotiator receiver, String service,
      Set<String> privileges, NegotiationParams negParams) {
    return false;
  }

  public boolean ask(Negotiator receiver, Negotiator provider, String serviceTag) {
    this.steps++;
    if (!serviceTag.equals(this.serviceTag)) {
      return receiver.leave(this, receiver);
    }
    this.
    return receiver.propose(receiver, this, serviceTag, this.privilegeTags, params);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || this.getClass() != o.getClass()) {
      return this == o;
    }
    return this.equals((Negotiator) o);
  }

  public boolean failure(Negotiator sender, Negotiator receiver) {
    return false;
  }

  public String getID() {
    return this.wearableID;
  }

  @Override
  public int hashCode() {
    return this.wearableID.hashCode();
  }

  public boolean leave(Negotiator sender, Negotiator receiver) {
    return false;
  }

  public boolean noMoreProposals(Negotiator sender, Negotiator receiver, String service) {
    return false;
  }

  public void notify(Set<Negotiator> providers) {
    for (Negotiator provider : providers) {
      provider.ask(this, provider, this.requestService);
    }
  }

  public boolean propose(Negotiator receive, Negotiator provider, String service,
      Set<String> privileges, NegotiationParams negParams) {
    return false;
  }
}
