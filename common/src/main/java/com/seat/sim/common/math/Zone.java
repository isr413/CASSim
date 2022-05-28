package com.seat.sim.common.math;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;

/** Represents the single cubic zone within the map grid. */
public class Zone extends JSONAble {
    public static final String ZONE_LOCATION = "location";
    public static final String ZONE_SIZE = "size";

    private Vector location;
    private int size;

    public Zone(Vector location, int size) {
        this.location = location;
        this.size = size;
    }

    public Zone(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.location = new Vector(json.getJSONOptional(Zone.ZONE_LOCATION));
        this.size = json.getInt(Zone.ZONE_SIZE);
    }

    public String getLabel() {
        return String.format("<z: %s>", this.location.toString());
    }

    public Vector getLocation() {
        return this.location;
    }

    public int getSize() {
        return this.size;
    }

    public JSONOptional toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Zone.ZONE_LOCATION, this.location.toJSON());
        json.put(Zone.ZONE_SIZE, this.size);
        return json.toJSON();
    }

    public boolean equals(Zone zone) {
        if (zone == null) return false;
        return this.location.equals(zone.location);
    }

}
