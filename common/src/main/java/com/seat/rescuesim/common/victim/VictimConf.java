package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfiguration;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable class to represent the configuration of Victim Remotes. */
public class VictimConf extends RemoteConfiguration {

    public static VictimConf None() {
        return new VictimConf();
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

    public VictimConf() {
        super();
    }

    public VictimConf(VictimSpec spec) {
        super(RemoteType.VICTIM, spec);
    }

    public VictimConf(VictimSpec spec, String[] remotes) {
        super(RemoteType.VICTIM, spec, remotes);
    }

    public VictimConf(VictimSpec spec, ArrayList<String> remotes) {
        super(RemoteType.VICTIM, spec, remotes);
    }

    public VictimConf(VictimSpec spec, HashSet<String> remotes) {
        super(RemoteType.VICTIM, spec, remotes);
    }

    public VictimConf(VictimSpec spec, int count) {
        super(RemoteType.VICTIM, spec, count);
    }

    public VictimConf(VictimSpec spec, boolean dynamic) {
        super(RemoteType.VICTIM, spec, dynamic);
    }

    public VictimConf(VictimSpec spec, String[] remotes, boolean dynamic) {
        super(RemoteType.VICTIM, spec, remotes, dynamic);
    }

    public VictimConf(VictimSpec spec, ArrayList<String> remotes, boolean dynamic) {
        super(RemoteType.VICTIM, spec, remotes, dynamic);
    }

    public VictimConf(VictimSpec spec, HashSet<String> remotes, boolean dynamic) {
        super(RemoteType.VICTIM, spec, remotes, dynamic);
    }

    public VictimSpec getSpecification() {
        return (VictimSpec) this.spec;
    }

    @Override
    protected void decodeSpecification(JSONObject jsonSpec) {
        this.spec = new VictimSpec(jsonSpec);
    }

}
