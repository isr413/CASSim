package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Remotes. */
public enum RemoteType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    KINETIC(2),
    STATIC(3);

    public static RemoteType decodeType(JSONObject json) {
        return RemoteType.Value(json.getInt(RemoteConst.REMOTE_TYPE));
    }

    public static RemoteType Value(int value) {
        return RemoteType.values()[value];
    }

    private int type;

    private RemoteType(int type) {
        this.type = type;
    }

    public boolean equals(RemoteType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
