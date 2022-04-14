package com.seat.sim.common.remote.intent;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.util.Debugger;

public class DeactivateIntention extends Intention {
    private static final String DEACTIVATIONS = "deactivations";

    private HashSet<String> deactivations;

    public DeactivateIntention() {
        super(IntentionType.DEACTIVATE);
        this.deactivations = new HashSet<>();
    }

    public DeactivateIntention(String sensorID) {
        this();
        this.addDeactivation(sensorID);
    }

    public DeactivateIntention(Collection<String> sensorIDs) {
        this();
        this.addDeactivations(sensorIDs);
    }

    public DeactivateIntention(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.deactivations = new HashSet<>();
        if (json.hasKey(DeactivateIntention.DEACTIVATIONS)) {
            this.deactivations = (json.hasKey(DeactivateIntention.DEACTIVATIONS)) ?
                new HashSet<>(json.getJSONArray(DeactivateIntention.DEACTIVATIONS).toList(String.class)) :
                new HashSet<>();
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasDeactivations()) {
            json.put(DeactivateIntention.DEACTIVATIONS, JSONBuilder.Array(this.deactivations));
        }
        return json;
    }

    public boolean addDeactivation(String sensorID) {
        if (this.hasDeactivationOfSensor(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is already intended to be deactivated", sensorID));
            return true;
        }
        this.deactivations.add(sensorID);
        return true;
    }

    public boolean addDeactivations(Collection<String> sensorIDs) {
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            flag = this.addDeactivation(sensorID) && flag;
        }
        return flag;
    }

    public Set<String> getDeactivations() {
        return this.deactivations;
    }

    @Override
    public String getLabel() {
        return "<DEACTIVATE>";
    }

    public boolean hasDeactivationOfSensor(String sensorID) {
        return this.deactivations.contains(sensorID);
    }

    public boolean hasDeactivations() {
        return !this.deactivations.isEmpty();
    }

    public boolean removeDeactivation(String sensorID) {
        if (!this.hasDeactivationOfSensor(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is not intended to be deactivated", sensorID));
            return true;
        }
        this.deactivations.remove(sensorID);
        return true;
    }

    public boolean removeDeactivations(Collection<String> sensorIDs) {
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            flag = this.removeDeactivation(sensorID) && flag;
        }
        return flag;
    }

    public boolean equals(DeactivateIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) && this.deactivations.equals(intent.deactivations);
    }

}
