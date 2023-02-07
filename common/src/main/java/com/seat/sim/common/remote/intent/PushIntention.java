package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

public class PushIntention extends Intention {
  private static final String FORCE = "force";

  private Vector force;

  public PushIntention() {
    this(new Vector());
  }

  public PushIntention(Vector force) {
    super(IntentionType.PUSH);
    this.force = (force != null) ? force : new Vector();
    if (!Double.isFinite(this.force.getMagnitude())) {
      throw new RuntimeException(
          String.format("cannot Push remote with a non-finite force `%s`", this.force.toString()));
    }
  }

  public PushIntention(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    super.decode(json);
    this.force = (json.hasKey(PushIntention.FORCE))
        ? new Vector(json.getJson(PushIntention.FORCE))
        : new Vector();
  }

  @Override
  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = super.getJsonBuilder();
    if (this.hasForce()) {
      json.put(PushIntention.FORCE, this.force.toJson());
    }
    return json;
  }

  public Vector getForce() {
    return this.force;
  }

  @Override
  public String getLabel() {
    return "<PUSH>";
  }

  public boolean hasForce() {
    return Double.isFinite(this.force.getMagnitude()) && this.force.getMagnitude() > 0;
  }
}
