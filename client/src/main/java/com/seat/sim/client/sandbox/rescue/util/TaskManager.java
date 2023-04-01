package com.seat.sim.client.sandbox.rescue.util;

import java.util.Optional;

import com.seat.sim.common.math.Zone;

public interface TaskManager {

  public void addTask(Zone task);

  public void clear();

  public Optional<Zone> getNextTask();

  public boolean hasNextTask();
}
