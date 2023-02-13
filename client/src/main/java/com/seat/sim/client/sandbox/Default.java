package com.seat.sim.client.sandbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.seat.sim.common.core.Application;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.kinematics.KinematicsProto;
import com.seat.sim.common.remote.kinematics.MotionProto;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Random;

public class Default implements Application {
  public static final int BLUE_COUNT = 128; // paper: "... have been equipped with 32 commercial drones"
  public static final int GRID_SIZE = 480; // paper: "... uses a 64x64 grid"
  public static final int MISSION_LENGTH = 360; // paper: "81 turns", each turn is 20 seconds
  public static final int RED_COUNT = 256; // paper: "1024 victims"
  public static final String SCENARIO_ID = "Default";
  public static final double STEP_SIZE = 1.;
  public static final int ZONE_SIZE = 1; // paper: "Each cell has an area of 200 m^2"

  private Random rng;
  private List<RemoteConfig> remotes;

  public Default(ArgsParser args) {
    this.remotes = new ArrayList<RemoteConfig>() {{
      add(new RemoteConfig(
            new RemoteProto(
                null,
                null,
                new KinematicsProto(
                    new Vector(Default.GRID_SIZE / 2, Default.GRID_SIZE / 2), 
                    null,
                    new MotionProto())),
            TeamColor.BLUE,
            Default.BLUE_COUNT,
            true,
            false)
          );
      add(new RemoteConfig(
            new RemoteProto(null, null, new KinematicsProto(null, null, new MotionProto())),
            TeamColor.RED,
            Default.RED_COUNT,
            true,
            false)
          );
    }};
    this.rng = new Random(this.getSeed());
  }

  public Optional<Grid> getGrid() {
    return Optional.of(new Grid(Default.GRID_SIZE, Default.GRID_SIZE, Default.ZONE_SIZE));
  }

  public int getMissionLength() {
    return Default.MISSION_LENGTH;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public String getScenarioID() {
    return Default.SCENARIO_ID;
  }

  public double getStepSize() {
    return Default.STEP_SIZE;
  }
  
  public boolean hasGrid() {
    return true;
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    return snap
        .getRemoteStates()
        .stream()
        .filter(state -> state.isActive() && state.isMobile())
        .map(state -> {
          IntentionSet intentions = new IntentionSet(state.getRemoteID());
          if (state.isInMotion()) {
            intentions.addIntention(IntentRegistry.Steer(this.rng.getRandomDirection2D()));
          } else if (this.hasGrid()) {
            intentions.addIntention(IntentRegistry.Move(this.rng.getRandomDirection2D().scale(this.getGrid().get().getZoneSize())));
          } else {
            intentions.addIntention(IntentRegistry.None());
          }
          return intentions;
        })
        .collect(Collectors.toList()); 
  }
}
