package com.seat.rescuesim.common.remote.stat.base;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.stat.StaticRemoteSpec;
import com.seat.rescuesim.common.remote.stat.StaticRemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Base Remote. */
public class BaseSpec extends StaticRemoteSpec {

    public BaseSpec() {
        super(StaticRemoteType.BASE);
    }

    public BaseSpec(double maxBatteryPower) {
        super(StaticRemoteType.BASE, maxBatteryPower);
    }

    public BaseSpec(Vector location) {
        super(StaticRemoteType.BASE, location);
    }

    public BaseSpec(Vector location, double maxBatteryPower) {
        super(StaticRemoteType.BASE, location, maxBatteryPower);
    }

    public BaseSpec(double maxBatteryPower, Collection<SensorConfig> sensors) {
        super(StaticRemoteType.BASE, maxBatteryPower, sensors);
    }

    public BaseSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        super(StaticRemoteType.BASE, location, maxBatteryPower, sensors);
    }

    public BaseSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public String getLabel() {
        return String.format("b:%s", this.getSpecType().getLabel());
    }

}
