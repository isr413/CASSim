package com.seat.sim.common.remote.intent;

import java.util.Collection;

import com.seat.sim.common.json.Json;
import com.seat.sim.common.json.JsonException;
import com.seat.sim.common.math.Vector;

public class IntentRegistry {

  public static ActivateIntention ActivateAllSensors() {
    return new ActivateIntention();
  }

  public static ActivateIntention ActivateSensor(String sensorID) {
    return new ActivateIntention(sensorID);
  }

  public static ActivateIntention ActivateSensors(Collection<String> sensorIDs) {
    return new ActivateIntention(sensorIDs);
  }

  public static DeactivateIntention DeactivateAllSensors() {
    return new DeactivateIntention();
  }

  public static DeactivateIntention DeactivateSensor(String sensorID) {
    return new DeactivateIntention(sensorID);
  }

  public static DeactivateIntention DeactivateSensors(Collection<String> sensorIDs) {
    return new DeactivateIntention(sensorIDs);
  }

  public static DoneIntention Done() {
    return new DoneIntention();
  }

  public static Intention From(Json json) throws JsonException {
    if (!json.isJsonObject()) {
      throw new JsonException(String.format("Cannot decode intention type of %s", json.toString()));
    }
    switch (IntentionType.decode(json.getJsonObject())) {
      case ACTIVATE:
        return new ActivateIntention(json);
      case DEACTIVATE:
        return new DeactivateIntention(json);
      case DONE:
        return new DoneIntention();
      case GOTO:
        return new GoToIntention(json);
      case MOVE:
        return new MoveIntention(json);
      case PUSH:
        return new PushIntention(json);
      case SHUTDOWN:
        return new ShutdownIntention();
      case STARTUP:
        return new StartupIntention();
      case STEER:
        return new SteerIntention(json);
      case STOP:
        return new StopIntention();
      default:
        return new NoneIntention();
    }
  }

  public static GoToIntention GoHome() {
    return new GoToIntention();
  }

  public static GoToIntention GoTo(Vector location) {
    return new GoToIntention(location);
  }

  public static GoToIntention GoTo(Vector location, double maxVelocity) {
    return new GoToIntention(location, maxVelocity);
  }

  public static GoToIntention GoTo(Vector location, double maxVelocity, double maxAcceleration) {
    return new GoToIntention(location, maxVelocity, maxAcceleration);
  }

  public static MoveIntention Move() {
    return new MoveIntention();
  }

  public static MoveIntention Move(Vector acceleration) {
    return new MoveIntention(acceleration);
  }

  public static NoneIntention None() {
    return new NoneIntention();
  }

  public static PushIntention Push() {
    return new PushIntention();
  }

  public static PushIntention Push(Vector force) {
    return new PushIntention(force);
  }

  public static ShutdownIntention Shutdown() {
    return new ShutdownIntention();
  }

  public static StartupIntention Startup() {
    return new StartupIntention();
  }

  public static SteerIntention Steer() {
    return new SteerIntention();
  }

  public static SteerIntention Steer(Vector direction) {
    return new SteerIntention(direction);
  }

  public static StopIntention Stop() {
    return new StopIntention();
  }
}
