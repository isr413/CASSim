package com.seat.sim.client.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

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

public class BmpExample implements Application {

  // Scenario info
  public static final String SCENARIO_ID = "BmpExample";
  public static final int MISSION_LENGTH = 20 * 81; // paper: "81 turns", each turn is 20 seconds
  public static final double STEP_SIZE = 0.5;

  public static final String BMP_FILE = "resources/example2.bmp";
  public static final int GRID_SIZE = 480; // paper: "... uses a 64x64 grid"
  public static final int ZONE_SIZE = 1; // paper: "Each cell has an area of 200 m^2"
 
  public static final Vector GRID_CENTER = new Vector(
        BmpExample.GRID_SIZE * BmpExample.ZONE_SIZE / 2,
        BmpExample.GRID_SIZE * BmpExample.ZONE_SIZE / 2
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
  public static final Vector DRONE_INITIAL_VELOCITY = Vector.ZERO;
  public static final double DRONE_MAX_ACCELERATION = 20.;
  public static final double DRONE_MAX_VELOCITY = 20.;
  public static final String DRONE_TAG = "Drone";

  // Victim info
  public static final TeamColor VICTIM_COLOR = TeamColor.PINK;
  public static final String VICTIM_COMMS_BLE = "Victim_BLE";
  public static final int VICTIM_COUNT = 64; // paper: "1024 victims"
  public static final Vector VICTIM_INITIAL_VELOCITY = Vector.ZERO;
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

  public BmpExample(ArgsParser args) {
    this.remotes = List.of(
          new RemoteConfig(
                new RemoteProto(
                      Set.of(BmpExample.BASE_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        BmpExample.LONG_RANGE_COMMS,
                                        Set.of(BmpExample.BASE_COMMS),
                                        Set.of(BmpExample.DRONE_COMMS),
                                        new SensorStats(BmpExample.LRC_BATT_USAGE, 1, 0)
                                      ),
                                  1,
                                  true
                                )
                          ),
                      new KinematicsProto(BmpExample.GRID_CENTER)
                    ),
                BmpExample.BASE_COLOR,
                BmpExample.BASE_COUNT,
                true,
                false
              ),
          new RemoteConfig(
                new RemoteProto(
                      Set.of(BmpExample.DRONE_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        BmpExample.LONG_RANGE_COMMS,
                                        Set.of(BmpExample.DRONE_COMMS),
                                        Set.of(BmpExample.BASE_COMMS),
                                        new SensorStats(BmpExample.LRC_BATT_USAGE, 1, 0)
                                      ),
                                  1,
                                  true
                                ),
                            new SensorConfig(
                                  new SensorProto(
                                        BmpExample.DRONE_CAMERA,
                                        Set.of(BmpExample.DRONE_CAMERA),
                                        Set.of(BmpExample.VICTIM_TAG),
                                        new SensorStats(
                                              BmpExample.CAM_BATT_USAGE,
                                              BmpExample.DRONE_DISCOVERY_PROBABILITY_BETA,
                                              0,
                                              BmpExample.CAM_RANGE
                                            )
                                      ),
                                  1,
                                  true
                                ),
                            new SensorConfig(
                                  new SensorProto(
                                        BmpExample.BLE_COMMS,
                                        Set.of(BmpExample.DRONE_COMMS_BLE),
                                        Set.of(BmpExample.VICTIM_COMMS_BLE),
                                        new SensorStats(
                                              BmpExample.BLE_BATT_USAGE,
                                              BmpExample.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                              0,
                                              BmpExample.BLE_RANGE
                                            )
                                      ),
                                  1,
                                  true
                                )
                          ),
                      new KinematicsProto(
                            BmpExample.GRID_CENTER,
                            new FuelProto(1., 1., BmpExample.DRONE_FUEL_USAGE),
                            new MotionProto(
                                  BmpExample.DRONE_INITIAL_VELOCITY,
                                  BmpExample.DRONE_MAX_VELOCITY,
                                  BmpExample.DRONE_MAX_ACCELERATION
                                )
                          )
                    ),
                BmpExample.DRONE_COLOR,
                BmpExample.DRONE_COUNT,
                true,
                true
              ),
          new RemoteConfig(
                new RemoteProto(
                      Set.of(BmpExample.VICTIM_TAG),
                      List.of(
                            new SensorConfig(
                                  new SensorProto(
                                        BmpExample.BLE_COMMS,
                                        Set.of(BmpExample.VICTIM_COMMS_BLE),
                                        Set.of(BmpExample.DRONE_COMMS_BLE),
                                        new SensorStats(
                                              BmpExample.BLE_BATT_USAGE,
                                              BmpExample.DRONE_DISCOVERY_PROBABILITY_GAMMA,
                                              0,
                                              BmpExample.BLE_RANGE
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
                                  BmpExample.VICTIM_INITIAL_VELOCITY,
                                  BmpExample.VICTIM_MAX_VELOCITY,
                                  BmpExample.VICTIM_MAX_ACCELERATION
                                )
                          )
                    ),
                BmpExample.VICTIM_COLOR,
                BmpExample.VICTIM_COUNT,
                true,
                false
              )
      );
    this.rng = new Random(this.getSeed());
  }

  public Optional<Grid> getGrid() {
    try {
      return Optional.of(new Grid(ImageIO.read(new File(BmpExample.BMP_FILE)), BmpExample.ZONE_SIZE));
    } catch (IOException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }

  public int getMissionLength() {
    return BmpExample.MISSION_LENGTH;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public String getScenarioID() {
    return BmpExample.SCENARIO_ID;
  }

  public double getStepSize() {
    return BmpExample.STEP_SIZE;
  }
  
  public boolean hasGrid() {
    return true;
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    return snap
        .getRemoteStates()
        .stream()
        .filter(state -> state.isActive() && state.isMobile() && state.hasTag(BmpExample.DRONE_TAG))
        .map(state -> {
          IntentionSet intentions = new IntentionSet(state.getRemoteID());
          if (state.isInMotion() && !Vector.near(state.getSpeed(), BmpExample.DRONE_MAX_VELOCITY)) {
            intentions.addIntention(
                  IntentRegistry.Move(state.getVelocity().getUnitVector().scale(BmpExample.DRONE_MAX_ACCELERATION))
                );
          } else if (state.isInMotion()) {
            intentions.addIntention(IntentRegistry.Steer(this.rng.getRandomDirection2D()));
          } else if (this.hasGrid()) {
            intentions.addIntention(
                  IntentRegistry.Move(this.rng.getRandomDirection2D().scale(BmpExample.DRONE_MAX_ACCELERATION))
                );
          } else {
            intentions.addIntention(IntentRegistry.None());
          }
          return intentions;
        })
        .collect(Collectors.toList()); 
  }
}
