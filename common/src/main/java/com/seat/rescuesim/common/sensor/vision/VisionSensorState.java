package com.seat.rescuesim.common.sensor.vision;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;

public class VisionSensorState extends SensorState {

    protected HashSet<String> observations;
    protected VisionSensorType specType;

    public VisionSensorState(String sensorID) {
        this(VisionSensorType.GENERIC, sensorID);
    }

    public VisionSensorState(String sensorID, boolean active, ArrayList<String> observations) {
        this(VisionSensorType.GENERIC, sensorID, active, observations);
    }

    public VisionSensorState(String sensorID, boolean active, HashSet<String> observations) {
        this(VisionSensorType.GENERIC, sensorID, active, observations);
    }

    public VisionSensorState(VisionSensorType specType, String sensorID) {
        this(specType, sensorID, false, new HashSet<String>());
    }

    public VisionSensorState(VisionSensorType specType, String sensorID, boolean active,
            ArrayList<String> observations) {
        this(specType, sensorID, active, new HashSet<String>(observations));
    }

    public VisionSensorState(VisionSensorType specType, String sensorID, boolean active, HashSet<String> observations) {
        super(SensorType.VISION, sensorID, active);
        this.specType = specType;
        this.observations = observations;
    }

    public VisionSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = VisionSensorType.decodeType(json);
        this.observations = new HashSet<>();
        if (json.hasKey(VisionSensorConst.OBSERVATIONS)) {
            JSONArray jsonObservations = json.getJSONArray(VisionSensorConst.OBSERVATIONS);
            for (int i = 0; i < jsonObservations.length(); i++) {
                this.observations.add(jsonObservations.getString(i));
            }
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VisionSensorConst.VISION_SENSOR_TYPE, this.specType.getType());
        if (this.hasObservations()) {
            JSONArrayBuilder jsonObservations = JSONBuilder.Array();
            for (String remoteID : this.observations) {
                jsonObservations.put(remoteID);
            }
            json.put(VisionSensorConst.OBSERVATIONS, jsonObservations.toJSON());
        }
        return json;
    }

    public HashSet<String> getObservations() {
        return this.observations;
    }

    @Override
    public VisionSensorType getSpecType() {
        return this.specType;
    }

    public boolean hasObservations() {
        return !this.observations.isEmpty();
    }

    public boolean hasObservationWithID(String remoteID) {
        return this.observations.contains(remoteID);
    }

    public boolean equals(VisionSensorState state) {
        return super.equals(state) && this.specType.equals(state.specType) &&
            this.observations.equals(state.observations);
    }

}
