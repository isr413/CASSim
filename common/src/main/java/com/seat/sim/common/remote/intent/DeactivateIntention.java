package com.seat.sim.common.remote.intent;


import java.util.Collection;
import java.util.HashSet;

import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
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

    public DeactivateIntention(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.deactivations = new HashSet<>();
        if (json.hasKey(DeactivateIntention.DEACTIVATIONS)) {
            JSONArray jsonSensorIDs = json.getJSONArray(DeactivateIntention.DEACTIVATIONS);
            for (int i = 0; i < jsonSensorIDs.length(); i++) {
                this.addDeactivation(jsonSensorIDs.getString(i));
            }
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasDeactivations()) {
            JSONArrayBuilder jsonSensorIDs = JSONBuilder.Array();
            for (String sensorID : this.deactivations) {
                jsonSensorIDs.put(sensorID);
            }
            json.put(DeactivateIntention.DEACTIVATIONS, jsonSensorIDs.toJSON());
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

    public Collection<String> getDeactivations() {
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
