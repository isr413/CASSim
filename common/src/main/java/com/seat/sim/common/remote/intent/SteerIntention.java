package com.seat.sim.common.remote.intent;

import java.util.Optional;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

public class SteerIntention extends Intention {
  private static final String DIRECTION = "direction";

  private Optional<Vector> direction;

  public SteerIntention() {
    this(Optional.empty());
  }

  public SteerIntention(Vector direction) {
    this(Optional.of(direction));
  }

  private SteerIntention(Optional<Vector> direction) {
    super(IntentionType.STEER);
    this.direction = (direction != null) ? direction : Optional.empty();
    if (this.direction.isPresent() && !Double.isFinite(this.direction.get().getMagnitude())) {
      throw new RuntimeException(
          String.format("cannot Steer remote with non-finite direction `%s`",
              this.direction.get().toString()));
    }
  }

  public SteerIntention(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    super.decode(json);
    this.direction = (json.hasKey(SteerIntention.DIRECTION))
        ? Optional.of(new Vector(json.getJson(SteerIntention.DIRECTION)))
        : Optional.empty();
  }

  @Override
  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = super.getJsonBuilder();
    if (this.hasDirection()) {
      json.put(SteerIntention.DIRECTION, this.getDirection().toJson());
    }
    return json;
  }

  public Vector getDirection() {
    return this.direction.get();
  }

  @Override
  public String getLabel() {
    return "<STEER>";
  }

  public boolean hasDirection() {
    return this.direction.isPresent() && Double.isFinite(this.getDirection().getMagnitude());
  }
}
