package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.DroneSpecification;

import org.json.JSONException;
import org.json.JSONObject;

public class DroneRemote {

    public static DroneRemote decode(JSONObject json, DroneSpecification droneSpec) throws JSONException {
        return null;
    }

    public static DroneRemote decode(String encoding, DroneSpecification droneSpec) throws JSONException {
        return DroneRemote.decode(new JSONObject(encoding), droneSpec);
    }

    private DroneSpecification spec;

    public DroneRemote(DroneSpecification droneSpec) {
        this.spec = droneSpec;
    }

    public DroneSpecification getDroneSpecification() {
        return this.spec;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        return new JSONObject();
    }

    public String toString() {
        return this.encode();
    }

}
