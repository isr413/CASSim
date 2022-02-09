package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.seat.rescuesim.common.SensorType;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.Intention;
import com.seat.rescuesim.common.remote.IntentionType;
import com.seat.rescuesim.common.util.Debugger;

public class DeactivateIntention extends Intention {
    private static final String DEACTIVATIONS = "deactivations";

    private HashSet<SensorType> deactivations;

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
        this(new HashSet<SensorType>());
    }

    public DeactivateIntention(SensorType[] sensors) {
        this(new ArrayList<SensorType>(Arrays.asList(sensors)));
    }

    public DeactivateIntention(ArrayList<SensorType> sensors) {
        this(new HashSet<SensorType>());
        for (SensorType type : sensors) {
            this.addDeactivation(type);
        }
    }

    public DeactivateIntention(HashSet<SensorType> sensors) {
        super(IntentionType.ACTIVATE);
        this.deactivations = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.deactivations = new HashSet<>();
        JSONArray jsonSensors = json.getJSONArray(DeactivateIntention.DEACTIVATIONS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.addDeactivation(SensorType.values()[jsonSensors.getInt(i)]);
        }
    }

    public boolean addDeactivation(SensorType type) {
        if (this.hasDeactivationWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s is already intended to be activated", type.getLabel()));
            return true;
        }
        this.deactivations.add(type);
        return true;
    }

    public HashSet<SensorType> getDeactivations() {
        return this.deactivations;
    }

    public boolean hasDeactivations() {
        return !this.deactivations.isEmpty();
    }

    public boolean hasDeactivationWithType(SensorType type) {
        return this.deactivations.contains(type);
    }

    public boolean removeDeactivation(SensorType type) {
        if (!this.hasDeactivationWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s is not intended to be activated", type.getLabel()));
            return true;
        }
        this.deactivations.remove(type);
        return true;
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorType type : this.deactivations) {
            jsonSensors.put(type.getType());
        }
        json.put(DeactivateIntention.DEACTIVATIONS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(DeactivateIntention intent) {
        return this.type == intent.type && this.deactivations.equals(intent.deactivations);
    }

}
