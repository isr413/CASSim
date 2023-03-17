package com.seat.sim.client.sandbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.seat.sim.common.core.Application;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.math.ZoneType;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.kinematics.FuelProto;
import com.seat.sim.common.remote.kinematics.KinematicsProto;
import com.seat.sim.common.remote.kinematics.MotionProto;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorProto;
import com.seat.sim.common.sensor.SensorStats;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Logger;
import com.seat.sim.common.util.Random;
import com.seat.sim.common.util.Range;

public class SmgWalkNeighbors implements Application {

  // Scenario info
  private static final String SCENARIO_ID = "SmgWalkNeighbors";
  private static final int NUM_TURNS = 81;
  private static final int TURN_LENGTH = 20;
  private static final int MISSION_LENGTH = SmgWalkNeighbors.NUM_TURNS * SmgWalkNeighbors.TURN_LENGTH;
  private static final double STEP_SIZE = 20.;

  // Grid info
  private static final int GRID_SIZE = 64;
  private static final int ZONE_SIZE = 14;

  private static final Vector GRID_CENTER = new Vector(
      SmgWalkNeighbors.GRID_SIZE * SmgWalkNeighbors.ZONE_SIZE / 2.,
      SmgWalkNeighbors.GRID_SIZE * SmgWalkNeighbors.ZONE_SIZE / 2.
    );

  // Base info
  private static final String BASE_TAG = "Base";
  private static final String BASE_LRC_TAG = "Base_LRC";
  private static final TeamColor BASE_COLOR = TeamColor.GREEN;
  private static final int BASE_COUNT = 1;

  // Drone info
  private static final String DRONE_TAG = "Drone";
  private static final String DRONE_LRC_TAG = "Drone_LRC";
  private static final String DRONE_BLE_TAG = "Drone_BLE";
  private static final TeamColor DRONE_COLOR = TeamColor.BLUE;
  private static final int DRONE_COUNT = 32;
  private static final Vector DRONE_FUEL_USAGE = new Vector(0.0001, 0.0001, 0.0001);
  private static final Vector DRONE_INITIAL_VELOCITY = Vector.ZERO;
  private static final double DRONE_SCAN_VELOCITY = 6.7;
  private static final double DRONE_SCAN_ACCELERATION = 1.4;
  private static final double DRONE_MAX_VELOCITY = 20.;
  private static final double DRONE_MAX_ACCELERATION = 3.;

  // Victim info
  private static final String VICTIM_TAG = "Victim";
  private static final String VICTIM_BLE_TAG = "Victim_BLE";
  private static final TeamColor VICTIM_COLOR = TeamColor.RED;
  private static final int VICTIM_COUNT = 1024;
  private static final Vector VICTIM_INITIAL_VELOCITY = Vector.ZERO;
  private static final double VICTIM_MAX_VELOCITY = 1.4;
  private static final double VICTIM_MAX_ACCELERATION = 2.5;

  // Sensors
  private static final String HUMAN_VISION = "Human_Vision";
  private static final double HUMAN_VISION_RANGE = 10.;
  private static final double BASE_VISION_RANGE = 20.;

  public static final String LONG_RANGE_COMMS = "Long_Range_Comms";
  public static final double LRC_BATT_USAGE = 0.0002;

  public static final String DRONE_CAMERA = "Drone_Camera";
  public static final double CAM_BATT_USAGE = 0.0002;
  public static final double CAM_RANGE = 10.;

  public static final String BLE_COMMS = "BLE_Comms";
  public static final double BLE_BATT_USAGE = 0.0001;
  public static final double BLE_RANGE = 10.;

  // Tunable params
  private static final Range VICTIM_STOP_PROBABILITY_ALPHA = new Range(0., 0.1, 1., true);
  private static final Range DRONE_DISCOVERY_PROBABILITY_BETA = new Range(0.1, 0.1, 1., true);
  private static final Range DRONE_DISCOVERY_PROBABILITY_GAMMA = new Range(0.1, 0.1, 1., true);
  private static final int TRIALS_PER = 1;
 
  private Range alpha;
  private Range beta;
  private Range gamma;
  private Set<String> doneRemotes;
  private Map<String, Zone> droneAssignment;
  private Grid grid;
  private Logger logger;
  private Random rng;
  private List<RemoteConfig> remotes;
  private long seed;
  private Iterator<Long> seeds;
  private int totalTrials;
  private int trials;
  private double[][] zones;

