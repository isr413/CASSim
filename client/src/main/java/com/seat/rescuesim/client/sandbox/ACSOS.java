package com.seat.rescuesim.client.sandbox;

import java.util.ArrayList;
import java.util.Collection;

import com.seat.rescuesim.common.core.SARApplication;
import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.base.BaseRemoteConfig;
import com.seat.rescuesim.common.remote.base.BaseRemoteProto;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteConfig;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteConfig;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.rescuesim.common.scenario.Snapshot;
import com.seat.rescuesim.common.sensor.SensorConfig;
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
            10,
            1,
            0.0,
            0
        );
    }

    private VisionSensorProto getDroneCamera() {
        return new VisionSensorProto(
            10,
            1,
            0.0
        );
    }

    private CommsSensorProto getLongRangeComms() {
        return new CommsSensorProto(
            Double.POSITIVE_INFINITY,
            1,
            0.0,
            0
        );
    }

    private MonitorSensorProto getVictimHRVM() {
        return new MonitorSensorProto(
            0,
            1,
            0.0
        );
    }

    public Collection<BaseRemoteConfig> getBaseRemoteConfigs() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getLongRangeComms(), 1, true));
        BaseRemoteProto base = new BaseRemoteProto(
            BASE_LOCATION,
            1,
            sensors
        );
        ArrayList<BaseRemoteConfig> baseConfig = new ArrayList<>();
        baseConfig.add(new BaseRemoteConfig(base, TeamColor.GREEN, 1, false, true));
        return baseConfig;
    }

    public double getDisasterScale() {
        return 0;
    }

    public Collection<DroneRemoteConfig> getDroneRemoteConfigs() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getLongRangeComms(), 1, true));
        sensors.add(new SensorConfig(this.getBluetoothComms(), 1, true));
        sensors.add(new SensorConfig(this.getDroneCamera(), 1, true));
        DroneRemoteProto drone = new DroneRemoteProto(
            BASE_LOCATION,
            1.0,
            sensors,
            new Vector(0.0, 0.0, 0.0),
            30.0,
            3.0,
            1.0
        );
        ArrayList<DroneRemoteConfig> droneConfig = new ArrayList<>();
        droneConfig.add(new DroneRemoteConfig(drone, TeamColor.BLUE, 1, true, true));
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
        remotes.addAll(this.getVictimRemoteConfigs());
        remotes.addAll(this.getBaseRemoteConfigs());
        remotes.addAll(this.getDroneRemoteConfigs());
        return remotes;
    }

    public String getScenarioID() {
        return "ACSOS";
    }

    public double getStepSize() {
        return 0.5;
    }

    public Collection<VictimRemoteConfig> getVictimRemoteConfigs() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getBluetoothComms(), 1, true));
        sensors.add(new SensorConfig(this.getVictimHRVM(), 1, true));
        VictimRemoteProto victim = new VictimRemoteProto(
            null,
            1.0,
            sensors,
            1.78,
            1.0,
            9.00,
            1.78,
            1.78
        );
        ArrayList<VictimRemoteConfig> victimConfig = new ArrayList<>();
        victimConfig.add(new VictimRemoteConfig(victim, TeamColor.RED, 100, false, true));
        return victimConfig;
    }

    public Collection<IntentionSet> update(Snapshot snap) {
        return new ArrayList<>();
    }

}
