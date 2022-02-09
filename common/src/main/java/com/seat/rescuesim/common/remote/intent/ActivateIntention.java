package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.seat.rescuesim.common.SensorType;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.Intention;
import com.seat.rescuesim.common.remote.IntentionType;
import com.seat.rescuesim.common.util.Debugger;

public class ActivateIntention extends Intention {
    private static final String ACTIVATIONS = "activations";

    private HashSet<SensorType> activations;

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
        this(new HashSet<SensorType>());
    }

    public ActivateIntention(SensorType[] sensors) {
        this(new ArrayList<SensorType>(Arrays.asList(sensors)));
    }

    public ActivateIntention(ArrayList<SensorType> sensors) {
        this(new HashSet<SensorType>());
        for (SensorType type : sensors) {
            this.addActivation(type);
        }
    }

    public ActivateIntention(HashSet<SensorType> sensors) {
        super(IntentionType.ACTIVATE);
        this.activations = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.activations = new HashSet<>();
        JSONArray jsonSensors = json.getJSONArray(ActivateIntention.ACTIVATIONS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.addActivation(SensorType.values()[jsonSensors.getInt(i)]);
        }
    }

    public boolean addActivation(SensorType type) {
        if (this.hasActivationWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s is already intended to be activated", type.getLabel()));
            return true;
        }
        this.activations.add(type);
        return true;
    }

    public HashSet<SensorType> getActivations() {
        return this.activations;
    }

    public boolean hasActivations() {
        return !this.activations.isEmpty();
    }

    public boolean hasActivationWithType(SensorType type) {
        return this.activations.contains(type);
    }

    public boolean removeActivation(SensorType type) {
        if (!this.hasActivationWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s is not intended to be activated", type.getLabel()));
            return true;
        }
        this.activations.remove(type);
        return true;
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorType type : this.activations) {
            jsonSensors.put(type.getType());
        }
        json.put(ActivateIntention.ACTIVATIONS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(ActivateIntention intent) {
        return this.type == intent.type && this.activations.equals(intent.activations);
    }

}
