package com.seat.sim.client.sandbox.rescue;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.seat.sim.client.sandbox.rescue.util.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class RandomFixed extends RescueScenario {

  public RandomFixed(ArgsParser args, int threadID, long seed) throws IOException {
    super("RandomFixed", 1, 32, 1024, 6, Range.Inclusive(0., 1., .1), 0.6, 0.6, 12, threadID, 4, seed);
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.of(new RandomFixedTaskManager(this));
  }

  private static class RandomFixedTaskManager implements TaskManager {

    private RescueScenario scenario;
    private List<Zone> tasks;

    public RandomFixedTaskManager(RescueScenario scenario) {
      this.scenario = scenario;
    }

    public void addTask(Zone task) {
      this.tasks.add(task);
    }

    public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
      if (state.getLocation().near(RescueScenario.GRID_CENTER)) {
        return (this.hasNextTask()) ? Optional.of(this.tasks.remove(0)) : Optional.empty();
      }
      return Optional.empty();
    }

    public boolean hasNextTask() {
      return !this.tasks.isEmpty();
    }

    public void init() {
      this.tasks = scenario.getZones().collect(Collectors.toCollection(LinkedList::new));
      Collections.shuffle(this.tasks, scenario.getRng().unwrap());
    }

    public void reset() {
      this.init();
    }
  }
}
