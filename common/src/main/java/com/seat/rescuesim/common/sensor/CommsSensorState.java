package com.seat.rescuesim.common.sensor;

import java.util.HashSet;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public class CommsSensorState extends SensorState {
    private static final String CONNECTIONS = "connections";

    private HashSet<String> connections;

    public CommsSensorState(JSONObject json) throws JSONException {
        super(json);
    }

    public CommsSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    public CommsSensorState(String encoding) throws JSONException {
        super(encoding);
    }

    public CommsSensorState(SensorType type, String sensorID) {
        super(type, sensorID);
        this.connections = new HashSet<>();
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        if (json.hasKey(CommsSensorState.CONNECTIONS)) {
            JSONArray jsonConnections = json.getJSONArray(CommsSensorState.CONNECTIONS);
            for (int i = 0; i < jsonConnections.length(); i++) {
                this.connections.add(jsonConnections.getString(i));
            }
        }
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

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasConnections()) {
            JSONArrayBuilder jsonObservations = JSONBuilder.Array();
            for (String remoteID : this.connections) {
                jsonObservations.put(remoteID);
            }
            json.put(CommsSensorState.CONNECTIONS, jsonObservations.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(CommsSensorState state) {
        return super.equals(state) && this.connections.equals(state.connections);
    }

}
