package com.seat.sim.client.negotiation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NegotiationManager {

    private Map<String, Set<String>> labels;
    private Map<String, Negotiator> negotiators;

    public NegotiationManager() {
        this.negotiators = new HashMap<>();
        this.labels = new HashMap<>();
    }

    public void addLabels(String id, Set<String> labels) {
        if (labels == null || labels.isEmpty()) {
            return;
        }
        if (this.labels.containsKey(id)) {
            this.labels.get(id).addAll(labels);
        } else {
            this.labels.put(id, labels);
        }
    }

    public Negotiator assignNegotiator(String id, Negotiator negotiator) {
        return this.assignNegotiator(id, negotiator, new HashSet<>());
    }

    public Negotiator assignNegotiator(String id, Negotiator negotiator, Set<String> labels) {
        this.negotiators.put(id, negotiator);
        if (this.labels == null) {
            return negotiator;
        }
        if (!labels.isEmpty()) {
            this.labels.put(id, labels);
        } else {
            this.labels.remove(id);
        }
        return negotiator;
    }

    public Set<String> getLabels(String id) {
        return (this.hasLabels(id)) ? this.labels.get(id) : new HashSet<>();
    }

    public Set<Negotiator> getMatches(String label) {
        Set<Negotiator> matches = new HashSet<>();
        for (String id : this.labels.keySet()) {
            if (this.labels.get(id).contains(label)) {
                matches.add(this.negotiators.get(id));
            }
        }
        return matches;
    }

    public Negotiator getNegotiator(String id) {
        return this.negotiators.get(id);
    }

    public boolean hasLabels(String id) {
        return this.labels.containsKey(id);
    }

    public boolean hasMatch(String id, Set<String> label) {
        for (String l : label) {
            if (this.hasMatch(id, l)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMatch(String id, String label) {
        return this.hasLabels(id) && this.labels.get(id).contains(label);
    }

    public boolean hasNegotiator(String id) {
        return this.negotiators.containsKey(id);
    }

    public PairwiseNegotiation initiate(String receiverID, String providerID) {
        if (!this.hasNegotiator(receiverID) || !this.hasNegotiator(providerID)) {
            return null;
        }
        Negotiator receiver = this.negotiators.get(receiverID);
        Negotiator provider = this.negotiators.get(providerID);
        if (receiver == null || provider == null) {
            return null;
        }
        receiver.setPartner(provider);
        provider.setPartner(receiver);
        return new PairwiseNegotiation(receiver, provider);
    }

    public void terminate(PairwiseNegotiation negotiation) {
        if (this.hasNegotiator(negotiation.getReceiver().getID())) {
            this.getNegotiator(negotiation.getReceiver().getID()).setPartner(null);
        }
        if (this.hasNegotiator(negotiation.getProvider().getID())) {
            this.getNegotiator(negotiation.getProvider().getID()).setPartner(null);
        }
    }
}
