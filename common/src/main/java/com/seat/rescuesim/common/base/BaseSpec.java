package com.seat.rescuesim.common.base;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Base Remote. */
public class BaseSpec extends RemoteSpec {

    private BaseType type;

    public BaseSpec(BaseType type) {
        super(RemoteType.BASE);
        this.type = type;
    }

    public BaseSpec(BaseType type, Vector location) {
        super(RemoteType.BASE, location);
        this.type = type;
    }

    public BaseSpec(BaseType type, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        super(RemoteType.BASE, maxBatteryPower, sensors);
        this.type = type;
    }

    public BaseSpec(BaseType type, Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        super(RemoteType.BASE, location, maxBatteryPower, sensors);
        this.type = type;
    }

    public BaseSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = BaseType.values()[json.getInt(BaseConst.BASE_TYPE)];
    }

    @Override
    public String getLabel() {
        return String.format("b%s", this.type.getLabel());
    }

    public BaseType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(BaseConst.BASE_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(BaseSpec spec) {
        return super.equals(spec) && this.type.equals(spec.type);
    }

}
