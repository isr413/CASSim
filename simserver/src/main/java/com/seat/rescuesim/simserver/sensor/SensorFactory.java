package com.seat.rescuesim.simserver.sensor;

import com.seat.rescuesim.common.sensor.SensorProto;

public class SensorFactory {

    public static Sensor getSensor(SensorProto proto, String sensorID, boolean active) {
        switch (proto.getSensorType()) {
            case BLUETOOTH_COMMS:
            case LTE_RADIO_COMMS:
            case COMMS: return new CommsSensor(proto, sensorID, active);
            case HRVM_MONITOR:
            case MONITOR: return new MonitorSensor(proto, sensorID, active);
            case CMOS_CAMERA_VISION:
            case VISION: return new VisionSensor(proto, sensorID, active);
            default: return new Sensor(proto, sensorID, active);
        }
    }

}
