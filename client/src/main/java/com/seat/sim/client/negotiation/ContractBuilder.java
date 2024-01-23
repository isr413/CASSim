package com.seat.sim.client.negotiation;

import java.util.HashSet;
import java.util.Set;

public class ContractBuilder {

    private NegotiationParams params;
    private Set<String> privileges;
    private Set<String> services;

    public ContractBuilder() {
        this.services = null;
        this.privileges = null;
        this.params = null;
    }

    public void addPrivilege(String privilege) {
        if (this.privileges == null) {
            this.privileges = new HashSet<>();
        }
        this.privileges.add(privilege);
    }

    public void addService(String service) {
        if (this.services == null) {
            this.services = new HashSet<>();
        }
        this.services.add(service);
    }

    public Contract build() {
        return new Contract(this.getServices(), this.getPrivileges(), this.getParams());
    }

    public NegotiationParams getParams() {
        return (this.hasParams()) ? this.params : new NegotiationParams();
    }

    public Set<String> getPrivileges() {
        return (this.hasPrivileges()) ? this.privileges : new HashSet<>();
    }

    public Set<String> getServices() {
        return (this.hasServices()) ? this.services : new HashSet<>();
    }

    public boolean hasParams() {
        return this.params != null;
    }

    public boolean hasPrivileges() {
        return this.privileges != null;
    }

    public boolean hasServices() {
        return this.services != null;
    }

    public void setParams(NegotiationParams params) {
        this.params = params;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }

    public void setServices(Set<String> services) {
        this.services = services;
    }

    public String toString() {
        return String.format("ContractBuilder(services=%s, privileges=%s, params=%s", this.getServices().toString(),
            this.getPrivileges().toString(), this.getParams().toString());
    }
}