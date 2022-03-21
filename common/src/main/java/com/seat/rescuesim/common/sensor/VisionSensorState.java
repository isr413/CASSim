package com.seat.rescuesim.common.sensor;

import java.util.HashSet;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public class VisionSensorState extends SensorState {
    private static final String OBSERVATIONS = "observations";

    private HashSet<String> observations;

    public VisionSensorState(JSONObject json) throws JSONException {
        super(json);
    }

    public VisionSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    public VisionSensorState(String encoding) throws JSONException {
        super(encoding);
    }

    public VisionSensorState(SensorType type, String sensorID) {
        super(type, sensorID);
        this.observations = new HashSet<>();
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        if (json.hasKey(VisionSensorState.OBSERVATIONS)) {
            JSONArray jsonObservations = json.getJSONArray(VisionSensorState.OBSERVATIONS);
            for (int i = 0; i < jsonObservations.length(); i++) {
                this.observations.add(jsonObservations.getString(i));
            }
        }
    }

    public HashSet<String> getObservations() {
        return this.observations;
    }

    public boolean hasObservations() {
        return !this.observations.isEmpty();
    }

    public boolean hasObservationWithID(String remoteID) {
        return this.observations.contains(remoteID);
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasObservations()) {
            JSONArrayBuilder jsonObservations = JSONBuilder.Array();
            for (String remoteID : this.observations) {
                jsonObservations.put(remoteID);
            }
            json.put(VisionSensorState.OBSERVATIONS, jsonObservations.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(VisionSensorState state) {
        return super.equals(state) && this.observations.equals(state.observations);
    }

}
