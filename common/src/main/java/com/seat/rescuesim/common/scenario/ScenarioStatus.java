package com.seat.rescuesim.common.scenario;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of scenario statuses. */
public enum ScenarioStatus implements SerializableEnum {
    NONE(0),
    START(1),
    IN_PROGRESS(2),
    DONE(3),
    ERROR(4);

    public static final String STATUS = "status";

    public static ScenarioStatus decodeStatus(JSONObject json) {
        return (json.hasKey(ScenarioStatus.STATUS)) ?
            ScenarioStatus.Value(json.getInt(ScenarioStatus.STATUS)) :
            ScenarioStatus.NONE;
    }

    public static ScenarioStatus Value(int value) {
        return ScenarioStatus.values()[value];
    }

    private int type;

    private ScenarioStatus(int type) {
        this.type = type;
    }

    public boolean equals(ScenarioStatus type) {
        if (type == null) return false;
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
