package com.seat.rescuesim.common.remote.stat;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of static remotes. */
public enum StaticRemoteType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    BASE(2);

    public static StaticRemoteType decodeType(JSONObject json) {
        return StaticRemoteType.Value(json.getInt(StaticRemoteConst.STATIC_REMOTE_TYPE));
    }

    public static StaticRemoteType Value(int value) {
        return StaticRemoteType.values()[value];
    }

    private int type;

    private StaticRemoteType(int type) {
        this.type = type;
    }

    public boolean equals(StaticRemoteType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
