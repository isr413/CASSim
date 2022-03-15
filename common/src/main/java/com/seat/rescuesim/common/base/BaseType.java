package com.seat.rescuesim.common.base;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Bases. */
public enum BaseType implements SerializableEnum {
    NONE(0),
    DEFAULT(1);

    private int type;

    private BaseType(int type) {
        this.type = type;
    }

    public boolean equals(BaseType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
