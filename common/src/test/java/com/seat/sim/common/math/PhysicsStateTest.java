package com.seat.sim.common.math;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;

public class PhysicsStateTest {

  public static PhysicsState MockPhysicsState() {
    return PhysicsStateTest.MockPhysicsState(false);
  }

  public static PhysicsState MockPhysicsState(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new PhysicsState(Vector.ZERO)
        : new PhysicsState(Vector.ZERO, new Vector(1., 1., 1.), 2., 1.);
  }

  @Test
  public void physicsStateToJson() {
    PhysicsState proto = PhysicsStateTest.MockPhysicsState();
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(new Vector(json.getJson(PhysicsState.LOCATION)), is(proto.getLocation()));
    assertThat(new Vector(json.getJson(PhysicsState.VELOCITY)), is(proto.getVelocity()));
    assertThat(json.getDouble(PhysicsState.MAX_VELOCITY), closeTo(proto.getMaxVelocity(), Vector.PRECISION));
    assertThat(json.getDouble(PhysicsState.MAX_ACCELERATION), closeTo(proto.getMaxAcceleration(), Vector.PRECISION));
  }

  @Test
  public void jsonToPhysicsState() {
    PhysicsState proto = PhysicsStateTest.MockPhysicsState();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(PhysicsState.LOCATION, proto.getLocation().toJson());
    json.put(PhysicsState.VELOCITY, proto.getVelocity().toJson());
    json.put(PhysicsState.MAX_VELOCITY, proto.getMaxVelocity());
    json.put(PhysicsState.MAX_ACCELERATION, proto.getMaxAcceleration());
    assertThat(new PhysicsState(json.toJson()), is(proto));
  }

  @Test
  public void physicsStateDecode() {
    PhysicsState proto = PhysicsStateTest.MockPhysicsState();
    assertThat(new PhysicsState(proto.toJson()), is(proto));
  }

  @Test
  public void physicsStatePartialDecode() {
    PhysicsState proto = PhysicsStateTest.MockPhysicsState(true);
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(new Vector(json.getJson(PhysicsState.LOCATION)), is(proto.getLocation()));
    assertThat(new Vector(json.getJson(PhysicsState.VELOCITY)), is(proto.getVelocity()));
    assertThat(json.hasKey(PhysicsState.MAX_VELOCITY), is(false));
    assertThat(json.hasKey(PhysicsState.MAX_ACCELERATION), is(false));
    assertThat(new PhysicsState(proto.toJson()), is(proto));
  }

  @Test
  public void hasMaxAcceleration() {
    assertThat(PhysicsStateTest.MockPhysicsState().hasMaxAcceleration(), is(true));
    assertThat(new PhysicsState(Vector.ZERO, Vector.ZERO).hasMaxAcceleration(), is(false));
  }

  @Test
  public void hasMaxVelocity() {
    assertThat(PhysicsStateTest.MockPhysicsState().hasMaxVelocity(), is(true));
    assertThat(new PhysicsState(Vector.ZERO, Vector.ZERO, Double.POSITIVE_INFINITY, 1.).hasMaxVelocity(), is(false));
  }

  @Test
  public void isMobile() {
    assertThat(PhysicsStateTest.MockPhysicsState().isMobile(), is(true));
    assertThat(new PhysicsState(Vector.ZERO, new Vector(1., 1., 1.), 0.).isMobile(), is(true));
    assertThat(new PhysicsState(Vector.ZERO, Vector.ZERO, 1., 1.).isMobile(), is(true));
    assertThat(new PhysicsState(Vector.ZERO, Vector.ZERO, 1., 0.).isMobile(), is(false));
    assertThat(new PhysicsState(Vector.ZERO, Vector.ZERO, 0., 1.).isMobile(), is(false));
    assertThat(new PhysicsState(Vector.ZERO, Vector.ZERO, 0., 0.).isMobile(), is(false));
  }
}