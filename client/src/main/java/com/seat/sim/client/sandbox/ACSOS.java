package com.seat.sim.client.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import com.seat.sim.common.core.SARApplication;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteProto;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.mobile.aerial.DroneRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.sim.common.remote.mobile.ground.VictimRemoteConfig;
import com.seat.sim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorRegistry;
import com.seat.sim.common.sensor.comms.CommsSensorProto;
import com.seat.sim.common.sensor.monitor.MonitorSensorProto;
import com.seat.sim.common.sensor.vision.VisionSensorProto;
import com.seat.sim.common.util.Random;

public class ACSOS implements SARApplication {

    private static final int BASE_COUNT = 1;
    private static final int DRONE_COUNT = 32;
    private static final int MAP_SIZE = 64;
    private static final int VICTIM_COUNT = 100;
    private static final int ZONE_SIZE = 10;

    private static final Vector BASE_LOCATION = new Vector(
        (ACSOS.MAP_SIZE * ACSOS.ZONE_SIZE)/2.0,
        (ACSOS.MAP_SIZE * ACSOS.ZONE_SIZE)/2.0
    );

    private static Grid getACSOSGrid() {
        return new Grid(ACSOS.ZONE_SIZE, ACSOS.MAP_SIZE, ACSOS.MAP_SIZE);
    }

    private static ArrayList<BaseRemoteConfig> getBaseRemoteConfiguration() {
        return new ArrayList<BaseRemoteConfig>(
            Arrays.asList(
                new BaseRemoteConfig(
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
                    false,
                    true
                )
            )
        );
    }

    private static CommsSensorProto getBluetoothComms() {
        return SensorRegistry.BluetoothSensor(10, 1, 0, 0);
    }

    private static VisionSensorProto getDroneCamera() {
        return SensorRegistry.CMOSCameraSensor(10, 1, 0);
    }

    private static ArrayList<DroneRemoteConfig> getDroneRemoteConfiguration() {
        return new ArrayList<DroneRemoteConfig>(
            Arrays.asList(
                new DroneRemoteConfig(
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
                        new Vector(),
                        30,
                        3,
                        1
                    ),
                    TeamColor.BLUE,
                    ACSOS.DRONE_COUNT,
                    true,
                    true
                )
            )
        );
    }

    private static CommsSensorProto getLongRangeComms() {
        return SensorRegistry.LTERadioSensor(Double.POSITIVE_INFINITY, 1, 0, 0);
    }

    private static MonitorSensorProto getVictimHRVM() {
        return SensorRegistry.HRVMSensor(0, 1, 0);
    }

    private static ArrayList<VictimRemoteConfig> getVictimRemoteConfiguration() {
        return new ArrayList<VictimRemoteConfig>(
            Arrays.asList(
                new VictimRemoteConfig(
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
                        1.78,
                        1,
                        9,
                        1.78,
                        1.78
                    ),
                    TeamColor.RED,
                    ACSOS.VICTIM_COUNT,
                    false,
                    true
                )
            )
        );
    }

    private ArrayList<BaseRemoteConfig> baseConfigs;
    private ArrayList<DroneRemoteConfig> droneConfigs;
    private Grid grid;
    private HashMap<String, Zone> localGoals;
    private ArrayList<RemoteConfig> remoteConfigs;
    private Random rng;
    private ArrayList<VictimRemoteConfig> victimConfigs;

    public ACSOS() {
        this(null);
    }

    public ACSOS(String[] args) {
        this.rng = new Random();
        this.grid = ACSOS.getACSOSGrid();
        this.baseConfigs = ACSOS.getBaseRemoteConfiguration();
        this.droneConfigs = ACSOS.getDroneRemoteConfiguration();
        this.victimConfigs = ACSOS.getVictimRemoteConfiguration();
        this.remoteConfigs = new ArrayList<RemoteConfig>(){{
            addAll(baseConfigs);
            addAll(droneConfigs);
            addAll(victimConfigs);
        }};
        this.localGoals = null;
    }

    public Collection<BaseRemoteConfig> getBaseRemoteConfigs() {
        return this.baseConfigs;
    }

    public double getDisasterScale() {
        return 0;
    }

    public Collection<DroneRemoteConfig> getDroneRemoteConfigs() {
        return this.droneConfigs;
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

    public Collection<VictimRemoteConfig> getVictimRemoteConfigs() {
        return this.victimConfigs;
    }

    public Collection<IntentionSet> update(Snapshot snap) {
        if (this.localGoals == null) {
            this.localGoals = new HashMap<>();
            for (String remoteID : snap.getActiveRemotes()) {
                this.localGoals.put(
                    remoteID,
                    this.grid.getZone(
                        this.rng.getRandomNumber(this.grid.getHeightInZones()),
                        this.rng.getRandomNumber(this.grid.getWidthInZones())
                    )
                );
            }
        }
        ArrayList<IntentionSet> intentions = new ArrayList<>();
        /**
        for (String remoteID : this.localGoals.keySet()) {
            DroneRemoteController controller = new DroneRemoteController(remoteID);
            controller.goToLocation(this.localGoals.get(remoteID).getLocation());
            intentions.add(controller.getIntentions());
        }
        */
        return intentions;
    }

}
