package com.seat.rescuesim.client.sandbox;

import java.util.ArrayList;

import com.seat.rescuesim.client.Application;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.base.BaseConfig;
import com.seat.rescuesim.common.base.BaseSpec;
import com.seat.rescuesim.common.base.BaseType;
import com.seat.rescuesim.common.drone.DroneConfig;
import com.seat.rescuesim.common.drone.DroneSpec;
import com.seat.rescuesim.common.drone.DroneType;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.victim.VictimConfig;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.common.victim.VictimType;

public class ACSOS implements Application {

    private static final int MAP_SIZE = 64;
    private static final int ZONE_SIZE = 10;
    private static final Vector BASE_LOCATION = new Vector(
        ((float) MAP_SIZE*ZONE_SIZE)/2.0,
        ((float) MAP_SIZE*ZONE_SIZE)/2.0
    );

    public ACSOS() {}

    public ACSOS(String[] args) {}

    private SensorSpec getBluetoothComms() {
        return new SensorSpec(
            SensorType.Comms,
            10,
            1,
            0.01,
            0
        );
    }

    private SensorSpec getDroneCamera() {
        return new SensorSpec(
            SensorType.Vision,
            10,
            1,
            0.01,
            0
        );
    }

    private SensorSpec getLongRangeComms() {
        return new SensorSpec(
            SensorType.Comms,
            100000,
            1,
            0.01,
            0
        );
    }

    private SensorSpec getVictimMonitor() {
        return new SensorSpec(
            SensorType.Comms,
            0,
            1,
            0.01,
            0
        );
    }

    public ArrayList<BaseConfig> getBaseConfigurations() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getLongRangeComms(), 1));
        BaseSpec baseSpec = new BaseSpec(
            BaseType.DEFAULT,
            BASE_LOCATION,
            1,
            sensors
        );
        ArrayList<BaseConfig> baseConf = new ArrayList<>();
        baseConf.add(new BaseConfig(baseSpec, 1, true));
        return baseConf;
    }

    public double getDisasterScale() {
        return 0;
    }

    public ArrayList<DroneConfig> getDroneConfigurations() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getLongRangeComms(), 1));
        sensors.add(new SensorConfig(this.getBluetoothComms(), 1));
        sensors.add(new SensorConfig(this.getDroneCamera(), 1));
        DroneSpec droneSpec = new DroneSpec(
            DroneType.DEFAULT,
            BASE_LOCATION,
            1,
            new Vector(0.01, 0.01, 0.01),
            sensors,
            30.0,
            3.0,
            1.0
        );
        ArrayList<DroneConfig> droneConf = new ArrayList<>();
        droneConf.add(new DroneConfig(droneSpec, 1, true));
        return droneConf;
    }

    public Map getMap() {
        return new Map(ZONE_SIZE, MAP_SIZE, MAP_SIZE);
    }

    public int getMissionLength() {
        // 27 minutes
        return 27*60;
    }

    public String getScenarioID() {
        return "ACSOS";
    }

    public double getStepSize() {
        return 0.5;
    }

    public ArrayList<VictimConfig> getVictimConfigurations() {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        sensors.add(new SensorConfig(this.getBluetoothComms(), 1));
        sensors.add(new SensorConfig(this.getVictimMonitor(), 1));
        VictimSpec victimSpec = new VictimSpec(
            VictimType.DEFAULT,
            new Vector(),
            1,
            sensors,
            1.50,
            1,
            1,
            1.25,
            0.01
        );
        ArrayList<VictimConfig> victimConf = new ArrayList<>();
        victimConf.add(new VictimConfig(victimSpec, 1, false));
        return victimConf;
    }

    public void run() {}

    public ArrayList<RemoteController> update(Snapshot snap) {
        return new ArrayList<>();
    }

}
