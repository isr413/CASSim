package com.seat.rescuesim.common;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Drones. */
public enum DroneType implements SerializableEnum {
    NONE(0),
    DEFAULT(1);

    private int type;

    private DroneType(int type) {
        this.type = type;
    }

    public boolean equals(DroneType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
