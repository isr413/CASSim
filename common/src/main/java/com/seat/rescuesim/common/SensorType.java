package com.seat.rescuesim.common;

import com.seat.rescuesim.common.util.SerializableEnum;

public enum SensorType implements SerializableEnum {
    None(0),
    Communication(1),
    Monitor(2),
    Vision(3);

    private int type;

    private SensorType(int type) {
        this.type = type;
    }

    public boolean equals(SensorType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
