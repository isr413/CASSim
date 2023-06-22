package com.seat.sim.common.remote.kinematics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.math.Vector;

public class MotionProtoTest {

  public static MotionProto MockMotionProto() {
    return MotionProtoTest.MockMotionProto(false);
  }

  public static MotionProto MockMotionProto(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new MotionProto(new Vector(1., 1., 1.))
        : new MotionProto(new Vector(1., 1., 1.), 1., 1.);
  }

  @Test
  public void motionProtoToJson() {
    MotionProto proto = MotionProtoTest.MockMotionProto();
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(new Vector(json.getJson(MotionProto.INITIAL_VELOCITY)), is(proto.getInitialVelocity()));
    assertThat(json.getDouble(MotionProto.MAX_VELOCITY), closeTo(proto.getMaxVelocity(), Vector.PRECISION));
    assertThat(json.getDouble(MotionProto.MAX_ACCELERATION), closeTo(proto.getMaxAcceleration(), Vector.PRECISION));
  }

  @Test
  public void jsonToMotionProto() {
    MotionProto proto = MotionProtoTest.MockMotionProto();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(MotionProto.INITIAL_VELOCITY, proto.getInitialVelocity().toJson());
    json.put(MotionProto.MAX_VELOCITY, proto.getMaxVelocity());
    json.put(MotionProto.MAX_ACCELERATION, proto.getMaxAcceleration());
    assertThat(new MotionProto(json.toJson()), is(proto));
  }

  @Test
  public void motionProtoDecode() {
    MotionProto proto = MotionProtoTest.MockMotionProto();
    assertThat(new MotionProto(proto.toJson()), is(proto));
  }

  @Test
  public void motionProtoPartialDecode() {
    MotionProto proto = MotionProtoTest.MockMotionProto(true);
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(new Vector(json.getJson(MotionProto.INITIAL_VELOCITY)), is(proto.getInitialVelocity()));
    assertThat(json.hasKey(MotionProto.MAX_VELOCITY), is(false));
    assertThat(json.hasKey(MotionProto.MAX_ACCELERATION), is(false));
    assertThat(new MotionProto(proto.toJson()), is(proto));
  }

  @Test
  public void hasInitialVelocity() {
    assertThat(MotionProtoTest.MockMotionProto().hasInitialVelocity(), is(true));
    assertThat(new MotionProto(Vector.ZERO, 1., 1.).hasInitialVelocity(), is(false));
  }

  @Test
  public void hasMaxAcceleration() {
    assertThat(MotionProtoTest.MockMotionProto().hasMaxAcceleration(), is(true));
    assertThat(new MotionProto(Vector.ZERO, 1.).hasMaxAcceleration(), is(false));
  }

  @Test
  public void hasMaxVelocity() {
    assertThat(MotionProtoTest.MockMotionProto().hasMaxVelocity(), is(true));
    assertThat(new MotionProto(Vector.ZERO, Double.POSITIVE_INFINITY, 1.).hasMaxVelocity(), is(false));
  }

  @Test
  public void isMobile() {
    assertThat(MotionProtoTest.MockMotionProto().isMobile(), is(true));
    assertThat(new MotionProto(new Vector(1., 1., 1.), Double.POSITIVE_INFINITY, 0.).isMobile(), is(true));
    assertThat(new MotionProto(Vector.ZERO, 1., 1.).isMobile(), is(true));
    assertThat(new MotionProto(Vector.ZERO, 1., 0.).isMobile(), is(false));
    assertThat(new MotionProto(Vector.ZERO, 0., 1.).isMobile(), is(false));
    assertThat(new MotionProto(Vector.ZERO, 0., 0.).isMobile(), is(false));
  }
}