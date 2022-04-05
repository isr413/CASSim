package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable enumeration to denote types of intents. */
public enum IntentionType implements SerializableEnum {
    NONE(0),
    ACTIVATE(1),
    DEACTIVATE(2),
    DONE(3),
    GOTO(4),
    MOVE(5),
    SHUTDOWN(6),
    STARTUP(7),
    STOP(8);

    public static IntentionType decodeType(JSONObject json) {
        return IntentionType.Value(json.getInt(IntentionConst.INTENTION_TYPE));
    }

    public static IntentionType Value(int value) {
        return IntentionType.values()[value];
    }

    private int type;

    private IntentionType(int type) {
        this.type = type;
    }

    public boolean equals(IntentionType type) {
        return this.type == type.type;
    }

    public int getType() {
        return this.type;
    }

    public String toString() {
        return this.getLabel();
    }

}
