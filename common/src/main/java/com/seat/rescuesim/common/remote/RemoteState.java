package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.CoreException;
import com.seat.rescuesim.common.util.SensorFactory;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable snapshot of a Remote. */
public abstract class RemoteState extends JSONAble {

    protected double battery;
    protected Vector location;
    protected String remoteID;
    protected HashMap<String, SensorState> sensors;
    protected RemoteType remoteType;

    public RemoteState(RemoteType remoteType, String remoteID, Vector location, double battery) {
        this(remoteType, remoteID, location, battery, new ArrayList<SensorState>());
    }

    public RemoteState(RemoteType remoteType, String remoteID, Vector location, double battery,
            ArrayList<SensorState> sensors) {
        this.remoteType = remoteType;
        this.remoteID = remoteID;
        this.location = location;
        this.battery = battery;
        this.sensors = new HashMap<>();
        for (SensorState sensor : sensors) {
            this.sensors.put(sensor.getSensorID(), sensor);
        }
    }

    public RemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.remoteType = RemoteType.decodeType(json);
        this.remoteID = json.getString(RemoteConst.REMOTE_ID);
        this.location = new Vector(json.getJSONOption(RemoteConst.LOCATION));
        this.battery = json.getDouble(RemoteConst.BATTERY);
        this.sensors = new HashMap<>();
        if (json.hasKey(RemoteConst.SENSORS)) {
            JSONArray jsonState = json.getJSONArray(RemoteConst.SENSORS);
            for (int i = 0; i < jsonState.length(); i++) {
                SensorState state = SensorFactory.decodeSensorState(jsonState.getJSONOption(i));
                this.sensors.put(state.getSensorID(), state);
            }
        }
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteConst.REMOTE_TYPE, this.remoteType.getType());
        json.put(RemoteConst.REMOTE_ID, this.remoteID);
        json.put(RemoteConst.LOCATION, this.location.toJSON());
        json.put(RemoteConst.BATTERY, this.battery);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (SensorState sensor : this.sensors.values()) {
                jsonSensors.put(sensor.toJSON());
            }
            json.put(RemoteConst.SENSORS, jsonSensors.toJSON());
        }
        return json;
    }

    public double getBattery() {
        return this.battery;
    }

    public String getLabel() {
        return String.format("%s%s", this.remoteID, this.remoteType.getLabel());
    }

    public Vector getLocation() {
        return this.location;
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteType getRemoteType() {
        return this.remoteType;
    }

    public ArrayList<SensorState> getSensors() {
        return new ArrayList<SensorState>(this.sensors.values());
    }

    public SensorState getSensorWithID(String sensorID) throws CoreException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new CoreException(String.format("No sensor %s found on remote %s", sensorID, this.remoteID));
        }
        return this.sensors.get(sensorID);
    }

    public abstract SerializableEnum getSpecType();

    public boolean hasSensorWithID(String sensorID) {
        return this.sensors.containsKey(sensorID);
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isEnabled() {
        return this.battery > 0;
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteState state) {
        return this.remoteType.equals(state.remoteType) && this.remoteID.equals(state.remoteID) &&
            this.location.equals(state.location) && this.battery == state.battery &&
            this.sensors.equals(state.sensors);
    }

}
