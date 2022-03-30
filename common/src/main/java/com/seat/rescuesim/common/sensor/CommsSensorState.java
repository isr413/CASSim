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

public class CommsSensorState extends SensorState {

    private HashSet<String> connections;

    public CommsSensorState(SensorType type, String sensorID) {
        this(type, sensorID, false, new HashSet<String>());
    }

    public CommsSensorState(SensorType type, String sensorID, boolean active, ArrayList<String> connections) {
        this(type, sensorID, active, new HashSet<String>(connections));
    }

    public CommsSensorState(SensorType type, String sensorID, boolean active, HashSet<String> connections) {
        super(type, sensorID, active);
        this.connections = connections;
    }

    public CommsSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.connections = new HashSet<>();
        if (json.hasKey(SensorConst.CONNECTIONS)) {
            JSONArray jsonConnections = json.getJSONArray(SensorConst.CONNECTIONS);
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
            json.put(SensorConst.CONNECTIONS, jsonObservations.toJSON());
        }
        return json.toJSON();
    }

}
