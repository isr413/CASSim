package com.seat.sim.client.sandbox.rescue.util;

import java.util.Optional;

import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public interface TaskManager {

  void addTask(Zone task);

  default void close() {}

  default void init() {}

  Optional<Zone> getNextTask(Snapshot snap, RemoteState state);

  boolean hasNextTask();

  default double predict(Snapshot snap, RemoteState state, double deadline) {
    return predict(snap, state, 0., 0., deadline);
  }

  double predict(Snapshot snap, RemoteState state, double eDeadline, double eSuccess, double deadline);

  default void reset() {}

  default void update(Snapshot snap) {}
}
