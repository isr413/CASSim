package com.seat.sim.common.core;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.Snapshot;

public interface Application {

  Optional<Grid> getGrid();

  int getMissionLength();

  Collection<RemoteConfig> getRemoteConfigs();

  default Set<String> getRemoteIDs() {
    return this.getRemoteConfigs()
        .stream()
        .map(config -> config.getRemoteIDs())
        .flatMap(remoteIDs -> remoteIDs.stream())
        .collect(Collectors.toSet());
  }

  default ScenarioConfig getScenarioConfig() {
    if (!hasGrid()) {
      return new ScenarioConfig(
        getScenarioID(),
        getSeed(),
        getMissionLength(),
        getStepSize(),
        getRemoteConfigs()
      );
    }
    return new ScenarioConfig(
      getScenarioID(),
      getSeed(),
      getGrid().get(),
      getMissionLength(),
      getStepSize(),
      getRemoteConfigs()
    );
  }

  String getScenarioID();

  default long getSeed() {
    return new Random().nextLong();
  }

  double getStepSize();

  boolean hasGrid();

  Collection<IntentionSet> update(Snapshot snap);
}
