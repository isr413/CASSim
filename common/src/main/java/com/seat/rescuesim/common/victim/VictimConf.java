package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConf;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Victim Remote. */
public class VictimConf extends RemoteConf {

    public static VictimConf None() {
        return new VictimConf(VictimSpec.None(), 0, false);
    }

    public VictimConf(JSONObject json) {
        super(json);
    }

    public VictimConf(JSONOption option) {
        super(option);
    }

    public VictimConf(String encoding) {
        super(encoding);
    }

    public VictimConf(VictimSpec spec, int count, boolean dynamic) {
        super(RemoteType.VICTIM, spec, count, dynamic);
    }

    public VictimConf(VictimSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.VICTIM, spec, remoteIDs, dynamic);
    }

    public VictimConf(VictimSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.VICTIM, spec, remoteIDs, dynamic);
    }

    @Override
    protected void decodeSpec(JSONObject jsonSpec) {
        this.spec = new VictimSpec(jsonSpec);
    }

    public VictimSpec getSpecification() {
        return (VictimSpec) this.spec;
    }

    public VictimType getSpecType() {
        return (VictimType) this.spec.getSpecType();
    }

    public boolean hasSpec() {
        return !this.getSpecType().equals(VictimType.NONE);
    }

}
