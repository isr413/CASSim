package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.VictimSpecification;

import org.json.JSONException;
import org.json.JSONObject;

public class Victim {
    private static final String VICTIM_ID = "victim_id";

    private static int victimCount = 0;

    public static Victim decode(VictimSpecification victimSpec, JSONObject json) throws JSONException {
        int victimID = json.getInt(Victim.VICTIM_ID);
        return new Victim(victimSpec, victimID);
    }

    public static Victim decode(VictimSpecification victimSpec, String encoding) throws JSONException {
        return Victim.decode(victimSpec, new JSONObject(encoding));
    }

    public static int getID() {
        return Victim.victimCount;
    }

    private static void updateVictimCount() {
        Victim.victimCount++;
    }

    private VictimSpecification spec;
    private int victimID;

    public Victim(VictimSpecification victimSpec) {
        this(victimSpec, Victim.getID());
    }

    public Victim(VictimSpecification victimSpec, int victimID) {
        this.spec = victimSpec;
        this.victimID = victimID;
        Victim.updateVictimCount();
    }

    public String getLabel() {
        return String.format("<%d>", this.victimID);
    }

    public int getVictimID() {
        return this.victimID;
    }

    public VictimSpecification getVictimSpecification() {
        return this.spec;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Victim.VICTIM_ID, this.victimID);
        return new JSONObject();
    }

    public String toString() {
        return this.encode();
    }

}
