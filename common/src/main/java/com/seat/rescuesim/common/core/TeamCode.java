package com.seat.rescuesim.common.core;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

public enum TeamCode implements SerializableEnum {
    NONE(0),
    BLACK(1),
    BLUE(2),
    CYAN(3),
    DARK_GRAY(4),
    GRAY(5),
    GREEN(6),
    LIGHT_GRAY(7),
    MAGENTA(8),
    ORANGE(9),
    PINK(10),
    RED(11),
    WHITE(12),
    YELLOW(13);

    public static final String TEAM = "team";

    public static TeamCode decodeType(JSONObject json) {
        return (json.hasKey(TeamCode.TEAM)) ?
            TeamCode.Value(json.getInt(TeamCode.TEAM)) :
            TeamCode.NONE;
    }

    public static TeamCode Value(int value) {
        return TeamCode.values()[value];
    }

    private int type;

    private TeamCode(int type) {
        this.type = type;
    }

    public boolean equals(TeamCode type) {
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
