package com.seat.sim.common.math;

import java.util.Optional;

import com.seat.sim.common.json.*;

/** Represents the single cubic zone within the map grid. */
public class Zone extends Jsonable {
  public static final String ZONE_LOCATION = "location";
  public static final String ZONE_SIZE = "size";

  public static final ZoneType DEFAULT_ZONE_TYPE = ZoneType.NONE;

  private Optional<Vector> location;
  private int size;
  private ZoneType zoneType;

  public Zone(Vector location, int size) {
    this(Zone.DEFAULT_ZONE_TYPE, location, size);
  }

  public Zone(ZoneType zoneType, Vector location, int size) {
    this.zoneType = (zoneType != null) ? zoneType : ZoneType.NONE;
    this.location = (location != null) ? Optional.of(location) : Optional.empty();
    this.size = size;
  }

  public Zone(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.zoneType = ZoneType.decode(json);
    this.location = (json.hasKey(Zone.ZONE_LOCATION)) ? Optional.of(new Vector(json.getJson(Zone.ZONE_LOCATION)))
        : Optional.empty();
    this.size = json.getInt(Zone.ZONE_SIZE);
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(ZoneType.ZONE_TYPE, this.zoneType.getType());
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

  public ZoneType getZoneType() {
    return this.zoneType;
  }

  public boolean hasLocation() {
    return this.location.isPresent();
  }

  public boolean hasZoneType(ZoneType zoneType) {
    if (zoneType == null || this.zoneType == null) {
      return this.zoneType == zoneType;
    }
    return this.zoneType.equals(zoneType);
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
