package com.seat.sim.client.sandbox.rescue.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class GlobalTaskManager extends HeatmapTaskManager {

  public GlobalTaskManager(RescueScenario scenario) {
    super(scenario, true, true, true);
  }

  public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
    OptionalDouble maxWeight = Arrays.stream(this.zones)
        .flatMapToDouble((row) -> Arrays.stream(row))
        .max();
    if (maxWeight.isEmpty()) {
      return Optional.empty();
    }
    List<Zone> choices = IntStream.range(0, this.zones.length)
        .boxed()
        .flatMap((i) -> {
          return IntStream.range(0, this.zones[i].length)
              .mapToObj((j) -> this.scenario.getGrid().get().getZoneAtLocation(i, j));
        })
        .filter((zone) -> !this.defer.containsKey(this.getZoneKey(zone)))
        .filter((zone) -> Vector.near(this.getZoneWeight(zone), maxWeight.getAsDouble()))
        .toList();
    if (choices.isEmpty()) {
      return Optional.empty();
    }
    choices = new ArrayList<>(choices);
    Collections.shuffle(choices, this.scenario.getRng().unwrap());
    Zone choice = choices.get(0);
    this.deferZone(choice);
    return Optional.of(choice);
  }
}

