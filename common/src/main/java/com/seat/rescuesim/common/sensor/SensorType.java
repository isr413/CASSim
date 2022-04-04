package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enum to denote types of Sensors. */
public enum SensorType implements SerializableEnum {
    NONE(0),
    DEFAULT(1),
    GENERIC(2),
    COMMS(3),
    MONITOR(4),
    VISION(5);

    public static SensorType decodeType(JSONObject json) {
        return SensorType.Value(json.getInt(SensorConst.SENSOR_TYPE));
    }

    public static SensorType Value(int value) {
        return SensorType.values()[value];
    }

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
