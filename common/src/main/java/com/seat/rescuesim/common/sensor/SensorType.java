package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enum to denote types of Sensors. */
public enum SensorType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    COMMS(2),
    BLUETOOTH_COMMS(3),
    LTE_RADIO_COMMS(4),
    MONITOR(5),
    HRVM_MONITOR(6),
    VISION(7),
    CMOS_CAMERA_VISION(8);

    public static final String SENSOR_TYPE = "sensor_type";

    public static SensorType decodeType(JSONObject json) {
        return (json.hasKey(SensorType.SENSOR_TYPE)) ?
            SensorType.Value(json.getInt(SensorType.SENSOR_TYPE)) :
            SensorType.NONE;
    }

    public static SensorType Value(int value) {
        return SensorType.values()[value];
    }

    private int type;

    private SensorType(int type) {
        this.type = type;
    }

    public boolean equals(SensorType type) {
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
