package com.seat.rescuesim.common.sensor;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfiguration;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable class to represent the configuration of Drone Remotes.  */
public class SensorConf extends RemoteConfiguration {

    public static SensorConf None() {
        return new SensorConf();
    }

    public SensorConf(JSONObject json) {
        super(json);
    }

    public SensorConf(JSONOption option) {
        super(option);
    }

    public SensorConf(String encoding) {
        super(encoding);
    }

    public SensorConf() {
        super();
    }

    public SensorConf(SensorSpec spec) {
        super(RemoteType.SENSOR, spec);
    }

    public SensorConf(SensorSpec spec, String[] remotes) {
        super(RemoteType.SENSOR, spec, remotes);
    }

    public SensorConf(SensorSpec spec, ArrayList<String> remotes) {
        super(RemoteType.SENSOR, spec, remotes);
    }

    public SensorConf(SensorSpec spec, HashSet<String> remotes) {
        super(RemoteType.SENSOR, spec, remotes);
    }

    public SensorConf(SensorSpec spec, int count) {
        super(RemoteType.SENSOR, spec, count);
    }

    public SensorConf(SensorSpec spec, boolean dynamic) {
        super(RemoteType.SENSOR, spec, dynamic);
    }

    public SensorConf(SensorSpec spec, String[] remotes, boolean dynamic) {
        super(RemoteType.SENSOR, spec, remotes, dynamic);
    }

    public SensorConf(SensorSpec spec, ArrayList<String> remotes, boolean dynamic) {
        super(RemoteType.SENSOR, spec, remotes, dynamic);
    }

    public SensorConf(SensorSpec spec, HashSet<String> remotes, boolean dynamic) {
        super(RemoteType.SENSOR, spec, remotes, dynamic);
    }

    public SensorSpec getSpecification() {
        return (SensorSpec) this.spec;
    }

    @Override
    protected void decodeSpecification(JSONObject json) {
        this.spec = new SensorSpec(json);
    }

}
