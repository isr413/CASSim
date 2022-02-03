package com.seat.rescuesim.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Zone {
    private static final String ZONE_LOCATION = "location";
    private static final String ZONE_FIELDS = "fields";
    private static final String ZONE_SIZE = "size";
    private static final String ZONE_TYPE = "type";

    private Vector location;
    private int size;
    private ZoneType type;
    private Field ground;
    private Field aerial;

    public static Zone decode(JSONObject json) throws JSONException {
        int type = json.getInt(Zone.ZONE_TYPE);
        Vector location = Vector.decode(json.getJSONArray(Zone.ZONE_LOCATION));
        int size = json.getInt(Zone.ZONE_SIZE);
        JSONArray fields = json.getJSONArray(Zone.ZONE_FIELDS);
        Field ground = Field.decode(fields.getJSONArray(0));
        Field aerial = Field.decode(fields.getJSONArray(1));
        return new Zone(location, size, ZoneType.values()[type], ground, aerial);
    }

    public static Zone decode(String encoding) throws JSONException {
        return Zone.decode(new JSONObject(encoding));
    }

    public Zone(Vector location, int size) {
        this.location = location;
        this.size = size;
        this.type = ZoneType.OPEN;
        this.ground = new Field();
        this.aerial = new Field();
    }

    public Zone(Vector location, int size, ZoneType zoneType, Field ground, Field aerial) {
        this.location = location;
        this.size = size;
        this.type = zoneType;
        this.ground = ground;
        this.aerial = aerial;
    }

    public Vector getLocation() {
        return this.location;
    }

    public int getSize() {
        return this.size;
    }

    public ZoneType getZoneType() {
        return this.type;
    }

    public Field getGroundField() {
        return this.ground;
    }

    public Field getAerialField() {
        return this.aerial;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Zone.ZONE_TYPE, this.type.getType());
        json.put(Zone.ZONE_LOCATION, this.location.toJSON());
        json.put(Zone.ZONE_SIZE, this.size);
        JSONArray fields = new JSONArray();
        fields.put(this.ground.toJSON());
        fields.put(this.aerial.toJSON());
        json.put(Zone.ZONE_FIELDS, fields);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(Zone zone) {
        return this.location.equals(zone.location);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
