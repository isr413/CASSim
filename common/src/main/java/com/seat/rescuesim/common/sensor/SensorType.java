package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enum to denote types of Sensors. */
public enum SensorType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    COMMS(2),
    MONITOR(3),
    VISION(4);

    public static final String SENSOR_TYPE = "sensor_type";

    public static SensorType decodeType(JSONObject json) throws JSONException {
        return SensorType.Value(json.getInt(SensorType.SENSOR_TYPE));
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
