package com.seat.rescuesim.common.map;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Maps. */
public enum MapType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    CUSTOM(2);

    public static MapType decodeType(JSONObject json) {
        return MapType.Value(json.getInt(Map.MAP_TYPE));
    }

    public static MapType Value(int value) {
        return MapType.values()[value];
    }

    private int type;

    private MapType(int type) {
        this.type = type;
    }

    public boolean equals(MapType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
