package com.seat.sim.client.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.seat.sim.common.core.SARApplication;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteProto;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.mobile.aerial.AerialRemoteController;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteProto;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteConfig;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteProto;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorRegistry;
import com.seat.sim.common.sensor.comms.CommsSensorProto;
import com.seat.sim.common.sensor.monitor.MonitorSensorProto;
import com.seat.sim.common.sensor.vision.VisionSensorProto;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.common.util.Random;

public class ACSOS implements SARApplication {

    private static final int BASE_COUNT = 1;
    private static final int DRONE_COUNT = 1;
    private static final int MAP_SIZE = 64;
    private static final int VICTIM_COUNT = 0;
    private static final int ZONE_SIZE = 10;

    private static final Vector BASE_LOCATION = new Vector(
        (ACSOS.MAP_SIZE * ACSOS.ZONE_SIZE)/2.0,
        (ACSOS.MAP_SIZE * ACSOS.ZONE_SIZE)/2.0
    );

    private static Grid getACSOSGrid() {
        return new Grid(ACSOS.MAP_SIZE, ACSOS.MAP_SIZE, ACSOS.ZONE_SIZE);
    }

    private static BaseRemoteConfig getBaseRemoteConfiguration() {
        return new BaseRemoteConfig(
            new BaseRemoteProto(
                ACSOS.BASE_LOCATION,
                1,
                new ArrayList<SensorConfig>(
                    Arrays.asList(
                        new SensorConfig(
                            ACSOS.getLongRangeComms(),
                            1,
                            true
                        )
                    )
                )
            ),
            TeamColor.GREEN,
            ACSOS.BASE_COUNT,
            true,
            false
        );
    }

    private static CommsSensorProto getBluetoothComms() {
        return SensorRegistry.BluetoothSensor(10, 1, 0, 0);
    }

    private static VisionSensorProto getDroneCamera() {
        return SensorRegistry.CMOSCameraSensor(10, 1, 0);
    }

    private static DroneRemoteConfig getDroneRemoteConfiguration() {
        return new DroneRemoteConfig(
            new DroneRemoteProto(
                ACSOS.BASE_LOCATION,
                1,
                new ArrayList<SensorConfig>(
                    Arrays.asList(
                        new SensorConfig(
                            ACSOS.getLongRangeComms(),
                            1,
                            true
                        ),
                        new SensorConfig(
                            ACSOS.getBluetoothComms(),
                            1,
                            true
                        ),
                        new SensorConfig(
                            ACSOS.getDroneCamera(),
                            1,
                            true
                        )
                    )
                ),
                30,
                10,
                new Vector()
            ),
            TeamColor.BLUE,
            ACSOS.DRONE_COUNT,
            true,
            true
        );
    }

    private static CommsSensorProto getLongRangeComms() {
        return SensorRegistry.LTERadioSensor(Double.POSITIVE_INFINITY, 1, 0, 0);
    }

    private static MonitorSensorProto getVictimHRVM() {
        return SensorRegistry.HRVMSensor(0, 1, 0);
    }

    private static VictimRemoteConfig getVictimRemoteConfiguration() {
        return new VictimRemoteConfig(
            new VictimRemoteProto(
                null,
                1,
                new ArrayList<SensorConfig>(
                    Arrays.asList(
                        new SensorConfig(
                            ACSOS.getBluetoothComms(),
                            1,
                            true
                        ),
                        new SensorConfig(
                            ACSOS.getVictimHRVM(),
                            1,
                            true
                        )
                    )
                ),
                9,
                3
            ),
            TeamColor.RED,
            ACSOS.VICTIM_COUNT,
            true,
            false,
            1.78,
            1
        );
    }

    private Grid grid;
    private HashMap<String, Zone> localGoals;
    private List<RemoteConfig> remoteConfigs;
    private Random rng;

    public ACSOS() {
        this(null);
    }

    public ACSOS(String[] args) {
        this.rng = new Random();
        this.grid = ACSOS.getACSOSGrid();
        this.remoteConfigs = new ArrayList<>(
            Arrays.asList(
                ACSOS.getBaseRemoteConfiguration(),
                ACSOS.getDroneRemoteConfiguration(),
                ACSOS.getVictimRemoteConfiguration()
            )
        );
        this.localGoals = null;
    }

    public double getDisasterScale() {
        return 0;
    }

    public Grid getGrid() {
        return this.grid;
    }

    public int getMissionLength() {
        // 27 minutes
        return 27 * 60;
    }

    public Collection<RemoteConfig> getRemoteConfigs() {
        return this.remoteConfigs;
    }

    public String getScenarioID() {
        return "ACSOS";
    }

    public double getStepSize() {
        return 0.5;
    }

    public Collection<IntentionSet> update(Snapshot snap) {
        if (this.localGoals == null) {
            this.localGoals = new HashMap<>();
            for (String remoteID : this.getScenarioConfig().getDroneRemoteIDs()) {
                this.localGoals.put(
                    remoteID,
                    this.grid.getZone(
                        this.rng.getRandomNumber(this.grid.getWidthInZones()),
                        this.rng.getRandomNumber(this.grid.getHeightInZones())
                    )
                );
            }
        }
        ArrayList<IntentionSet> intentions = new ArrayList<>();
        for (String remoteID : this.localGoals.keySet()) {
            if (this.localGoals.get(remoteID) == null) {
                Debugger.logger.warn("Null goal");
                continue;
            }
            AerialRemoteController controller = new AerialRemoteController(remoteID);
            //controller.goToLocation(this.localGoals.get(remoteID).getLocation());
            controller.goToLocation(new Vector(280, 280));
            intentions.add(controller.getIntentions());
        }
        return intentions;
    }

}
