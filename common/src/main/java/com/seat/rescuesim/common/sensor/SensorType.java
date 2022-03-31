package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enum to denote types of sensors. */
public enum SensorType implements SerializableEnum {
    None(0, SensorType.NONE_SENSOR),
    Comms(1, SensorType.COMMS_SENSOR),
    Monitor(2, SensorType.MONITOR_SENSOR),
    Vision(3, SensorType.VISION_SENSOR),
    Bluetooth(4, SensorType.COMMS_SENSOR),
    LTERadio(5, SensorType.COMMS_SENSOR),
    HRVM(6, SensorType.MONITOR_SENSOR),
    CMOSCamera(7, SensorType.VISION_SENSOR);

    public static final int NONE_SENSOR = 0;
    public static final int COMMS_SENSOR = 1;
    public static final int MONITOR_SENSOR = 1 << 1;
    public static final int VISION_SENSOR = 1 << 2;

    private int categories;
    private int type;

    private SensorType(int type, int categories) {
        this.type = type;
        this.categories = categories;
    }

    public boolean equals(SensorType type) {
        return this.type == type.type;
    }

    public int getCategories() {
        return this.categories;
    }

    public int getType() {
        return this.type;
    }

    public boolean hasCategory(int categoryFlags) {
        return (this.categories & categoryFlags) > 0;
    }

    public String toString() {
        return this.getLabel();
    }

}
