package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.remote.Intention;
import com.seat.rescuesim.common.remote.IntentionType;

public class ShutdownIntention extends Intention {

    public ShutdownIntention() {
        super(IntentionType.SHUTDOWN);
    }

}
