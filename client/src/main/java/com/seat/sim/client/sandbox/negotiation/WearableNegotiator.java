package com.seat.sim.client.sandbox.negotiation;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class WearableNegotiator implements NegotiationProtocol {

  private Stream<Double> deviceReports;
  private Set<String> privileges;
  private String requestService;
  private Function<Contract, Double> utility;

  public WearableNegotiator(Set<String> privileges, String service, Stream<Double> reports,
      Function<Contract, Double> utility) {
    this.privileges = privileges;
    this.requestService = service;
    this.deviceReports = reports;
    this.utility = utility;
  }

  public boolean accept(String sender, String receiver, String service, Set<String> privileges,
      NegotiationParams negParams) {
    return false;
  }

  public boolean ask(String receiver, String provider, String service) {
    return false;
  }

  public boolean failure(String sender, String receiver) {
    return false;
  }

  public boolean leave(String sender, String receiver) {
    return false;
  }

  public boolean noMoreProposals(String sender, String receiver, String service) {
    return false;
  }

  public void notify(Set<NegotiationProtocol> providers) {

  }

  public boolean propose(String receive, String provider, String service, Set<String> privileges,
      NegotiationParams negParams) {
    return false;
  }
}
