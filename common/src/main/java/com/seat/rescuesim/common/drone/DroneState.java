package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.Debugger;

public class DroneState extends RemoteState {
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

    public DroneState(JSONObject json) {
        super(json);
    }

    public DroneState(JSONOption option) {
        super(option);
    }

    public DroneState(String encoding) {
        super(encoding);
    }

    public DroneState(String remote, Vector location) {
        this(remote, location, new Vector(), new Vector(), 1, new HashMap<String, SensorState>());
    }

    public DroneState(String remote, Vector location, SensorState[] sensors) {
        this(remote, location, new Vector(), new Vector(), 1, new ArrayList<SensorState>(Arrays.asList(sensors)));
    }

    public DroneState(String remote, Vector location, ArrayList<SensorState> sensors) {
        this(remote, location, new Vector(), new Vector(), 1, sensors);
    }

    public DroneState(String remote, Vector location, Vector velocity, Vector acceleration) {
        this(remote, location, location, acceleration, 1, new HashMap<String, SensorState>());
    }

    public DroneState(String remote, Vector location, Vector velocity, Vector acceleration, double battery,
            SensorState[] sensors) {
        this(remote, location, velocity, acceleration, battery, new ArrayList<SensorState>(Arrays.asList(sensors)));
    }

    public DroneState(String remote, Vector location, Vector velocity, Vector acceleration, double battery,
            ArrayList<SensorState> sensors) {
        this(remote, location, velocity, acceleration, battery, new HashMap<String, SensorState>());
        for (SensorState state : sensors) {
            this.sensors.put(state.getRemoteID(), state);
        }
    }

    public DroneState(String remote, Vector location, Vector velocity, Vector acceleration, double battery,
            HashMap<String, SensorState> sensors) {
        super(RemoteType.DRONE, remote);
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.battery = battery;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.location = new Vector(json.getJSONArray(DroneState.LOCATION));
        this.velocity = new Vector(json.getJSONArray(DroneState.VELOCITY));
        this.acceleration = new Vector(json.getJSONArray(DroneState.ACCELERATION));
        this.battery = json.getDouble(DroneState.BATTERY);
        this.sensors = new HashMap<>();
        JSONArray jsonState = json.getJSONArray(DroneState.SENSORS);
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
            Debugger.logger.err(String.format("No sensor %s found on drone %s", sensor, this.remote));
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
        json.put(DroneState.LOCATION, this.location.toJSON());
        json.put(DroneState.VELOCITY, this.velocity.toJSON());
        json.put(DroneState.ACCELERATION, this.acceleration.toJSON());
        json.put(DroneState.BATTERY, this.battery);
        JSONArrayBuilder jsonState = JSONBuilder.Array();
        for (SensorState state : this.sensors.values()) {
            jsonState.put(state.toJSON());
        }
        json.put(DroneState.SENSORS, jsonState.toJSON());
        return json.toJSON();
    }

    public boolean equals(DroneState state) {
        return this.location.equals(state.location) && this.velocity.equals(state.velocity) &&
            this.acceleration.equals(state.acceleration) && this.battery == state.battery &&
            this.sensors.equals(state.sensors);
    }

}
