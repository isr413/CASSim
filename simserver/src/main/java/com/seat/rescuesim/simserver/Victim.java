package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.VictimSpecification;

import org.json.JSONException;
import org.json.JSONObject;

public class Victim {

    public static Victim decode(JSONObject json, VictimSpecification victimSpec) throws JSONException {
        return null;
    }

    public static Victim decode(String encoding, VictimSpecification victimSpec) throws JSONException {
        return Victim.decode(new JSONObject(encoding), victimSpec);
    }

    private VictimSpecification spec;

    public Victim(VictimSpecification victimSpec) {
        this.spec = victimSpec;
    }

    public VictimSpecification getVictimSpecification() {
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
