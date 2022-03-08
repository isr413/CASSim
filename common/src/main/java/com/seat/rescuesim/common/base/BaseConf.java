package com.seat.rescuesim.common.base;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfiguration;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable class to represent the configuration of Base Remotes. */
public class BaseConf extends RemoteConfiguration {

    public static BaseConf None() {
        return new BaseConf();
    }

    public BaseConf(JSONObject json) {
        super(json);
    }

    public BaseConf(JSONOption option) {
        super(option);
    }

    public BaseConf(String encoding) {
        super(encoding);
    }

    public BaseConf() {
        super();
    }

    public BaseConf(BaseSpec spec) {
        super(RemoteType.BASE, spec);
    }

    public BaseConf(BaseSpec spec, String[] remotes) {
        super(RemoteType.BASE, spec, remotes);
    }

    public BaseConf(BaseSpec spec, ArrayList<String> remotes) {
        super(RemoteType.BASE, spec, remotes);
    }

    public BaseConf(BaseSpec spec, HashSet<String> remotes) {
        super(RemoteType.BASE, spec, remotes);
    }

    public BaseConf(BaseSpec spec, int count) {
        super(RemoteType.BASE, spec, count);
    }

    public BaseConf(BaseSpec spec, boolean dynamic) {
        super(RemoteType.BASE, spec, dynamic);
    }

    public BaseConf(BaseSpec spec, String[] remotes, boolean dynamic) {
        super(RemoteType.BASE, spec, remotes, dynamic);
    }

    public BaseConf(BaseSpec spec, ArrayList<String> remotes, boolean dynamic) {
        super(RemoteType.BASE, spec, remotes, dynamic);
    }

    public BaseConf(BaseSpec spec, HashSet<String> remotes, boolean dynamic) {
        super(RemoteType.BASE, spec, remotes, dynamic);
    }

    public BaseSpec getSpecification() {
        return (BaseSpec) this.spec;
    }

    @Override
    protected void decodeSpecification(JSONObject jsonSpec) {
        this.spec = new BaseSpec(jsonSpec);
    }

}
