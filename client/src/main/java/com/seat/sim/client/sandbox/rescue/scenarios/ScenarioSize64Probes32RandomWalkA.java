package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.RandomWalkTaskManager;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.util.ArgsParser;

public class ScenarioSize64Probes32RandomWalkA extends RescueScenario {

  public ScenarioSize64Probes32RandomWalkA(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32RandomWalkA",  // scenarioID
        0,                                    // base count
        32,                                   // drone count
        1024,                                 // victim count
        0,                                    // cooldown
        0.875,                                // alpha
        0.75,                                 // beta
        0.,                                   // gamma
        100,                                  // trials
        threadID,                             // threadID
        4,                                    // threadCount
        seed                                  // seed
      );
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new RandomWalkTaskManager(this));
  }
}
