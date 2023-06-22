package com.seat.sim.common.remote.kinematics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
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
  public void hasFuelProto() {
    assertThat(KinematicsProtoTest.MockKinematicsProto().hasFuelProto(), is(true));
    assertThat(KinematicsProtoTest.MockKinematicsProto(true).hasFuelProto(), is(false));
  }

  @Test
  public void hasLocation() {
    assertThat(KinematicsProtoTest.MockKinematicsProto().hasLocation(), is(true));
    assertThat(new KinematicsProto().hasLocation(), is(false));
  }

  @Test
  public void hasMotionProto() {
    assertThat(KinematicsProtoTest.MockKinematicsProto().hasMotionProto(), is(true));
    assertThat(KinematicsProtoTest.MockKinematicsProto(true).hasMotionProto(), is(false));
  }

  @Test
  public void isEnabled() {
    assertThat(KinematicsProtoTest.MockKinematicsProto().isEnabled(), is(true));
    assertThat(KinematicsProtoTest.MockKinematicsProto(true).isEnabled(), is(true));
    assertThat(
        new KinematicsProto(
            Vector.ZERO,
            new FuelProto(0., Vector.ZERO),
            MotionProtoTest.MockMotionProto()
          )
          .isEnabled(),
        is(false)
      );
  }

  @Test
  public void isMobile() {
    assertThat(KinematicsProtoTest.MockKinematicsProto().isMobile(), is(true));
    assertThat(KinematicsProtoTest.MockKinematicsProto(true).isMobile(), is(false));
    assertThat(
        new KinematicsProto(
            Vector.ZERO,
            FuelProtoTest.MockFuelProto(),
            new MotionProto(Vector.ZERO, 0., 0.)
          )
          .isMobile(),
        is(false)
      );
  }

  @Test
  public void remoteFuelUsage() {
    assertThat(KinematicsProtoTest.MockKinematicsProto(true).getRemoteFuelUsage(), closeTo(0., Vector.PRECISION));
    assertThat(KinematicsProtoTest.MockKinematicsProto(true).getRemoteFuelUsage(1.), closeTo(0., Vector.PRECISION));
    assertThat(
        KinematicsProtoTest.MockKinematicsProto(true).getRemoteFuelUsage(new Vector(1., 1., 1.)),
        closeTo(0., Vector.PRECISION)
      );
    KinematicsProto proto = new KinematicsProto(
        Vector.ZERO,
        new FuelProto(1., 1., new Vector(1., 0., 0.)),
        MotionProtoTest.MockMotionProto()
      );
    assertThat(proto.getRemoteFuelUsage(), closeTo(1., Vector.PRECISION));
    assertThat(proto.getRemoteFuelUsage(2.), closeTo(1., Vector.PRECISION));
    assertThat(proto.getRemoteFuelUsage(new Vector(2., 2., 2.)), closeTo(1., Vector.PRECISION));
    proto = new KinematicsProto(
        Vector.ZERO,
        new FuelProto(1., 1., new Vector(1., 1., 0.)),
        MotionProtoTest.MockMotionProto()
      );
    assertThat(proto.getRemoteFuelUsage(), closeTo(1., Vector.PRECISION));
    assertThat(proto.getRemoteFuelUsage(2.), closeTo(3., Vector.PRECISION));
    assertThat(proto.getRemoteFuelUsage(new Vector(3., 4., 0.)), closeTo(6., Vector.PRECISION));
    proto = new KinematicsProto(
        Vector.ZERO,
        new FuelProto(1., 1., new Vector(1., 1., 1.)),
        MotionProtoTest.MockMotionProto()
      );
    assertThat(proto.getRemoteFuelUsage(), closeTo(1., Vector.PRECISION));
    assertThat(proto.getRemoteFuelUsage(2.), closeTo(3., Vector.PRECISION));
    assertThat(proto.getRemoteFuelUsage(new Vector(3., 4., 5.)), closeTo(11., Vector.PRECISION));
  }
}
