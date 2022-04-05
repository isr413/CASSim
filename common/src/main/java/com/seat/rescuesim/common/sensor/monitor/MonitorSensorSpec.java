package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable representation of a Monitor Sensor spec. */
public class MonitorSensorSpec extends SensorSpec {

    protected static final MonitorSensorType DEFAULT_SPEC_TYPE = MonitorSensorType.GENERIC;

    private MonitorSensorType specType;

    public MonitorSensorSpec() {
        this(MonitorSensorSpec.DEFAULT_SPEC_TYPE, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY,
            SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorSpec(double range) {
        this(MonitorSensorSpec.DEFAULT_SPEC_TYPE, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorSpec(double range, double accuracy) {
        this(MonitorSensorSpec.DEFAULT_SPEC_TYPE, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorSpec(double range, double accuracy, double batteryUsage) {
        this(MonitorSensorSpec.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage);
    }

    public MonitorSensorSpec(MonitorSensorType specType) {
        this(specType, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorSpec(MonitorSensorType specType, double range) {
        this(specType, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorSpec(MonitorSensorType specType, double range, double accuracy) {
        this(specType, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorSpec(MonitorSensorType specType, double range, double accuracy, double batteryUsage) {
        super(SensorType.MONITOR, range, accuracy, batteryUsage);
        this.specType = specType;
    }

    public MonitorSensorSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = MonitorSensorType.decodeType(json);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MonitorSensorConst.MONITOR_SENSOR_TYPE, this.specType.getType());
        return json;
    }

    @Override
    public MonitorSensorType getSpecType() {
        return (MonitorSensorType) this.specType;
    }

    public boolean equals(MonitorSensorSpec spec) {
        return super.equals(spec) && this.specType.equals(spec.specType);
    }

}
