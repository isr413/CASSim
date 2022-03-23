package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Victim Remote. */
public class VictimConfig extends RemoteConfig {

    public VictimConfig(JSONObject json) throws JSONException {
        super(json);
    }

    public VictimConfig(JSONOption option) throws JSONException {
        super(option);
    }

    public VictimConfig(String encoding) throws JSONException {
        super(encoding);
    }

    public VictimConfig(VictimSpec spec, int count, boolean dynamic) {
        super(RemoteType.VICTIM, spec, count, dynamic);
    }

    public VictimConfig(VictimSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.VICTIM, spec, remoteIDs, dynamic);
    }

    public VictimConfig(VictimSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.VICTIM, spec, remoteIDs, dynamic);
    }

    @Override
    protected void decodeSpec(JSONObject jsonSpec) throws JSONException {
        this.spec = new VictimSpec(jsonSpec);
    }

    public VictimSpec getSpec() {
        return (VictimSpec) this.spec;
    }

    public VictimType getSpecType() {
        return (VictimType) this.spec.getSpecType();
    }

    public boolean hasSpec() {
        return !this.getSpecType().equals(VictimType.NONE);
    }

}
