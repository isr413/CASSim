package com.seat.sim.common.remote.intent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.util.Debugger;

public class ActivateIntention extends Intention {
    private static final String ACTIVATIONS = "activations";

    private Set<String> activations;

    public ActivateIntention() {
        super(IntentionType.ACTIVATE);
        this.activations = new HashSet<>();
    }

    public ActivateIntention(String sensorID) {
        this();
        this.addActivation(sensorID);
    }

    public ActivateIntention(Collection<String> sensorIDs) {
        this();
        this.addActivations(sensorIDs);
    }

    public ActivateIntention(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.activations = new HashSet<>();
        if (json.hasKey(ActivateIntention.ACTIVATIONS)) {
            JSONArray jsonSensorIDs = json.getJSONArray(ActivateIntention.ACTIVATIONS);
            for (int i = 0; i < jsonSensorIDs.length(); i++) {
                this.addActivation(jsonSensorIDs.getString(i));
            }
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasActivations()) {
            JSONArrayBuilder jsonSensorIDs = JSONBuilder.Array();
            for (String sensorID : this.activations) {
                jsonSensorIDs.put(sensorID);
            }
            json.put(ActivateIntention.ACTIVATIONS, jsonSensorIDs.toJSON());
        }
        return json;
    }

    public boolean addActivation(String sensorID) {
        if (this.hasActivationOfSensor(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is already intended to be activated", sensorID));
            return true;
        }
        this.activations.add(sensorID);
        return true;
    }

    public boolean addActivations(Collection<String> sensorIDs) {
        if (sensorIDs == null) return false;
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            flag = this.addActivation(sensorID) && flag;
        }
        return flag;
    }

    public Collection<String> getActivations() {
        return this.activations;
    }

    @Override
    public String getLabel() {
        return "<ACTIVATE>";
    }

    public boolean hasActivationOfSensor(String sensorID) {
        return this.activations.contains(sensorID);
    }

    public boolean hasActivations() {
        return !this.activations.isEmpty();
    }

    public boolean removeActivation(String sensorID) {
        if (!this.hasActivationOfSensor(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is not intended to be activated", sensorID));
            return true;
        }
        this.activations.remove(sensorID);
        return true;
    }

    public boolean removeActivations(Collection<String> sensorIDs) {
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            flag = this.removeActivation(sensorID) && flag;
        }
        return flag;
    }

    public boolean equals(ActivateIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) && this.activations.equals(intent.activations);
    }

}
