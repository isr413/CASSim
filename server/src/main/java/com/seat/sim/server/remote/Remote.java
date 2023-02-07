package com.seat.sim.server.remote;

import java.util.Optional;
import java.util.Set;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.ActivateIntention;
import com.seat.sim.common.remote.intent.DeactivateIntention;
import com.seat.sim.common.remote.intent.GoToIntention;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.intent.IntentionType;
import com.seat.sim.common.remote.intent.MoveIntention;
import com.seat.sim.common.remote.intent.PushIntention;
import com.seat.sim.common.remote.intent.SteerIntention;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.remote.components.Kinematics;
import com.seat.sim.server.remote.components.SensorController;
import com.seat.sim.server.scenario.Scenario;

public class Remote {

  private boolean active;
  private boolean done;
  private Optional<Kinematics> kinematics;
  private RemoteProto proto;
  private String remoteID;
  private Optional<SensorController> sensors;
  private TeamColor team;

  public Remote(Scenario scenario, RemoteProto proto, String remoteID, TeamColor team, boolean active) {
    this.proto = (proto != null) ? proto : new RemoteProto();
    this.remoteID = remoteID;
    this.team = team;
    this.kinematics = (proto.hasKinematicsProto()) ? Optional.of(new Kinematics(proto.getKinematicsProto()))
        : Optional.empty();
    this.active = active;
    this.done = false;
    this.sensors = (proto.hasSensors()) ? Optional.of(new SensorController(scenario, this)) : Optional.empty();
  }

  private void shutoff(double stepSize) {
    if (this.isActive()) {
      this.setInactive();
    }
    this.stop(stepSize);
  }

  private void stop(double stepSize) {
    if (this.hasKinematics() && this.getKinematics().isInMotion()) {
      Vector delta = this.getKinematics().shiftVelocityTo(Vector.ZERO);
      this.getKinematics().update(delta, stepSize);
    }
  }

  public Vector getDirection() {
    return this.getVelocity().getUnitVector();
  }

  public Kinematics getKinematics() {
    return this.kinematics.get();
  }

  public String getLabel() {
    return this.getRemoteID();
  }

  public Vector getLocation() {
    return this.getKinematics().getLocation();
  }

  public double getMaxVelocity() {
    if (!this.hasKinematics()) {
      return 0;
    }
    return this.getKinematics().getMaxVelocity();
  }

  public RemoteProto getProto() {
    return this.proto;
  }

  public String getRemoteID() {
    return this.remoteID;
  }

  public RemoteState getRemoteState() {
    if (this.hasKinematics() && this.getKinematics().hasFuel()) {
      return new RemoteState(
          this.getRemoteID(),
          this.getTags(),
          this.getTeam(),
          (this.hasKinematics() && this.getKinematics().hasLocation()) ? this.getKinematics().getLocation()
              : null,
          (this.hasKinematics() && this.getKinematics().isMobile())
              ? this.getKinematics().getVelocity()
              : null,
          this.getKinematics().getFuelAmount(),
          (this.hasSensors()) ? this.getSensorController().getSensorStates() : null,
          this.isActive());
    }
    return new RemoteState(
        this.getRemoteID(),
        this.getTags(),
        this.getTeam(),
        (this.hasKinematics() && this.getKinematics().hasLocation()) ? this.getKinematics().getLocation()
            : null,
        (this.hasKinematics() && this.getKinematics().isMobile())
            ? this.getKinematics().getVelocity()
            : null,
        (this.hasSensors()) ? this.getSensorController().getSensorStates() : null,
        this.isActive());
  }

  public SensorController getSensorController() {
    return this.sensors.get();
  }

  public double getSpeed() {
    return this.getVelocity().getMagnitude();
  }

  public Set<String> getTags() {
    return this.proto.getTags();
  }

  public TeamColor getTeam() {
    return this.team;
  }

  public Vector getVelocity() {
    if (!this.hasKinematics()) {
      return Vector.ZERO;
    }
    return this.getKinematics().getVelocity();
  }

  public boolean hasKinematics() {
    return this.kinematics.isPresent();
  }

  public boolean hasLocation() {
    return this.hasKinematics() && this.getKinematics().hasLocation();
  }

  public boolean hasMatch(Set<String> matchers) {
    return this.hasRemoteMatch(matchers) || this.hasSensorMatch(matchers);
  }

  public boolean hasMaxVelocity() {
    return this.hasKinematics() && this.getKinematics().hasMaxVelocity();
  }

  public boolean hasRemoteMatch(Set<String> matchers) {
    return this.proto.hasRemoteMatch(matchers);
  }

  public boolean hasSensorMatch(Set<String> matchers) {
    return this.hasSensors() && this.getSensorController().hasSensorWithMatch(matchers);
  }

  public boolean hasSensors() {
    return this.sensors.isPresent();
  }

  public boolean hasTag(String tag) {
    return this.proto.hasTag(tag);
  }

  public boolean hasTags() {
    return this.proto.hasTags();
  }

