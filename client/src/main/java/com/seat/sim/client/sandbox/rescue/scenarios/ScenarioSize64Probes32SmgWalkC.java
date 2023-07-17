package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.SmgTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes32SmgWalkC extends RescueScenario {

  public ScenarioSize64Probes32SmgWalkC(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32SmgWalkC",     // scenarioID
        0,                                    // base count
        32,                                   // drone count
        1024,                                 // victim count
        3,                                    // cooldown
        Range.Inclusive(0.0, 1.0, 0.25),      // alpha
        0.,                                   // beta
        Range.Inclusive(0.5, 1.0, 0.25),      // gamma
        20,                                   // trials
        threadID,                             // threadID
        4,                                    // threadCount
        seed                                  // seed
      );
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new SmgTaskManager(this));
  }
}
