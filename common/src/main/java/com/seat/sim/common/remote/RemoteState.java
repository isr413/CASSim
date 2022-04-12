package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorRegistry;
import com.seat.sim.common.sensor.SensorState;

/** A serializable snapshot of a Remote. */
public class RemoteState extends JSONAble {
    public static final String ACTIVE = "active";
    public static final String BATTERY = "battery";
    public static final String LOCATION = "location";
    public static final String REMOTE_ID = "remote_id";

    private boolean active;
    private double battery;
    private Vector location;
    private String remoteID;
    private HashMap<String, SensorState> sensors;
    private TeamColor team;

    public RemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active) {
        this(remoteID, team, location, battery, active, null);
    }

    public RemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors) {
        this.remoteID = remoteID;
        this.team = team;
        this.location = location;
        this.battery = battery;
        this.active = active;
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
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.team = (json.hasKey(TeamColor.TEAM)) ?
            TeamColor.decodeType(json) :
            TeamColor.NONE;
        this.location = new Vector(json.getJSONOption(RemoteProto.LOCATION));
        this.battery = json.getDouble(RemoteState.BATTERY);
        this.active = json.getBoolean(RemoteState.ACTIVE);
        this.sensors = new HashMap<>();
        if (json.hasKey(RemoteProto.SENSORS)) {
            JSONArray jsonState = json.getJSONArray(RemoteProto.SENSORS);
            for (int i = 0; i < jsonState.length(); i++) {
                SensorState state = SensorRegistry.decodeTo(jsonState.getJSONOption(i), SensorState.class);
                this.sensors.put(state.getSensorID(), state);
            }
        }
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteRegistry.REMOTE_TYPE, this.getRemoteType());
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        if (this.hasTeam()) {
            json.put(TeamColor.TEAM, this.team.getType());
        }
        json.put(RemoteProto.LOCATION, this.location.toJSON());
        json.put(RemoteState.BATTERY, this.battery);
        json.put(RemoteState.ACTIVE, this.active);
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
        return String.format("%s:<remote>", this.remoteID);
    }

    public Vector getLocation() {
        return this.location;
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public String getRemoteType() {
        return this.getClass().getName();
    }

    public Collection<SensorState> getSensors() {
        return new ArrayList<SensorState>(this.sensors.values());
    }

    public Collection<SensorState> getSensorsWithModel(String sensorModel) {
        ArrayList<SensorState> sensors = new ArrayList<>();
        for (SensorState state : this.sensors.values()) {
            if (state.getSensorModel().equals(sensorModel)) {
                sensors.add(state);
            }
        }
        return sensors;
    }

    public Collection<SensorState> getSensorsWithType(Class<? extends SensorState> classType) {
        ArrayList<SensorState> sensors = new ArrayList<>();
        for (SensorState state : this.sensors.values()) {
            if (classType.isAssignableFrom(state.getClass())) {
                sensors.add(state);
            }
        }
        return sensors;
    }

    public SensorState getSensorWithID(String sensorID) throws CommonException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new CommonException(String.format("No sensor %s found on remote %s", sensorID, this.remoteID));
        }
        return this.sensors.get(sensorID);
    }

    public TeamColor getTeam() {
        return this.team;
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensors.containsKey(sensorID);
    }

    public boolean hasSensorWithModel(String sensorModel) {
        for (SensorState state : this.sensors.values()) {
            if (state.getSensorModel().equals(sensorModel)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensorWithType(Class<? extends SensorState> classType) {
        for (SensorState state : this.sensors.values()) {
            if (classType.isAssignableFrom(state.getClass())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasTeam() {
        return !(this.team == null || this.team.equals(TeamColor.NONE));
    }

    public boolean isActive() {
        return this.active && this.isEnabled();
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isEnabled() {
        return this.battery > 0;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public JSONOption toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteState state) {
        if (state == null) return false;
        return this.getRemoteType().equals(state.getRemoteType()) && this.remoteID.equals(state.remoteID) &&
            this.team.equals(state.team) && this.location.equals(state.location) && this.battery == state.battery &&
            this.active == state.active && this.sensors.equals(state.sensors);
    }

}
