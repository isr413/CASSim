package com.seat.rescuesim.common.math;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Fields. */
public enum FieldType implements SerializableEnum {
    NONE(0),
    PUSH(1),
    PULL(2);

    private int type;

    private FieldType(int type) {
        this.type = type;
    }

    public boolean equals(FieldType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
