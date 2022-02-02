package com.seat.rescuesim.common;

public enum SensorType {
    None(0),
    Vision(1),
    Bluetooth(2),
    ShortRangeRadio(3),
    LongRangeRadio(4),
    GPS(5),
    Medical(6);

    private int type;

    private SensorType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return String.valueOf(this.type);
    }

}