  public SmgWalkNeighbors(ArgsParser args, int threadID, long seed) throws IOException {
    this.logger = (threadID > 0) ? new Logger(String.format("%s_%d", SmgWalkNeighbors.SCENARIO_ID, threadID))
        : new Logger(SmgWalkNeighbors.SCENARIO_ID);
    this.alpha = new Range(SmgWalkNeighbors.VICTIM_STOP_PROBABILITY_ALPHA);
    this.beta = new Range(SmgWalkNeighbors.DRONE_DISCOVERY_PROBABILITY_BETA);
    this.gamma = new Range(SmgWalkNeighbors.DRONE_DISCOVERY_PROBABILITY_GAMMA);
    this.grid = new Grid(
        SmgWalkNeighbors.GRID_SIZE,
        SmgWalkNeighbors.GRID_SIZE,
        SmgWalkNeighbors.ZONE_SIZE
      );
    this.doneRemotes = new HashSet<>();
    this.droneAssignment = new HashMap<>();
    this.init(threadID, seed);
  }

  private void init(int threadID, long seed) throws IOException {
    this.loadSeeds();
    this.setTrials(threadID);
    this.setSeed();
    while (seed > 0 && this.seed != seed) {
      this.reset();
    }
    this.loadRemotes();
    this.loadZones();
  }

  private void loadZones() {
    this.zones = new double[this.grid.getHeightInZones()][this.grid.getWidthInZones()];
    for (int i = 0; i < this.zones.length; i++) {
      for (int j = 0; j < this.zones[i].length; j++) {
        this.zones[i][j] = 1. / (this.grid.getHeightInZones() * this.grid.getWidthInZones());
      }
    }
  }

