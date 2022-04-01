package com.seat.rescuesim.common;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Snapshot statuses. */
public enum ScenarioStatus implements SerializableEnum {
    NONE(0),
    START(1),
    IN_PROGRESS(2),
    DONE(3),
    ERROR(4);

    private int type;

    private ScenarioStatus(int type) {
        this.type = type;
    }

    public boolean equals(ScenarioStatus type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
