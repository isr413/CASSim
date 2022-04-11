package com.seat.rescuesim.common.map;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Field;
import com.seat.rescuesim.common.math.FieldType;
import com.seat.rescuesim.common.math.Vector;

/** Represents the single cubic zone within the map grid. */
public class Zone extends JSONAble {
    public static final String ZONE_AERIAL = "aerial_field";
    public static final String ZONE_GROUND = "ground_field";
    public static final String ZONE_LOCATION = "location";
    public static final String ZONE_SIZE = "size";

    private Field aerial;
    private Field ground;
    private Vector location;
    private int size;

    public Zone(Vector location, int size) {
        this(location, size, null, null);
    }

    public Zone(Vector location, int size, Field ground, Field aerial) {
        this.location = location;
        this.size = size;
        this.ground = (ground != null) ? ground : new Field();
        this.aerial = (aerial != null) ? aerial : new Field();
    }

    public Zone(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.location = new Vector(json.getJSONOption(Zone.ZONE_LOCATION));
        this.size = json.getInt(Zone.ZONE_SIZE);
        this.ground = (json.hasKey(Zone.ZONE_GROUND)) ?
            this.ground = new Field(json.getJSONOption(Zone.ZONE_GROUND)) :
            new Field();
        this.aerial = (json.hasKey(Zone.ZONE_AERIAL)) ?
            this.aerial = new Field(json.getJSONOption(Zone.ZONE_AERIAL)) :
            new Field();
    }

    public Field getAerialField() {
        return this.aerial;
    }

    public Field getGroundField() {
        return this.ground;
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

    public boolean hasAerialField() {
        return !(this.aerial == null || this.aerial.getFieldType().equals(FieldType.NONE));
    }

    public boolean hasGroundField() {
        return !(this.ground == null || this.ground.getFieldType().equals(FieldType.NONE));
    }

    public JSONOption toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Zone.ZONE_LOCATION, this.location.toJSON());
        json.put(Zone.ZONE_SIZE, this.size);
        if (this.hasGroundField()) {
            json.put(Zone.ZONE_GROUND, this.ground.toJSON());
        }
        if (this.hasAerialField()) {
            json.put(Zone.ZONE_AERIAL, this.aerial.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(Zone zone) {
        if (zone == null) return false;
        return this.location.equals(zone.location);
    }

}
