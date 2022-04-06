package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Vision Sensors. */
public enum VisionSensorType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    CMOS_CAMERA(2);

    public static final String VISION_SENSOR_TYPE = "vision_sensor_type";

    public static VisionSensorType decodeType(JSONObject json) {
        return (json.hasKey(VisionSensorType.VISION_SENSOR_TYPE)) ?
            VisionSensorType.Value(json.getInt(VisionSensorType.VISION_SENSOR_TYPE)) :
            VisionSensorType.NONE;
    }

    public static VisionSensorType Value(int value) {
        return VisionSensorType.values()[value];
    }

    private int type;

    private VisionSensorType(int type) {
        this.type = type;
    }

    public boolean equals(VisionSensorType type) {
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
