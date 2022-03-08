package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.Debugger;

public class VictimState extends RemoteState {
    private static final String ACCELERATION = "acceleration";
    private static final String BATTERY = "battery";
    private static final String LOCATION = "location";
    private static final String SENSORS = "sensors";
    private static final String VELOCITY = "velocity";

    private Vector acceleration;
    private double battery;
    private Vector location;
    private HashMap<String, SensorState> sensors;
    private Vector velocity;

    public VictimState(JSONObject json) {
        super(json);
    }

    public VictimState(JSONOption option) {
        super(option);
    }

    public VictimState(String encoding) {
        super(encoding);
    }

    public VictimState(String remote, Vector location) {
        this(remote, location, new Vector(), new Vector(), 1, new HashMap<String, SensorState>());
    }

    public VictimState(String remote, Vector location, SensorState[] sensors) {
        this(remote, location, new Vector(), new Vector(), 1, new ArrayList<SensorState>(Arrays.asList(sensors)));
    }

    public VictimState(String remote, Vector location, ArrayList<SensorState> sensors) {
        this(remote, location, new Vector(), new Vector(), 1, sensors);
    }

    public VictimState(String remote, Vector location, Vector velocity, Vector acceleration) {
        this(remote, location, location, acceleration, 1, new HashMap<String, SensorState>());
    }

    public VictimState(String remote, Vector location, Vector velocity, Vector acceleration, double battery,
            SensorState[] sensors) {
        this(remote, location, velocity, acceleration, battery, new ArrayList<SensorState>(Arrays.asList(sensors)));
    }

    public VictimState(String remote, Vector location, Vector velocity, Vector acceleration, double battery,
            ArrayList<SensorState> sensors) {
        this(remote, location, velocity, acceleration, battery, new HashMap<String, SensorState>());
        for (SensorState state : sensors) {
            this.sensors.put(state.getRemoteID(), state);
        }
    }

    public VictimState(String remote, Vector location, Vector velocity, Vector acceleration, double battery,
            HashMap<String, SensorState> sensors) {
        super(RemoteType.BASE, remote);
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.battery = battery;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.location = new Vector(json.getJSONArray(VictimState.LOCATION));
        this.velocity = new Vector(json.getJSONArray(VictimState.VELOCITY));
        this.acceleration = new Vector(json.getJSONArray(VictimState.ACCELERATION));
        this.battery = json.getDouble(VictimState.BATTERY);
        this.sensors = new HashMap<>();
        JSONArray jsonState = json.getJSONArray(VictimState.SENSORS);
        for (int i = 0; i < jsonState.length(); i++) {
            SensorState state = new SensorState(jsonState.getJSONObject(i));
            this.sensors.put(state.getRemoteID(), state);
        }
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public double getBattery() {
        return this.battery;
    }

    public Vector getLocation() {
        return this.location;
    }

    public SensorState getSensor(String sensor) {
        if (!this.hasSensor(sensor)) {
            Debugger.logger.err(String.format("No sensor %s found on victim %s", sensor, this.remote));
            return null;
        }
        return this.sensors.get(sensor);
    }

    public ArrayList<SensorState> getSensors() {
        return new ArrayList<SensorState>(this.sensors.values());
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public boolean hasAcceleration() {
        return this.acceleration.getMagnitude() > 0;
    }

    public boolean hasBattery() {
        return this.battery > 0;
    }

    public boolean hasSensor(String sensor) {
        return this.sensors.containsKey(sensor);
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasVelocity() {
        return this.velocity.getMagnitude() > 0;
    }

    public boolean isMoving() {
        return this.hasAcceleration() || this.hasVelocity();
    }

    public boolean isStatic() {
        return !this.isMoving();
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimState.LOCATION, this.location.toJSON());
        json.put(VictimState.VELOCITY, this.velocity.toJSON());
        json.put(VictimState.ACCELERATION, this.acceleration.toJSON());
        json.put(VictimState.BATTERY, this.battery);
        JSONArrayBuilder jsonState = JSONBuilder.Array();
        for (SensorState state : this.sensors.values()) {
            jsonState.put(state.toJSON());
        }
        json.put(VictimState.SENSORS, jsonState.toJSON());
        return json.toJSON();
    }

    public boolean equals(VictimState state) {
        return this.location.equals(state.location) && this.velocity.equals(state.velocity) &&
            this.acceleration.equals(state.acceleration) && this.battery == state.battery &&
            this.sensors.equals(state.sensors);
    }

}
