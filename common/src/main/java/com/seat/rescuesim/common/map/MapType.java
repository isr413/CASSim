package com.seat.rescuesim.common.map;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Maps. */
public enum MapType implements SerializableEnum {
    NONE(0),
    DEFAULT(1),
    CUSTOM(2);

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
