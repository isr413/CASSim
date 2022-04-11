package com.seat.rescuesim.simserver.sensor;

import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.comms.CommsSensorProto;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorProto;
import com.seat.rescuesim.common.sensor.vision.VisionSensorProto;

public class SensorFactory {

    public static Sensor getSensor(SensorProto proto, String sensorID, boolean active) {
        if (CommsSensorProto.class.isAssignableFrom(proto.getClass()))
            return new CommsSensor((CommsSensorProto) proto, sensorID, active);
        if (MonitorSensorProto.class.isAssignableFrom(proto.getClass()))
            return new MonitorSensor((MonitorSensorProto) proto, sensorID, active);
        if (VisionSensorProto.class.isAssignableFrom(proto.getClass()))
            return new VisionSensor((VisionSensorProto) proto, sensorID, active);
        return new Sensor(proto, sensorID, active);
    }

}
