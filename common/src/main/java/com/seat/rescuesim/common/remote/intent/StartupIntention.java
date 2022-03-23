package com.seat.rescuesim.common.remote.intent;

public class StartupIntention extends Intention {

    public StartupIntention() {
        super(IntentionType.STARTUP);
    }

    @Override
    public String getLabel() {
        return "<STARTUP>";
    }

}
