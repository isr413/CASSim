package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.Debugger;
import com.seat.rescuesim.common.VictimSpecification;

import org.json.JSONException;
import org.json.JSONObject;

public class Victim {
    private static final String VICTIM_BATTERY = "battery_power";
    private static final String VICTIM_ID = "victim_id";

    private static int victimCount = 0;

    public static Victim decode(VictimSpecification victimSpec, JSONObject json) throws JSONException {
        int victimID = json.getInt(Victim.VICTIM_ID);
        double batteryPower = json.getDouble(Victim.VICTIM_BATTERY);
        return new Victim(victimSpec, victimID, batteryPower);
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

    private double batteryPower;
    private VictimSpecification spec;
    private int victimID;

    public Victim(VictimSpecification victimSpec) {
        this(victimSpec, Victim.getID(), victimSpec.getMaxBatteryPower());
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower) {
        this.spec = victimSpec;
        this.victimID = victimID;
        this.batteryPower = batteryPower;
        Victim.updateVictimCount();
    }

    public void chargeBattery(double batteryPowerDelta) {
        if (batteryPowerDelta < 0) {
            Debugger.logger.err(String.format("Cannot update battery power of victim %s by %.2f",
                this.getLabel(), batteryPowerDelta));
            return;
        }
        if (this.spec.getMaxBatteryPower() < this.batteryPower + batteryPowerDelta) {
            this.setBatteryPower(this.spec.getMaxBatteryPower());
        } else {
            this.setBatteryPower(this.batteryPower + batteryPowerDelta);
        }
    }

    public void disable() {
        this.batteryPower = 0;
    }

    public double getBatteryPower() {
        return this.batteryPower;
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

    public boolean isAlive() {
        return this.batteryPower > 0;
    }

    public boolean isDisabled() {
        return !this.isAlive();
    }

    private void setBatteryPower(double batteryPower) {
        if (batteryPower < 0 || this.spec.getMaxBatteryPower() < batteryPower) {
            Debugger.logger.warn(String.format("Cannot set battery power of victim %s to be %.2f",
                this.getLabel(), batteryPower));
            batteryPower = (batteryPower < 0) ? 0 : this.spec.getMaxBatteryPower();
            Debugger.logger.state(String.format("Setting battery power of victim %s to be %.2f",
                this.getLabel(), batteryPower));
        }
        this.batteryPower = batteryPower;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Victim.VICTIM_ID, this.victimID);
        json.put(Victim.VICTIM_BATTERY, this.batteryPower);
        return new JSONObject();
    }

    public String toString() {
        return this.encode();
    }

}
