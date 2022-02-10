package com.seat.rescuesim.common.remote.snap;

import com.seat.rescuesim.common.json.*;

public class RemoteState extends JSONAble {

    public RemoteState(JSONObject json) {
        super(json);
    }

    public RemoteState(JSONOption option) {
        super(option);
    }

    public RemoteState(String encoding) {
        super(encoding);
    }

    @Override
    public void decode(JSONObject json) {

    }

    public String getRemoteLabel() {
        return null;
    }

    public JSONOption toJSON() {
        return null;
    }

    public boolean equals(RemoteState state) {
        return false;
    }

}
