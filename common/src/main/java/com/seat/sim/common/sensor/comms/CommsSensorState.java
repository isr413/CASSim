package com.seat.sim.common.sensor.comms;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.sensor.SensorState;

/** A serializable Comms Sensor state. */
public class CommsSensorState extends SensorState {
    public static final String CONNECTIONS = "connections";

    private Set<String> connections;

    public CommsSensorState(String sensorModel, String sensorID, boolean active) {
        this(sensorModel, sensorID, active, null);
    }

    public CommsSensorState(String sensorModel, String sensorID, boolean active, Collection<String> connections) {
        super(sensorModel, sensorID, active);
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

    public Collection<String> getConnections() {
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
