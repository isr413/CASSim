package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable class to represent a snapshot of a Remote. */
public abstract class RemoteState extends JSONAble {
    private static final String BATTERY = "battery";
    private static final String LOCATION = "location";
    private static final String REMOTE_ID = "remote_id";
    private static final String REMOTE_TYPE = "remote_type";
    private static final String SENSORS = "sensors";

    public static RemoteType decodeType(JSONObject json) {
        return RemoteType.values()[json.getInt(RemoteState.REMOTE_TYPE)];
    }

    public static RemoteType decodeType(JSONOption option) {
        if (option.isSomeObject()) {
            return RemoteState.decodeType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode remote type of %s", option.toString()));
        return null;
    }

    public static RemoteType decodeType(String encoding) {
        return RemoteState.decodeType(JSONOption.String(encoding));
    }

    private double battery;
    private Vector location;
    protected String remoteID;
    protected RemoteType type;
    private HashMap<String, SensorState> sensors;

    public RemoteState(JSONObject json) {
        super(json);
    }

    public RemoteState(JSONOption option) {
        super(option);
    }

    public RemoteState(String encoding) {
        super(encoding);
    }

    public RemoteState(RemoteType type, String remoteID, Vector location, double battery,
            ArrayList<SensorState> sensors) {
        this(type, remoteID, location, battery, new HashMap<String, SensorState>());
        for (SensorState sensor : sensors) {

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
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(RemoteState.REMOTE_TYPE)];
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.location = new Vector(json.getJSONArray(RemoteState.LOCATION));
        this.battery = json.getDouble(RemoteState.BATTERY);
        this.sensors = new HashMap<>();
        if (json.hasKey(RemoteState.SENSORS)) {
            JSONArray jsonState = json.getJSONArray(RemoteState.SENSORS);
            for (int i = 0; i < jsonState.length(); i++) {
                SensorState state = new SensorState(jsonState.getJSONObject(i));
                this.sensors.put(state.getRemoteID(), state);
            }
        }
    }

    public String getLabel() {
        return String.format("%s%s", this.remoteID, this.type.getLabel());
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public abstract SerializableEnum getSpecType();

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteState.REMOTE_TYPE, this.type.getType());
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(RemoteState state) {
        return this.type.equals(state.type) && this.remoteID.equals(state.remoteID);
    }

}
