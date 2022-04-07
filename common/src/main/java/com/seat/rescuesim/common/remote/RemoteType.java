package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of Remotes. */
public enum RemoteType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    BASE(2),
    MOBILE(3),
    DRONE(4),
    VICTIM(5);

    public static final String REMOTE_TYPE = "remote_type";

    public static RemoteType decodeType(JSONObject json) {
        return (json.hasKey(RemoteType.REMOTE_TYPE)) ?
            RemoteType.Value(json.getInt(RemoteType.REMOTE_TYPE)) :
            RemoteType.NONE;
    }

    public static RemoteType Value(int value) {
        return RemoteType.values()[value];
    }

    private int type;

    private RemoteType(int type) {
        this.type = type;
    }

    public boolean equals(RemoteType type) {
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
