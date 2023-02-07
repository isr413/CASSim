package com.seat.sim.common.remote.intent;

public class DoneIntention extends Intention {

  public DoneIntention() {
    super(IntentionType.DONE);
  }

  @Override
  public String getLabel() {
    return "<DONE>";
  }
}
