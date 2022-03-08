package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.SerializableEnum;

/** An abstract interface to represent serializable prototypes. */
public interface RemoteSpecification {

    String getLabel();

    SerializableEnum getType();

    JSONOption toJSON();

}
