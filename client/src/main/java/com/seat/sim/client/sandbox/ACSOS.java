package com.seat.sim.client.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.seat.sim.client.core.mape.Analyzer;
import com.seat.sim.client.core.mape.Executor;
import com.seat.sim.client.core.mape.Knowledge;
import com.seat.sim.client.core.mape.Monitor;
import com.seat.sim.client.core.mape.Planner;
import com.seat.sim.common.core.Application;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.kinematics.Fuel;
import com.seat.sim.common.remote.kinematics.Kinematics;
import com.seat.sim.common.remote.kinematics.Motion;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorRegistry;
import com.seat.sim.common.sensor.comms.CommsSensorProto;
import com.seat.sim.common.sensor.monitor.MonitorSensorProto;
import com.seat.sim.common.sensor.vision.VisionSensorProto;
import com.seat.sim.common.util.Debugger;

public class ACSOS implements Application {

    private static final int BASE_COUNT = 1; // paper: "... have constructed a triage tent"
    private static final int DRONE_COUNT = 32; // paper: "... have been equipped with 32 commercial drones"
    private static final int MAP_SIZE = 64; // paper: "... uses a 64x64 grid"
    private static final int MISSION_LENGTH = 20 * 81; // paper: "81 turns", each turn is 20 seconds
    private static final String SCENARIO_ID = "ACSOS";
    private static final double STEP_SIZE = 0.5;
    private static final int VICTIM_COUNT = 1024; // paper: "1024 victims"
    private static final int ZONE_SIZE = 14; // paper: "Each cell has an area of 200 m^2"

    private static final double VICTIM_STOP_PROBABILITY_ALPHA = 0.5;
    private static final double DRONE_DISCOVERY_PROBABILITY_BETA = 0.5;
    private static final double DRONE_DISCOVERY_PROBABILITY_GAMMA = 0.5;
    private static final double DISASTER_SCALE = 0;

    // paper: "... have constructed a triage tent at the center"
    private static final Vector TRIAGE_TENT = new Vector(
        (ACSOS.MAP_SIZE * ACSOS.ZONE_SIZE)/2.0,
        (ACSOS.MAP_SIZE * ACSOS.ZONE_SIZE)/2.0
    );

    private static Grid getACSOSGrid() {
        return new Grid(ACSOS.MAP_SIZE, ACSOS.MAP_SIZE, ACSOS.ZONE_SIZE);
    }

    private static RemoteConfig getBaseRemoteConfiguration() {
        return new RemoteConfig(
            new RemoteProto(
                new Kinematics(ACSOS.TRIAGE_TENT, new Motion(), new Fuel(1), new Vector()),
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

    // paper: "Bluetooth enabled (10 m)"
    private static CommsSensorProto getBluetoothComms() {
        return SensorRegistry.BluetoothSensor(10, 1, 0, 0);
    }

    // limiting drone visual detection to be the same as Bluetooth detection
    private static VisionSensorProto getDroneCamera() {
        return SensorRegistry.CMOSCameraSensor(10, 1, 0);
    }

    private static RemoteConfig getDroneRemoteConfiguration() {
        return new RemoteConfig(
            new RemoteProto(
                new Kinematics(ACSOS.TRIAGE_TENT, new Motion(30, 10), new Fuel(1), new Vector()),
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
                )
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

    private static VisionSensorProto getVictimNaturalVision() {
        return SensorRegistry.NaturalVisionSensor(10, 1, 0);
    }

    private static RemoteConfig getVictimRemoteConfiguration() {
        return new RemoteConfig(
            new RemoteProto(
                new Kinematics(null, new Motion(9, 9), new Fuel(1), new Vector()),
                new ArrayList<SensorConfig>(
                    Arrays.asList(
                        new SensorConfig(
                            ACSOS.getVictimNaturalVision(),
                            1,
                            true
                        ),
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
                )
            ),
            TeamColor.RED,
            ACSOS.VICTIM_COUNT,
            true,
            true // 1.78 mean and 1 std dev
        );
    }

    private Analyzer analyzer;
    private Executor executor;
    private Knowledge knowledge;
    private Monitor monitor;
    private Planner planner;
    private List<RemoteConfig> remoteConfigs;

    public ACSOS() {
        this(null);
    }

    public ACSOS(String[] args) {
        this.remoteConfigs = new ArrayList<>(
            Arrays.asList(
                ACSOS.getBaseRemoteConfiguration(),
                ACSOS.getDroneRemoteConfiguration(),
                ACSOS.getVictimRemoteConfiguration()
            )
        );
        this.knowledge = new Knowledge(ACSOS.getACSOSGrid());
        this.monitor = new Monitor(this.knowledge);
        this.analyzer = new Analyzer(this.knowledge);
        this.planner = new Planner(this.knowledge);
        this.executor = new Executor(this.knowledge);
        this.init();
    }

    private Collection<String> getBaseRemoteIDs() {
        return ACSOS.getBaseRemoteConfiguration().getRemoteIDs();
    }

    private Collection<String> getDroneRemoteIDs() {
        return ACSOS.getDroneRemoteConfiguration().getRemoteIDs();
    }

    private Collection<String> getVictimRemoteIDs() {
        return ACSOS.getVictimRemoteConfiguration().getRemoteIDs();
    }

    private void init() {
        this.knowledge.setHomeLocation(ACSOS.TRIAGE_TENT);
        this.knowledge.addBases(this.getBaseRemoteIDs());
        this.knowledge.addDrones(this.getDroneRemoteIDs());
        this.knowledge.addVictims(this.getVictimRemoteIDs());
        this.knowledge.setVictimStopProbability(ACSOS.VICTIM_STOP_PROBABILITY_ALPHA);
        this.knowledge.setDroneVisionDiscoveryProbability(ACSOS.DRONE_DISCOVERY_PROBABILITY_BETA);
        this.knowledge.setDroneBluetoothDiscoveryProbability(ACSOS.DRONE_DISCOVERY_PROBABILITY_GAMMA);
    }

    public double getDisasterScale() {
        return ACSOS.DISASTER_SCALE;
    }

    public Grid getGrid() {
        return this.knowledge.getGrid();
    }

    public int getMissionLength() {
        return ACSOS.MISSION_LENGTH;
    }

    public Collection<RemoteConfig> getRemoteConfigs() {
        return this.remoteConfigs;
    }

    public String getScenarioID() {
        return ACSOS.SCENARIO_ID;
    }

    public double getStepSize() {
        return ACSOS.STEP_SIZE;
    }

    public Collection<IntentionSet> update(Snapshot snap) {
        Debugger.logger.info(String.format("Located %d of %d victims", this.knowledge.reportVictimsLocated(),
            ACSOS.VICTIM_COUNT));
        this.monitor.update(snap);
        this.analyzer.update(snap);
        this.planner.update(snap);
        return this.executor.update(snap);
    }

}
