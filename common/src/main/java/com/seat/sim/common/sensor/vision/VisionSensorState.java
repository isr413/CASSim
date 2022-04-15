package com.seat.sim.common.sensor.vision;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.sensor.SensorState;

/** A serializable Vision Sensor state. */
public class VisionSensorState extends SensorState {
    public static final String OBSERVATIONS = "observations";

    private Set<String> observations;

    public VisionSensorState(String sensorModel, String sensorID, boolean active, Collection<String> observations) {
        super(sensorModel, sensorID, active);
        this.observations = (observations != null) ? new HashSet<>(observations) : new HashSet<>();
    }

    public VisionSensorState(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.observations = (json.hasKey(VisionSensorState.OBSERVATIONS)) ?
            new HashSet<>(json.getJSONArray(VisionSensorState.OBSERVATIONS).toList(String.class)) :
            new HashSet<>();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasObservations()) {
            json.put(VisionSensorState.OBSERVATIONS, JSONBuilder.Array(this.observations).toJSON());
        }
        return json;
    }

    public Set<String> getObservations() {
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
