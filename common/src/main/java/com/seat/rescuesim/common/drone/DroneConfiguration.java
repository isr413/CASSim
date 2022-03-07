package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;

/** A serializable class to represent the configuration of Drone Remotes.  */
public class DroneConfiguration extends JSONAble {
    private static final String DRONE_SPEC = "drone_spec";
    private static final String REMOTES = "remotes";

    private DroneSpecification droneSpec;
    private HashSet<String> remotes;

    public DroneConfiguration(JSONObject json) {
        super(json);
    }

    public DroneConfiguration(JSONOption option) {
        super(option);
    }

    public DroneConfiguration(String encoding) {
        super(encoding);
    }

    public DroneConfiguration() {
        this(new HashSet<String>(), new DroneSpecification());
    }

    public DroneConfiguration(DroneSpecification droneSpec) {
        this(new HashSet<String>(), droneSpec);
    }

    public DroneConfiguration(String[] remotes, DroneSpecification droneSpec) {
        this(new ArrayList<String>(Arrays.asList(remotes)), droneSpec);
    }

    public DroneConfiguration(ArrayList<String> remotes, DroneSpecification droneSpec) {
        this(new HashSet<String>(), droneSpec);
        for (String remote : remotes) {
            this.remotes.add(remote);
        }
    }

    public DroneConfiguration(HashSet<String> remotes, DroneSpecification droneSpec) {
        this.remotes = remotes;
        this.droneSpec = droneSpec;
    }

    public DroneSpecification getDroneSpecification() {
        return this.droneSpec;
    }

    public boolean hasDroneSpecification() {
        return this.droneSpec.getDroneType() != DroneType.NONE;
    }

    @Override
    protected void decode(JSONObject json) {
        this.remotes = new HashSet<>();
        JSONArray jsonRemotes = json.getJSONArray(DroneConfiguration.REMOTES);
        for (int i = 0; i < jsonRemotes.length(); i++) {
            this.remotes.add(jsonRemotes.getString(i));
        }
        this.droneSpec = new DroneSpecification(json.getJSONObject(DroneConfiguration.DRONE_SPEC));
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
        for (String remote : this.remotes) {
            jsonRemotes.put(remote);
        }
        json.put(DroneConfiguration.REMOTES, jsonRemotes.toJSON());
        json.put(DroneConfiguration.DRONE_SPEC, this.droneSpec.toJSON());
        return json.toJSON();
    }

    public boolean equals(DroneConfiguration conf) {
        return this.remotes.equals(conf.remotes) && this.droneSpec.equals(conf.droneSpec);
    }

}
