package com.seat.rescuesim.common.map;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Zones. */
public enum ZoneType implements SerializableEnum {
    NONE(0),
    CLOSED(1),
    OPEN(2);

    public static final String ZONE_TYPE = "zone_type";

    public static ZoneType decodeType(JSONObject json) {
        return (json.hasKey(ZoneType.ZONE_TYPE)) ?
            ZoneType.Value(json.getInt(ZoneType.ZONE_TYPE)) :
            ZoneType.NONE;
    }

    public static ZoneType Value(int value) {
        return ZoneType.values()[value];
    }

    private int type;

    private ZoneType(int type) {
        this.type = type;
    }

    public boolean equals(ZoneType type) {
        if (type == null) return false;
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
