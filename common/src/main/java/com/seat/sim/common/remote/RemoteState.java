package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private Map<String, SensorState> sensorStates;
    private TeamColor team;

    public RemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active) {
        this(remoteID, team, location, battery, active, null);
    }

    public RemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensorStates) {
        this.remoteID = remoteID;
        this.team = team;
        this.location = location;
        this.battery = battery;
        this.active = active;
        this.sensorStates = sensorStates.stream()
            .collect(Collectors.toMap(SensorState::getSensorID, Function.identity()));
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
        this.sensorStates = new HashMap<>();
        if (json.hasKey(RemoteProto.SENSORS)) {
            JSONArray jsonSensorStates = json.getJSONArray(RemoteProto.SENSORS);
            for (int i = 0; i < jsonSensorStates.length(); i++) {
                SensorState sensorState = SensorRegistry.decodeTo(jsonSensorStates.getJSONOption(i), SensorState.class);
                this.sensorStates.put(sensorState.getSensorID(), sensorState);
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
            JSONArrayBuilder jsonSensorStates = JSONBuilder.Array();
            for (SensorState sensorState : this.sensorStates.values()) {
                jsonSensorStates.put(sensorState.toJSON());
            }
            json.put(RemoteProto.SENSORS, jsonSensorStates.toJSON());
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

    public Collection<String> getSensorIDs() {
        return this.sensorStates.keySet();
    }

    public Collection<SensorState> getSensorStates() {
        return this.sensorStates.values();
    }

    public Collection<SensorState> getSensorStatesWithModel(String sensorModel) {
        return this.getSensorStates().stream()
            .filter(sensorState -> sensorState.getSensorModel().equals(sensorModel))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T extends SensorState> Collection<T> getSensorStatesWithType(Class<? extends T> classType) {
        return this.getSensorStates().stream()
            .filter(sensorState -> classType.isAssignableFrom(sensorState.getClass()))
            .map(sensorState -> (T) sensorState)
            .collect(Collectors.toList());
    }

    public SensorState getSensorStateWithID(String sensorID) throws CommonException {
        if (!this.hasSensorStateWithID(sensorID)) {
            throw new CommonException(String.format("Remote %s has no sensor %s", this.remoteID, sensorID));
        }
        return this.sensorStates.get(sensorID);
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public TeamColor getTeam() {
        return this.team;
    }

    public boolean hasSensors() {
        return !this.sensorStates.isEmpty();
    }

    public boolean hasSensorStateWithID(String sensorID) {
        return this.sensorStates.containsKey(sensorID);
    }

    public boolean hasSensorStateWithModel(String sensorModel) {
        return !this.getSensorStatesWithModel(sensorModel).isEmpty();
    }

    public boolean hasSensorStateWithType(Class<? extends SensorState> classType) {
        return !this.getSensorStatesWithType(classType).isEmpty();
    }

    public boolean hasTeam() {
        return !(this.team == null || this.team.equals(TeamColor.NONE));
    }

    public boolean isActive() {
        return this.active && this.isEnabled();
    }

    public boolean isAerial() {
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isEnabled() {
        return this.battery > 0;
    }

    public boolean isGround() {
        return !this.isAerial();
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public boolean isMobile() {
        return false;
    }

    public boolean isStationary() {
        return !this.isMobile();
    }

    public JSONOption toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteState state) {
        if (state == null) return false;
        return this.getRemoteType().equals(state.getRemoteType()) && this.remoteID.equals(state.remoteID) &&
            this.team.equals(state.team) && this.location.equals(state.location) && this.battery == state.battery &&
            this.active == state.active && this.sensorStates.equals(state.sensorStates);
    }

}
