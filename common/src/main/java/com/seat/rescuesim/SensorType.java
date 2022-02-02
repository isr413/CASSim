package com.seat.rescuesim;

public enum SensorType {
    None(0),
    Vision(1),
    Bluetooth(2);

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
