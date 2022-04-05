package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Monitor Sensors. */
public enum MonitorSensorType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    HRVM(2);

    public static MonitorSensorType decodeType(JSONObject json) {
        return MonitorSensorType.Value(json.getInt(MonitorSensorConst.MONITOR_SENSOR_TYPE));
    }

    public static MonitorSensorType Value(int value) {
        return MonitorSensorType.values()[value];
    }

    private int type;

    private MonitorSensorType(int type) {
        this.type = type;
    }

    public boolean equals(MonitorSensorType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
