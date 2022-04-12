package com.seat.sim.common.sensor.vision;

import java.util.Collection;
import java.util.HashSet;

import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.sensor.SensorState;

/** A serializable Vision Sensor state. */
public class VisionSensorState extends SensorState {
    public static final String OBSERVATIONS = "observations";

    private HashSet<String> observations;

    public VisionSensorState(String sensorModel, String sensorID, boolean active) {
        this(sensorModel, sensorID, active, null);
    }

    public VisionSensorState(String sensorModel, String sensorID, boolean active, Collection<String> observations) {
        super(sensorModel, sensorID, active);
        this.observations = (observations != null) ? new HashSet<>(observations) : new HashSet<>();
    }

    public VisionSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.observations = new HashSet<>();
        if (json.hasKey(VisionSensorState.OBSERVATIONS)) {
            JSONArray jsonObservations = json.getJSONArray(VisionSensorState.OBSERVATIONS);
            for (int i = 0; i < jsonObservations.length(); i++) {
                this.observations.add(jsonObservations.getString(i));
            }
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasObservations()) {
            JSONArrayBuilder jsonObservations = JSONBuilder.Array();
            for (String remoteID : this.observations) {
                jsonObservations.put(remoteID);
            }
            json.put(VisionSensorState.OBSERVATIONS, jsonObservations.toJSON());
        }
        return json;
    }

    public Collection<String> getObservations() {
        return this.observations;
    }

    public boolean hasObservations() {
        return !this.observations.isEmpty();
    }

    public boolean hasObservationWithID(String remoteID) {
        return this.observations.contains(remoteID);
    }

    public boolean equals(VisionSensorState state) {
        if (state == null) return false;
        return super.equals(state) && this.observations.equals(state.observations);
    }

}