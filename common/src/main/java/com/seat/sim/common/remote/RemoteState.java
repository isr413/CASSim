package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorState;

/** A serializable snapshot of a Remote. */
public class RemoteState extends Jsonable {
    public static final String ACTIVE = "active";
    public static final String FUEL = "fuel";
    public static final String LOCATION = "location";
    public static final String REMOTE_GROUPS = "remote_groups";
    public static final String REMOTE_ID = "remote_id";
    public static final String VELOCITY = "velocity";

    private boolean active;
    private Optional<Double> fuel;
    private Optional<Vector> location;
    private Set<String> remoteGroups;
    private String remoteID;
    private Map<String, SensorState> sensorStates;
    private TeamColor team;
    private Optional<Vector> velocity;

    public RemoteState(String remoteID, Set<String> remoteGroups, TeamColor team, Collection<SensorState> sensorStates,
            boolean active) {
        this(remoteID, remoteGroups, team, null, null, null, sensorStates, active);
    }

    public RemoteState(String remoteID, Set<String> remoteGroups, TeamColor team, Vector location,
            Collection<SensorState> sensorStates, boolean active) {
        this(remoteID, remoteGroups, team, location, null, null, sensorStates, active);
    }

    public RemoteState(String remoteID, Set<String> remoteGroups, TeamColor team, Vector location, Vector velocity,
            Collection<SensorState> sensorStates, boolean active) {
        this(remoteID, remoteGroups, team, location, velocity, null, sensorStates, active);
    }

    public RemoteState(String remoteID, Set<String> remoteGroups, TeamColor team, double fuel,
            Collection<SensorState> sensorStates, boolean active) {
        this(remoteID, remoteGroups, team, null, null, fuel, sensorStates, active);
    }

    public RemoteState(String remoteID, Set<String> remoteGroups, TeamColor team, Vector location,
            double fuel, Collection<SensorState> sensorStates, boolean active) {
        this(remoteID, remoteGroups, team, location, null, fuel, sensorStates, active);
    }

    public RemoteState(String remoteID, Set<String> remoteGroups, TeamColor team, Vector location, Vector velocity,
            double fuel, Collection<SensorState> sensorStates, boolean active) {
        this(remoteID, remoteGroups, team, location, null, Optional.of(fuel), sensorStates, active);
    }

    private RemoteState(String remoteID, Set<String> remoteGroups, TeamColor team, Vector location, Vector velocity,
            Optional<Double> fuel, Collection<SensorState> sensorStates, boolean active) {
        this.remoteID = remoteID;
        this.remoteGroups = (remoteGroups != null) ? new HashSet<>(remoteGroups) : new HashSet<>();
        this.team = (team != null) ? team : TeamColor.NONE;
        this.location = (location != null) ? Optional.of(location) : Optional.empty();
        this.velocity = (velocity != null) ? Optional.of(velocity) : Optional.empty();
        this.fuel = (fuel != null) ? fuel : Optional.empty();
        this.sensorStates = (sensorStates != null)
                ? sensorStates.stream().collect(Collectors.toMap(SensorState::getSensorID, Function.identity()))
                : new HashMap<>();
        this.active = active;
    }

