package com.seat.sim.client.sandbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.seat.sim.common.core.Application;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.kinematics.KinematicsProto;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;

public class Default implements Application {

  private List<RemoteConfig> remotes;

  public Default(ArgsParser args) {
    this.remotes = new ArrayList<RemoteConfig>() {{
      add(new RemoteConfig(
            new RemoteProto(null, null, new KinematicsProto(new Vector(240, 240), null, null)),
            TeamColor.BLUE,
            1,
            true,
            false)
          );
    }};
  }

  public Optional<Grid> getGrid() {
    return Optional.of(new Grid(480, 480, 1));
  }

  public int getMissionLength() {
    return 60;
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
    return null;
  }
}
