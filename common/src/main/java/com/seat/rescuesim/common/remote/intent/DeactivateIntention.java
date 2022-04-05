package com.seat.rescuesim.common.remote.intent;


import java.util.Collection;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.Debugger;

public class DeactivateIntention extends Intention {
    private static final String DEACTIVATIONS = "deactivations";

    private HashSet<String> deactivations;

    public DeactivateIntention() {
        super(IntentionType.DEACTIVATE);
        this.deactivations = new HashSet<>();
    }

    public DeactivateIntention(String sensor) {
        super(IntentionType.DEACTIVATE);
        this.deactivations = new HashSet<>();
        this.addDeactivation(sensor);
    }

    public DeactivateIntention(Collection<String> sensors) {
        super(IntentionType.DEACTIVATE);
        this.deactivations = new HashSet<>(sensors);
    }

    public DeactivateIntention(JSONOption option) {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.deactivations = new HashSet<>();
        if (json.hasKey(DeactivateIntention.DEACTIVATIONS)) {
            JSONArray jsonSensors = json.getJSONArray(DeactivateIntention.DEACTIVATIONS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.addDeactivation(jsonSensors.getString(i));
            }
        }
    }

    public boolean addDeactivation(String sensor) {
        if (this.hasDeactivationOfSensor(sensor)) {
            Debugger.logger.warn(String.format("Sensor %s is already intended to be deactivated", sensor));
            return true;
        }
        this.deactivations.add(sensor);
        return true;
    }

    public boolean addDeactivations(Collection<String> sensors) {
        boolean flag = true;
        for (String sensor : sensors) {
            flag = this.addDeactivation(sensor) && flag;
        }
        return flag;
    }

    public HashSet<String> getDeactivations() {
        return this.deactivations;
    }

    @Override
    public String getLabel() {
        return "<DEACTIVATE>";
    }

    public boolean hasDeactivationOfSensor(String sensor) {
        return this.deactivations.contains(sensor);
    }

    public boolean hasDeactivations() {
        return !this.deactivations.isEmpty();
    }

    public boolean removeDeactivation(String sensor) {
        if (!this.hasDeactivationOfSensor(sensor)) {
            Debugger.logger.warn(String.format("Sensor %s is not intended to be deactivated", sensor));
            return true;
        }
        this.deactivations.remove(sensor);
        return true;
    }

    public boolean removeDeactivations(Collection<String> sensors) {
        boolean flag = true;
        for (String sensor : sensors) {
            flag = this.removeDeactivation(sensor) && flag;
        }
        return flag;
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasDeactivations()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (String sensor : this.deactivations) {
                jsonSensors.put(sensor);
            }
            json.put(DeactivateIntention.DEACTIVATIONS, jsonSensors.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(DeactivateIntention intent) {
        return super.equals(intent) && this.deactivations.equals(intent.deactivations);
    }

}
