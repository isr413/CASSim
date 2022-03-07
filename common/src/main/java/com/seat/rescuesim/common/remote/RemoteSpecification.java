package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.json.JSONOption;

/** An abstract interface to represent remote specifications. */
public interface RemoteSpecification {

    public abstract String getLabel();

    public abstract JSONOption toJSON();

}
