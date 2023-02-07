package com.seat.sim.common.remote.intent;

public class ShutdownIntention extends Intention {

  public ShutdownIntention() {
    super(IntentionType.SHUTDOWN);
  }

  @Override
  public String getLabel() {
    return "<SHUTDOWN>";
  }
}
