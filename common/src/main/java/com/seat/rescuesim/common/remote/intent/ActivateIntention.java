package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.Debugger;

public class ActivateIntention extends Intention {
    private static final String ACTIVATIONS = "activations";

    private HashSet<String> activations;

    public ActivateIntention() {
        this(new HashSet<String>());
    }

    public ActivateIntention(ArrayList<String> sensors) {
        this(new HashSet<String>(sensors));
    }

    public ActivateIntention(HashSet<String> sensors) {
        super(IntentionType.ACTIVATE);
        this.activations = sensors;
    }

    public ActivateIntention(JSONOption option) {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.activations = new HashSet<>();
        if (json.hasKey(ActivateIntention.ACTIVATIONS)) {
            JSONArray jsonSensors = json.getJSONArray(ActivateIntention.ACTIVATIONS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.addActivation(jsonSensors.getString(i));
            }
        }
    }

    public boolean addActivation(String sensor) {
        if (this.hasActivationOfSensor(sensor)) {
            Debugger.logger.warn(String.format("Sensor %s is already intended to be activated", sensor));
            return true;
        }
        this.activations.add(sensor);
        return true;
    }

    public HashSet<String> getActivations() {
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

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasActivations()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (String sensor : this.activations) {
                jsonSensors.put(sensor);
            }
            json.put(ActivateIntention.ACTIVATIONS, jsonSensors.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(ActivateIntention intent) {
        return this.type == intent.type && this.activations.equals(intent.activations);
    }

}
