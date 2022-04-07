package com.seat.rescuesim.common.remote.base;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable prototype of a Base Remote. */
public class BaseProto extends RemoteProto {

    public BaseProto() {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null);
    }

    public BaseProto(double maxBatteryPower) {
        this(null, maxBatteryPower, null);
    }

    public BaseProto(Vector location) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null);
    }

    public BaseProto(Vector location, double maxBatteryPower) {
        this(location, maxBatteryPower, null);
    }

    public BaseProto(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(null, maxBatteryPower, sensors);
    }

    public BaseProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        super(RemoteType.BASE, location, maxBatteryPower, sensors);
    }

    public BaseProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public String getLabel() {
        return String.format("b:%s", this.getRemoteType().getLabel());
    }

}
