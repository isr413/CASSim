package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;

/** An abstract class to represent remote configurations. */
public abstract class RemoteConfiguration extends JSONAble {
    private static final String COUNT = "count";
    private static final String IS_DYNAMIC = "is_dynamic";
    private static final String REMOTES = "remotes";
    private static final String REMOTE_TYPE = "remote_type";
    private static final String SPEC = "spec";

    protected int count;
    protected boolean dynamic;
    protected HashSet<String> remotes;
    protected RemoteSpecification spec;
    protected RemoteType type;

    public RemoteConfiguration(JSONObject json) {
        super(json);
    }

    public RemoteConfiguration(JSONOption option) {
        super(option);
    }

    public RemoteConfiguration(String encoding) {
        super(encoding);
    }

    public RemoteConfiguration() {
        this(RemoteType.NONE, null, 0, new HashSet<String>(), false);
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec) {
        this(type, spec, 1, new HashSet<String>(), false);
        this.remotes.add(String.format("%s:(1)", spec.getLabel()));
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, String[] remotes) {
        this(type, spec, remotes.length, new HashSet<String>(Arrays.asList(remotes)), false);
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, ArrayList<String> remotes) {
        this(type, spec, remotes.size(), new HashSet<String>(remotes), false);
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, HashSet<String> remotes) {
        this(type, spec, remotes.size(), remotes, false);
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, int count) {
        this(type, spec, count, new HashSet<String>(), false);
        for (int i = 0; i < count; i++) {
            this.remotes.add(String.format("%s:(%d)", spec.getLabel(), i));
        }
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, boolean dynamic) {
        this(type, spec, 1, new HashSet<String>(), dynamic);
        this.remotes.add(String.format("%s:(1)", spec.getLabel()));
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, String[] remotes, boolean dynamic) {
        this(type, spec, remotes.length, new HashSet<String>(Arrays.asList(remotes)), dynamic);
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, ArrayList<String> remotes, boolean dynamic) {
        this(type, spec, remotes.size(), new HashSet<String>(remotes), dynamic);
    }

    public RemoteConfiguration(RemoteType type, RemoteSpecification spec, HashSet<String> remotes, boolean dynamic) {
        this(type, spec, remotes.size(), remotes, dynamic);
    }

    private RemoteConfiguration(RemoteType type, RemoteSpecification spec, int count, HashSet<String> remotes,
            boolean dynamic) {
        this.type = type;
        this.spec = spec;
        this.count = count;
        this.remotes = remotes;
        this.dynamic = dynamic;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(RemoteConfiguration.REMOTE_TYPE)];
        this.decodeSpecification(json.getJSONObject(RemoteConfiguration.SPEC));
        this.count = json.getInt(RemoteConfiguration.COUNT);
        JSONArray jsonRemotes = json.getJSONArray(RemoteConfiguration.REMOTES);
        this.remotes = new HashSet<>();
        for (int i = 0; i < jsonRemotes.length(); i++) {
            this.remotes.add(jsonRemotes.getString(i));
        }
        this.dynamic = json.getBoolean(RemoteConfiguration.IS_DYNAMIC);
    }

    /** Child class needs to override. */
    protected abstract void decodeSpecification(JSONObject json);

    public int getCount() {
        return this.count;
    }

    public HashSet<String> getRemotes() {
        return this.remotes;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public RemoteSpecification getSpecification() {
        return this.spec;
    }

    public boolean hasRemote(String remote) {
        return this.remotes.contains(remote);
    }

    public boolean hasRemotes() {
        return !this.remotes.isEmpty();
    }

    public boolean hasSpecification() {
        return this.spec != null;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteConfiguration.REMOTE_TYPE, this.type.getType());
        json.put(RemoteConfiguration.SPEC, this.spec.toJSON());
        json.put(RemoteConfiguration.COUNT, this.count);
        JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
        for (String remote : this.remotes) {
            jsonRemotes.put(remote);
        }
        json.put(RemoteConfiguration.REMOTES, jsonRemotes.toJSON());
        json.put(RemoteConfiguration.IS_DYNAMIC, this.dynamic);
        return json.toJSON();
    }

    public boolean equals(RemoteConfiguration conf) {
        return this.type.equals(conf.type) && this.spec.equals(conf.spec) && this.count == conf.count &&
            this.remotes.equals(conf.remotes) && this.dynamic == conf.dynamic;
    }

}
