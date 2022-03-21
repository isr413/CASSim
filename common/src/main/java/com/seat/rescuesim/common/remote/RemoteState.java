package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorFactory;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable snapshot of a Remote. */
public abstract class RemoteState extends JSONAble {
    private static final String BATTERY = "battery";
    private static final String LOCATION = "location";
    private static final String REMOTE_ID = "remote_id";
    private static final String REMOTE_TYPE = "remote_type";
    private static final String SENSORS = "sensors";

    public static RemoteType decodeType(JSONObject json) throws JSONException {
        return RemoteType.values()[json.getInt(RemoteState.REMOTE_TYPE)];
    }

    public static RemoteType decodeType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return RemoteState.decodeType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode remote type of %s", option.toString()));
        return null;
    }

    public static RemoteType decodeType(String encoding) throws JSONException {
        return RemoteState.decodeType(JSONOption.String(encoding));
    }

    protected double battery;
    protected Vector location;
    protected String remoteID;
    protected HashMap<String, SensorState> sensors;
    protected RemoteType type;

    public RemoteState(JSONObject json) throws JSONException {
        super(json);
    }

    public RemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    public RemoteState(String encoding) throws JSONException {
        super(encoding);
    }

    public RemoteState(RemoteType type, String remoteID, Vector location, double battery,
            ArrayList<SensorState> sensors) {
        this(type, remoteID, location, battery, new HashMap<String, SensorState>());
        for (SensorState sensor : sensors) {
            this.sensors.put(sensor.getSensorID(), sensor);
        }
    }

    public RemoteState(RemoteType type, String remoteID, Vector location, double battery,
            HashMap<String, SensorState> sensors) {
        this.type = type;
        this.remoteID = remoteID;
        this.location = location;
        this.battery = battery;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = RemoteType.values()[json.getInt(RemoteState.REMOTE_TYPE)];
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.location = new Vector(json.getJSONArray(RemoteState.LOCATION));
        this.battery = json.getDouble(RemoteState.BATTERY);
        this.sensors = new HashMap<>();
        if (json.hasKey(RemoteState.SENSORS)) {
            JSONArray jsonState = json.getJSONArray(RemoteState.SENSORS);
            for (int i = 0; i < jsonState.length(); i++) {
                SensorState state = SensorFactory.decodeSensorState(jsonState.getJSONObject(i));
                this.sensors.put(state.getSensorID(), state);
            }
        }
    }

    public double getBattery() {
        return this.battery;
    }

    public String getLabel() {
        return String.format("%s%s", this.remoteID, this.type.getLabel());
    }

    public Vector getLocation() {
        return this.location;
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public ArrayList<SensorState> getSensors() {
        return new ArrayList<SensorState>(this.sensors.values());
    }

    public SensorState getSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID)) {
            Debugger.logger.err(String.format("No sensor %s found on remote %s", sensorID, this.remoteID));
            return null;
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

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteState.REMOTE_TYPE, this.type.getType());
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        json.put(RemoteState.LOCATION, this.location.toJSON());
        json.put(RemoteState.BATTERY, this.battery);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (SensorState sensor : this.sensors.values()) {
                jsonSensors.put(sensor.toJSON());
            }
            json.put(RemoteState.SENSORS, jsonSensors.toJSON());
        }
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(RemoteState state) {
        return this.type.equals(state.type) && this.remoteID.equals(state.remoteID) &&
            this.location.equals(state.location) && this.battery == state.battery &&
            this.sensors.equals(state.sensors);
    }

}
