package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable prototype of a Monitor Sensor. */
public class MonitorSensorProto extends SensorProto {

    protected static final MonitorSensorType DEFAULT_SPEC_TYPE = MonitorSensorType.GENERIC;

    private MonitorSensorType specType;

    public MonitorSensorProto() {
        this(MonitorSensorProto.DEFAULT_SPEC_TYPE, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorProto(double range) {
        this(MonitorSensorProto.DEFAULT_SPEC_TYPE, range, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorProto(double range, double accuracy) {
        this(MonitorSensorProto.DEFAULT_SPEC_TYPE, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorProto(double range, double accuracy, double batteryUsage) {
        this(MonitorSensorProto.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage);
    }

    public MonitorSensorProto(MonitorSensorType specType) {
        this(specType, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorProto(MonitorSensorType specType, double range) {
        this(specType, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorProto(MonitorSensorType specType, double range, double accuracy) {
        this(specType, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public MonitorSensorProto(MonitorSensorType specType, double range, double accuracy, double batteryUsage) {
        super(SensorType.MONITOR, range, accuracy, batteryUsage);
        this.specType = specType;
    }

    public MonitorSensorProto(JSONOption option) throws JSONException {
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
        json.put(MonitorSensorType.MONITOR_SENSOR_TYPE, this.specType.getType());
        return json;
    }

    @Override
    public MonitorSensorType getSpecType() {
        return (MonitorSensorType) this.specType;
    }

    public boolean equals(MonitorSensorProto spec) {
        return super.equals(spec) && this.specType.equals(spec.specType);
    }

}
