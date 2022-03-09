package com.seat.rescuesim.common.victim;

import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Victims. */
public enum VictimType implements SerializableEnum {
    NONE(0),
    DEFAULT(1);

    private int type;

    private VictimType(int type) {
        this.type = type;
    }

    public boolean equals(VictimType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
