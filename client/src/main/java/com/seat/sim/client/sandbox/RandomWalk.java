package com.seat.sim.client.sandbox;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.core.Application;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteProto;
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
import com.seat.sim.common.util.Random;

public class RandomWalk implements Application {

  // Scenario info
  public static final String SCENARIO_ID = "RandomWalk";
  public static final int MISSION_LENGTH = 20 * 81; // paper: "81 turns", each turn is 20 seconds
  public static final double STEP_SIZE = 0.5;

  // Grid info
  public static final int GRID_SIZE = 20; // paper: "... uses a 64x64 grid"
  public static final int ZONE_SIZE = 14; // paper: "Each cell has an area of 200 m^2"
 
  public static final Vector GRID_CENTER = new Vector(
        RandomWalk.GRID_SIZE * RandomWalk.ZONE_SIZE / 2,
        RandomWalk.GRID_SIZE * RandomWalk.ZONE_SIZE / 2
      );

  // Base info
  public static final TeamColor BASE_COLOR = TeamColor.GREEN;
  public static final String BASE_COMMS = "Base_Comms";
  public static final int BASE_COUNT = 1; // paper: "... have constructed a triage tent"
  public static final String BASE_TAG = "Base";
 
  // Drone info
  public static final TeamColor DRONE_COLOR = TeamColor.BLUE;
  public static final String DRONE_COMMS = "Drone_Comms";
  public static final String DRONE_COMMS_BLE = "Drone_BLE";
  public static final int DRONE_COUNT = 32; // paper: "... have been equipped with 32 commercial drones"
  public static final Vector DRONE_FUEL_USAGE = new Vector(0.001, 0.001, 0.001);
  public static final Vector DRONE_INITIAL_VELOCITY = new Vector(1, 1);
  public static final double DRONE_MAX_ACCELERATION = 15.;
  public static final double DRONE_MAX_VELOCITY = 15.;
  public static final String DRONE_TAG = "Drone";

  // Victim info
  public static final TeamColor VICTIM_COLOR = TeamColor.RED;
  public static final String VICTIM_COMMS_BLE = "Victim_BLE";
  public static final int VICTIM_COUNT = 1024; // paper: "1024 victims"
  public static final Vector VICTIM_INITIAL_VELOCITY = new Vector();
  public static final double VICTIM_MAX_ACCELERATION = 1.4;
  public static final double VICTIM_MAX_VELOCITY = 4.;
  public static final String VICTIM_TAG = "Victim";

  // Sensors
  public static final String LONG_RANGE_COMMS = "Long_Range_Comms";
  public static final double LRC_BATT_USAGE = 0.001;

  public static final String DRONE_CAMERA = "Drone_Camera";
  public static final double CAM_BATT_USAGE = 0.001;
  public static final double CAM_RANGE = 14.;

  public static final String BLE_COMMS = "BLE_Comms";
  public static final double BLE_BATT_USAGE = 0.001;
  public static final double BLE_RANGE = 14.;

  // Tunable params
  public static final double VICTIM_STOP_PROBABILITY_ALPHA = 0.5;
  public static final double DRONE_DISCOVERY_PROBABILITY_BETA = 0.5;
  public static final double DRONE_DISCOVERY_PROBABILITY_GAMMA = 0.5;

  private Random rng;
  private List<RemoteConfig> remotes;

  public RandomWalk(ArgsParser args) {
    this.remotes = List.of(
          new RemoteConfig(
                new RemoteProto(
                      Set.of(RandomWalk.BASE_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.LONG_RANGE_COMMS,
                                        Set.of(RandomWalk.BASE_COMMS),
                                        Set.of(RandomWalk.DRONE_COMMS),
                                        new SensorStats(RandomWalk.LRC_BATT_USAGE, 1, 0)
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
                                        RandomWalk.LONG_RANGE_COMMS,
                                        Set.of(RandomWalk.DRONE_COMMS),
                                        Set.of(RandomWalk.BASE_COMMS),
                                        new SensorStats(RandomWalk.LRC_BATT_USAGE, 1, 0)
                                      ),
                                  1,
                                  true
                                ),
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.DRONE_CAMERA,
                                        Set.of(RandomWalk.DRONE_CAMERA),
                                        Set.of(RandomWalk.VICTIM_TAG),
                                        new SensorStats(
                                              RandomWalk.CAM_BATT_USAGE,
                                              RandomWalk.DRONE_DISCOVERY_PROBABILITY_BETA,
                                              0,
                                              RandomWalk.CAM_RANGE
                                            )
                                      ),
                                  1,
                                  true
                                ),
                            new SensorConfig(
                                  new SensorProto(
                                        RandomWalk.BLE_COMMS,
                                        Set.of(RandomWalk.DRONE_COMMS_BLE),
                                        Set.of(RandomWalk.VICTIM_COMMS_BLE),
                                        new SensorStats(
                                              RandomWalk.BLE_BATT_USAGE,
                                              RandomWalk.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                              0,
                                              RandomWalk.BLE_RANGE
                                            )
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
                                        RandomWalk.BLE_COMMS,
                                        Set.of(RandomWalk.VICTIM_COMMS_BLE),
                                        Set.of(RandomWalk.DRONE_COMMS_BLE),
                                        new SensorStats(
                                              RandomWalk.BLE_BATT_USAGE,
                                              RandomWalk.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                              0,
                                              RandomWalk.BLE_RANGE
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
                                  RandomWalk.VICTIM_INITIAL_VELOCITY,
                                  RandomWalk.VICTIM_MAX_VELOCITY,
                                  RandomWalk.VICTIM_MAX_ACCELERATION
                                )
                          )
                    ),
                RandomWalk.VICTIM_COLOR,
                RandomWalk.VICTIM_COUNT,
                true,
                false
              )
      );
    this.rng = new Random(this.getSeed());
  }

  public Optional<Grid> getGrid() {
    return Optional.of(new Grid(RandomWalk.GRID_SIZE, RandomWalk.GRID_SIZE, RandomWalk.ZONE_SIZE));
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

  public double getStepSize() {
    return RandomWalk.STEP_SIZE;
  }
  
  public boolean hasGrid() {
    return true;
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    return snap
        .getRemoteStates()
        .stream()
        .filter(state -> state.isActive() && state.isMobile() && state.hasTag(RandomWalk.DRONE_TAG))
        .map(state -> {
          IntentionSet intentions = new IntentionSet(state.getRemoteID());
          System.out.println(state.getFuelAmount());
          if (state.isInMotion() && !Vector.near(state.getSpeed(), RandomWalk.DRONE_MAX_VELOCITY)) {
            intentions.addIntention(
                  IntentRegistry.Move(state.getVelocity().getUnitVector().scale(RandomWalk.DRONE_MAX_ACCELERATION))
                );
          } else if (state.isInMotion()) {
            intentions.addIntention(IntentRegistry.Steer(this.rng.getRandomDirection2D()));
          } else if (this.hasGrid()) {
            intentions.addIntention(
                  IntentRegistry.Move(this.rng.getRandomDirection2D().scale(RandomWalk.DRONE_MAX_ACCELERATION))
                );
          } else {
            intentions.addIntention(IntentRegistry.None());
          }
          return intentions;
        })
        .collect(Collectors.toList()); 
  }
}
