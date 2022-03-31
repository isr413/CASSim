package com.seat.rescuesim.common.remote.kinetic.victim;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteConfig;

/** A serializable configuration of a Victim Remote. */
public class VictimConfig extends KineticRemoteConfig {

    public VictimConfig(VictimSpec spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public VictimConfig(VictimSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public VictimConfig(VictimSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public VictimConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decodeSpec(JSONOption jsonSpec) throws JSONException {
        this.spec = new VictimSpec(jsonSpec);
    }

    public VictimSpec getSpec() {
        return (VictimSpec) this.spec;
    }

}
