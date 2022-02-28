package com.seat.rescuesim.common.remote.snap;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Snapshot statuses. */
public enum SnapStatus implements SerializableEnum {
    DONE(0),
    START(1),
    IN_PROGRESS(2),
    ERROR(3);

    private int type;

    private SnapStatus(int type) {
        this.type = type;
    }

    public boolean equals(SnapStatus type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}