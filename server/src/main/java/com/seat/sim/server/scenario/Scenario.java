package com.seat.sim.server.scenario;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.ZoneType;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteController;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.ScenarioStatus;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.common.util.Random;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.math.Collision;
import com.seat.sim.server.math.Physics;
import com.seat.sim.server.remote.Remote;

public class Scenario {

  private Map<String, Remote> activeRemotes;
  private Map<String, Remote> allRemotes;
  private ScenarioConfig config;
  private Map<String, Remote> dynamicRemotes;
  private Random rng;
  private ScenarioStatus status;
  private double time;

  public Scenario(ScenarioConfig config) {
    this(config, new Random(config.getSeed()));
  }

  public Scenario(ScenarioConfig config, Random rng) {
    this.config = config;
    this.rng = rng;
    this.time = 0;
    this.status = ScenarioStatus.START;
    this.init();
  }

  private void enforceBounds(Remote remote, Vector prevLocation) {
    if (!this.hasGrid() || remote == null || prevLocation == null || remote.getLocation().near(prevLocation)) {
      return;
    }
    for (Optional<Collision> coll = Physics.getRayTrace(prevLocation, remote.getLocation(), this.getGrid());
         coll.isPresent();
         coll = Physics.getRayTrace(coll.get().getPoint(), remote.getLocation(), this.getGrid())
        ) {
      remote.setLocationTo(coll.get().reflect(remote.getLocation()));
      remote.setVelocityTo(Vector.sub(
            coll.get().reflect(Vector.add(coll.get().getPoint(), remote.getVelocity())),
            coll.get().getPoint()
          ));
    }
  }

  private void init() {
    this.allRemotes = new HashMap<>();
    this.activeRemotes = new HashMap<>();
    this.dynamicRemotes = new HashMap<>();
    for (RemoteConfig remoteConfig : this.config.getRemoteConfigs()) {
      RemoteProto remoteProto = remoteConfig.getProto();
      for (String remoteID : remoteConfig.getRemoteIDs()) {
        Remote remote = new Remote(this, remoteProto, remoteID, remoteConfig.getTeam(), remoteConfig.isActive());
        this.allRemotes.put(remoteID, remote);
        if (remoteConfig.isActive()) {
          this.activeRemotes.put(remoteID, remote);
        }
        if (remoteConfig.isDynamic()) {
          this.dynamicRemotes.put(remoteID, remote);
        }
        if (this.hasGrid() && !remote.hasLocation()) {
          while (!remote.hasLocation() ||
              this.getGrid().getZoneAtLocation(remote.getLocation()).hasZoneType(ZoneType.BLOCKED)) {
            remote.setLocationTo(this.rng.getRandomLocation2D(this.getGridWidth(), this.getGridHeight()));
          }
        }
        if (!remoteConfig.isDynamic() && remote.isActive() && remote.isMobile()) {
          if (remote.hasMaxVelocity()) {
            remote.setVelocityTo(this.rng.getRandomSpeed2D(remote.getMaxVelocity()));
          } else {
            remote.setVelocityTo(this.rng.getRandomDirection2D().scale(this.getZoneSize()));
          }
        }
      }
    }
  }

  public Collection<String> getActiveRemoteIDs() {
    return this.activeRemotes.keySet();
  }

  public Collection<Remote> getActiveRemotes() {
    return this.activeRemotes.values();
  }

  public Remote getActiveRemoteWithID(String remoteID) throws SimException {
    if (!this.hasActiveRemoteWithID(remoteID)) {
      throw new SimException(String.format("Scenario <%s> has no active remote %s", this.getScenarioID(),
        remoteID));
    }
    return this.activeRemotes.get(remoteID);
  }

  public Collection<String> getActiveDynamicRemoteIDs() {
    return this.getActiveRemoteIDs()
        .stream()
        .filter(remoteID -> this.hasDynamicRemoteWithID(remoteID))
        .collect(Collectors.toList());
  }

  public Collection<Remote> getActiveDynamicRemotes() {
    return this.getActiveDynamicRemoteIDs()
        .stream()
        .map(remoteID -> this.getRemoteWithID(remoteID))
        .collect(Collectors.toList());
  }

