package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
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
import com.seat.rescuesim.common.sensor.SensorRegistry;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.CoreException;

/** A serializable snapshot of a Remote. */
public class RemoteState extends JSONAble {
    public static final String BATTERY = "battery";
    public static final String LOCATION = "location";
    public static final String REMOTE_ID = "remote_id";

    private double battery;
    private Vector location;
    private String remoteID;
    private RemoteType remoteType;
    private HashMap<String, SensorState> sensors;

    public RemoteState(String remoteID, Vector location, double battery) {
        this(RemoteProto.DEFAULT_REMOTE_TYPE, remoteID, location, battery, null);
    }

    public RemoteState(String remoteID, Vector location, double battery, Collection<SensorState> sensors) {
        this(RemoteProto.DEFAULT_REMOTE_TYPE, remoteID, location, battery, sensors);
    }

    protected RemoteState(RemoteType remoteType, String remoteID, Vector location, double battery) {
        this(remoteType, remoteID, location, battery, null);
    }

    protected RemoteState(RemoteType remoteType, String remoteID, Vector location, double battery,
            Collection<SensorState> sensors) {
        this.remoteType = remoteType;
        this.remoteID = remoteID;
        this.location = location;
        this.battery = battery;
        this.sensors = new HashMap<>();
        if (sensors != null) {
            for (SensorState sensor : sensors) {
                this.sensors.put(sensor.getSensorID(), sensor);
            }
        }
    }

    public RemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.remoteType = RemoteType.decodeType(json);
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.location = new Vector(json.getJSONOption(RemoteProto.LOCATION));
        this.battery = json.getDouble(RemoteState.BATTERY);
        this.sensors = new HashMap<>();
        if (json.hasKey(RemoteProto.SENSORS)) {
            JSONArray jsonState = json.getJSONArray(RemoteProto.SENSORS);
            for (int i = 0; i < jsonState.length(); i++) {
                SensorState state = SensorRegistry.decodeTo(jsonState.getJSONOption(i), SensorState.class);
                this.sensors.put(state.getSensorID(), state);
            }
        }
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteType.REMOTE_TYPE, this.remoteType.getType());
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        json.put(RemoteProto.LOCATION, this.location.toJSON());
        json.put(RemoteState.BATTERY, this.battery);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (SensorState sensor : this.sensors.values()) {
                jsonSensors.put(sensor.toJSON());
            }
            json.put(RemoteProto.SENSORS, jsonSensors.toJSON());
        }
        return json;
    }

    public double getBattery() {
        return this.battery;
    }

    public String getLabel() {
        return String.format("%s:%s", this.remoteID, this.remoteType.getLabel());
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
        if (state == null) return false;
        return this.remoteType.equals(state.remoteType) && this.remoteID.equals(state.remoteID) &&
            this.location.equals(state.location) && this.battery == state.battery &&
            this.sensors.equals(state.sensors);
    }

}