  private void loadRemotes() {
    this.remotes = List.of(
        new RemoteConfig(
            new RemoteProto(
                Set.of(SmgWalkNeighbors.BASE_TAG),
                List.of(
                    new SensorConfig(
                        new SensorProto(
                            SmgWalkNeighbors.HUMAN_VISION,
                            Set.of(SmgWalkNeighbors.BASE_TAG),
                            Set.of(SmgWalkNeighbors.VICTIM_TAG),
                            new SensorStats(0., 1., 0., SmgWalkNeighbors.BASE_VISION_RANGE)
                          ),
                        1,
                        true
                      ),
                    new SensorConfig(
                        new SensorProto(
                            SmgWalkNeighbors.LONG_RANGE_COMMS,
                            Set.of(SmgWalkNeighbors.BASE_LRC_TAG),
                            Set.of(SmgWalkNeighbors.DRONE_LRC_TAG),
                            new SensorStats(SmgWalkNeighbors.LRC_BATT_USAGE, 1., 0.)
                          ),
                        1,
                        true
                      )
                  ),
                new KinematicsProto(SmgWalkNeighbors.GRID_CENTER)
              ),
            SmgWalkNeighbors.BASE_COLOR,
            SmgWalkNeighbors.BASE_COUNT,
            true,
            false
          ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(SmgWalkNeighbors.DRONE_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              SmgWalkNeighbors.DRONE_CAMERA,
                              Set.of(SmgWalkNeighbors.DRONE_TAG),
                              Set.of(SmgWalkNeighbors.VICTIM_TAG),
                              new SensorStats(
                                  SmgWalkNeighbors.CAM_BATT_USAGE,
                                  this.beta.getStart(),
                                  0.,
                                  SmgWalkNeighbors.CAM_RANGE
                                )
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              SmgWalkNeighbors.LONG_RANGE_COMMS,
                              Set.of(SmgWalkNeighbors.DRONE_LRC_TAG),
                              Set.of(SmgWalkNeighbors.BASE_LRC_TAG),
                              new SensorStats(SmgWalkNeighbors.LRC_BATT_USAGE, 1., 0.)
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              SmgWalkNeighbors.BLE_COMMS,
                              Set.of(SmgWalkNeighbors.DRONE_BLE_TAG),
                              Set.of(SmgWalkNeighbors.VICTIM_BLE_TAG),
                              new SensorStats(
                                  SmgWalkNeighbors.BLE_BATT_USAGE,
                                  this.gamma.getStart(),
                                  0.,
                                  SmgWalkNeighbors.BLE_RANGE
                                )
                            ),
                          1,
                          true
                        )
                    ),
                  new KinematicsProto(
                      SmgWalkNeighbors.GRID_CENTER,
                      new FuelProto(1., 1., SmgWalkNeighbors.DRONE_FUEL_USAGE),
                      new MotionProto(
                          SmgWalkNeighbors.DRONE_INITIAL_VELOCITY,
                          SmgWalkNeighbors.DRONE_MAX_VELOCITY,
                          SmgWalkNeighbors.DRONE_MAX_ACCELERATION
                        )
                    )
                ),
              SmgWalkNeighbors.DRONE_COLOR,
              SmgWalkNeighbors.DRONE_COUNT,
              true,
              true
            ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(SmgWalkNeighbors.VICTIM_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              SmgWalkNeighbors.HUMAN_VISION,
                              Set.of(SmgWalkNeighbors.VICTIM_TAG),
                              Set.of(SmgWalkNeighbors.BASE_TAG),
                              new SensorStats(0., 1., 0., SmgWalkNeighbors.HUMAN_VISION_RANGE)
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              SmgWalkNeighbors.BLE_COMMS,
                              Set.of(SmgWalkNeighbors.VICTIM_BLE_TAG),
                              Set.of(SmgWalkNeighbors.DRONE_BLE_TAG),
                              new SensorStats(
                                  SmgWalkNeighbors.BLE_BATT_USAGE,
                                  this.gamma.getStart(),
                                  0.,
                                  SmgWalkNeighbors.BLE_RANGE
                                )
                            ),
                          1,
                          true
                        )
                    ),
                  new KinematicsProto(
                      null,
                      null,
                      new MotionProto(
                          SmgWalkNeighbors.VICTIM_INITIAL_VELOCITY,
                          SmgWalkNeighbors.VICTIM_MAX_VELOCITY,
                          SmgWalkNeighbors.VICTIM_MAX_ACCELERATION
                        )
                    )
                ),
              SmgWalkNeighbors.VICTIM_COLOR,
              SmgWalkNeighbors.VICTIM_COUNT,
              true,
              true
            )
      );
  }

  private void loadSeeds() throws IOException {
    this.seeds = Files.lines(Path.of("config/seeds.txt"))
      .mapToLong(Long::parseLong)
      .boxed()
      .collect(Collectors.toList())
      .iterator();
  }

  private void reset(boolean skip) {
    this.trials++;
    if (this.trials % SmgWalkNeighbors.TRIALS_PER == 0) {
      this.gamma.update();
    }
    if (this.gamma.isDone()) {
      this.beta.update();
      this.gamma = new Range(SmgWalkNeighbors.DRONE_DISCOVERY_PROBABILITY_GAMMA);
    }
    if (this.beta.isDone()) {
      this.alpha.update();
      this.beta = new Range(SmgWalkNeighbors.DRONE_DISCOVERY_PROBABILITY_BETA);
    }
    if (this.alpha.isDone()) {
      this.alpha = new Range(SmgWalkNeighbors.VICTIM_STOP_PROBABILITY_ALPHA);
    }
    this.setSeed(skip);
    if (!skip) {
      this.doneRemotes = new HashSet<>();
      this.droneAssignment = new HashMap<>();
      this.loadRemotes();
      this.loadZones();
    }
  }

  private void setSeed() {
    this.setSeed(false);
  }

  private void setSeed(boolean skip) {
    if (this.seeds.hasNext()) {
      this.seed = this.seeds.next();
    } else {
      this.seed = this.rng.getRandomNumber(Integer.MAX_VALUE);
    }
    this.rng = new Random(this.seed);
    if (!skip) {
      this.logger.log(-1.,
          String.format("Seed %d - ALPHA %.2f - BETA %.2f - GAMMA %.2f", this.rng.getSeed(), this.alpha.getStart(),
              this.beta.getStart(), this.gamma.getStart()));
    }
  }

  private void setTrials(int threadID) {
    this.totalTrials = SmgWalkNeighbors.VICTIM_STOP_PROBABILITY_ALPHA.points() *
        SmgWalkNeighbors.DRONE_DISCOVERY_PROBABILITY_BETA.points() *
        SmgWalkNeighbors.DRONE_DISCOVERY_PROBABILITY_GAMMA.points() * SmgWalkNeighbors.TRIALS_PER;
    this.trials = 0;
    if (threadID > 0) {
      this.totalTrials /= 4;
      for (int t = 0; t < (threadID - 1) * this.totalTrials; t++) {
        this.skip();
      }
    }
  }

  private void skip() {
    this.reset(true);
  }

  protected Zone getBestNeighbor(Vector location) {
    List<Zone> neighbors = this.grid.getNeighborhood(location);
    Collections.sort(neighbors, new Comparator<Zone>() {
      @Override
      public int compare(Zone z1, Zone z2) {
        return -Double.compare(
            SmgWalkNeighbors.this.getNeighborhoodWeight(z1.getLocation()),
            SmgWalkNeighbors.this.getNeighborhoodWeight(z2.getLocation())
          );
      }
    });
    return neighbors.get(0);
  }

  protected double getNeighborhoodWeight(Vector location) {
    return this.grid.getNeighborhood(location)
      .stream()
      .mapToDouble(zone -> this.getWeight(zone.getLocation()))
      .sum();
  }

  protected double getWeight(Vector location) {
    double x = location.getX(), y = location.getY(), size = this.grid.getZoneSize();
    return this.zones[(int) (y / size)][(int) (x / size)];
  }

  protected boolean shouldReturnHome(Snapshot snap, RemoteState state) {
    if (!state.hasTag(SmgWalkNeighbors.DRONE_TAG)) {
      return false;
    }
    if (!state.hasLocation() || !this.grid.hasZoneAtLocation(state.getLocation())) {
      return snap.getTime() + SmgWalkNeighbors.TURN_LENGTH >= SmgWalkNeighbors.MISSION_LENGTH;
    }
    return !this.validChoice(snap, state, this.grid.getZoneAtLocation(state.getLocation()));
  }

  protected Optional<Vector> randomWalk(Snapshot snap, RemoteState state, boolean toZoneCenter) {
    if (!state.hasLocation() || !this.grid.hasZoneAtLocation(state.getLocation())) {
      return Optional.empty();
    }
    Zone zone = this.grid.getZoneAtLocation(state.getLocation());
    List<Zone> neighborhood = this.grid.getNeighborhood(zone);
    Collections.shuffle(neighborhood, this.rng.getRng());
    Optional<Zone> nextZone = neighborhood
      .stream()
      .filter(neighbor -> neighbor != zone)
      .filter(neighbor -> !neighbor.hasZoneType(ZoneType.BLOCKED))
      .filter(neighbor -> state.hasTag(SmgWalkNeighbors.VICTIM_TAG) || this.validChoice(snap, state, zone))
      .findFirst();
    if (nextZone.isEmpty()) {
      return Optional.empty();
    }
    if (toZoneCenter) {
      return Optional.of(nextZone.get().getLocation());
    }
    Vector nextLocation = new Vector(
        this.rng.getRandomPoint(
            nextZone.get().getLocation().getX() - nextZone.get().getSize() / 2.,
            nextZone.get().getLocation().getX() + nextZone.get().getSize() / 2.
          ),
        this.rng.getRandomPoint(
            nextZone.get().getLocation().getY() - nextZone.get().getSize() / 2.,
            nextZone.get().getLocation().getY() + nextZone.get().getSize() / 2.
          )
      );
    return Optional.of(nextLocation);
  }

  protected void scanZone(Zone zone, double weight) {
    int i = (int) (zone.getLocation().getY() / this.grid.getZoneSize());
    int j = (int) (zone.getLocation().getX() / this.grid.getZoneSize());
    this.zones[i][j] *= weight;
  }

  protected double timeToCrossDistance(double dist, double speed, double maxSpeed, double acceleration) {
    double timeToBrake = (speed > 0.) ? speed / acceleration : 0.;
    double distToBrake = speed * timeToBrake - 0.5 * acceleration * timeToBrake * timeToBrake;
    if (distToBrake >= dist) {
      return timeToBrake;
    }
    double timeToTopSpeed = (speed < maxSpeed) ? (maxSpeed - speed) / acceleration : 0.;
    double distToTopSpeed = speed * timeToTopSpeed + 0.5 * acceleration * timeToTopSpeed * timeToTopSpeed;
    double maxTimeToBrake = maxSpeed / acceleration;
    double maxDistToBrake = maxSpeed * maxTimeToBrake - 0.5 * acceleration * maxTimeToBrake * maxTimeToBrake;
    if (distToTopSpeed + maxDistToBrake > dist) {
      double timeToCover = (Math.sqrt(4 * acceleration * (dist - distToBrake) / 2. + speed * speed) - speed) /
          (2. * acceleration);
      return 2 * timeToCover + timeToBrake;
    }
    double timeToCover = (dist - distToTopSpeed - maxDistToBrake) / maxSpeed;
    return timeToTopSpeed + timeToCover + maxTimeToBrake;
  }

  protected void updateZones() {
    double[][] tmp = this.zones;
    this.zones = new double[this.grid.getHeightInZones()][this.grid.getWidthInZones()];
    for (int i = 0; i < this.zones.length; i++) {
      for (int j = 0; j < this.zones[i].length; j++) {
        double weight = 0.;
        if (i > 0) {
          if (j > 0) {
            weight += tmp[i - 1][j - 1];
          }
          weight += tmp[i - 1][j];
          if (j < this.grid.getWidthInZones() - 1) {
            weight += tmp[i - 1][j + 1];
          }
        }
        if (j > 0) {
          weight += tmp[i][j - 1];
        }
        if (j < this.grid.getWidthInZones() - 1) {
          weight += tmp[i][j + 1];
        }
        if (i < this.grid.getHeightInZones() - 1) {
          if (j > 0) {
            weight += tmp[i + 1][j - 1];
          }
          weight += tmp[i + 1][j];
          if (j < this.grid.getWidthInZones() - 1) {
            weight += tmp[i + 1][j + 1];
          }
        }
        double numNeighbors = 8.;
        if ((i == 0 || i == this.grid.getHeightInZones() - 1) && (j == 0 || j == this.grid.getWidthInZones() - 1)) {
          numNeighbors = 3.;
        } else if (i == 0 || i == this.grid.getHeightInZones() - 1 || j == 0 || j == this.grid.getWidthInZones() - 1) {
          numNeighbors = 5.;
        }
        this.zones[i][j] = (tmp[i][j] * this.alpha.getStart()) +
            (weight * (1. - this.alpha.getStart()) * 1. / numNeighbors);
      }
    }
  }

  protected boolean validChoice(Snapshot snap, RemoteState state, Zone zone) {
    double timeToMove = Math.max(
        this.timeToCrossDistance(
            Vector.dist(state.getLocation(), zone.getLocation()),
            state.getSpeed(),
            SmgWalkNeighbors.DRONE_MAX_VELOCITY,
            SmgWalkNeighbors.DRONE_MAX_ACCELERATION
          ),
        SmgWalkNeighbors.TURN_LENGTH
      );
    double timeToGoHome = Math.max(
        this.timeToCrossDistance(
          Vector.dist(zone.getLocation(), SmgWalkNeighbors.GRID_CENTER),
          0.,
          SmgWalkNeighbors.DRONE_MAX_VELOCITY,
          SmgWalkNeighbors.DRONE_MAX_ACCELERATION
        ),
        SmgWalkNeighbors.TURN_LENGTH
      );
    if ((timeToMove + timeToGoHome) >= (SmgWalkNeighbors.MISSION_LENGTH - snap.getTime())) {
      return false;
    }
    double avgBattUsage = (1. - state.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= state.getFuelAmount()) {
      return false;
    }
    return snap.getTime() + SmgWalkNeighbors.TURN_LENGTH < SmgWalkNeighbors.MISSION_LENGTH;
  }

  public Optional<Grid> getGrid() {
    return Optional.of(this.grid);
  }

  public int getMissionLength() {
    return SmgWalkNeighbors.MISSION_LENGTH;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public String getScenarioID() {
    return SmgWalkNeighbors.SCENARIO_ID;
  }

  public long getSeed() {
    return this.seed;
  }

  public double getStepSize() {
    return SmgWalkNeighbors.STEP_SIZE;
  }
  
  public int getTrials() {
    return this.totalTrials;
  }

  public boolean hasGrid() {
    return true;
  }

  public void reset() {
    this.reset(false);
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    if (!snap.hasActiveRemoteWithTag(SmgWalkNeighbors.DRONE_TAG) && !snap.hasActiveRemoteWithTag(SmgWalkNeighbors.VICTIM_TAG)) {
      IntentionSet intentions = new IntentionSet(SmgWalkNeighbors.SCENARIO_ID);
      intentions.addIntention(IntentRegistry.Done());
      return List.of(intentions);
    }
    Stream.concat(
          Stream.of(snap.getRemoteStateWithTag(SmgWalkNeighbors.BASE_TAG).get()),
          snap.getActiveRemoteStates().stream().filter(state -> state.hasTag(SmgWalkNeighbors.DRONE_TAG))
        )
      .flatMap(state -> state.getSubjects().stream())
      .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(SmgWalkNeighbors.VICTIM_TAG))
      .forEach(victimID -> this.doneRemotes.add(victimID));
    this.updateZones();
    double halfSize = this.grid.getZoneSize() / 2.;
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgWalkNeighbors.GRID_CENTER, new Vector(-halfSize, -halfSize))), 0.);
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgWalkNeighbors.GRID_CENTER, new Vector(-halfSize, halfSize))), 0.);
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgWalkNeighbors.GRID_CENTER, new Vector(halfSize, halfSize))), 0.);
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgWalkNeighbors.GRID_CENTER, new Vector(halfSize, -halfSize))), 0.);
    return Stream.concat(
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(SmgWalkNeighbors.DRONE_TAG))
            .map(state -> {
                IntentionSet intent = new IntentionSet(state.getRemoteID());
                if (this.doneRemotes.contains(state.getRemoteID())) {
                  if (state.getLocation().near(SmgWalkNeighbors.GRID_CENTER)) {
                    intent.addIntention(IntentRegistry.Done());
                    this.logger.log(snap.getTime(), String.format("Drone done %s", state.getRemoteID()));
                    return intent;
                  }
                  intent.addIntention(IntentRegistry.GoHome());
                  return intent;
                }
                if (this.shouldReturnHome(snap, state)) {
                  intent.addIntention(IntentRegistry.DeactivateAllSensors());
                  intent.addIntention(IntentRegistry.GoHome());
                  this.doneRemotes.add(state.getRemoteID());
                  if (this.droneAssignment.containsKey(state.getRemoteID())) {
                    this.droneAssignment.remove(state.getRemoteID());
                  }
                  this.logger.log(snap.getTime(), String.format("Drone returning home %s", state.getRemoteID()));
                  return intent;
                }
                if (!this.droneAssignment.containsKey(state.getRemoteID())) {
                  Zone task = this.getBestNeighbor(state.getLocation());
                  this.droneAssignment.put(state.getRemoteID(), task);
                  // done early to avoid collisions
                  this.scanZone(task, (1. - this.beta.getStart()) * (1. - this.gamma.getStart()));
                }
                if (state.getLocation().near(this.droneAssignment.get(state.getRemoteID()).getLocation())) {
                  intent.addIntention(IntentRegistry.Stop());
                  this.droneAssignment.remove(state.getRemoteID());
                  return intent;
                }
                intent.addIntention(
                    IntentRegistry.GoTo(
                      this.droneAssignment.get(state.getRemoteID()).getLocation(),
                      SmgWalkNeighbors.DRONE_SCAN_VELOCITY,
                      SmgWalkNeighbors.DRONE_SCAN_ACCELERATION
                    )
                  );
                return intent;
              }),
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(SmgWalkNeighbors.VICTIM_TAG))
            .map(state -> {
                IntentionSet intent = new IntentionSet(state.getRemoteID());
                if (this.doneRemotes.contains(state.getRemoteID())) {
                  intent.addIntention(IntentRegistry.Done());
                  this.logger.log(
                      snap.getTime(),
                      String.format(
                          "Rescued victim %s (%.2f) (%.2f) (%.2f)",
                          state.getRemoteID(),
                          this.alpha.getStart(),
                          this.beta.getStart(),
                          this.gamma.getStart()
                        )
                    );
                  return intent;
                }
                if (this.rng.getRandomProbability() < this.alpha.getStart()) {
                  intent.addIntention(IntentRegistry.Stop());
                  return intent;
                }
                Optional<Vector> location = this.randomWalk(snap, state, false);
                if (location.isPresent()) {
                  intent.addIntention(IntentRegistry.GoTo(location.get()));
                } else {
                  intent.addIntention(IntentRegistry.Stop());
                }
                return intent;
              })
        )
      .collect(Collectors.toList()); 
  }
}
