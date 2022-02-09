package com.seat.rescuesim.common;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Zones. */
public enum ZoneType implements SerializableEnum {
    NONE(0),
    CLOSED(1),
    OPEN(2);

    private int type;

    private ZoneType(int type) {
        this.type = type;
    }

    public boolean equals(ZoneType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
