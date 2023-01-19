package com.seat.sim.common.core;

import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.SerializableEnum;

public enum TeamColor implements SerializableEnum {
    NONE(0),
    BLUE(1),
    CYAN(2),
    GRAY(3),
    GREEN(4),
    MAGENTA(5),
    ORANGE(6),
    PINK(7),
    RED(8),
    YELLOW(9);

    public static final String TEAM = "team";

    public static TeamColor decodeType(JsonObject json) {
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
