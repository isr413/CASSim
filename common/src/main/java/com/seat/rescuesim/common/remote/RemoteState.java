package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

public abstract class RemoteState extends JSONAble {
    private static final String REMOTE = "remote";
    private static final String REMOTE_TYPE = "remote_type";

    public static RemoteType decodeType(JSONObject json) {
        return RemoteType.values()[json.getInt(RemoteState.REMOTE_TYPE)];
    }

    public static RemoteType decodeType(JSONOption option) {
        if (option.isSomeObject()) {
            return RemoteState.decodeType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode remote type of %s", option.toString()));
        return null;
    }

    public static RemoteType decodeType(String encoding) {
        return RemoteState.decodeType(JSONOption.String(encoding));
    }

    protected String remote;
    protected RemoteType type;

    public RemoteState(JSONObject json) {
        super(json);
    }

    public RemoteState(JSONOption option) {
        super(option);
    }

    public RemoteState(String encoding) {
        super(encoding);
    }

    public RemoteState(RemoteType type, String remote) {
        this.type = type;
        this.remote = remote;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(RemoteState.REMOTE_TYPE)];
        this.remote = json.getString(RemoteState.REMOTE);
    }

    public String getLabel() {
        return String.format("%s%s", this.remote, this.type.getLabel());
    }

    public String getRemoteID() {
        return this.remote;
    }

    public RemoteType getType() {
        return this.type;
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteState.REMOTE_TYPE, this.type.getType());
        json.put(RemoteState.REMOTE, this.remote);
        return json;
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteState state) {
        return this.type.equals(state.type) && this.remote.equals(state.remote);
    }

}
