package com.seat.sim.common.remote.kinematics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.math.Vector;

public class KinematicsProtoTest {

  public static KinematicsProto MockKinematicsProto() {
    return KinematicsProtoTest.MockKinematicsProto(false);
  }

  public static KinematicsProto MockKinematicsProto(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new KinematicsProto(new Vector(1., 1., 1.))
        : new KinematicsProto(
            new Vector(1., 1., 1.),
            FuelProtoTest.MockFuelProto(),
            MotionProtoTest.MockMotionProto()
          );
  }

  @Test
  public void kinematicsProtoToJson() {
    KinematicsProto proto = KinematicsProtoTest.MockKinematicsProto();
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(new Vector(json.getJson(KinematicsProto.LOCATION)), is(proto.getLocation()));
    assertThat(new FuelProto(json.getJson(KinematicsProto.FUEL_PROTO)), is(proto.getFuelProto()));
    assertThat(new MotionProto(json.getJson(KinematicsProto.MOTION_PROTO)), is(proto.getMotionProto()));
  }

  @Test
  public void jsonToKinematicsProto() {
    KinematicsProto proto = KinematicsProtoTest.MockKinematicsProto();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(KinematicsProto.LOCATION, proto.getLocation().toJson());
    json.put(KinematicsProto.FUEL_PROTO, proto.getFuelProto().toJson());
    json.put(KinematicsProto.MOTION_PROTO, proto.getMotionProto().toJson());
    assertThat(new KinematicsProto(json.toJson()), is(proto));
  }

  @Test
  public void kinematicsProtoDecode() {
    KinematicsProto proto = KinematicsProtoTest.MockKinematicsProto();
    assertThat(new KinematicsProto(proto.toJson()), is(proto));
  }

  @Test
  public void kinematicsProtoPartialDecode() {
    KinematicsProto proto = KinematicsProtoTest.MockKinematicsProto(true);
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(new Vector(json.getJson(KinematicsProto.LOCATION)), is(proto.getLocation()));
    assertThat(json.hasKey(KinematicsProto.FUEL_PROTO), is(false));
    assertThat(json.hasKey(KinematicsProto.MOTION_PROTO), is(false));
    assertThat(new KinematicsProto(proto.toJson()), is(proto));
  }

  @Test
  public void isMobile() {
    assertThat(KinematicsProtoTest.MockKinematicsProto().isMobile(), is(true));
    assertThat(KinematicsProtoTest.MockKinematicsProto(true).isMobile(), is(false));
    assertThat(
        new KinematicsProto(
            new Vector(1., 1., 1.),
            FuelProtoTest.MockFuelProto(),
            new MotionProto(Vector.ZERO, 0., 0.)
          )
          .isMobile(),
        is(false)
      );
  }
}
