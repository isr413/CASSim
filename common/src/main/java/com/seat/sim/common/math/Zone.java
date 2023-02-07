package com.seat.sim.common.math;

import java.util.Optional;

import com.seat.sim.common.json.*;

/** Represents the single cubic zone within the map grid. */
public class Zone extends Jsonable {
  public static final String ZONE_LOCATION = "location";
  public static final String ZONE_SIZE = "size";

  private Optional<Vector> location;
  private int size;

  public Zone(Vector location, int size) {
    this.location = (location != null) ? Optional.of(location) : Optional.empty();
    this.size = size;
  }

  public Zone(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.location = (json.hasKey(Zone.ZONE_LOCATION)) ? Optional.of(new Vector(json.getJson(Zone.ZONE_LOCATION)))
        : Optional.empty();
    this.size = json.getInt(Zone.ZONE_SIZE);
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    if (this.hasLocation()) {
      json.put(Zone.ZONE_LOCATION, this.getLocation().toJson());
    }
    json.put(Zone.ZONE_SIZE, this.size);
    return json;
  }

  public String getLabel() {
    return String.format("<z: %s>", this.location.toString());
  }

  public Vector getLocation() {
    return this.location.get();
  }

  public int getSize() {
    return this.size;
  }

  public boolean hasLocation() {
    return this.location.isPresent();
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
