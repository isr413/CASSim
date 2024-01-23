package com.seat.sim.client.negotiation;

public class PairwiseNegotiation {

    private Negotiator provider;
    private Negotiator receiver;

    public PairwiseNegotiation(Negotiator receiver, Negotiator provider) {
        this.receiver = receiver;
        this.provider = provider;
    }

    public Negotiator getProvider() {
        return provider;
    }

    public Negotiator getReceiver() {
        return receiver;
    }
}
