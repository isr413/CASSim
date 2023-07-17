package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.SmgTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.util.ArgsParser;

public class ScenarioSize64Probes32WeightedWalkA extends RescueScenario {

  public ScenarioSize64Probes32WeightedWalkA(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32WeightedWalkA",  // scenarioID
        0,                                      // base count
        32,                                     // drone count
        1024,                                   // victim count
        0,                                      // cooldown
        1.,                                     // alpha
        1.,                                     // beta
        0.,                                     // gamma
        20,                                     // trials
        threadID,                               // threadID
        4,                                      // threadCount
        seed                                    // seed
      );
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new SmgTaskManager(this, false, false, false));
  }
}
