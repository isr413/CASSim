package com.seat.rescuesim.common;

public enum SensorType {
    None(0),
    Vision(1),
    Bluetooth(2),
    ShortRangeRadio(3),
    GPS(4),
    Medical(5);

    private int type;

    private SensorType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String encode() {
        return String.valueOf(this.type);
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(SensorType type) {
        return this.type == type.type;
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
