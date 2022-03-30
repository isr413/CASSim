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

public class DeactivateIntention extends Intention {
    private static final String DEACTIVATIONS = "deactivations";

    private HashSet<String> deactivations;

    public DeactivateIntention() {
        this(new HashSet<String>());
    }

    public DeactivateIntention(ArrayList<String> sensors) {
        this(new HashSet<String>(sensors));
    }

    public DeactivateIntention(HashSet<String> sensors) {
        super(IntentionType.DEACTIVATE);
        this.deactivations = sensors;
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
        return this.type == intent.type && this.deactivations.equals(intent.deactivations);
    }

}
