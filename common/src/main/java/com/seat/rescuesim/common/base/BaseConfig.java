package com.seat.rescuesim.common.base;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Base Remote. */
public class BaseConfig extends RemoteConfig {

    public static BaseConfig None() {
        return new BaseConfig(BaseSpec.None(), 0, false);
    }

    public BaseConfig(JSONObject json) {
        super(json);
    }

    public BaseConfig(JSONOption option) {
        super(option);
    }

    public BaseConfig(String encoding) {
        super(encoding);
    }

    public BaseConfig(BaseSpec spec, int count, boolean dynamic) {
        super(RemoteType.BASE, spec, count, dynamic);
    }

    public BaseConfig(BaseSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.BASE, spec, remoteIDs, dynamic);
    }

    public BaseConfig(BaseSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
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
