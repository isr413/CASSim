package com.seat.rescuesim.client.sandbox;

import java.util.ArrayList;

import com.seat.rescuesim.client.Application;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.base.BaseConf;
import com.seat.rescuesim.common.base.BaseSpec;
import com.seat.rescuesim.common.base.BaseType;
import com.seat.rescuesim.common.drone.DroneConf;
import com.seat.rescuesim.common.drone.DroneSpec;
import com.seat.rescuesim.common.drone.DroneType;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.Remote;
import com.seat.rescuesim.common.sensor.SensorConf;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.victim.VictimConf;
import com.seat.rescuesim.common.victim.VictimSpec;

public class ACSOS implements Application {

    private static final int MAP_SIZE = 64;
    private static final int ZONE_SIZE = 10;
    private static final Vector BASE_LOCATION = new Vector(
        ((float) MAP_SIZE*ZONE_SIZE)/10.0,
        ((float) MAP_SIZE*ZONE_SIZE)/10.0
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

    public ArrayList<BaseConf> getBaseConfigurations() {
        ArrayList<SensorConf> sensors = new ArrayList<>();
        sensors.add(new SensorConf(this.getLongRangeComms()));
        BaseSpec baseSpec = new BaseSpec(
            BaseType.DEFAULT,
            BASE_LOCATION,
            sensors
        );
        ArrayList<BaseConf> baseConf = new ArrayList<>();
        baseConf.add(new BaseConf(baseSpec));
        return baseConf;
    }

    public double getDisasterScale() {
        return 0;
    }

    public ArrayList<DroneConf> getDroneConfigurations() {
        ArrayList<SensorConf> sensors = new ArrayList<>();
        sensors.add(new SensorConf(this.getLongRangeComms()));
        sensors.add(new SensorConf(this.getBluetoothComms()));
        sensors.add(new SensorConf(this.getDroneCamera()));
        DroneSpec droneSpec = new DroneSpec(
            DroneType.DEFAULT,
            1,
            new Vector(0.01, 0.01, 0.01),
            BASE_LOCATION,
            30.0,
            3.0,
            1.0,
            sensors
        );
        ArrayList<DroneConf> droneConf = new ArrayList<>();
        droneConf.add(new DroneConf(droneSpec));
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

    public ArrayList<VictimConf> getVictimConfigurations() {
        ArrayList<SensorConf> sensors = new ArrayList<>();
        sensors.add(new SensorConf(this.getBluetoothComms()));
        sensors.add(new SensorConf(this.getVictimMonitor()));
        VictimSpec victimSpec = new VictimSpec(
            new Double[]{1.25, 0.01},
            1.50,
            1,
            0.01,
            sensors
        );
        ArrayList<VictimConf> victimConf = new ArrayList<>();
        victimConf.add(new VictimConf(victimSpec));
        return victimConf;
    }

    public void run() {}

    public ArrayList<Remote> update(Snapshot snap) {
        return null;
    }

}
