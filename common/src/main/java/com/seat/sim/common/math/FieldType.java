package com.seat.sim.common.math;

import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Fields. */
public enum FieldType implements SerializableEnum {
    NONE(0),
    JERK(1),
    PUSH(2),
    PULL(3);

    public static final String FIELD_TYPE = "field_type";

    public static FieldType decodeType(JSONArray json) {
        return (json.length() > 0) ?
            FieldType.Value(json.getInt(0)) :
            FieldType.NONE;
    }

    public static FieldType decodeType(JSONObject json) throws JSONException {
        return (json.hasKey(FieldType.FIELD_TYPE)) ?
            FieldType.Value(json.getInt(FieldType.FIELD_TYPE)) :
            FieldType.NONE;
    }

    public static FieldType Value(int value) {
        return FieldType.values()[value];
    }

    private int type;

    private FieldType(int type) {
        this.type = type;
    }

    public boolean equals(FieldType type) {
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