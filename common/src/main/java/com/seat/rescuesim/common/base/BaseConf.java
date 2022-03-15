package com.seat.rescuesim.common.base;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConf;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Base Remote. */
public class BaseConf extends RemoteConf {

    public static BaseConf None() {
        return new BaseConf(BaseSpec.None(), 0, false);
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

    public BaseConf(BaseSpec spec, int count, boolean dynamic) {
        super(RemoteType.BASE, spec, count, dynamic);
    }

    public BaseConf(BaseSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.BASE, spec, remoteIDs, dynamic);
    }

    public BaseConf(BaseSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.BASE, spec, remoteIDs, dynamic);
    }

    @Override
    protected void decodeSpec(JSONObject jsonSpec) {
        this.spec = new BaseSpec(jsonSpec);
    }

    public BaseSpec getSpec() {
        return (BaseSpec) this.spec;
    }

    public BaseType getSpecType() {
        return (BaseType) this.spec.getSpecType();
    }

    public boolean hasSpec() {
        return !this.getSpecType().equals(BaseType.NONE);
    }

}
