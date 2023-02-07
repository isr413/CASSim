package com.seat.sim.common.remote;

import java.util.Collection;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.Intention;
import com.seat.sim.common.remote.intent.IntentionSet;

/** A controller to signal intents to a Remote. */
public class RemoteController extends Jsonable {

  private IntentionSet intentions;

  public RemoteController(String remoteID) {
    this.intentions = new IntentionSet(remoteID);
  }

  public RemoteController(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(Json json) throws JsonException {
    this.intentions = new IntentionSet(json);
  }

  protected void addIntention(Intention intent) {
    this.intentions.addIntention(intent);
  }

  public void activateAllSensors() {
    this.addIntention(IntentRegistry.Activate());
  }

  public void activateAllSensors(Collection<String> sensorIDs) {
    this.addIntention(IntentRegistry.Activate(sensorIDs));
  }

  public void activateSensor(String sensorID) {
    this.addIntention(IntentRegistry.Activate(sensorID));
  }

  public void brake(Vector acceleration) {
    this.addIntention(IntentRegistry.Push(acceleration.invert()));
  }

  public void deactivateAllSensors() {
    this.addIntention(IntentRegistry.Deactivate());
  }

  public void deactivateAllSensors(Collection<String> sensorIDs) {
    this.addIntention(IntentRegistry.Deactivate(sensorIDs));
  }

  public void deactivateSensor(String sensorID) {
    this.addIntention(IntentRegistry.Deactivate(sensorID));
  }

  public void done() {
    this.addIntention(IntentRegistry.Done());
  }

  public IntentionSet getIntentions() {
    return this.intentions;
  }
  
  public void goHome() {
    this.addIntention(IntentRegistry.GoTo());
  }

  public void goToLocation(Vector location) {
    this.addIntention(IntentRegistry.GoTo(location));
  }

  public void goToLocation(Vector location, double maxVelocity) {
    this.addIntention(IntentRegistry.GoTo(location, maxVelocity));
  }

  public void goToLocation(Vector location, double maxVelocity, double maxAcceleration) {
    this.addIntention(IntentRegistry.GoTo(location, maxVelocity, maxAcceleration));
  }

  public boolean hasRemoteID(String remoteID) {
    return this.intentions.hasRemoteID(remoteID);
  }

  public void maintainCourse() {
    this.addIntention(IntentRegistry.Move());
  }

  public void move(Vector acceleration) {
    this.addIntention(IntentRegistry.Move(acceleration));
  }

  public void none() {
    this.addIntention(IntentRegistry.None());
  }

  public void push(Vector force) {
    this.addIntention(IntentRegistry.Push(force));
  }

  public void shutdown() {
    this.addIntention(IntentRegistry.Shutdown());
  }

  public void startup() {
    this.addIntention(IntentRegistry.Startup());
  }

  public void steer(Vector direction) {
    this.addIntention(IntentRegistry.Steer(direction));
  }
  
  public void steerHome() {
    this.addIntention(IntentRegistry.Steer());
  }

  public void stop() {
    this.addIntention(IntentRegistry.Stop());
  }

  public Json toJson() throws JsonException {
    return this.intentions.toJson();
  }
}