    public RemoteState(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.remoteGroups = (json.hasKey(RemoteState.REMOTE_GROUPS))
                ? new HashSet<>(json.getJsonArray(RemoteState.REMOTE_GROUPS).toList(String.class))
                : new HashSet<>();
        this.team = (json.hasKey(TeamColor.TEAM)) ? TeamColor.decodeType(json) : TeamColor.NONE;
        this.location = (json.hasKey(RemoteState.LOCATION))
                ? Optional.of(new Vector(json.getJson(RemoteState.LOCATION)))
                : Optional.empty();
        this.velocity = (json.hasKey(RemoteState.VELOCITY))
                ? Optional.of(new Vector(json.getJson(RemoteState.VELOCITY)))
                : Optional.empty();
        this.fuel = (json.hasKey(RemoteState.FUEL)) ? Optional.of(json.getDouble(RemoteState.FUEL)) : Optional.empty();
        this.active = json.getBoolean(RemoteState.ACTIVE);
        this.sensorStates = (json.hasKey(RemoteProto.SENSORS))
                ? json.getJsonArray(RemoteProto.SENSORS).toList(Json.class).stream()
                        .map(state -> new SensorState(state))
                        .collect(Collectors.toMap(SensorState::getSensorID, Function.identity()))
                : new HashMap<>();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        if (this.hasRemoteGroups()) {
            json.put(RemoteState.REMOTE_GROUPS, JsonBuilder.toJsonArray(this.remoteGroups));
        }
        if (this.hasTeam()) {
            json.put(TeamColor.TEAM, this.team.getType());
        }
        if (this.hasLocation()) {
            json.put(RemoteState.LOCATION, this.getLocation().toJson());
        }
        if (this.isMobile()) {
            json.put(RemoteState.VELOCITY, this.getVelocity().toJson());
        }
        if (this.hasFuel()) {
            json.put(RemoteState.FUEL, this.getFuel());
        }
        json.put(RemoteState.ACTIVE, this.active);
        if (this.hasSensors()) {
            json.put(
                    RemoteProto.SENSORS,
                    JsonBuilder.toJsonArray(
                            this.sensorStates.values().stream()
                                    .map(sensorState -> sensorState.toJson())
                                    .collect(Collectors.toList())));
        }
        return json;
    }

    public boolean equals(RemoteState state) {
        if (state == null)
            return false;
        return this.remoteID.equals(state.remoteID) && this.team.equals(state.team) &&
                this.location.equals(state.location) && this.velocity.equals(state.velocity) &&
                this.fuel.equals(state.fuel) &&
                this.active == state.active && this.sensorStates.equals(state.sensorStates);
    }

    public double getFuel() {
        return this.fuel.get();
    }

    public String getLabel() {
        return String.format("%s:<remote>", this.remoteID);
    }

    public Vector getLocation() {
        return this.location.get();
    }

    public Set<String> getRemoteGroups() {
        return this.remoteGroups;
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public Set<String> getSensorIDs() {
        return this.sensorStates.keySet();
    }

    public Collection<SensorState> getSensorStates() {
        return this.sensorStates.values();
    }

    public Collection<SensorState> getSensorStatesWithModel(String sensorModel) {
        return this.getSensorStates().stream()
                .filter(sensorState -> sensorState.hasSensorModel(sensorModel))
                .collect(Collectors.toList());
    }

    public Collection<SensorState> getSensorStatesWithTag(String groupTag) {
        return this.getSensorStates().stream()
                .filter(sensorState -> sensorState.hasSensorGroupWithTag(groupTag))
                .collect(Collectors.toList());
    }

    public SensorState getSensorStateWithID(String sensorID) {
        return this.sensorStates.get(sensorID);
    }

    public TeamColor getTeam() {
        return this.team;
    }

    public Vector getVelocity() {
        return this.velocity.get();
    }

    public boolean hasFuel() {
        return this.fuel.isPresent();
    }

    public boolean hasLocation() {
        return this.location.isPresent();
    }

    public boolean hasRemoteGroups() {
        return !this.remoteGroups.isEmpty();
    }

    public boolean hasRemoteGroupWithTag(String groupTag) {
        return this.remoteGroups.contains(groupTag);
    }

    public boolean hasRemoteID(String remoteID) {
        if (remoteID == null || this.remoteID == null)
            return this.remoteID == remoteID;
        return this.remoteID.equals(remoteID);
    }

    public boolean hasRemoteMatch(Set<String> matchers) {
        for (String matcher : matchers) {
            if (this.hasRemoteGroupWithTag(matcher))
                return true;
        }
        return false;
    }

    public boolean hasSensorMatch(Set<String> matchers) {
        for (String matcher : matchers) {
            if (this.hasSensorStateWithTag(matcher))
                return true;
        }
        return false;
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

    public boolean hasSensorStateWithTag(String groupTag) {
        return !this.getSensorStatesWithTag(groupTag).isEmpty();
    }

    public boolean hasTeam() {
        return !this.team.equals(TeamColor.NONE);
    }

    public boolean isActive() {
        return this.active && this.isEnabled();
    }

    public boolean isEnabled() {
        return !this.hasFuel() || this.getFuel() > 0;
    }

    public boolean isInMotion() {
        return this.isMobile() && this.getVelocity().getMagnitude() > 0;
    }

    public boolean isMobile() {
        return this.velocity.isPresent();
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
