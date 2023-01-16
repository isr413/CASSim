package com.seat.sim.common.math;

import com.seat.sim.common.json.*;

/** Represents the single cubic zone within the map grid. */
public class Zone extends Jsonable {
    public static final String ZONE_LOCATION = "location";
    public static final String ZONE_SIZE = "size";

    private Vector location;
    private int size;

    public Zone(Vector location, int size) {
        this.location = location;
        this.size = size;
    }

    public Zone(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.location = new Vector(json.getJson(Zone.ZONE_LOCATION));
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

    public Json toJson() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(Zone.ZONE_LOCATION, this.location.toJson());
        json.put(Zone.ZONE_SIZE, this.size);
        return Json.of(json.toJson());
    }

    public boolean equals(Zone zone) {
        if (zone == null) return false;
        return this.location.equals(zone.location);
    }
}
