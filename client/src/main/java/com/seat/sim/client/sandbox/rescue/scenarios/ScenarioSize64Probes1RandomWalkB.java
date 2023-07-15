package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.addons.RandomWalkTaskManager;
import com.seat.sim.client.sandbox.rescue.util.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes1RandomWalkB extends RescueScenario {

  public ScenarioSize64Probes1RandomWalkB(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes1RandomWalkB",   // scenarioID
        1,                                    // base count
        1,                                    // drone count
        1024,                                 // victim count
        0,                                    // cooldown
        Range.Inclusive(0.0, 1.0, 0.1),       // alpha
        1.,                                   // beta
        1.,                                   // gamma
        12,                                   // trials
        threadID,                             // threadID
        4,                                    // threadCount
        seed                                  // seed
      );
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new RandomWalkTaskManager(this));
  }
}
