package com.seat.rescuesim.common.remote.stat;

import java.util.ArrayList;

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

    protected StaticRemoteType specType;

    public StaticRemoteSpec() {
        this(StaticRemoteType.GENERIC);
    }

    public StaticRemoteSpec(Vector location) {
        this(StaticRemoteType.GENERIC, location);
    }

    public StaticRemoteSpec(double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        this(StaticRemoteType.GENERIC, maxBatteryPower, sensors);
    }

    public StaticRemoteSpec(Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors) {
        this(StaticRemoteType.GENERIC, location, maxBatteryPower, sensors);
    }

    public StaticRemoteSpec(StaticRemoteType specType) {
        super(RemoteType.STATIC);
        this.specType = specType;
    }

    public StaticRemoteSpec(StaticRemoteType specType, Vector location) {
        super(RemoteType.STATIC, location);
        this.specType = specType;
    }

    public StaticRemoteSpec(StaticRemoteType specType, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        super(RemoteType.STATIC, maxBatteryPower, sensors);
        this.specType = specType;
    }

    public StaticRemoteSpec(StaticRemoteType specType, Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors) {
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
        json.put(StaticRemoteConst.STATIC_REMOTE_TYPE, this.specType.getType());
        return json;
    }

    @Override
    public String getLabel() {
        return String.format("r%s", this.specType.getLabel());
    }

    public StaticRemoteType getSpecType() {
        return this.specType;
    }

    public boolean equals(StaticRemoteSpec spec) {
        return super.equals(spec) && this.specType.equals(spec.specType);
    }

}
