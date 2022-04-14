package com.seat.sim.common.remote.base;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable prototype of a Base Remote. */
public class BaseRemoteProto extends RemoteProto {

    public BaseRemoteProto(double maxBatteryPower) {
        this(null, maxBatteryPower, null);
    }

    public BaseRemoteProto(Vector location, double maxBatteryPower) {
        this(location, maxBatteryPower, null);
    }

    public BaseRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(null, maxBatteryPower, sensors);
    }

    public BaseRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        super(location, maxBatteryPower, sensors);
    }

    public BaseRemoteProto(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    public String getLabel() {
        return "r:<base>";
    }

}
