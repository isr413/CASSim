package com.seat.rescuesim.common.remote.stat;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a static Remote. */
public class StaticRemoteSpec extends RemoteSpec {

    protected static final StaticRemoteType DEFAULT_SPEC_TYPE = StaticRemoteType.GENERIC;

    private StaticRemoteType specType;

    public StaticRemoteSpec() {
        this(StaticRemoteSpec.DEFAULT_SPEC_TYPE, null, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    public StaticRemoteSpec(double maxBatteryPower) {
        this(StaticRemoteSpec.DEFAULT_SPEC_TYPE, null, maxBatteryPower, null);
    }

    public StaticRemoteSpec(Vector location) {
        this(StaticRemoteSpec.DEFAULT_SPEC_TYPE, location, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    public StaticRemoteSpec(Vector location, double maxBatteryPower) {
        this(StaticRemoteSpec.DEFAULT_SPEC_TYPE, location, maxBatteryPower, null);
    }

    public StaticRemoteSpec(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(StaticRemoteSpec.DEFAULT_SPEC_TYPE, null, maxBatteryPower, sensors);
    }

    public StaticRemoteSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(StaticRemoteSpec.DEFAULT_SPEC_TYPE, location, maxBatteryPower, sensors);
    }

    public StaticRemoteSpec(StaticRemoteType specType) {
        this(specType, null, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    public StaticRemoteSpec(StaticRemoteType specType, double maxBatteryPower) {
        this(specType, null, maxBatteryPower, null);
    }

    public StaticRemoteSpec(StaticRemoteType specType, Vector location) {
        this(specType, location, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    public StaticRemoteSpec(StaticRemoteType specType, Vector location, double maxBatteryPower) {
        this(specType, location, maxBatteryPower, null);
    }

    public StaticRemoteSpec(StaticRemoteType specType, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(specType, null, maxBatteryPower, sensors);
    }

    public StaticRemoteSpec(StaticRemoteType specType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        super(RemoteType.STATIC, location, maxBatteryPower, sensors);
        this.specType = specType;
    }

    public StaticRemoteSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = StaticRemoteType.decodeType(json);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(StaticRemoteType.STATIC_REMOTE_TYPE, this.specType.getType());
        return json;
    }

    @Override
    public StaticRemoteType getSpecType() {
        return this.specType;
    }

    public boolean equals(StaticRemoteSpec spec) {
        return super.equals(spec) && this.specType.equals(spec.specType);
    }

}
