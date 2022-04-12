package com.seat.sim.common.remote.intent;

public class NoneIntention extends Intention {

    public NoneIntention() {
        super(IntentionType.NONE);
    }

    @Override
    public String getLabel() {
        return "<NONE>";
    }

}
