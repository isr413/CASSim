package com.seat.sim.client.sandbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

public class RandomWalk implements Application {
  // Scenario info
  private static final String SCENARIO_ID = "RandomWalk";
  private static final int NUM_TURNS = 81;
  private static final int TURN_LENGTH = 20;
  private static final int MISSION_LENGTH = RandomWalk.NUM_TURNS * RandomWalk.TURN_LENGTH;
  private static final double STEP_SIZE = 20.;

  // Grid info
  private static final int GRID_SIZE = 64;
  private static final int ZONE_SIZE = 14;

  private static final Vector GRID_CENTER = new Vector(
        RandomWalk.GRID_SIZE * RandomWalk.ZONE_SIZE / 2.,
        RandomWalk.GRID_SIZE * RandomWalk.ZONE_SIZE / 2.
      );

  // Base info
  private static final String BASE_TAG = "Base";
  private static final String BASE_LRC_TAG = "Base_LRC";
  private static final TeamColor BASE_COLOR = TeamColor.GREEN;
  private static final int BASE_COUNT = 1;

  // Drone info
  private static final String DRONE_TAG = "Drone";
  private static final String DRONE_LRC_TAG = "Drone_LRC";
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
  private static final TeamColor VICTIM_COLOR = TeamColor.RED;
  private static final int VICTIM_COUNT = 1024;
  private static final Vector VICTIM_INITIAL_VELOCITY = Vector.ZERO;
  private static final double VICTIM_MAX_VELOCITY = 1.4;
  private static final double VICTIM_MAX_ACCELERATION = 2.5;

  // Sensors
  private static final String HUMAN_VISION = "Human_Vision";
  private static final double HUMAN_VISION_RANGE = RandomWalk.ZONE_SIZE;

  public static final String LONG_RANGE_COMMS = "Long_Range_Comms";
  public static final double LRC_BATT_USAGE = 0.0001;

  public static final String DRONE_CAMERA = "Drone_Camera";
  public static final double CAM_BATT_USAGE = 0.0001;
  public static final double CAM_RANGE = RandomWalk.ZONE_SIZE;

  // Tunable params
  private static final Range VICTIM_STOP_PROBABILITY_ALPHA = new Range(0., 0.1, 1., true);
  private static final Range DRONE_DISCOVERY_PROBABILITY_BETA = new Range(0.1, 0.1, 1., true);
  private static final int TRIALS_PER = 10;
 
  private Range alpha;
  private Range beta;
  private Set<String> doneRemotes;
  private Grid grid;
  private Logger logger;
  private Random rng;
  private List<RemoteConfig> remotes;
  private long seed;
  private Iterator<Long> seeds;
  private int totalTrials;
  private int trials;

