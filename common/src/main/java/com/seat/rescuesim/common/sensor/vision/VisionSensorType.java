package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Vision Sensors. */
public enum VisionSensorType implements SerializableEnum {
    NONE(0),
    DEFAULT(1),
    GENERIC(2),
    CMOS_CAMERA(3);

    public static VisionSensorType decodeType(JSONObject json) {
        return VisionSensorType.Value(json.getInt(VisionSensorConst.VISION_SENSOR_TYPE));
    }

    public static VisionSensorType Value(int value) {
        return VisionSensorType.values()[value];
    }

    private int type;

    private VisionSensorType(int type) {
        this.type = type;
    }

    public boolean equals(VisionSensorType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
