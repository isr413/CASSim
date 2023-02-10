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

  private Random rng;
  private List<RemoteConfig> remotes;

  public Default(ArgsParser args) {
    this.remotes = new ArrayList<RemoteConfig>() {{
      add(new RemoteConfig(
            new RemoteProto(null, null, new KinematicsProto(new Vector(240, 240), null, new MotionProto())),
            TeamColor.BLUE,
            10,
            true,
            false)
          );
    }};
    this.rng = new Random(this.getSeed());
  }

  public Optional<Grid> getGrid() {
    return Optional.of(new Grid(480, 480, 1));
  }

  public int getMissionLength() {
    return 360;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public String getScenarioID() {
    return this.getClass().getName();
  }

  public double getStepSize() {
    return 1;
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
