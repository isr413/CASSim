package com.seat.sim.common.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.util.Random;

/** A serializable class to store scenario configurations. */
public class ScenarioConfig extends Jsonable {
    public static final String GRID = "grid";
    public static final String MISSION_LENGTH = "mission_length";
    public static final String NUM_REMOTES = "num_remotes";
    public static final String REMOTE_CONFIG = "remote_config";
    public static final String SCENARIO_ID = "scenario_id";
    public static final String SEED = "seed";
    public static final String STEP_SIZE = "step_size";

    protected static final String DEFAULT_ID = "default";
    protected static final long DEFAULT_SEED = new Random().getSeed();

    private Optional<Grid> grid;
    private int missionLength;
    private List<RemoteConfig> remoteConfigs;
    private Map<String, RemoteConfig> remoteConfigsByID;
    private String scenarioID;
    private long seed;
    private double stepSize;

    public ScenarioConfig(int missionLength, double stepSize, Collection<RemoteConfig> remoteConfigs) {
        this(Optional.empty(), missionLength, stepSize, remoteConfigs);
    }

    public ScenarioConfig(String scenarioID, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(scenarioID, Optional.empty(), missionLength, stepSize, remoteConfigs);
    }

    public ScenarioConfig(long seed, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfigs) {
        this(seed, Optional.empty(), missionLength, stepSize, remoteConfigs);
    }

    public ScenarioConfig(String scenarioID, long seed, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(scenarioID, seed, Optional.empty(), missionLength, stepSize, remoteConfigs);
    }

    public ScenarioConfig(Grid grid, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfigs) {
        this(Optional.of(grid), missionLength, stepSize, remoteConfigs);
    }

    public ScenarioConfig(String scenarioID, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(scenarioID, Optional.of(grid), missionLength, stepSize, remoteConfigs);
    }

    public ScenarioConfig(long seed, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(seed, Optional.of(grid), missionLength, stepSize, remoteConfigs);
    }

    public ScenarioConfig(String scenarioID, long seed, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(scenarioID, seed, Optional.of(grid), missionLength, stepSize, remoteConfigs);
    }

    private ScenarioConfig(Optional<Grid> grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, grid, missionLength, stepSize, remoteConfigs);
    }

    private ScenarioConfig(String scenarioID, Optional<Grid> grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, grid, missionLength, stepSize, remoteConfigs);
    }

    private ScenarioConfig(long seed, Optional<Grid> grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this(ScenarioConfig.DEFAULT_ID, seed, grid, missionLength, stepSize, remoteConfigs);
    }

    private ScenarioConfig(String scenarioID, long seed, Optional<Grid> grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.grid = (grid != null) ? grid : Optional.empty();
        this.missionLength = missionLength;
        this.stepSize = stepSize;
        this.remoteConfigs = (remoteConfigs != null) ? new ArrayList<>(remoteConfigs) : new ArrayList<>();
        this.init();
    }

    public ScenarioConfig(Json json) throws JsonException {
        super(json);
    }

    private void init() {
        this.remoteConfigsByID = new HashMap<>();
        for (RemoteConfig config : this.remoteConfigs) {
            for (String remoteID : config.getRemoteIDs()) {
                this.remoteConfigsByID.put(remoteID, config);
            }
        }
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.scenarioID = json.getString(ScenarioConfig.SCENARIO_ID);
        this.seed = json.getLong(ScenarioConfig.SEED);
        this.grid = (json.hasKey(ScenarioConfig.GRID)) ? Optional.of(new Grid(json.getJson(ScenarioConfig.GRID)))
                : Optional.empty();
        this.missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        this.stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        this.remoteConfigs = (json.hasKey(ScenarioConfig.REMOTE_CONFIG))
                ? json.getJsonArray(ScenarioConfig.REMOTE_CONFIG).toList(Json.class).stream()
                        .map(config -> new RemoteConfig(config))
                        .collect(Collectors.toList())
                : new ArrayList<>();
        this.init();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(ScenarioConfig.SCENARIO_ID, this.scenarioID);
        json.put(ScenarioConfig.SEED, this.seed);
        if (this.hasGrid()) {
            json.put(ScenarioConfig.GRID, this.getGrid().toJson());
        }
        json.put(ScenarioConfig.MISSION_LENGTH, this.missionLength);
        json.put(ScenarioConfig.STEP_SIZE, this.stepSize);
        json.put(ScenarioConfig.NUM_REMOTES, this.getNumberOfRemotes());
        if (this.hasRemotes()) {
            json.put(
                    ScenarioConfig.REMOTE_CONFIG,
                    JsonBuilder.toJsonArray(
                            this.remoteConfigs.stream()
                                    .map(config -> config.toJson())
                                    .collect(Collectors.toList())));
        }
        return json;
    }

    public boolean equals(ScenarioConfig config) {
        if (config == null)
            return false;
        return this.scenarioID.equals(config.scenarioID) && this.seed == config.seed &&
                this.grid.equals(config.grid) && this.missionLength == config.missionLength &&
                this.stepSize == config.stepSize && this.remoteConfigs.equals(config.remoteConfigs);
    }

    public Grid getGrid() {
        return this.grid.get();
    }

    public int getGridHeight() {
        return this.getGrid().getHeight();
    }

    public int getGridWidth() {
        return this.getGrid().getWidth();
    }

    public int getMissionLength() {
        return this.missionLength;
    }

    public int getNumberOfRemotes() {
        return this.getRemoteIDs().size();
    }

    public Collection<RemoteConfig> getRemoteConfigs() {
        return this.remoteConfigs;
    }

    public Collection<RemoteConfig> getRemoteConfigsWithTag(String groupTag) {
        return this.getRemoteConfigs().stream()
                .filter(config -> config.hasRemoteWithTag(groupTag))
                .collect(Collectors.toList());
    }

    public RemoteConfig getRemoteConfigWithID(String remoteID) {
        return this.remoteConfigsByID.get(remoteID);
    }

    public Set<String> getRemoteIDs() {
        return this.remoteConfigsByID.keySet();
    }

    public String getScenarioID() {
        return this.scenarioID;
    }

    public long getSeed() {
        return this.seed;
    }

    public double getStepSize() {
        return this.stepSize;
    }

    public boolean hasGrid() {
        return this.grid.isPresent();
    }

    public boolean hasRemoteConfigWithID(String remoteID) {
        return this.remoteConfigsByID.containsKey(remoteID);
    }

    public boolean hasRemoteConfigWithTag(String groupTag) {
        return !this.getRemoteConfigsWithTag(groupTag).isEmpty();
    }

    public boolean hasRemoteMatch(Set<String> matchers) {
        for (String matcher : matchers) {
            if (this.hasRemoteConfigWithTag(matcher))
                return true;
        }
        return false;
    }

    public boolean hasRemotes() {
        return !this.getRemoteIDs().isEmpty();
    }

    public boolean hasScenarioID(String scenarioID) {
        if (scenarioID == null || this.scenarioID == null)
            return this.scenarioID == scenarioID;
        return this.scenarioID.equals(scenarioID);
    }

    public Json toJson() {
        return this.getJsonBuilder().toJson();
    }
}
