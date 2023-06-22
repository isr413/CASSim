package com.seat.sim.common.remote.kinematics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.math.Vector;

public class FuelProtoTest {

  public static FuelProto MockFuelProto() {
    return FuelProtoTest.MockFuelProto(false);
  }

  public static FuelProto MockFuelProto(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new FuelProto(1., new Vector(1., 1., 1.))
        : new FuelProto(1., 1., new Vector(1., 1., 1.));
  }

  @Test
  public void fuelProtoToJson() {
    FuelProto proto = FuelProtoTest.MockFuelProto();
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(json.getDouble(FuelProto.INITIAL_FUEL), closeTo(proto.getInitialFuel(), Vector.PRECISION));
    assertThat(json.getDouble(FuelProto.MAX_FUEL), closeTo(proto.getMaxFuel(), Vector.PRECISION));
    assertThat(new Vector(json.getJson(FuelProto.FUEL_USAGE)), is(proto.getFuelUsage()));
  }

  @Test
  public void jsonToFuelProto() {
    FuelProto proto = FuelProtoTest.MockFuelProto();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(FuelProto.INITIAL_FUEL, proto.getInitialFuel());
    json.put(FuelProto.MAX_FUEL, proto.getMaxFuel());
    json.put(FuelProto.FUEL_USAGE, proto.getFuelUsage().toJson());
    assertThat(new FuelProto(json.toJson()), is(proto));
  }

  @Test
  public void fuelProtoDecode() {
    FuelProto proto = FuelProtoTest.MockFuelProto();
    assertThat(new FuelProto(proto.toJson()), is(proto));
  }

  @Test
  public void fuelProtoPartialDecode() {
    FuelProto proto = FuelProtoTest.MockFuelProto(true);
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(json.getDouble(FuelProto.INITIAL_FUEL), closeTo(proto.getInitialFuel(), Vector.PRECISION));
    assertThat(json.hasKey(FuelProto.MAX_FUEL), is(false));
    assertThat(new Vector(json.getJson(FuelProto.FUEL_USAGE)), is(proto.getFuelUsage()));
    assertThat(new FuelProto(proto.toJson()), is(proto));
  }

  @Test
  public void hasFuelUsage() {
    assertThat(FuelProtoTest.MockFuelProto().hasFuelUsage(), is(true));
    assertThat(new FuelProto(1., 1., Vector.ZERO).hasFuelUsage(), is(false));
  }

  @Test
  public void hasInitialFuel() {
    assertThat(FuelProtoTest.MockFuelProto().hasInitialFuel(), is(true));
    assertThat(new FuelProto(0., 1., new Vector(1., 1., 1.)).hasInitialFuel(), is(false));
  }

  @Test
  public void hasMaxFuel() {
    assertThat(FuelProtoTest.MockFuelProto().hasMaxFuel(), is(true));
    assertThat(new FuelProto(1., new Vector(1., 1., 1.)).hasMaxFuel(), is(false));
  }
}