package com.seat.rescuesim.client.sandbox;

import java.util.ArrayList;
import java.util.Collection;

import com.seat.rescuesim.common.core.SARApplication;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.base.BaseConfig;
import com.seat.rescuesim.common.remote.base.BaseProto;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneConfig;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimConfig;
import com.seat.rescuesim.common.remote.mobile.ground.VictimProto;
import com.seat.rescuesim.common.scenario.Snapshot;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.sensor.comms.CommsSensorProto;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorProto;
import com.seat.rescuesim.common.sensor.vision.VisionSensorProto;

public class ACSOS implements SARApplication {

    private static final int MAP_SIZE = 64;
    private static final int ZONE_SIZE = 10;
    private static final Vector BASE_LOCATION = new Vector(
        ((float) MAP_SIZE*ZONE_SIZE)/2.0,
        ((float) MAP_SIZE*ZONE_SIZE)/2.0
    );

    public ACSOS() {}

    public ACSOS(String[] args) {}

    private CommsSensorProto getBluetoothComms() {
        return new CommsSensorProto(
            SensorType.BLUETOOTH_COMMS,
            10,
            1,
            0.0,
            0
        );
    }

    private VisionSensorProto getDroneCamera() {
        return new VisionSensorProto(
            SensorType.CMOS_CAMERA_VISION,
            10,
            1,
            0.0
        );
    }

    private CommsSensorProto getLongRangeComms() {
        return new CommsSensorProto(
            SensorType.LTE_RADIO_COMMS,
            Double.POSITIVE_INFINITY,
            1,
            0.0,
            0
        );
    }

    private MonitorSensorProto getVictimHRVM() {
        return new MonitorSensorProto(
            SensorType.HRVM_MONITOR,
            0,
            1,
            0.0
        );
    }

    public Collection<BaseConfig> getBaseConfigs() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getLongRangeComms(), 1, true));
        BaseProto base = new BaseProto(
            BASE_LOCATION,
            1,
            sensors
        );
        ArrayList<BaseConfig> baseConfig = new ArrayList<>();
        baseConfig.add(new BaseConfig(base, 1, false, true));
        return baseConfig;
    }

    public double getDisasterScale() {
        return 0;
    }

    public Collection<DroneConfig> getDroneConfigs() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getLongRangeComms(), 1, true));
        sensors.add(new SensorConfig(this.getBluetoothComms(), 1, true));
        sensors.add(new SensorConfig(this.getDroneCamera(), 1, true));
        DroneProto drone = new DroneProto(
            BASE_LOCATION,
            1.0,
            sensors,
            new Vector(0.0, 0.0, 0.0),
            30.0,
            3.0,
            1.0
        );
        ArrayList<DroneConfig> droneConfig = new ArrayList<>();
        droneConfig.add(new DroneConfig(drone, 1, true, true));
        return droneConfig;
    }

    public Map getMap() {
        return new Map(ZONE_SIZE, MAP_SIZE, MAP_SIZE);
    }

    public int getMissionLength() {
        // 27 minutes
        return 27*60;
    }

    public Collection<RemoteConfig> getRemoteConfigs() {
        ArrayList<RemoteConfig> remotes = new ArrayList<>();
        remotes.addAll(this.getVictimConfigs());
        remotes.addAll(this.getBaseConfigs());
        remotes.addAll(this.getDroneConfigs());
        return remotes;
    }

    public String getScenarioID() {
        return "ACSOS";
    }

    public double getStepSize() {
        return 0.5;
    }

    public Collection<VictimConfig> getVictimConfigs() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getBluetoothComms(), 1, true));
        sensors.add(new SensorConfig(this.getVictimHRVM(), 1, true));
        VictimProto victim = new VictimProto(
            null,
            1.0,
            sensors,
            1.78,
            1.0,
            9.00,
            1.78,
            1.78
        );
        ArrayList<VictimConfig> victimConfig = new ArrayList<>();
        victimConfig.add(new VictimConfig(victim, 100, false, true));
        return victimConfig;
    }

    public Collection<IntentionSet> update(Snapshot snap) {
        return new ArrayList<>();
    }

}
