package com.seat.sim.client.sandbox.rescue.scenarios;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.StochasticHeatmap;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class ScenarioSize64Probes32SmgWalkB extends RescueScenario {

  public ScenarioSize64Probes32SmgWalkB(ArgsParser args, int threadID, long seed) throws IOException {
    super(
        "ScenarioSize64Probes32SmgWalkB",     // scenarioID
        0,                                    // base count
        32,                                   // drone count
        1024,                                 // victim count
        0,                                    // cooldown
        Range.Inclusive(0.0, 1.0, 0.125),     // alpha
        Range.Inclusive(0.125, 1.0, 0.125),   // beta
        0.,                                   // gamma
        10,                                   // trials
        threadID,                             // threadID
        4,                                    // threadCount
        seed,                                 // seed
        "seedsBC"                             // seedFile
      );
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new StochasticHeatmap(this, false, true, true));
  }
}