  public boolean hasTeam() {
    return !(this.team == null || this.team.equals(TeamColor.NONE));
  }

  public boolean isActive() {
    return this.active && this.isEnabled();
  }

  public boolean isDone() {
    return this.done;
  }

  public boolean isEnabled() {
    return !this.hasKinematics() || !this.getKinematics().hasFuel() || !this.getKinematics().isFuelEmpty();
  }

  public boolean isMobile() {
    return this.hasLocation() && this.getKinematics().isMobile();
  }

  public void setActive() {
    this.active = true;
  }

  public void setDone() {
    this.setInactive();
    this.done = true;
  }

  public void setInactive() {
    if (this.hasSensors()) {
      this.getSensorController().deactivateSensors();
    }
    this.active = false;
  }

  public void setLocationTo(Vector location) {
    if (!this.hasKinematics()) {
      return;
    }
    this.getKinematics().setLocationTo(location);
  }

  public void setVelocityTo(Vector velocity) {
    if (!this.hasKinematics()) {
      return;
    }
    this.getKinematics().setVelocityTo(velocity);
  }

  public void update(double stepSize) throws SimException {
    this.update(null, stepSize);
  }

  public void update(IntentionSet intentions, double stepSize) throws SimException {
    if (!this.isEnabled() || this.isDone()) {
      this.shutoff(stepSize);
      return;
    }
    if (intentions != null && intentions.hasIntentions()) {
      if (intentions.hasIntentionWithType(IntentionType.STARTUP)) {
        this.setActive();
      }
      if (intentions.hasIntentionWithType(IntentionType.SHUTDOWN)) {
        this.setInactive();
      }
      if (intentions.hasIntentionWithType(IntentionType.DONE)) {
        this.setDone();
      }
    }
    if (!this.isActive() || !this.isEnabled() || this.isDone()) {
      return;
    }
    if (this.hasSensors()) {
      if (intentions != null && intentions.hasIntentions()) {
        if (intentions.hasIntentionWithType(IntentionType.ACTIVATE)) {
          ActivateIntention intent = (ActivateIntention) intentions.getIntentionWithType(IntentionType.ACTIVATE);
          if (intent.hasActivations()) {
            this.getSensorController().activateSensors(intent.getActivations());
          } else {
            this.getSensorController().activateSensors();
          }
        }
        if (intentions.hasIntentionWithType(IntentionType.DEACTIVATE)) {
          DeactivateIntention intent = (DeactivateIntention) intentions.getIntentionWithType(IntentionType.DEACTIVATE);
          if (intent.hasDeactivations()) {
            this.getSensorController().deactivateSensors(intent.getDeactivations());
          } else {
            this.getSensorController().deactivateSensors();
          }
        }
      }
      this.getKinematics().updateFuelBy(this.getSensorController().getSensorFuelUsage(), stepSize);
    }
    if (!this.isActive() || !this.isEnabled() || this.isDone()) {
      return;
    }
    if (!this.hasKinematics() || !this.getKinematics().hasLocation()) {
      return;
    }
    if (intentions == null || !intentions.hasIntentions() || !this.getKinematics().isMobile()) {
      this.getKinematics().update(stepSize);
      return;
    }
    if (intentions.hasIntentionWithType(IntentionType.PUSH)) {
      PushIntention intent = (PushIntention) intentions.getIntentionWithType(IntentionType.PUSH);
      if (intent.hasForce()) {
        this.getKinematics().updateVelocityBy(intent.getForce(), stepSize);
      }
    }
    if (intentions.hasIntentionWithType(IntentionType.GOTO)) {
      GoToIntention intent = (GoToIntention) intentions.getIntentionWithType(IntentionType.GOTO);
      Vector delta = this.getKinematics().shiftLocationTo((intent.hasLocation()) ? intent.getLocation()
          : this.getKinematics().getHomeLocation(), intent.getMaxVelocity(), intent.getMaxAcceleration()); 
      this.getKinematics().update(delta, stepSize);
      return;
    } 
    if (intentions.hasIntentionWithType(IntentionType.MOVE)) {
      MoveIntention intent = (MoveIntention) intentions.getIntentionWithType(IntentionType.MOVE);
      if (intent.hasAcceleration()) {
        this.getKinematics().update(intent.getAcceleration(), stepSize);
      } else {
        this.getKinematics().update(stepSize);
      }
      return;
    }
    if (intentions.hasIntentionWithType(IntentionType.STEER)) {
      SteerIntention intent = (SteerIntention) intentions.getIntentionWithType(IntentionType.STEER);
      Vector direction = (intent.hasDirection()) ? intent.getDirection()
          : Vector.sub(this.getKinematics().getHomeLocation(), this.getKinematics().getLocation());
      Vector delta = this.getKinematics().shiftVelocityTo(
          direction.getUnitVector().scale(this.getKinematics().getMotion().getSpeed()));
      this.getKinematics().update(delta, stepSize);
      return;
    }
  }
}
