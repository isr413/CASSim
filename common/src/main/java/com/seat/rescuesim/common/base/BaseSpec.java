package com.seat.rescuesim.common.base;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConf;

/** A serializable specification of a Base. */
public class BaseSpec extends RemoteSpec {
    private static final String BASE_TYPE = "drone_type";

    public static BaseSpec None() {
        return new BaseSpec(BaseType.NONE, new Vector(), 0, new ArrayList<SensorConf>());
    }

    public static BaseSpec Static(Vector location) {
        return new BaseSpec(BaseType.DEFAULT, location, 1, new ArrayList<SensorConf>());
    }

    public static BaseSpec StaticWithSensors(Vector location, double maxBatteryPower, ArrayList<SensorConf> sensors) {
        return new BaseSpec(BaseType.DEFAULT, location, maxBatteryPower, sensors);
    }

    private BaseType type;

    public BaseSpec(JSONObject json) {
        super(json);
    }

    public BaseSpec(JSONOption option) {
        super(option);
    }

    public BaseSpec(String encoding) {
        super(encoding);
    }

    public BaseSpec(BaseType type, Vector location, double maxBatteryPower, ArrayList<SensorConf> sensors) {
        super(RemoteType.BASE, location, maxBatteryPower, sensors);
        this.type = type;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.type = BaseType.values()[json.getInt(BaseSpec.BASE_TYPE)];
    }

    @Override
    public String getLabel() {
        return String.format("d%s", this.type.getLabel());
    }

    public BaseType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(BaseSpec.BASE_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(BaseSpec spec) {
        return super.equals(spec) && this.type.equals(spec.type);
    }

}
