package com.seat.sim.client.negotiation;

import java.util.Set;

public class Contract {

    private NegotiationParams params;
    private Set<String> privileges;
    private Set<String> services;

    public Contract(Set<String> services, Set<String> privileges, NegotiationParams params) {
        this.services = services;
        this.privileges = privileges;
        this.params = params;
    }

    public NegotiationParams getParams() {
        return this.params;
    }

    public Set<String> getPrivileges() {
        return this.privileges;
    }

    public Set<String> getServices() {
        return this.services;
    }

    public String toString() {
        return String.format("Contract(services=%s, privileges=%s, params=%s", this.services.toString(),
            this.privileges.toString(), this.params.toString());
    }
}