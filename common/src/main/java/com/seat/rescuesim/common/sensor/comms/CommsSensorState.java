package com.seat.rescuesim.common.sensor.comms;

import java.util.Collection;
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

public class CommsSensorState extends SensorState {

    protected HashSet<String> connections;
    protected CommsSensorType specType;

    public CommsSensorState(String sensorID) {
        this(CommsSensorType.GENERIC, sensorID);
    }

    public CommsSensorState(String sensorID, boolean active, Collection<String> connections) {
        this(CommsSensorType.GENERIC, sensorID, active, connections);
    }

    public CommsSensorState(CommsSensorType specType, String sensorID) {
        super(SensorType.COMMS, sensorID, false);
        this.specType = specType;
        this.connections = new HashSet<>();
    }

    public CommsSensorState(CommsSensorType specType, String sensorID, boolean active, Collection<String> connections) {
        super(SensorType.COMMS, sensorID, active);
        this.specType = specType;
        this.connections = new HashSet<>(connections);
    }

    public CommsSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = CommsSensorType.decodeType(json);
        this.connections = new HashSet<>();
        if (json.hasKey(CommsSensorConst.CONNECTIONS)) {
            JSONArray jsonConnections = json.getJSONArray(CommsSensorConst.CONNECTIONS);
            for (int i = 0; i < jsonConnections.length(); i++) {
                this.connections.add(jsonConnections.getString(i));
            }
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasConnections()) {
            JSONArrayBuilder jsonObservations = JSONBuilder.Array();
            for (String remoteID : this.connections) {
                jsonObservations.put(remoteID);
            }
            json.put(CommsSensorConst.CONNECTIONS, jsonObservations.toJSON());
        }
        return json;
    }

    public HashSet<String> getConnections() {
        return this.connections;
    }

    @Override
    public CommsSensorType getSpecType() {
        return this.specType;
    }

    public boolean hasConnections() {
        return !this.connections.isEmpty();
    }

    public boolean hasConnectionWithID(String remoteID) {
        return this.connections.contains(remoteID);
    }

    public boolean equals(CommsSensorState state) {
        return super.equals(state) && this.specType.equals(state.specType) &&
            this.connections.equals(state.connections);
    }

}
