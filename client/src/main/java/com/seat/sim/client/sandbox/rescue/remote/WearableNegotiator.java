package com.seat.sim.client.sandbox.rescue.remote;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.seat.sim.client.negotiation.Negotiator;
import com.seat.sim.client.negotiation.ParamsBuilder;

public class WearableNegotiator implements Negotiator {

  private Stream<Double> deviceReports;
  private ParamsBuilder paramsBuilder;
  private Set<String> privilegeTags;
  private String requestService;
  private String serviceTag;
  private double steps;
  private double stepSize;
  private double time;
  private Function<Contract, Double> utility;
  private String wearableID;

  public WearableNegotiator(String wearableID, Set<String> privilegeTags, String serviceTag,
      Stream<Double> reports, Function<Contract, Double> utility, ParamsBuilder params,
      double time, double stepSize) {
    this.wearableID = wearableID;
    this.privilegeTags = privilegeTags;
    this.serviceTag = serviceTag;
    this.deviceReports = reports;
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

  public boolean ask(Negotiator receiver, Negotiator provider, String service) {
    if (!service.equals(this.requestService)) {
      return provider.leave(this, provider);
    }
    return provider.propose(this, provider, service, this.privilegeTags, null);
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
    this.steps++;
    return false;
  }

  public boolean noMoreProposals(Negotiator sender, Negotiator receiver, String service) {
    return false;
  }

  public void notify(Set<Negotiator> provider) {
    this.steps++;
    for (Negotiator p : provider) {
      p.ask(this, p, this.serviceTag);
    }
  }

  public boolean propose(Negotiator receive, Negotiator provider, String service,
      Set<String> privileges, NegotiationParams negParams) {
    return false;
  }
}
