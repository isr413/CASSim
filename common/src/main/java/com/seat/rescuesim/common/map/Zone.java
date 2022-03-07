package com.seat.rescuesim.common.map;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.*;

/** Represents the single cubic zone within the map grid. */
public class Zone extends JSONAble {
    private static final String ZONE_LOCATION = "location";
    private static final String ZONE_FIELDS = "fields";
    private static final String ZONE_SIZE = "size";
    private static final String ZONE_TYPE = "type";

    private Field aerial;
    private Field ground;
    private Vector location;
    private int size;
    private ZoneType type;

    public Zone(JSONObject json) {
        super(json);
    }

    public Zone(JSONOption option) {
        super(option);
    }

    public Zone(String encoding) {
        super(encoding);
    }

    public Zone(Vector location, int size) {
        this(ZoneType.OPEN, location, size, new Field(), new Field());
    }

    public Zone(ZoneType type, Vector location, int size) {
        this(type, location, size, new Field(), new Field());
    }

    public Zone(ZoneType type, Vector location, int size, Field ground, Field aerial) {
        this.type = type;
        this.location = location;
        this.size = size;
        this.ground = ground;
        this.aerial = aerial;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = ZoneType.values()[json.getInt(Zone.ZONE_TYPE)];
        this.location = new Vector(json.getJSONArray(Zone.ZONE_LOCATION));
        this.size = json.getInt(Zone.ZONE_SIZE);
        JSONArray fields = json.getJSONArray(Zone.ZONE_FIELDS);
        this.ground = new Field(fields.getJSONArray(0));
        this.aerial = new Field(fields.getJSONArray(1));
    }

    public Field getAerialField() {
        return this.aerial;
    }

    public Field getGroundField() {
        return this.ground;
    }

    public String getLabel() {
        return String.format("<%d, %s>", this.type.getType(), this.location.toString());
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

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Zone.ZONE_TYPE, this.type.getType());
        json.put(Zone.ZONE_LOCATION, this.location.toJSON());
        json.put(Zone.ZONE_SIZE, this.size);
        JSONArrayBuilder fields = JSONBuilder.Array();
        fields.put(this.ground.toJSON());
        fields.put(this.aerial.toJSON());
        json.put(Zone.ZONE_FIELDS, fields.toJSON());
        return json.toJSON();
    }

    public boolean equals(Zone zone) {
        return this.location.equals(zone.location);
    }

}
