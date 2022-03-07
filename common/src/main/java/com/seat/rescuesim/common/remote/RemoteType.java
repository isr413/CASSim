package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Remotes. */
public enum RemoteType implements SerializableEnum {
    NONE(0),
    BASE(1),
    DRONE(2),
    SENSOR(3),
    VICTIM(4);

    private int type;

    private RemoteType(int type) {
        this.type = type;
    }

    public boolean equals(RemoteType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
