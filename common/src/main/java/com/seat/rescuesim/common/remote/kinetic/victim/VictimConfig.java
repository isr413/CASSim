package com.seat.rescuesim.common.remote.kinetic.victim;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteConfig;

/** A serializable configuration of a Victim Remote. */
public class VictimConfig extends KineticRemoteConfig {

    public VictimConfig(VictimSpec spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public VictimConfig(VictimSpec spec, Collection<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public VictimConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected VictimSpec decodeProto(JSONOption jsonSpec) throws JSONException {
        return new VictimSpec(jsonSpec);
    }

    @Override
    public VictimSpec getProto() {
        return (VictimSpec) super.getProto();
    }

}
