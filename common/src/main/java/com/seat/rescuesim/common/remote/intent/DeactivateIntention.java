package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

public class DeactivateIntention extends Intention {
    private static final String DEACTIVATIONS = "deactivations";

    private HashSet<String> deactivations;

    public DeactivateIntention(JSONObject json) {
        super(json);
    }

    public DeactivateIntention(JSONOption option) {
        super(option);
    }

    public DeactivateIntention(String encoding) {
        super(encoding);
    }

    public DeactivateIntention() {
        this(new HashSet<String>());
    }

    public DeactivateIntention(ArrayList<String> sensors) {
        this(new HashSet<String>());
        for (String sensor : sensors) {
            this.addDeactivation(sensor);
        }
    }

    public DeactivateIntention(HashSet<String> sensors) {
        super(IntentionType.DEACTIVATE);
        this.deactivations = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.deactivations = new HashSet<>();
        JSONArray jsonSensors = json.getJSONArray(DeactivateIntention.DEACTIVATIONS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.addDeactivation(jsonSensors.getString(i));
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
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (String sensor : this.deactivations) {
            jsonSensors.put(sensor);
        }
        json.put(DeactivateIntention.DEACTIVATIONS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(DeactivateIntention intent) {
        return this.type == intent.type && this.deactivations.equals(intent.deactivations);
    }

}
