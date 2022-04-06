package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Comms Sensors. */
public enum CommsSensorType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    BLUETOOTH(2),
    LTE_RADIO(3);

    public static final String COMMS_SENSOR_TYPE = "comms_sensor_type";

    public static CommsSensorType decodeType(JSONObject json) throws JSONException {
        return CommsSensorType.Value(json.getInt(CommsSensorType.COMMS_SENSOR_TYPE));
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
