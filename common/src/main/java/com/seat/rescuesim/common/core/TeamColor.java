package com.seat.rescuesim.common.core;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

public enum TeamColor implements SerializableEnum {
    NONE(0),
    BLUE(1),
    CYAN(2),
    DARK_GRAY(3),
    GRAY(4),
    GREEN(5),
    LIGHT_GRAY(6),
    MAGENTA(7),
    ORANGE(8),
    PINK(9),
    RED(10),
    YELLOW(11);

    public static final String TEAM = "team";

    public static TeamColor decodeType(JSONObject json) {
        return (json.hasKey(TeamColor.TEAM)) ?
            TeamColor.Value(json.getInt(TeamColor.TEAM)) :
            TeamColor.NONE;
    }

    public static TeamColor Value(int value) {
        return TeamColor.values()[value];
    }

    private int type;

    private TeamColor(int type) {
        this.type = type;
    }

    public boolean equals(TeamColor type) {
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
