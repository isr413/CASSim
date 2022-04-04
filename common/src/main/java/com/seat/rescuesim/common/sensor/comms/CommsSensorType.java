package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Comms Sensors. */
public enum CommsSensorType implements SerializableEnum {
    NONE(0),
    DEFAULT(1),
    GENERIC(2),
    BLUETOOTH(3),
    LTE_RADIO(4);

    public static CommsSensorType decodeType(JSONObject json) {
        return CommsSensorType.Value(json.getInt(CommsSensorConst.COMMS_SENSOR_TYPE));
    }

    public static CommsSensorType Value(int value) {
        return CommsSensorType.values()[value];
    }

    private int type;

    private CommsSensorType(int type) {
        this.type = type;
    }

    public boolean equals(CommsSensorType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
