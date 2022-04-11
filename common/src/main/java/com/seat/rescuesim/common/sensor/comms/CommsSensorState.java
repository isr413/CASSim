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

/** A serializable Comms Sensor state. */
public class CommsSensorState extends SensorState {
    public static final String CONNECTIONS = "connections";

    private HashSet<String> connections;

    public CommsSensorState(String sensorID, boolean active) {
        this(sensorID, active, null);
    }

    public CommsSensorState(String sensorID, boolean active,
            Collection<String> connections) {
        super(sensorID, active);
        this.connections = (connections != null) ? new HashSet<>(connections) : new HashSet<>();
    }

    public CommsSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.connections = new HashSet<>();
        if (json.hasKey(CommsSensorState.CONNECTIONS)) {
            JSONArray jsonConnections = json.getJSONArray(CommsSensorState.CONNECTIONS);
            for (int i = 0; i < jsonConnections.length(); i++) {
                this.connections.add(jsonConnections.getString(i));
            }
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasConnections()) {
            JSONArrayBuilder jsonObservations = JSONBuilder.Array();
            for (String remoteID : this.connections) {
                jsonObservations.put(remoteID);
            }
            json.put(CommsSensorState.CONNECTIONS, jsonObservations.toJSON());
        }
        return json;
    }

    public HashSet<String> getConnections() {
        return this.connections;
    }

    public boolean hasConnections() {
        return !this.connections.isEmpty();
    }

    public boolean hasConnectionWithID(String remoteID) {
        return this.connections.contains(remoteID);
    }

    public boolean equals(CommsSensorState state) {
        if (state == null) return false;
        return super.equals(state) && this.connections.equals(state.connections);
    }

}
