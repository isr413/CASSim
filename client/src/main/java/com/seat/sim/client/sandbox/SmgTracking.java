package com.seat.sim.client.sandbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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

public class SmgTracking implements Application {

  // Scenario info
  private static final String SCENARIO_ID = "SmgTracking";
  private static final int NUM_TURNS = 81;
  private static final int TURN_LENGTH = 20;
  private static final int MISSION_LENGTH = SmgTracking.NUM_TURNS * SmgTracking.TURN_LENGTH;
  private static final double STEP_SIZE = 20.;
  private static final int TRACKING_TURNS = 6;

  // Grid info
  private static final int GRID_SIZE = 64;
  private static final int ZONE_SIZE = 14;

  private static final Vector GRID_CENTER = new Vector(
      SmgTracking.GRID_SIZE * SmgTracking.ZONE_SIZE / 2.,
      SmgTracking.GRID_SIZE * SmgTracking.ZONE_SIZE / 2.
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
  private static final double DRONE_DISCOVERY_PROBABILITY_BETA = 0.5;
  private static final double DRONE_DISCOVERY_PROBABILITY_GAMMA = 0.5;
  private static final int TRIALS_PER = 12;
 
  private Range alpha;
  private Set<String> doneRemotes;
  private Map<String, Zone> droneAssignment;
  private Grid grid;
  private Logger logger;
  private Random rng;
  private List<RemoteConfig> remotes;
  private long seed;
  private Iterator<Long> seeds;
  private LinkedList<Zone> tasks;
  private int totalTrials;
  private int trials;
  private Map<String, Integer> waitTime;
  private double[][] zones;

  public SmgTracking(ArgsParser args, int threadID, long seed) throws IOException {
    this.logger = (threadID > 0) ? new Logger(String.format("%s_%d", SmgTracking.SCENARIO_ID, threadID))
        : new Logger(SmgTracking.SCENARIO_ID);
    this.alpha = new Range(SmgTracking.VICTIM_STOP_PROBABILITY_ALPHA);
    this.grid = new Grid(
        SmgTracking.GRID_SIZE,
        SmgTracking.GRID_SIZE,
        SmgTracking.ZONE_SIZE
      );
    this.doneRemotes = new HashSet<>();
    this.droneAssignment = new HashMap<>();
    this.tasks = new LinkedList<>();
    this.waitTime = new HashMap<>();
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
    List<Zone> allZones = Arrays
      .stream(this.grid.getZones())
      .flatMap(row -> Arrays.stream(row))
      .collect(Collectors.toList());
    Collections.shuffle(allZones);
    this.tasks = new LinkedList<Zone>(allZones);
  }

  private void loadRemotes() {
    this.remotes = List.of(
        new RemoteConfig(
            new RemoteProto(
                Set.of(SmgTracking.BASE_TAG),
                List.of(
                    new SensorConfig(
                        new SensorProto(
                            SmgTracking.HUMAN_VISION,
                            Set.of(SmgTracking.BASE_TAG),
                            Set.of(SmgTracking.VICTIM_TAG),
                            new SensorStats(0., 1., 0., SmgTracking.BASE_VISION_RANGE)
                          ),
                        1,
                        true
                      ),
                    new SensorConfig(
                        new SensorProto(
                            SmgTracking.LONG_RANGE_COMMS,
                            Set.of(SmgTracking.BASE_LRC_TAG),
                            Set.of(SmgTracking.DRONE_LRC_TAG),
                            new SensorStats(SmgTracking.LRC_BATT_USAGE, 1., 0.)
                          ),
                        1,
                        true
                      )
                  ),
                new KinematicsProto(SmgTracking.GRID_CENTER)
              ),
            SmgTracking.BASE_COLOR,
            SmgTracking.BASE_COUNT,
            true,
            false
          ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(SmgTracking.DRONE_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              SmgTracking.DRONE_CAMERA,
                              Set.of(SmgTracking.DRONE_TAG),
                              Set.of(SmgTracking.VICTIM_TAG),
                              new SensorStats(
                                  SmgTracking.CAM_BATT_USAGE,
                                  SmgTracking.DRONE_DISCOVERY_PROBABILITY_BETA,
                                  0.,
                                  SmgTracking.CAM_RANGE
                                )
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              SmgTracking.LONG_RANGE_COMMS,
                              Set.of(SmgTracking.DRONE_LRC_TAG),
                              Set.of(SmgTracking.BASE_LRC_TAG),
                              new SensorStats(SmgTracking.LRC_BATT_USAGE, 1., 0.)
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              SmgTracking.BLE_COMMS,
                              Set.of(SmgTracking.DRONE_BLE_TAG),
                              Set.of(SmgTracking.VICTIM_BLE_TAG),
                              new SensorStats(
                                  SmgTracking.BLE_BATT_USAGE,
                                  SmgTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                  0.,
                                  SmgTracking.BLE_RANGE
                                )
                            ),
                          1,
                          true
                        )
                    ),
                  new KinematicsProto(
                      SmgTracking.GRID_CENTER,
                      new FuelProto(1., 1., SmgTracking.DRONE_FUEL_USAGE),
                      new MotionProto(
                          SmgTracking.DRONE_INITIAL_VELOCITY,
                          SmgTracking.DRONE_MAX_VELOCITY,
                          SmgTracking.DRONE_MAX_ACCELERATION
                        )
                    )
                ),
              SmgTracking.DRONE_COLOR,
              SmgTracking.DRONE_COUNT,
              true,
              true
            ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(SmgTracking.VICTIM_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              SmgTracking.HUMAN_VISION,
                              Set.of(SmgTracking.VICTIM_TAG),
                              Set.of(SmgTracking.BASE_TAG),
                              new SensorStats(0., 1., 0., SmgTracking.HUMAN_VISION_RANGE)
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              SmgTracking.BLE_COMMS,
                              Set.of(SmgTracking.VICTIM_BLE_TAG),
                              Set.of(SmgTracking.DRONE_BLE_TAG),
                              new SensorStats(
                                  SmgTracking.BLE_BATT_USAGE,
                                  SmgTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                  0.,
                                  SmgTracking.BLE_RANGE
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
                          SmgTracking.VICTIM_INITIAL_VELOCITY,
                          SmgTracking.VICTIM_MAX_VELOCITY,
                          SmgTracking.VICTIM_MAX_ACCELERATION
                        )
                    )
                ),
              SmgTracking.VICTIM_COLOR,
              SmgTracking.VICTIM_COUNT,
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
    if (this.trials % SmgTracking.TRIALS_PER == 0) {
      this.alpha.update();
    }
    if (this.alpha.isDone()) {
      this.alpha = new Range(SmgTracking.VICTIM_STOP_PROBABILITY_ALPHA);
    }
    this.setSeed(skip);
    if (!skip) {
      this.doneRemotes = new HashSet<>();
      this.droneAssignment = new HashMap<>();
      this.tasks = new LinkedList<>();
      this.waitTime = new HashMap<>();
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
              SmgTracking.DRONE_DISCOVERY_PROBABILITY_BETA,
              SmgTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA));
    }
  }

  private void setTrials(int threadID) {
    this.totalTrials = SmgTracking.VICTIM_STOP_PROBABILITY_ALPHA.points() * SmgTracking.TRIALS_PER;
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
    return this.tasks.poll();
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
    if (!state.hasTag(SmgTracking.DRONE_TAG)) {
      return false;
    }
    if (!state.hasLocation() || !this.grid.hasZoneAtLocation(state.getLocation())) {
      return snap.getTime() + SmgTracking.TURN_LENGTH >= SmgTracking.MISSION_LENGTH;
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
      .filter(neighbor -> state.hasTag(SmgTracking.VICTIM_TAG) || this.validChoice(snap, state, zone))
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

  protected void sortTasks() {
    Collections.shuffle(this.tasks);
    Collections.sort(this.tasks, new Comparator<Zone>() {
      @Override
      public int compare(Zone z1, Zone z2) {
        double z1weight = SmgTracking.this.getNeighborhoodWeight(z1.getLocation());
        double z2weight = SmgTracking.this.getNeighborhoodWeight(z2.getLocation());
        return -Double.compare(z1weight, z2weight);
      }
    });
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

  protected int trackedVictims(Snapshot snap, RemoteState state) {
    return (int) state
      .getSensorStateWithModel(SmgTracking.BLE_COMMS)
      .get()
      .getSubjects()
      .stream()
      .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(SmgTracking.VICTIM_TAG))
      .count();
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
    this.sortTasks();
  }

  protected boolean validChoice(Snapshot snap, RemoteState state, Zone zone) {
    double timeToMove = Math.max(
        this.timeToCrossDistance(
            Vector.dist(state.getLocation(), zone.getLocation()),
            state.getSpeed(),
            SmgTracking.DRONE_MAX_VELOCITY,
            SmgTracking.DRONE_MAX_ACCELERATION
          ),
        SmgTracking.TURN_LENGTH
      );
    double timeToGoHome = Math.max(
        this.timeToCrossDistance(
          Vector.dist(zone.getLocation(), SmgTracking.GRID_CENTER),
          0.,
          SmgTracking.DRONE_MAX_VELOCITY,
          SmgTracking.DRONE_MAX_ACCELERATION
        ),
        SmgTracking.TURN_LENGTH
      );
    if ((timeToMove + timeToGoHome) >= (SmgTracking.MISSION_LENGTH - snap.getTime())) {
      return false;
    }
    double avgBattUsage = (1. - state.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= state.getFuelAmount()) {
      return false;
    }
    return snap.getTime() + SmgTracking.TURN_LENGTH < SmgTracking.MISSION_LENGTH;
  }

  public Optional<Grid> getGrid() {
    return Optional.of(this.grid);
  }

  public int getMissionLength() {
    return SmgTracking.MISSION_LENGTH;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public String getScenarioID() {
    return SmgTracking.SCENARIO_ID;
  }

  public long getSeed() {
    return this.seed;
  }

  public double getStepSize() {
    return SmgTracking.STEP_SIZE;
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
    if (!snap.hasActiveRemoteWithTag(SmgTracking.DRONE_TAG) && !snap.hasActiveRemoteWithTag(SmgTracking.VICTIM_TAG)) {
      IntentionSet intentions = new IntentionSet(SmgTracking.SCENARIO_ID);
      intentions.addIntention(IntentRegistry.Done());
      return List.of(intentions);
    }
    Stream.concat(
          Stream.of(snap.getRemoteStateWithTag(SmgTracking.BASE_TAG).get()),
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(SmgTracking.DRONE_TAG))
            .filter(state -> !this.waitTime.containsKey(state.getRemoteID()))
        )
      .flatMap(state -> state.getSubjects().stream())
      .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(SmgTracking.VICTIM_TAG))
      .forEach(victimID -> this.doneRemotes.add(victimID));
    this.updateZones();
    double halfSize = this.grid.getZoneSize() / 2.;
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgTracking.GRID_CENTER, new Vector(-halfSize, -halfSize))), 0.);
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgTracking.GRID_CENTER, new Vector(-halfSize, halfSize))), 0.);
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgTracking.GRID_CENTER, new Vector(halfSize, halfSize))), 0.);
    this.scanZone(this.grid.getZoneAtLocation(Vector.add(SmgTracking.GRID_CENTER, new Vector(halfSize, -halfSize))), 0.);
    return Stream.concat(
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(SmgTracking.DRONE_TAG))
            .map(state -> {
                IntentionSet intent = new IntentionSet(state.getRemoteID());
                if (this.doneRemotes.contains(state.getRemoteID())) {
                  if (state.getLocation().near(SmgTracking.GRID_CENTER)) {
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
                    Zone task = this.droneAssignment.get(state.getRemoteID());
                    this.droneAssignment.remove(state.getRemoteID());
                    this.tasks.add(task);
                  }
                  this.logger.log(snap.getTime(), String.format("Drone returning home %s", state.getRemoteID()));
                  return intent;
                }
                if (this.waitTime.containsKey(state.getRemoteID())) {
                  if (this.waitTime.get(state.getRemoteID()) > 0) {
                    intent.addIntention(IntentRegistry.Stop());
                    this.waitTime.put(state.getRemoteID(), this.waitTime.get(state.getRemoteID()) - 1);
                    return intent;
                  }
                  this.waitTime.remove(state.getRemoteID());
                  intent.addIntention(IntentRegistry.ActivateAllSensors());
                }
                if (this.trackedVictims(snap, state) > 0) {
                  intent.addIntention(IntentRegistry.DeactivateAllSensors());
                  intent.addIntention(IntentRegistry.Stop());
                  this.waitTime.put(state.getRemoteID(), SmgTracking.TRACKING_TURNS);
                  return intent;
                }
                if (!this.droneAssignment.containsKey(state.getRemoteID())) {
                  Zone task = this.getBestNeighbor(state.getLocation());
                  this.droneAssignment.put(state.getRemoteID(), task);
                }
                if (state.getLocation().near(this.droneAssignment.get(state.getRemoteID()).getLocation())) {
                  intent.addIntention(IntentRegistry.Stop());
                  Zone task = this.droneAssignment.get(state.getRemoteID());
                  this.scanZone(
                      task,
                      (1. - SmgTracking.DRONE_DISCOVERY_PROBABILITY_BETA) *
                        (1. - SmgTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA)
                    );
                  this.droneAssignment.remove(state.getRemoteID());
                  this.tasks.add(task);
                  return intent;
                }
                intent.addIntention(
                    IntentRegistry.GoTo(
                      this.droneAssignment.get(state.getRemoteID()).getLocation(),
                      SmgTracking.DRONE_SCAN_VELOCITY,
                      SmgTracking.DRONE_SCAN_ACCELERATION
                    )
                  );
                return intent;
              }),
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(SmgTracking.VICTIM_TAG))
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
                          SmgTracking.DRONE_DISCOVERY_PROBABILITY_BETA,
                          SmgTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA
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
