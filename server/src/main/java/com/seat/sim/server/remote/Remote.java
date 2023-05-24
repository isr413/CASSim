package com.seat.sim.server.remote;

import java.util.Optional;
import java.util.Set;

import com.seat.sim.common.gui.TeamColor;
import com.seat.sim.common.math.RemotePhysics;
import com.seat.sim.common.math.PhysicsState;
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
import com.seat.sim.server.remote.components.Destination;
import com.seat.sim.server.remote.components.Kinematics;
import com.seat.sim.server.remote.components.SensorController;
import com.seat.sim.server.scenario.Scenario;

public class Remote {

  private boolean active;
  private Optional<Destination> destination;
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
    this.destination = Optional.empty();
  }

  private void shutoff(double stepSize) {
    if (this.isActive()) {
      this.setInactive();
    }
    this.stop(stepSize);
  }

  private void stop(double stepSize) {
    if (this.hasDestination()) {
      this.destination = Optional.empty();
    }
    if (this.hasKinematics() && this.getKinematics().isInMotion()) {
      Vector delta = this.getKinematics().shiftVelocityTo(Vector.ZERO);
      this.getKinematics().update(delta, stepSize);
    }
  }

  public Destination getDestination() {
    return this.destination.get();
  }

  public Vector getDirection() {
    return this.getVelocity().getUnitVector();
  }

  public double getFuelAmount() {
    if (this.kinematics.isEmpty()) {
      return 0.;
    }
    return this.getKinematics().getFuelAmount();
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

  public double getMaxAcceleration() {
    if (!this.hasKinematics()) {
      return 0.;
    }
    return this.getKinematics().getMaxAcceleration();
  }

  public double getMaxVelocity() {
    if (!this.hasKinematics()) {
      return 0.;
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
    if (this.hasFuel()) {
      return new RemoteState(
          this.getRemoteID(),
          this.getTags(),
          this.getTeam(),
          (this.hasLocation()) ? this.toPhysicsState() : null,
          this.getKinematics().getFuelAmount(),
          (this.hasSensors()) ? this.getSensorController().getSensorStates() : null,
          this.isActive(),
          this.isDone());
    }
    return new RemoteState(
        this.getRemoteID(),
        this.getTags(),
        this.getTeam(),
        (this.hasLocation()) ? this.toPhysicsState() : null,
        (this.hasSensors()) ? this.getSensorController().getSensorStates() : null,
        this.isActive(),
        this.isDone());
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

  public boolean hasDestination() {
    return this.destination.isPresent();
  }

  public boolean hasFuel() {
    return this.hasKinematics() && this.getKinematics().hasFuel();
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

  public boolean hasMaxAcceleration() {
    return this.hasKinematics() && this.getKinematics().hasMaxAcceleration();
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
    return !this.hasFuel() || !this.getKinematics().isFuelEmpty();
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
    this.destination = Optional.empty();
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

  public void shiftToDestination(double stepSize) {
    if (!this.hasDestination()) {
      return;
    }
    double timeToReach = RemotePhysics.timeToReachDest(
        this.toPhysicsState(),
        this.getDestination().getLocation()
      );
    if (timeToReach <= stepSize) {
      if (!this.hasMaxVelocity() && !this.getDestination().hasMaxVelocity()) {
        this.getKinematics().updateFuelBy(
            -this.getKinematics().getRemoteFuelUsage(
                Vector.dist(this.getLocation(), this.getDestination().getLocation())
              ),
            1.
          );
      } else {
        double maxVelocity = Math.min(this.getMaxVelocity(), this.getDestination().getMaxVelocity());
        double maxAcceleration = Math.min(this.getMaxAcceleration(), this.getDestination().getMaxAcceleration());
        double timeToZero = (Double.isFinite(maxAcceleration) && maxAcceleration > 0.)
          ? this.getSpeed() / maxAcceleration
          : 0.;
        if (timeToZero >= timeToReach) {
          this.getKinematics().updateFuelBy(
              -this.getKinematics().getRemoteFuelUsage(this.getSpeed()),
              1.
            );
        } else {
          double timeToMax = (Double.isFinite(maxAcceleration) && maxAcceleration > 0.)
            ? (maxVelocity - this.getSpeed()) / maxAcceleration
            : 0.;
          if (2 * timeToMax + timeToZero >= timeToReach) {
            this.getKinematics().updateFuelBy(
                -this.getKinematics().getRemoteFuelUsage(
                    this.getSpeed() + this.getMaxAcceleration() * (timeToReach - timeToZero)
                  ),
                1.
              );
          } else {
            this.getKinematics().updateFuelBy(
                -this.getKinematics().getRemoteFuelUsage(2 * maxVelocity - this.getSpeed()),
                1.
              );
          }
        }
      }
      this.setLocationTo(this.getDestination().getLocation());
      this.setVelocityTo(Vector.ZERO);
      this.destination = Optional.empty();
      this.getKinematics().update(stepSize);
      return;
    }
    Vector delta = this.getKinematics().shiftLocationTo(
        this.getDestination().getLocation(),
        (this.hasMaxVelocity())
            ? Math.min(this.getDestination().getMaxVelocity(), this.getMaxVelocity())
            : this.getDestination().getMaxVelocity(),
        (this.hasMaxAcceleration())
            ? Math.min(this.getDestination().getMaxAcceleration(), this.getMaxAcceleration())
            : this.getDestination().getMaxAcceleration()
      );
    this.getKinematics().update(delta, stepSize);
    if (this.getLocation().near(this.getDestination().getLocation())) {
      this.destination = Optional.empty();
    }
  }

  public PhysicsState toPhysicsState() {
    return new PhysicsState(
        this.getLocation(),
        this.getVelocity(),
        (this.hasDestination())
            ? Math.min(this.getDestination().getMaxVelocity(), this.getMaxVelocity())
            : this.getMaxVelocity(),
        (this.hasDestination())
            ? Math.min(this.getDestination().getMaxAcceleration(), this.getMaxAcceleration())
            : this.getMaxAcceleration()
      );
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
      this.getKinematics().updateFuelBy(-this.getSensorController().getSensorFuelUsage(), stepSize);
    }
    if (!this.isActive() || !this.isEnabled() || this.isDone()) {
      return;
    }
    if (!this.hasKinematics() || !this.getKinematics().hasLocation()) {
      return;
    }
    if (intentions == null || !intentions.hasIntentions() || !this.isMobile()) {
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
      this.destination = Optional.of(
          new Destination(
            (intent.hasLocation()) ? intent.getLocation() : this.getKinematics().getHomeLocation(),
            intent.getMaxVelocity(),
            intent.getMaxAcceleration()
          )
        );
      this.shiftToDestination(stepSize);
      return;
    } 
    if (intentions.hasIntentionWithType(IntentionType.MOVE)) {
      MoveIntention intent = (MoveIntention) intentions.getIntentionWithType(IntentionType.MOVE);
      if (intent.hasAcceleration()) {
        this.destination = Optional.empty();
        this.getKinematics().update(intent.getAcceleration(), stepSize);
      } else if (this.hasDestination()) {
        this.shiftToDestination(stepSize);
      } else {
        this.getKinematics().update(stepSize);
      }
      return;
    }
    if (intentions.hasIntentionWithType(IntentionType.STEER)) {
      SteerIntention intent = (SteerIntention) intentions.getIntentionWithType(IntentionType.STEER);
      if (intent.hasDirection()) {
        Vector delta = this
            .getKinematics()
            .shiftVelocityTo(intent.getDirection().getUnitVector().scale(this.getSpeed()));
        this.destination = Optional.empty();
        this.getKinematics().update(delta, stepSize);
      } else if (this.hasDestination()) {
        Vector direction = Vector.sub(this.getDestination().getLocation(), this.getLocation());
        Vector delta = this
            .getKinematics()
            .shiftVelocityTo(direction.getUnitVector().scale(this.getSpeed()));
        this.getKinematics().update(delta, stepSize);
      } else {
        this.getKinematics().update(stepSize);
      }
      return;
    }
    if (this.hasDestination()) {
      this.shiftToDestination(stepSize);
      return;
    }
    this.getKinematics().update(stepSize);
  }
}
