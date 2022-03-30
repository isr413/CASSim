package com.seat.rescuesim.common.sensor;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public class VisionSensorState extends SensorState {

    private HashSet<String> observations;

    public VisionSensorState(SensorType type, String sensorID) {
        this(type, sensorID, false, new HashSet<String>());
    }

    public VisionSensorState(SensorType type, String sensorID, boolean active, ArrayList<String> observations) {
        this(type, sensorID, active, new HashSet<String>(observations));
    }

    public VisionSensorState(SensorType type, String sensorID, boolean active, HashSet<String> observations) {
        super(type, sensorID, active);
        this.observations = observations;
    }

    public VisionSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.observations = new HashSet<>();
        if (json.hasKey(SensorConst.OBSERVATIONS)) {
            JSONArray jsonObservations = json.getJSONArray(SensorConst.OBSERVATIONS);
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
            json.put(SensorConst.OBSERVATIONS, jsonObservations.toJSON());
        }
        return json.toJSON();
    }

}
