package com.seat.rescuesim.simserver.sensor;

import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.comms.CommsSensorProto;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorProto;
import com.seat.rescuesim.common.sensor.vision.VisionSensorProto;

public class SensorFactory {

    public static Sensor getSensor(SensorProto proto, String sensorID, boolean active) {
        switch (proto.getSensorType()) {
            case BLUETOOTH_COMMS:
            case LTE_RADIO_COMMS:
            case COMMS: return new CommsSensor((CommsSensorProto) proto, sensorID, active);
            case HRVM_MONITOR:
            case MONITOR: return new MonitorSensor((MonitorSensorProto) proto, sensorID, active);
            case CMOS_CAMERA_VISION:
            case VISION: return new VisionSensor((VisionSensorProto) proto, sensorID, active);
            default: return new Sensor(proto, sensorID, active);
        }
    }

}