  public Collection<String> getDynamicRemoteIDs() {
    return this.dynamicRemotes.keySet();
  }

  public Collection<Remote> getDynamicRemotes() {
    return this.dynamicRemotes.values();
  }

  public Grid getGrid() {
    return this.config.getGrid();
  }

  public int getGridHeight() {
    if (!this.hasGrid()) {
      return 0;
    }
    return this.getGrid().getHeight();
  }

  public int getGridWidth() {
    if (!this.hasGrid()) {
      return 0;
    }
    return this.getGrid().getWidth();
  }

  public int getMissionLength() {
    return this.config.getMissionLength();
  }

  public Collection<String> getPassiveRemoteIDs() {
    return this.getRemoteIDs()
        .stream()
        .filter(remoteID -> !this.hasDynamicRemoteWithID(remoteID))
        .collect(Collectors.toList());
  }

  public Collection<Remote> getPassiveRemotes() {
    return this.getPassiveRemoteIDs()
        .stream()
        .map(remoteID -> this.getRemoteWithID(remoteID))
        .collect(Collectors.toList());
  }

  public Collection<String> getRemoteIDs() {
    return this.allRemotes.keySet();
  }

  public Collection<Remote> getRemotes() {
    return this.allRemotes.values();
  }

  public Map<String, RemoteState> getRemoteStates() {
    return this.getRemotes().stream().collect(Collectors.toMap(Remote::getRemoteID, Remote::getRemoteState));
  }

  public Remote getRemoteWithID(String remoteID) throws SimException {
    if (!this.hasRemoteWithID(remoteID)) {
      throw new SimException(String.format("Scenario <%s> has no remote %s", this.getScenarioID(),
        remoteID));
    }
    return this.allRemotes.get(remoteID);
  }

  public String getScenarioID() {
    return this.config.getScenarioID();
  }

  public Snapshot getSnapshot() {
    return new Snapshot(
      LocalTime.now().toString(),
      this.getScenarioID(),
      this.status,
      this.time,
      this.getStepSize(),
      this.getActiveRemoteIDs(),
      this.getDynamicRemoteIDs(),
      this.getRemoteStates()
    );
  }

  public ScenarioStatus getStatus() {
    return this.status;
  }

  public double getStepSize() {
    return this.config.getStepSize();
  }

  public double getTime() {
    return this.time;
  }

  public int getZoneSize() {
    if (!this.hasGrid()) {
      return 0;
    }
    return this.getGrid().getZoneSize();
  }

  public boolean hasActiveRemotes() {
    return !this.activeRemotes.isEmpty();
  }

  public boolean hasActiveRemoteWithID(String remoteID) {
    return this.activeRemotes.containsKey(remoteID);
  }

  public boolean hasActiveDynamicRemotes() {
    return !this.getActiveDynamicRemoteIDs().isEmpty();
  }

  public boolean hasActiveDynamicRemoteWithID(String remoteID) {
    return this.hasActiveRemoteWithID(remoteID) && this.hasDynamicRemoteWithID(remoteID);
  }

  public boolean hasDynamicRemotes() {
    return !this.dynamicRemotes.isEmpty();
  }

  public boolean hasDynamicRemoteWithID(String remoteID) {
    return this.dynamicRemotes.containsKey(remoteID);
  }

  public boolean hasError() {
    return this.status.equals(ScenarioStatus.ERROR);
  }

  public boolean hasGrid() {
    return this.config.hasGrid();
  }

  public boolean hasPassiveRemotes() {
    return this.dynamicRemotes.size() < this.allRemotes.size();
  }

  public boolean hasPassiveRemoteWithID(String remoteID) {
    return this.hasRemoteWithID(remoteID) && !this.hasDynamicRemoteWithID(remoteID);
  }

  public boolean hasRemotes() {
    return !this.allRemotes.isEmpty();
  }

  public boolean hasRemoteWithID(String remoteID) {
    return this.allRemotes.containsKey(remoteID);
  }

  public boolean isDone() {
    return this.status.equals(ScenarioStatus.DONE);
  }

