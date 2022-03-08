package com.seat.rescuesim.common.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.Debugger;

public class BaseState extends RemoteState {
    private static final String BASE_LOCATION = "location";
    private static final String BASE_SENSORS = "sensors";

    private Vector location;
    private HashMap<String, SensorState> sensors;

    public BaseState(JSONObject json) {
        super(json);
    }

    public BaseState(JSONOption option) {
        super(option);
    }

    public BaseState(String encoding) {
        super(encoding);
    }

    public BaseState(String remote, Vector location) {
        this(remote, location, new HashMap<String, SensorState>());
    }

    public BaseState(String remote, Vector location, SensorState[] sensors) {
        this(remote, location, new ArrayList<SensorState>(Arrays.asList(sensors)));
    }

    public BaseState(String remote, Vector location, ArrayList<SensorState> sensors) {
        this(remote, location, new HashMap<String, SensorState>());
        for (SensorState state : sensors) {
            this.sensors.put(state.getRemoteID(), state);
        }
    }

    public BaseState(String remote, Vector location, HashMap<String, SensorState> sensors) {
        super(RemoteType.BASE, remote);
        this.location = location;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.location = new Vector(json.getJSONArray(BaseState.BASE_LOCATION));
        this.sensors = new HashMap<>();
        JSONArray jsonState = json.getJSONArray(BaseState.BASE_SENSORS);
        for (int i = 0; i < jsonState.length(); i++) {
            SensorState state = new SensorState(jsonState.getJSONObject(i));
            this.sensors.put(state.getRemoteID(), state);
        }
    }

    public Vector getLocation() {
        return this.location;
    }

    public SensorState getSensor(String sensor) {
        if (!this.hasSensor(sensor)) {
            Debugger.logger.err(String.format("No sensor %s found on base %s", sensor, this.remote));
            return null;
        }
        return this.sensors.get(sensor);
    }

    public ArrayList<SensorState> getSensors() {
        return new ArrayList<SensorState>(this.sensors.values());
    }

    public boolean hasSensor(String sensor) {
        return this.sensors.containsKey(sensor);
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(BaseState.BASE_LOCATION, this.location.toJSON());
        JSONArrayBuilder jsonState = JSONBuilder.Array();
        for (SensorState state : this.sensors.values()) {
            jsonState.put(state.toJSON());
        }
        json.put(BaseState.BASE_SENSORS, jsonState.toJSON());
        return json.toJSON();
    }

    public boolean equals(BaseState state) {
        return this.location.equals(state.location) && this.sensors.equals(state.sensors);
    }

}
