package com.seat.sim.client.sandbox.rescue;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.util.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class Default extends RescueScenario {

  public Default(ArgsParser args, int threadID, long seed) throws IOException {
    super("Default", 1, 0, 1024, 0, new Range(0., .1, 1., true), 0., 0., 12, threadID, 4, seed);
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.empty();
  }
}
