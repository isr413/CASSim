package com.seat.sim.common.remote.intent;

public class StopIntention extends Intention {

    public StopIntention() {
        super(IntentionType.STOP);
    }

    @Override
    public String getLabel() {
        return "<STOP>";
    }

}
