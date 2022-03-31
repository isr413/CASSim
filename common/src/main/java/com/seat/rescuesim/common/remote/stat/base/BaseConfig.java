package com.seat.rescuesim.common.remote.stat.base;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.stat.StaticRemoteConfig;

/** A serializable configuration of a Base Remote. */
public class BaseConfig extends StaticRemoteConfig {

    public BaseConfig(BaseSpec spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public BaseConfig(BaseSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public BaseConfig(BaseSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public BaseConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decodeSpec(JSONOption jsonSpec) throws JSONException {
        this.spec = new BaseSpec(jsonSpec);
    }

    public BaseSpec getSpec() {
        return (BaseSpec) this.spec;
    }

}