  public RandomWalk(ArgsParser args, int threadID, long seed) throws IOException {
    this.logger = (threadID > 0) ? new Logger(String.format("%s_%d", RandomWalk.SCENARIO_ID, threadID))
        : new Logger(RandomWalk.SCENARIO_ID);
    this.alpha = new Range(RandomWalk.VICTIM_STOP_PROBABILITY_ALPHA);
    this.beta = new Range(RandomWalk.DRONE_DISCOVERY_PROBABILITY_BETA);
    this.grid = new Grid(RandomWalk.GRID_SIZE, RandomWalk.GRID_SIZE, RandomWalk.ZONE_SIZE);
    this.doneRemotes = new HashSet<>();
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
                      Set.of(RandomWalk.BASE_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.HUMAN_VISION,
                                        Set.of(RandomWalk.BASE_TAG),
                                        Set.of(RandomWalk.VICTIM_TAG),
                                        new SensorStats(0., 1., 0., RandomWalk.HUMAN_VISION_RANGE)
                                      ),
                                  1,
                                  true
                                ),
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.LONG_RANGE_COMMS,
                                        Set.of(RandomWalk.BASE_LRC_TAG),
                                        Set.of(RandomWalk.DRONE_LRC_TAG),
                                        new SensorStats(RandomWalk.LRC_BATT_USAGE, 1., 0.)
                                      ),
                                  1,
                                  true
                                )
                          ),
                      new KinematicsProto(RandomWalk.GRID_CENTER)
                    ),
                RandomWalk.BASE_COLOR,
                RandomWalk.BASE_COUNT,
                true,
                false
              ),
          new RemoteConfig(
                new RemoteProto(
                      Set.of(RandomWalk.DRONE_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.DRONE_CAMERA,
                                        Set.of(RandomWalk.DRONE_TAG),
                                        Set.of(RandomWalk.VICTIM_TAG),
                                        new SensorStats(
                                              RandomWalk.CAM_BATT_USAGE,
                                              this.beta.getStart(),
                                              0.,
                                              RandomWalk.CAM_RANGE
                                            )
                                      ),
                                  1,
                                  true
                                ),
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.LONG_RANGE_COMMS,
                                        Set.of(RandomWalk.DRONE_LRC_TAG),
                                        Set.of(RandomWalk.BASE_LRC_TAG),
                                        new SensorStats(RandomWalk.LRC_BATT_USAGE, 1., 0.)
                                      ),
                                  1,
                                  true
                                )
                          ),
                      new KinematicsProto(
                            RandomWalk.GRID_CENTER,
                            new FuelProto(1., 1., RandomWalk.DRONE_FUEL_USAGE),
                            new MotionProto(
                                  RandomWalk.DRONE_INITIAL_VELOCITY,
                                  RandomWalk.DRONE_MAX_VELOCITY,
                                  RandomWalk.DRONE_MAX_ACCELERATION
                                )
                          )
                    ),
                RandomWalk.DRONE_COLOR,
                RandomWalk.DRONE_COUNT,
                true,
                true
              ),
          new RemoteConfig(
                new RemoteProto(
                      Set.of(RandomWalk.VICTIM_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.HUMAN_VISION,
                                        Set.of(RandomWalk.VICTIM_TAG),
                                        Set.of(RandomWalk.BASE_TAG),
                                        new SensorStats(0., 1., 0., RandomWalk.HUMAN_VISION_RANGE)
                                      ),
                                  1,
                                  true
                                )
                          ),
                      new KinematicsProto(
                            null,
                            null,
                            new MotionProto(
                                  RandomWalk.VICTIM_INITIAL_VELOCITY,
                                  RandomWalk.VICTIM_MAX_VELOCITY,
                                  RandomWalk.VICTIM_MAX_ACCELERATION
                                )
                          )
                    ),
                RandomWalk.VICTIM_COLOR,
                RandomWalk.VICTIM_COUNT,
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
    if (this.trials % RandomWalk.TRIALS_PER == 0 && !this.alpha.isDone()) {
      if (this.beta.isDone()) {
        this.alpha.update();
        this.beta = new Range(RandomWalk.DRONE_DISCOVERY_PROBABILITY_BETA);
      } else {
        this.beta.update();
      }
    }
    if (this.alpha.isDone()) {
      this.alpha = new Range(RandomWalk.VICTIM_STOP_PROBABILITY_ALPHA);
    }
    this.setSeed(skip);
    if (!skip) {
      this.loadRemotes();
      this.doneRemotes = new HashSet<>();
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
          String.format("Seed %d - ALPHA %.2f - BETA %.2f", this.rng.getSeed(), this.alpha.getStart(),
              this.beta.getStart()));
    }
  }

  private void setTrials(int threadID) {
    this.totalTrials = RandomWalk.VICTIM_STOP_PROBABILITY_ALPHA.points() *
        RandomWalk.DRONE_DISCOVERY_PROBABILITY_BETA.points() * RandomWalk.TRIALS_PER;
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
    if (!state.hasTag(RandomWalk.DRONE_TAG)) {
      return false;
    }
    if (!state.hasLocation() || !this.grid.hasZoneAtLocation(state.getLocation())) {
      return snap.getTime() + RandomWalk.TURN_LENGTH >= RandomWalk.MISSION_LENGTH;
    }
    for (Zone neighbor : this.grid.getNeighborhood(state.getLocation())) {
      if (this.validChoice(snap, state, neighbor)) {
        return snap.getTime() + RandomWalk.TURN_LENGTH >= RandomWalk.MISSION_LENGTH;
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
        .filter(neighbor -> state.hasTag(RandomWalk.VICTIM_TAG) || this.validChoice(snap, state, zone))
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

  protected boolean validChoice(Snapshot snap, RemoteState state, Zone zone) {
    double timeToMove = Math.max(
        this.timeToCrossDistance(
            Vector.dist(state.getLocation(), zone.getLocation()),
            state.getSpeed(),
            RandomWalk.DRONE_MAX_VELOCITY,
            RandomWalk.DRONE_MAX_ACCELERATION
          ),
        RandomWalk.TURN_LENGTH
      );
    double timeToGoHome = this.timeToCrossDistance(
        Vector.dist(zone.getLocation(), RandomWalk.GRID_CENTER),
        0.,
        RandomWalk.DRONE_MAX_VELOCITY,
        RandomWalk.DRONE_MAX_ACCELERATION
      );
    if ((timeToMove + timeToGoHome) >= (RandomWalk.MISSION_LENGTH - snap.getTime())) {
      return false;
    }
    double avgBattUsage = (1. - state.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= state.getFuelAmount()) {
      return false;
    }
    return snap.getTime() + RandomWalk.TURN_LENGTH < RandomWalk.MISSION_LENGTH;
  }

  public Optional<Grid> getGrid() {
    return Optional.of(this.grid);
  }

  public int getMissionLength() {
    return RandomWalk.MISSION_LENGTH;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public String getScenarioID() {
    return RandomWalk.SCENARIO_ID;
  }

  public long getSeed() {
    return this.seed;
  }

  public double getStepSize() {
    return RandomWalk.STEP_SIZE;
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
    if (!snap.hasActiveRemoteWithTag(RandomWalk.DRONE_TAG) && !snap.hasActiveRemoteWithTag(RandomWalk.VICTIM_TAG)) {
      IntentionSet intentions = new IntentionSet(RandomWalk.SCENARIO_ID);
      intentions.addIntention(IntentRegistry.Done());
      return List.of(intentions);
    }
    Collection<IntentionSet> droneIntentions = snap
      .getRemoteStates()
      .stream()
      .filter(state -> state.isActive() && state.hasTag(RandomWalk.DRONE_TAG))
      .map(state -> {
          IntentionSet intent = new IntentionSet(state.getRemoteID());
          state
            .getSubjects()
            .stream()
            .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(RandomWalk.VICTIM_TAG))
            .forEach(victimID -> this.doneRemotes.add(victimID));
          if (this.doneRemotes.contains(state.getRemoteID())) {
            if (state.getLocation().near(RandomWalk.GRID_CENTER)) {
              intent.addIntention(IntentRegistry.Done());
              this.logger.log(snap.getTime(), String.format("Drone done %s", state.getRemoteID()));
              return intent;
            }
            intent.addIntention(IntentRegistry.GoHome());
            return intent;
          }
          if (this.shouldReturnHome(snap, state)) {
            intent.addIntention(IntentRegistry.GoHome());
            intent.addIntention(IntentRegistry.DeactivateAllSensors());
            this.doneRemotes.add(state.getRemoteID());
            this.logger.log(snap.getTime(), String.format("Drone returning home %s", state.getRemoteID()));
            return intent;
          }
          if (snap.getTime() % RandomWalk.TURN_LENGTH != 0.) {
            intent.addIntention(IntentRegistry.None());
            return intent;
          }
          Optional<Vector> location = this.randomWalk(snap, state, true);
          if (location.isPresent()) {
            intent.addIntention(
                IntentRegistry.GoTo(
                    location.get(),
                    RandomWalk.DRONE_SCAN_VELOCITY,
                    RandomWalk.DRONE_SCAN_ACCELERATION
                  )
              );
          } else {
            intent.addIntention(IntentRegistry.Stop());
          }
          return intent;
        })
      .collect(Collectors.toList());
    String baseID = snap.getRemoteStateWithTag(RandomWalk.BASE_TAG).get().getRemoteID();
    Collection<IntentionSet> victimIntentions = snap
      .getRemoteStates()
      .stream()
      .filter(state -> state.isActive() && state.hasTag(RandomWalk.VICTIM_TAG))
      .map(state -> {
          IntentionSet intent = new IntentionSet(state.getRemoteID());
          if (state.hasSubjectWithID(baseID) || this.doneRemotes.contains(state.getRemoteID())) {
            intent.addIntention(IntentRegistry.Done());
            this.doneRemotes.add(state.getRemoteID());
            this.logger.log(
                  snap.getTime(),
                  String.format(
                        "Rescued victim %s (%.2f) (%.2f)",
                        state.getRemoteID(),
                        this.alpha.getStart(),
                        this.beta.getStart()
                      )
                );
            return intent;
          }
          if (snap.getTime() % RandomWalk.TURN_LENGTH != 0.) {
            intent.addIntention(IntentRegistry.None());
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
      .collect(Collectors.toList()); 
    droneIntentions.addAll(victimIntentions);
    return droneIntentions;
  }
}