  public boolean isInProgress() {
    return this.justStarted() || this.status.equals(ScenarioStatus.IN_PROGRESS);
  }

  public boolean justStarted() {
    return this.status.equals(ScenarioStatus.START);
  }

  public void setStatus(ScenarioStatus status) {
    this.status = status;
  }

  public void setTime(double time) throws SimException {
    if (time < 0 || this.getMissionLength() < time) {
      throw new SimException(String.format("Scenario %s cannot set time to %f", this.getScenarioID(),
        time));
    }
    this.time = time;
  }

  public void update() throws SimException {
    this.update(null, this.config.getStepSize());
  }

  public void update(double stepSize) throws SimException {
    this.update(null, stepSize);
  }

  public void update(Map<String, IntentionSet> intentions, double stepSize) throws SimException {
    this.time += stepSize;
    if (this.isDone()) {
        return;
    }
    if (this.hasError()) {
      this.setStatus(ScenarioStatus.IN_PROGRESS);
    }
    Debugger.logger.info("Updating remotes ...");
    this.updateRemotes(intentions, stepSize);
    if (this.justStarted()) {
      this.setStatus(ScenarioStatus.IN_PROGRESS);
    }
    if (this.getMissionLength() <= this.time) {
      this.setStatus(ScenarioStatus.DONE);
    }
  }

  private void updateRemotes(Map<String, IntentionSet> intentions, double stepSize) throws SimException {
    for (Remote remote : this.getRemotes()) {
      if (!remote.isEnabled() || remote.isDone()) {
        if (this.hasActiveRemoteWithID(remote.getRemoteID())) {
          this.activeRemotes.remove(remote.getRemoteID());
        }
        continue;
      }
      Debugger.logger.info(String.format("Updating remote %s ...", remote.getRemoteID()));
      Optional<Vector> prevLocation = Optional.empty();
      if (this.hasGrid() && remote.hasLocation() && Physics.isInbounds(remote.getLocation(), this.getGrid())) {
        prevLocation = Optional.of(remote.getLocation());
        if (remote.isMobile()) {
          if (this.getGrid().hasZoneAtLocation(prevLocation.get()) &&
              this.getGrid().getZoneAtLocation(prevLocation.get()).hasZoneType(ZoneType.BLOCKED)) {
            prevLocation = Optional.of(Vector.sub(prevLocation.get(), remote.getVelocity().scale(stepSize)));
          }
          if (!Vector.near(remote.getLocation(), prevLocation.get())) {
            this.enforceBounds(remote, prevLocation.get());
          }
        }
      }
      if (intentions != null && intentions.containsKey(remote.getRemoteID())) {
        remote.update(intentions.get(remote.getRemoteID()), stepSize);
      } else if (this.hasDynamicRemoteWithID(remote.getRemoteID())) {
        remote.update(stepSize);
      } else {
        this.updatePassiveRemote(remote, stepSize);
      }
      if (this.hasGrid() && remote.hasLocation() &&
          prevLocation.isPresent() && !remote.getLocation().near(prevLocation.get())) {
        this.enforceBounds(remote, prevLocation.get());
      }
      if ((!remote.isActive() || remote.isDone()) && this.hasActiveRemoteWithID(remote.getRemoteID())) {
        this.activeRemotes.remove(remote.getRemoteID());
      } else if (remote.isActive() && !this.hasActiveRemoteWithID(remote.getRemoteID())) {
        this.activeRemotes.put(remote.getRemoteID(), remote);
      }
    }
  }

  private void updatePassiveRemote(Remote remote, double stepSize) throws SimException {
    RemoteController controller = new RemoteController(remote.getRemoteID());
    if (this.justStarted() && remote.isActive()) {
      controller.activateAllSensors();
    }
    if ((!remote.isActive() || remote.isDone())) {
      controller.deactivateAllSensors();
    }
    if (this.hasGrid() && remote.hasLocation() && Physics.isInbounds(remote.getLocation(), this.getGrid())) {
      controller.maintainCourse();
    }
    remote.update(controller.getIntentions(), stepSize);
  }
}
