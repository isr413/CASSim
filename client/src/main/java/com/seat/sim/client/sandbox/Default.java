package com.seat.sim.client.sandbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
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
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
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

public class Default implements Application {
  // Scenario info
  private static final String SCENARIO_ID = "Default";
  private static final int NUM_TURNS = 81;
  private static final int TURN_LENGTH = 20;
  private static final int MISSION_LENGTH = Default.NUM_TURNS * Default.TURN_LENGTH;
  private static final double STEP_SIZE = 20.;

  // Grid info
  private static final int GRID_SIZE = 64;
  private static final int ZONE_SIZE = 14;

  private static final Vector GRID_CENTER = new Vector(
        Default.GRID_SIZE * Default.ZONE_SIZE / 2.,
        Default.GRID_SIZE * Default.ZONE_SIZE / 2.
      );

  // Base info
  private static final String BASE_TAG = "Base";
  private static final TeamColor BASE_COLOR = TeamColor.GREEN;
  private static final int BASE_COUNT = 1;

  // Victim info
  private static final String VICTIM_TAG = "Victim";
  private static final TeamColor VICTIM_COLOR = TeamColor.RED;
  private static final int VICTIM_COUNT = 1024;
  private static final Vector VICTIM_INITIAL_VELOCITY = Vector.ZERO;
  private static final double VICTIM_MAX_ACCELERATION = 2.5;
  private static final double VICTIM_MAX_VELOCITY = 1.4;

  // Sensors
  private static final String HUMAN_VISION = "Human_Vision";
  private static final double HUMAN_VISION_RANGE = Default.ZONE_SIZE;

  // Tunable params
  private static final Range VICTIM_STOP_PROBABILITY_ALPHA = new Range(0., 0.1, 1., true);
  private static final int TRIALS_PER = 100;
 
  private Range alpha;
  private Grid grid;
  private Logger logger;
  private Random rng;
  private List<RemoteConfig> remotes;
  private long seed;
  private Iterator<Long> seeds;
  private int totalTrials;
  private int trials;

  public Default(ArgsParser args, int threadID, long seed) throws IOException {
    this.logger = (threadID > 0) ? new Logger(String.format("%s_%d", Default.SCENARIO_ID, threadID))
        : new Logger(Default.SCENARIO_ID);
    this.alpha = new Range(Default.VICTIM_STOP_PROBABILITY_ALPHA);
    this.grid = new Grid(Default.GRID_SIZE, Default.GRID_SIZE, Default.ZONE_SIZE);
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
                      Set.of(Default.BASE_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        Default.HUMAN_VISION,
                                        Set.of(Default.BASE_TAG),
                                        Set.of(Default.VICTIM_TAG),
                                        new SensorStats(0., 1., 0., Default.HUMAN_VISION_RANGE)
                                      ),
                                  1,
                                  true
                                )
                          ),
                      new KinematicsProto(Default.GRID_CENTER)
                    ),
                Default.BASE_COLOR,
                Default.BASE_COUNT,
                true,
                false
              ),
          new RemoteConfig(
                new RemoteProto(
                      Set.of(Default.VICTIM_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        Default.HUMAN_VISION,
                                        Set.of(Default.VICTIM_TAG),
                                        Set.of(Default.BASE_TAG),
                                        new SensorStats(0., 1., 0., Default.HUMAN_VISION_RANGE)
                                      ),
                                  1,
                                  true
                                )
                          ),
                      new KinematicsProto(
                            null,
                            null,
                            new MotionProto(
                                  Default.VICTIM_INITIAL_VELOCITY,
                                  Default.VICTIM_MAX_VELOCITY,
                                  Default.VICTIM_MAX_ACCELERATION
                                )
                          )
                    ),
                Default.VICTIM_COLOR,
                Default.VICTIM_COUNT,
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
    if (this.trials % Default.TRIALS_PER == 0 && !this.alpha.isDone()) {
      this.alpha.update();
    }
    if (this.alpha.isDone()) {
      this.alpha = new Range(Default.VICTIM_STOP_PROBABILITY_ALPHA);
    }
    this.setSeed(skip);
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
      this.logger.log(-1., String.format("Seed %d - ALPHA %.2f", this.rng.getSeed(), this.alpha.getStart()));
    }
  }

  private void setTrials(int threadID) {
    this.totalTrials = Default.VICTIM_STOP_PROBABILITY_ALPHA.points() * Default.TRIALS_PER;
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

  protected Optional<Vector> randomWalk(Vector location) {
    if (!this.grid.hasZoneAtLocation(location)) {
      return Optional.empty();
    }
    Zone zone = this.grid.getZoneAtLocation(location);
    List<Zone> neighborhood = this.grid.getNeighborhood(zone);
    Collections.shuffle(neighborhood, this.rng.getRng());
    Optional<Zone> nextZone = neighborhood
        .stream()
        .filter(neighbor -> neighbor != zone && !neighbor.hasZoneType(ZoneType.BLOCKED))
        .findFirst();
    if (nextZone.isEmpty()) {
      return Optional.empty();
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

  public Optional<Grid> getGrid() {
    return Optional.of(this.grid);
  }

  public int getMissionLength() {
    return Default.MISSION_LENGTH;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public long getSeed() {
    return this.seed;
  }

  public String getScenarioID() {
    return Default.SCENARIO_ID;
  }

  public double getStepSize() {
    return Default.STEP_SIZE;
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
    if (!snap.hasActiveRemoteWithTag(Default.VICTIM_TAG)) {
      IntentionSet intentions = new IntentionSet(Default.SCENARIO_ID);
      intentions.addIntention(IntentRegistry.Done());
      return List.of(intentions);
    }
    String baseID = snap.getRemoteStateWithTag(Default.BASE_TAG).get().getRemoteID();
    return snap
      .getRemoteStates()
      .stream()
      .filter(state -> state.isActive() && state.hasTag(Default.VICTIM_TAG))
      .map(state -> {
          IntentionSet intent = new IntentionSet(state.getRemoteID());
          if (state.hasSubjectWithID(baseID)) {
            intent.addIntention(IntentRegistry.Done());
            this.logger.log(
                  snap.getTime(),
                  String.format(
                        "Rescued victim %s (%.2f)",
                        state.getRemoteID(),
                        this.alpha.getStart()
                      )
                );
            return intent;
          }
          if (this.rng.getRandomProbability() < this.alpha.getStart()) {
            intent.addIntention(IntentRegistry.Stop());
            return intent;
          }
          Optional<Vector> location = this.randomWalk(state.getLocation());
          if (location.isPresent()) {
            intent.addIntention(IntentRegistry.GoTo(location.get()));
          } else {
            intent.addIntention(IntentRegistry.Stop());
          }
          return intent;
        })
      .collect(Collectors.toList()); 
  }
}
