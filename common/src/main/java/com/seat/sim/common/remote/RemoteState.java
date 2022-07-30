package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorRegistry;
import com.seat.sim.common.sensor.SensorState;

/** A serializable snapshot of a Remote. */
public class RemoteState extends JSONAble {
    public static final String ACTIVE = "active";
    public static final String FUEL = "fuel";
    public static final String LOCATION = "location";
    public static final String REMOTE_ID = "remote_id";
    public static final String VELOCITY = "velocity";

    private boolean active;
    private double fuel;
    private Vector location;
    private String remoteID;
    private Map<String, SensorState> sensorStates;
    private TeamColor team;
    private Vector velocity;

    public RemoteState(String remoteID, TeamColor team, Vector location, Vector velocity, double fuel, boolean active,
            Collection<SensorState> sensorStates) {
        this.remoteID = remoteID;
        this.team = (team != null) ? team : TeamColor.NONE;
        this.location = location;
        this.velocity = velocity;
        this.fuel = fuel;
        this.active = active;
        this.sensorStates = (sensorStates != null) ?
            sensorStates.stream().collect(Collectors.toMap(SensorState::getSensorID, Function.identity())) :
            new HashMap<>();
    }

    public RemoteState(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.team = (json.hasKey(TeamColor.TEAM)) ?
            TeamColor.decodeType(json) :
            TeamColor.NONE;
        this.location = (json.hasKey(RemoteState.LOCATION)) ?
            new Vector(json.getJSONOptional(RemoteState.LOCATION)) :
            null;
        this.velocity = (json.hasKey(RemoteState.VELOCITY)) ?
            new Vector(json.getJSONOptional(RemoteState.VELOCITY)) :
            null;
        this.fuel = (json.hasKey(RemoteState.FUEL)) ?
            json.getDouble(RemoteState.FUEL) :
            Double.POSITIVE_INFINITY;
        this.active = json.getBoolean(RemoteState.ACTIVE);
        this.sensorStates = (json.hasKey(RemoteProto.SENSORS)) ?
            json.getJSONArray(RemoteProto.SENSORS).toList(JSONOptional.class).stream()
                .map(optional -> SensorRegistry.decodeTo(SensorState.class, optional))
                .collect(Collectors.toMap(SensorState::getSensorID, Function.identity())) :
            new HashMap<>();
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteRegistry.REMOTE_TYPE, this.getRemoteType());
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        if (this.hasTeam()) {
            json.put(TeamColor.TEAM, this.team.getType());
        }
        if (this.hasLocation()) {
            json.put(RemoteState.LOCATION, this.location.toJSON());
        }
        if (this.hasVelocity()) {
            json.put(RemoteState.VELOCITY, this.velocity.toJSON());
        }
        if (this.hasFiniteFuel()) {
            json.put(RemoteState.FUEL, this.fuel);
        }
        json.put(RemoteState.ACTIVE, this.active);
        if (this.hasSensors()) {
            json.put(
                RemoteProto.SENSORS,
                JSONBuilder.Array(
                    this.sensorStates.values().stream()
                        .map(sensorState -> sensorState.toJSON())
                        .collect(Collectors.toList())
                ).toJSON()
            );
        }
        return json;
    }

    public double getFuel() {
        return this.fuel;
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

    public Set<String> getSensorIDs() {
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
    public <T extends SensorState> Collection<T> getSensorStatesWithType(Class<T> cls) {
        return this.getSensorStates().stream()
            .filter(sensorState -> cls.isAssignableFrom(sensorState.getClass()))
            .map(sensorState -> (T) sensorState)
            .collect(Collectors.toList());
    }

    public SensorState getSensorStateWithID(String sensorID) throws CommonException {
        if (!this.hasSensorStateWithID(sensorID)) {
            throw new CommonException(String.format("Remote %s has no sensor %s", this.remoteID, sensorID));
        }
        return this.sensorStates.get(sensorID);
    }

    public TeamColor getTeam() {
        return this.team;
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public boolean hasFiniteFuel() {
        return this.fuel != Double.POSITIVE_INFINITY;
    }

    public boolean hasLocation() {
        return this.location != null;
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

    public boolean hasSensorStateWithType(Class<? extends SensorState> cls) {
        return !this.getSensorStatesWithType(cls).isEmpty();
    }

    public boolean hasTeam() {
        return !this.team.equals(TeamColor.NONE);
    }

    public boolean hasVelocity() {
        return this.velocity != null;
    }

    public boolean isActive() {
        return this.active && this.isEnabled();
    }

    public boolean isEnabled() {
        return this.fuel > 0;
    }

    public boolean isInMotion() {
        return this.isActive() && this.isMobile() && this.getVelocity().getMagnitude() > 0;
    }

    public boolean isMobile() {
        return this.isActive() && this.hasVelocity();
    }

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteState state) {
        if (state == null) return false;
        return this.getRemoteType().equals(state.getRemoteType()) && this.remoteID.equals(state.remoteID) &&
            this.team.equals(state.team) && this.location.equals(state.location) &&
            this.velocity.equals(state.velocity) && this.fuel == state.fuel &&
            this.active == state.active && this.sensorStates.equals(state.sensorStates);
    }

}
