package com.seat.rescuesim.common;

public enum ZoneType {
    NONE(-1),
    CLOSED(0),
    OPEN(1);

    private int type;

    private ZoneType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return String.valueOf(this.type);
    }

    public boolean equals(ZoneType type) {
        return this.type == type.type;
    }

}
