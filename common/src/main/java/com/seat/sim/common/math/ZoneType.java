package com.seat.sim.common.math;

import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Zones. */
public enum ZoneType implements SerializableEnum {
  NONE(0),
  BLOCKED(1);

  public static final String ZONE_TYPE = "zone_type";

  public static ZoneType decode(JsonObject json) {
    return (json.hasKey(ZoneType.ZONE_TYPE))
        ? ZoneType.Value(json.getInt(ZoneType.ZONE_TYPE))
        : ZoneType.NONE;
  }

  public static ZoneType Value(int value) {
    return ZoneType.values()[value];
  }

  private int type;

  private ZoneType(int type) {
    this.type = type;
  }

  public boolean equals(ZoneType zoneType) {
    if (zoneType == null) {
      return false;
    }
    return this.type == zoneType.type;
  }

  public int getType() {
    return this.type;
  }

  public String toString() {
    return this.getLabel();
  }
}
