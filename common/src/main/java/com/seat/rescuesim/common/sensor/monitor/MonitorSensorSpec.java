package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable representation of a Monitor Sensor spec. */
public class MonitorSensorSpec extends SensorSpec {

    protected MonitorSensorType specType;

    public MonitorSensorSpec() {
        this(MonitorSensorType.GENERIC);
    }

    public MonitorSensorSpec(double range) {
        this(MonitorSensorType.GENERIC, range);
    }

    public MonitorSensorSpec(double range, double accuracy) {
        this(MonitorSensorType.GENERIC, range, accuracy);
    }

    public MonitorSensorSpec(double range, double accuracy, double batteryUsage) {
        this(MonitorSensorType.GENERIC, range, accuracy, batteryUsage);
    }

    public MonitorSensorSpec(MonitorSensorType specType) {
        super(SensorType.MONITOR);
        this.specType = specType;
    }

    public MonitorSensorSpec(MonitorSensorType specType, double range) {
        super(SensorType.MONITOR, range);
        this.specType = specType;
    }

    public MonitorSensorSpec(MonitorSensorType specType, double range, double accuracy) {
        super(SensorType.MONITOR, range, accuracy);
        this.specType = specType;
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
