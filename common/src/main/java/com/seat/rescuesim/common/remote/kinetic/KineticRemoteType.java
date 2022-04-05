package com.seat.rescuesim.common.remote.kinetic;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of kinetic remotes. */
public enum KineticRemoteType implements SerializableEnum {
    NONE(0),
    GENERIC(1),
    DRONE(2),
    VICTIM(3);

    public static KineticRemoteType decodeType(JSONObject json) {
        return KineticRemoteType.Value(json.getInt(KineticRemoteConst.KINETIC_REMOTE_TYPE));
    }

    public static KineticRemoteType Value(int value) {
        return KineticRemoteType.values()[value];
    }

    private int type;

    private KineticRemoteType(int type) {
        this.type = type;
    }

    public boolean equals(KineticRemoteType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
