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

public class ActivateIntention extends Intention {
    private static final String ACTIVATIONS = "activations";

    private HashSet<String> activations;

    public ActivateIntention() {
        super(IntentionType.ACTIVATE);
        this.activations = new HashSet<>();
    }

    public ActivateIntention(String sensor) {
        this();
        this.addActivation(sensor);
    }

    public ActivateIntention(Collection<String> sensors) {
        this();
        this.addActivations(sensors);
    }

    public ActivateIntention(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.activations = new HashSet<>();
        if (json.hasKey(ActivateIntention.ACTIVATIONS)) {
            JSONArray jsonSensors = json.getJSONArray(ActivateIntention.ACTIVATIONS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.addActivation(jsonSensors.getString(i));
            }
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasActivations()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (String sensor : this.activations) {
                jsonSensors.put(sensor);
            }
            json.put(ActivateIntention.ACTIVATIONS, jsonSensors.toJSON());
        }
        return json;
    }

    public boolean addActivation(String sensor) {
        if (this.hasActivationOfSensor(sensor)) {
            Debugger.logger.warn(String.format("Sensor %s is already intended to be activated", sensor));
            return true;
        }
        this.activations.add(sensor);
        return true;
    }

    public boolean addActivations(Collection<String> sensors) {
        if (sensors == null) return false;
        boolean flag = true;
        for (String sensor : sensors) {
            flag = this.addActivation(sensor) && flag;
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

    public boolean hasActivationOfSensor(String sensor) {
        return this.activations.contains(sensor);
    }

    public boolean hasActivations() {
        return !this.activations.isEmpty();
    }

    public boolean removeActivation(String sensor) {
        if (!this.hasActivationOfSensor(sensor)) {
            Debugger.logger.warn(String.format("Sensor %s is not intended to be activated", sensor));
            return true;
        }
        this.activations.remove(sensor);
        return true;
    }

    public boolean removeActivations(Collection<String> sensors) {
        boolean flag = true;
        for (String sensor : sensors) {
            flag = this.removeActivation(sensor) && flag;
        }
        return flag;
    }

    public boolean equals(ActivateIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) && this.activations.equals(intent.activations);
    }

}
