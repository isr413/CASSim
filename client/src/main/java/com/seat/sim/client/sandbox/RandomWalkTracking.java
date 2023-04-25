package com.seat.sim.client.sandbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
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

public class RandomWalkTracking implements Application {
  // Scenario info
  private static final String SCENARIO_ID = "RandomWalkTracking";
  private static final int NUM_TURNS = 81;
  private static final int TURN_LENGTH = 20;
  private static final int MISSION_LENGTH = RandomWalkTracking.NUM_TURNS * RandomWalkTracking.TURN_LENGTH;
  private static final double STEP_SIZE = 20.;
  private static final int TRACKING_TURNS = 6;

  // Grid info
  private static final int GRID_SIZE = 64;
  private static final int ZONE_SIZE = 14;

  private static final Vector GRID_CENTER = new Vector(
      RandomWalkTracking.GRID_SIZE * RandomWalkTracking.ZONE_SIZE / 2.,
      RandomWalkTracking.GRID_SIZE * RandomWalkTracking.ZONE_SIZE / 2.
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
  private Grid grid;
  private Logger logger;
  private Random rng;
  private List<RemoteConfig> remotes;
  private long seed;
  private Iterator<Long> seeds;
  private int totalTrials;
  private int trials;
  private Map<String, Integer> waitTime;

  public RandomWalkTracking(ArgsParser args, int threadID, long seed) throws IOException {
    this.logger = (threadID > 0) ? new Logger(String.format("%s_%d", RandomWalkTracking.SCENARIO_ID, threadID))
        : new Logger(RandomWalkTracking.SCENARIO_ID);
    this.alpha = new Range(RandomWalkTracking.VICTIM_STOP_PROBABILITY_ALPHA);
    this.grid = new Grid(RandomWalkTracking.GRID_SIZE, RandomWalkTracking.GRID_SIZE, RandomWalkTracking.ZONE_SIZE);
    this.doneRemotes = new HashSet<>();
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
  }

  private void loadRemotes() {
    this.remotes = List.of(
        new RemoteConfig(
            new RemoteProto(
                Set.of(RandomWalkTracking.BASE_TAG),
                List.of(
                    new SensorConfig(
                        new SensorProto(
                            RandomWalkTracking.HUMAN_VISION,        // model 
                            Set.of(RandomWalkTracking.BASE_TAG),    // tags
                            Set.of(RandomWalkTracking.VICTIM_TAG),  // matchers
                            new SensorStats(0., 1., 0., RandomWalkTracking.BASE_VISION_RANGE) // usage, accuracy, delay, range
                          ),
                        1,    // count
                        true  // active
                      ),
                    new SensorConfig(
                        new SensorProto(
                            RandomWalkTracking.LONG_RANGE_COMMS,        // model
                            Set.of(RandomWalkTracking.BASE_LRC_TAG),    // tags
                            Set.of(RandomWalkTracking.DRONE_LRC_TAG),   // matchers
                            new SensorStats(RandomWalkTracking.LRC_BATT_USAGE, 1., 0.) // usage, accuracy, delay, range
                          ),
                        1,    // count
                        true  // active
                      )
                  ),
                new KinematicsProto(RandomWalkTracking.GRID_CENTER) // location, no fuel, no motion
              ),
            RandomWalkTracking.BASE_COLOR,
            RandomWalkTracking.BASE_COUNT,
            true,   // active
            false   // dynamic
          ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(RandomWalkTracking.DRONE_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              RandomWalkTracking.DRONE_CAMERA,        // model
                              Set.of(RandomWalkTracking.DRONE_TAG),   // tags
                              Set.of(RandomWalkTracking.VICTIM_TAG),  // matchers
                              new SensorStats(
                                  RandomWalkTracking.CAM_BATT_USAGE,  // usage
                                  RandomWalkTracking.DRONE_DISCOVERY_PROBABILITY_BETA,
                                  0.,                         // delay
                                  RandomWalkTracking.CAM_RANGE        // range
                                )
                            ),
                          1,    // count
                          true  // active
                        ),
                      new SensorConfig(
                          new SensorProto(
                              RandomWalkTracking.LONG_RANGE_COMMS,        // model
                              Set.of(RandomWalkTracking.DRONE_LRC_TAG),   // tags
                              Set.of(RandomWalkTracking.BASE_LRC_TAG),    // matchers
                              new SensorStats(RandomWalkTracking.LRC_BATT_USAGE, 1., 0.) // usage, accuracy, delay, inf range
                            ),
                          1,    // count
                          true  // active
                        ),
                      new SensorConfig(
                          new SensorProto(
                              RandomWalkTracking.BLE_COMMS,              // model
                              Set.of(RandomWalkTracking.DRONE_BLE_TAG),  // tags
                              Set.of(RandomWalkTracking.VICTIM_BLE_TAG), // matchers
                              new SensorStats(
                                  RandomWalkTracking.BLE_BATT_USAGE,     // usage
                                  RandomWalkTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                  0.,                                 // delay
                                  RandomWalkTracking.BLE_RANGE           // range
                                )
                            ),
                          1,    // count
                          true  // active
                        )
                    ),
                  new KinematicsProto(
                      RandomWalkTracking.GRID_CENTER, // location
                      new FuelProto(1., 1., RandomWalkTracking.DRONE_FUEL_USAGE), // initial, max, usage
                      new MotionProto(
                          RandomWalkTracking.DRONE_INITIAL_VELOCITY,  // initial velocity
                          RandomWalkTracking.DRONE_MAX_VELOCITY,      // max velocity
                          RandomWalkTracking.DRONE_MAX_ACCELERATION   // max acceleration
                        )
                    )
                ),
              RandomWalkTracking.DRONE_COLOR,
              RandomWalkTracking.DRONE_COUNT,
              true, // active
              true  // dynamic
            ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(RandomWalkTracking.VICTIM_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              RandomWalkTracking.HUMAN_VISION,          // model
                              Set.of(RandomWalkTracking.VICTIM_TAG),    // tags
                              Set.of(RandomWalkTracking.BASE_TAG),      // matchers
                              new SensorStats(0., 1., 0., RandomWalkTracking.HUMAN_VISION_RANGE) // usage, acc., delay, range
                            ),
                          1,    // count
                          true  // active
                        ),
                      new SensorConfig(
                          new SensorProto(
                              RandomWalkTracking.BLE_COMMS,              // model
                              Set.of(RandomWalkTracking.VICTIM_BLE_TAG), // tags
                              Set.of(RandomWalkTracking.DRONE_BLE_TAG),  // matchers
                              new SensorStats(
                                  RandomWalkTracking.BLE_BATT_USAGE,     // usage
                                  RandomWalkTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                  0.,                                 // delay
                                  RandomWalkTracking.BLE_RANGE           // range
                                )
                            ),
                          1,    // count
                          true  // active
                        )
                    ),
                  new KinematicsProto(
                      null, // random location
                      null, // no fuel
                      new MotionProto(
                          RandomWalkTracking.VICTIM_INITIAL_VELOCITY,   // initial velocity
                          RandomWalkTracking.VICTIM_MAX_VELOCITY,       // max velocity
                          RandomWalkTracking.VICTIM_MAX_ACCELERATION    // max acceleration
                        )
                    )
                ),
              RandomWalkTracking.VICTIM_COLOR,
              RandomWalkTracking.VICTIM_COUNT,
              true,   // active
              true    // dynamic
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
    if (this.trials % RandomWalkTracking.TRIALS_PER == 0) {
      this.alpha.update();
    }
    if (this.alpha.isDone()) {
      this.alpha = new Range(RandomWalkTracking.VICTIM_STOP_PROBABILITY_ALPHA);
    }
    this.setSeed(skip);
    if (!skip) {
      this.doneRemotes = new HashSet<>();
      this.waitTime = new HashMap<>();
      this.loadRemotes();
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
              RandomWalkTracking.DRONE_DISCOVERY_PROBABILITY_BETA,
              RandomWalkTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA));
    }
  }

  private void setTrials(int threadID) {
    this.totalTrials = RandomWalkTracking.VICTIM_STOP_PROBABILITY_ALPHA.points() * RandomWalkTracking.TRIALS_PER;
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

  protected boolean shouldReturnHome(Snapshot snap, RemoteState state) {
    if (!state.hasTag(RandomWalkTracking.DRONE_TAG)) {
      return false;
    }
    if (!state.hasLocation() || !this.grid.hasZoneAtLocation(state.getLocation())) {
      return snap.getTime() + RandomWalkTracking.TURN_LENGTH >= RandomWalkTracking.MISSION_LENGTH;
    }
    for (Zone neighbor : this.grid.getNeighborhood(state.getLocation())) {
      if (this.validChoice(snap, state, neighbor)) {
        return snap.getTime() + RandomWalkTracking.TURN_LENGTH >= RandomWalkTracking.MISSION_LENGTH;
      }
    }
    return true;
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
      .filter(neighbor -> state.hasTag(RandomWalkTracking.VICTIM_TAG) || this.validChoice(snap, state, zone))
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
      .getSensorStateWithModel(RandomWalkTracking.BLE_COMMS)
      .get()
      .getSubjects()
      .stream()
      .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(RandomWalkTracking.VICTIM_TAG))
      .count();
  }

  protected boolean validChoice(Snapshot snap, RemoteState state, Zone zone) {
    double timeToMove = Math.max(
        this.timeToCrossDistance(
            Vector.dist(state.getLocation(), zone.getLocation()),
            state.getSpeed(),
            RandomWalkTracking.DRONE_MAX_VELOCITY,
            RandomWalkTracking.DRONE_MAX_ACCELERATION
          ),
        RandomWalkTracking.TURN_LENGTH
      );
    double timeToGoHome = Math.max(
        this.timeToCrossDistance(
            Vector.dist(zone.getLocation(), RandomWalkTracking.GRID_CENTER),
            0.,
            RandomWalkTracking.DRONE_MAX_VELOCITY,
            RandomWalkTracking.DRONE_MAX_ACCELERATION
          ),
        RandomWalkTracking.TURN_LENGTH
      );
    if ((timeToMove + timeToGoHome) >= (RandomWalkTracking.MISSION_LENGTH - snap.getTime())) {
      return false;
    }
    double avgBattUsage = (1. - state.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= state.getFuelAmount()) {
      return false;
    }
    return snap.getTime() + RandomWalkTracking.TURN_LENGTH < RandomWalkTracking.MISSION_LENGTH;
  }

  public Optional<Grid> getGrid() {
    return Optional.of(this.grid);
  }

  public int getMissionLength() {
    return RandomWalkTracking.MISSION_LENGTH;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public String getScenarioID() {
    return RandomWalkTracking.SCENARIO_ID;
  }

  public long getSeed() {
    return this.seed;
  }

  public double getStepSize() {
    return RandomWalkTracking.STEP_SIZE;
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
    if (!snap.hasActiveRemoteWithTag(RandomWalkTracking.DRONE_TAG) && !snap.hasActiveRemoteWithTag(RandomWalkTracking.VICTIM_TAG)) {
      IntentionSet intentions = new IntentionSet(RandomWalkTracking.SCENARIO_ID);
      intentions.addIntention(IntentRegistry.Done());
      return List.of(intentions);
    }
    Stream.concat(
          Stream.of(snap.getRemoteStateWithTag(RandomWalkTracking.BASE_TAG).get()),
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(RandomWalkTracking.DRONE_TAG))
            .filter(state -> !this.waitTime.containsKey(state.getRemoteID()))
        )
      .flatMap(state -> state.getSubjects().stream())
      .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(RandomWalkTracking.VICTIM_TAG))
      .forEach(victimID -> this.doneRemotes.add(victimID));
    return Stream.concat(
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(RandomWalkTracking.DRONE_TAG))
            .map(state -> {
                IntentionSet intent = new IntentionSet(state.getRemoteID());
                if (this.doneRemotes.contains(state.getRemoteID())) {
                  if (state.getLocation().near(RandomWalkTracking.GRID_CENTER)) {
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
                  this.waitTime.put(state.getRemoteID(), RandomWalkTracking.TRACKING_TURNS);
                  return intent;
                }
                Optional<Vector> location = this.randomWalk(snap, state, true);
                if (location.isPresent()) {
                  intent.addIntention(
                      IntentRegistry.GoTo(
                          location.get(),
                          RandomWalkTracking.DRONE_SCAN_VELOCITY,
                          RandomWalkTracking.DRONE_SCAN_ACCELERATION
                        )
                    );
                } else {
                  intent.addIntention(IntentRegistry.Stop());
                }
                return intent;
              }),
          snap
            .getActiveRemoteStates()
            .stream()
            .filter(state -> state.hasTag(RandomWalkTracking.VICTIM_TAG))
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
                          RandomWalkTracking.DRONE_DISCOVERY_PROBABILITY_BETA,
                          RandomWalkTracking.DRONE_DISCOVERY_PROBABILITY_GAMMA
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
