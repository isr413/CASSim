package com.seat.sim.common.sensor.comms;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.sensor.SensorState;

/** A serializable Comms Sensor state. */
public class CommsSensorState extends SensorState {
    public static final String CONNECTIONS = "connections";

    private Set<String> connections;

    public CommsSensorState(String sensorModel, String sensorID, boolean active, Collection<String> connections) {
        super(sensorModel, sensorID, active);
        this.connections = (connections != null) ? new HashSet<>(connections) : new HashSet<>();
    }

    public CommsSensorState(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.connections = (json.hasKey(CommsSensorState.CONNECTIONS)) ?
            new HashSet<>(json.getJSONArray(CommsSensorState.CONNECTIONS).toList(String.class)) :
            new HashSet<>();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasConnections()) {
            json.put(CommsSensorState.CONNECTIONS, JSONBuilder.Array(this.connections).toJSON());
        }
        return json;
    }

    public Set<String> getConnections() {
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
