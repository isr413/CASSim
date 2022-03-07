package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

public class ActivateIntention extends Intention {
    private static final String ACTIVATIONS = "activations";

    private HashSet<String> activations;

    public ActivateIntention(JSONObject json) {
        super(json);
    }

    public ActivateIntention(JSONOption option) {
        super(option);
    }

    public ActivateIntention(String encoding) {
        super(encoding);
    }

    public ActivateIntention() {
        this(new HashSet<String>());
    }

    public ActivateIntention(String[] sensors) {
        this(new ArrayList<String>(Arrays.asList(sensors)));
    }

    public ActivateIntention(ArrayList<String> sensors) {
        this(new HashSet<String>(sensors));
    }

    public ActivateIntention(HashSet<String> sensors) {
        super(IntentionType.ACTIVATE);
        this.activations = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.activations = new HashSet<>();
        JSONArray jsonSensors = json.getJSONArray(ActivateIntention.ACTIVATIONS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.addActivation(jsonSensors.getString(i));
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

    public boolean hasActivations() {
        return !this.activations.isEmpty();
    }

    public boolean hasActivationOfSensor(String sensor) {
        return this.activations.contains(sensor);
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
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (String sensor : this.activations) {
            jsonSensors.put(sensor);
        }
        json.put(ActivateIntention.ACTIVATIONS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(ActivateIntention intent) {
        return this.type == intent.type && this.activations.equals(intent.activations);
    }

}
