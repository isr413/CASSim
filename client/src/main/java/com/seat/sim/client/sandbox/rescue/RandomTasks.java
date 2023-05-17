package com.seat.sim.client.sandbox.rescue;

import java.io.IOException;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.util.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class RandomTasks extends RescueScenario {

  public RandomTasks(ArgsParser args, int threadID, long seed) throws IOException {
    super("RandomTasks", 1, 32, 1024, 6, Range.Inclusive(0., 1., .1), 0.6, 0.6, 12, threadID, 4, seed);
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new RandomTaskManager(this));
  }

  private static class RandomTaskManager implements TaskManager {

    private RescueScenario scenario;

    public RandomTaskManager(RescueScenario scenario) {
      this.scenario = scenario;
    }

    public void addTask(Zone task) {}

    public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
      Grid grid = this.scenario.getGrid().get();
      return Optional.of(grid.getZoneAtLocation(
          this.scenario.getRng().getRandomPoint(grid.getWidth()),
          this.scenario.getRng().getRandomPoint(grid.getHeight())
        ));
    }

    public boolean hasNextTask() {
      return true;
    }
  }
}
