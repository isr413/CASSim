package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.SerializableEnum;

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
  STEER(8),
  STOP(9),
  PUSH(10);

  public static final String INTENTION_TYPE = "intention_type";

  public static IntentionType decode(JsonObject json) {
    return (json.hasKey(IntentionType.INTENTION_TYPE))
        ? IntentionType.Value(json.getInt(IntentionType.INTENTION_TYPE))
        : IntentionType.NONE;
  }

  public static IntentionType Value(int value) {
    return IntentionType.values()[value];
  }

  private int type;

  private IntentionType(int type) {
    this.type = type;
  }

  public boolean equals(IntentionType intentType) {
    if (intentType == null) {
      return false;
    }
    return this.type == intentType.type;
  }

  public int getType() {
    return this.type;
  }

  public String toString() {
    return this.getLabel();
  }
}
