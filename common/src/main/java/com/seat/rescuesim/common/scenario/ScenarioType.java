package com.seat.rescuesim.common.scenario;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Scenarios. */
public enum ScenarioType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    SAR(2);

    public static final String SCENARIO_TYPE = "scenario_type";

    public static ScenarioType decodeType(JSONObject json) {
        return (json.hasKey(ScenarioType.SCENARIO_TYPE)) ?
            ScenarioType.Value(json.getInt(ScenarioType.SCENARIO_TYPE)) :
            ScenarioType.NONE;
    }

    public static ScenarioType Value(int value) {
        return ScenarioType.values()[value];
    }

    private int type;

    private ScenarioType(int type) {
        this.type = type;
    }

    public boolean equals(ScenarioType type) {
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
