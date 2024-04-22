package com.seat.sim.client.sandbox.recon.remote;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.seat.sim.client.core.DroneScenario;
import com.seat.sim.client.negotiation.Contract;
import com.seat.sim.client.negotiation.NegotiationManager;
import com.seat.sim.client.negotiation.Proposal;
import com.seat.sim.client.sandbox.rescue.remote.RemoteManager;
import com.seat.sim.client.sandbox.rescue.util.Experiment;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.gui.TeamColor;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.kinematics.FuelProto;
import com.seat.sim.common.remote.kinematics.KinematicsProto;
import com.seat.sim.common.remote.kinematics.MotionProto;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorProto;
import com.seat.sim.common.sensor.SensorStats;
import com.seat.sim.common.util.Logger;
import com.seat.sim.common.util.Random;
import com.seat.sim.common.util.Range;

public abstract class ReconScenario implements DroneScenario {

  // Scenario info
  public static final int NUM_TURNS = 180;
  public static final int TURN_LENGTH = 10;
  public static final int MISSION_LENGTH = ReconScenario.NUM_TURNS * ReconScenario.TURN_LENGTH;
  public static final double STEP_SIZE = 10.;

  // Grid info
  public static final int GRID_SIZE = 102;
  public static final int ZONE_SIZE = 127;

  public static final Vector GRID_CENTER = new Vector(
      ReconScenario.GRID_SIZE * ReconScenario.ZONE_SIZE / 2.,
      ReconScenario.GRID_SIZE * ReconScenario.ZONE_SIZE / 2.
    );

  // Base info
  public static final String BASE_TAG = "Base";
  public static final String BASE_LRC_TAG = "Base_LRC";
  public static final TeamColor BASE_COLOR = TeamColor.GREEN;

  // Drone info
  public static final String DRONE_TAG = "Drone";
  public static final String DRONE_LRC_TAG = "Drone_LRC";
  public static final String DRONE_SENSOR_A_TAG = "Drone_SENSOR_A";
  public static final TeamColor DRONE_COLOR = TeamColor.BLUE;
  public static final Vector DRONE_FUEL_USAGE = new Vector(0.00001, 0.00005, 0.00005);
  public static final Vector DRONE_INITIAL_VELOCITY = Vector.ZERO;
  public static final double DRONE_SCAN_VELOCITY = 30.;
  public static final double DRONE_SCAN_ACCELERATION = 1.4;
  public static final double DRONE_MAX_VELOCITY = 30.;
  public static final double DRONE_MAX_ACCELERATION = 10.;

  // Intel info
  public static final String INTEL_TAG = "Intel";
  public static final String INTEL_POPUP_TAG = "Intel_Popup";
  public static final String INTEL_SENSOR_A_TAG = "Intel_SENSOR_A";
  public static final TeamColor INTEL_COLOR = TeamColor.RED;
  public static final Vector INTEL_INITIAL_VELOCITY = Vector.ZERO;
  public static final double INTEL_MAX_VELOCITY = 0.;
  public static final double INTEL_MAX_ACCELERATION = 0.;

  // Sensors
  public static final String LONG_RANGE_COMMS = "Long_Range_Comms";
  public static final double LRC_BATT_USAGE = 0.00001;

  public static final String DRONE_CAMERA = "Drone_Camera";
  public static final double CAM_BATT_USAGE = 0.00001;
  public static final double CAM_RANGE = 90.;

  public static final String SENSOR_A_COMMS = "SENSOR_A_Comms";
  public static final double SENSOR_A_BATT_USAGE = 0.00001;
  public static final double SENSOR_A_RANGE = 90.;

  private Grid grid;
  private Logger logger;
  private String scenarioID;

  protected Experiment exp;
  protected RemoteManager manager;
  protected Optional<NegotiationManager> negotiations;
  protected double score;
  protected Optional<TaskManager> tasks;

  public ReconScenario(String scenarioID, Range alpha, double beta, double gamma, int trialsPer,
      int threadID, int threadCount, long seed, String seedFile) throws IOException {
    this(scenarioID, 1, 32, 1024, 0, 0, Range.Inclusive(0, 0), 0, alpha, beta, gamma, trialsPer, threadID, threadCount,
        seed, seedFile);
  }

  public ReconScenario(String scenarioID, int baseCount, int droneCount, int intelCount, int popupCount, int advCount,
      Range popupTime, int cooldown, double alpha, double beta, double gamma, int trialsPer, int threadID, int threadCount,
      long seed) throws IOException {
    this(scenarioID, baseCount, droneCount, intelCount, popupCount, advCount, popupTime, cooldown,
        Range.Inclusive(alpha, alpha, 0.), Range.Inclusive(beta, beta, 0.), Range.Inclusive(gamma, gamma, 0.),
        trialsPer, threadID, threadCount, seed, "seeds");
  }

  public ReconScenario(String scenarioID, int baseCount, int droneCount, int intelCount, int popupCount, int advCount,
      Range popupTime, int cooldown, Range alpha, double beta, double gamma, int trialsPer, int threadID, int threadCount,
      long seed, String seedFile) throws IOException {
    this(scenarioID, baseCount, droneCount, intelCount, popupCount, advCount, popupTime,
        cooldown, alpha, Range.Inclusive(beta, beta, 0.),
        Range.Inclusive(gamma, gamma, 0.), trialsPer, threadID, threadCount, seed, seedFile);
  }

  public ReconScenario(String scenarioID, int baseCount, int droneCount, int intelCount, int popupCount, int advCount,
      Range popupTime, int cooldown, Range alpha, Range beta, double gamma, int trialsPer, int threadID, int threadCount,
      long seed, String seedFile) throws IOException {
    this(scenarioID, baseCount, droneCount, intelCount, popupCount, advCount, popupTime, cooldown, alpha, beta,
        Range.Inclusive(gamma, gamma, 0.), trialsPer, threadID, threadCount, seed, seedFile);
  }

  public ReconScenario(String scenarioID, int baseCount, int droneCount, int intelCount, int popupCount, int advCount,
      Range popupTime, int cooldown, Range alpha, double beta, Range gamma, int trialsPer, int threadID, int threadCount,
      long seed, String seedFile) throws IOException {
    this(scenarioID, baseCount, droneCount, intelCount, popupCount, advCount, popupTime, cooldown,
        alpha, Range.Inclusive(beta, beta, 0.), gamma, trialsPer, threadID, threadCount, seed, seedFile);
  }

  public ReconScenario(String scenarioID, Range alpha, Range beta, Range gamma, int trialsPer,
      int threadID, int threadCount, long seed, String seedFile) throws IOException {
    this(scenarioID, 1, 32, 1024, 0, 0, Range.Inclusive(0, 0), 0, alpha, beta, gamma, trialsPer, threadID, threadCount,
        seed, seedFile);
  }

  public ReconScenario(String scenarioID, int baseCount, int droneCount, int intelCount, int popupCount, int advCount,
      Range popupTime, int cooldown, Range alpha, Range beta, Range gamma, int trialsPer, int threadID, int threadCount,
      long seed, String seedFile) throws IOException {
    this.scenarioID = scenarioID;
    this.logger = (threadID > 0) ? new Logger(String.format("%s_%d", scenarioID, threadID)) : new Logger(scenarioID);
    this.grid = new Grid(
        ReconScenario.GRID_SIZE,
        ReconScenario.GRID_SIZE,
        ReconScenario.ZONE_SIZE
      );
    this.exp = new Experiment(alpha, beta, gamma, trialsPer, threadID, threadCount, seed, this.loadSeeds(seedFile));
    this.manager = new RemoteManager(
        this,
        new ReconDroneManager(this, droneCount, cooldown),
        new IntelManager(this, intelCount, popupCount, advCount, popupTime),
        baseCount,
        cooldown
      );
  }

  protected abstract Optional<TaskManager> getTaskManager();

  protected List<RemoteConfig> loadRemotes() {
    return List.of(
        new RemoteConfig(
            new RemoteProto(
                Set.of(ReconScenario.BASE_TAG),
                List.of(
                    new SensorConfig(
                        new SensorProto(
                            ReconScenario.SENSOR_A_COMMS,
                            Set.of(ReconScenario.BASE_TAG),
                            Set.of(ReconScenario.INTEL_TAG, ReconScenario.INTEL_POPUP_TAG),
                            new SensorStats(0., 1., 0., ReconScenario.SENSOR_A_RANGE)
                          ),
                        1,
                        true
                      ),
                    new SensorConfig(
                        new SensorProto(
                            ReconScenario.LONG_RANGE_COMMS,
                            Set.of(ReconScenario.BASE_LRC_TAG),
                            Set.of(ReconScenario.DRONE_LRC_TAG),
                            new SensorStats(ReconScenario.LRC_BATT_USAGE, 1., 0.)
                          ),
                        1,
                        true
                      )
                  ),
                new KinematicsProto(ReconScenario.GRID_CENTER)
              ),
            ReconScenario.BASE_COLOR,
            this.manager.getBaseCount(),
            true,
            false
          ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(ReconScenario.DRONE_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              ReconScenario.DRONE_CAMERA,
                              Set.of(ReconScenario.DRONE_TAG),
                              Set.of(ReconScenario.INTEL_TAG, ReconScenario.INTEL_POPUP_TAG),
                              new SensorStats(
                                  ReconScenario.CAM_BATT_USAGE,
                                  this.exp.getBeta(),
                                  0.,
                                  ReconScenario.CAM_RANGE
                                )
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              ReconScenario.LONG_RANGE_COMMS,
                              Set.of(ReconScenario.DRONE_LRC_TAG),
                              Set.of(ReconScenario.BASE_LRC_TAG),
                              new SensorStats(ReconScenario.LRC_BATT_USAGE, 1., 0.)
                            ),
                          1,
                          true
                        ),
                      new SensorConfig(
                          new SensorProto(
                              ReconScenario.SENSOR_A_COMMS,
                              Set.of(ReconScenario.DRONE_SENSOR_A_TAG),
                              Set.of(ReconScenario.INTEL_SENSOR_A_TAG),
                              new SensorStats(
                                  ReconScenario.SENSOR_A_BATT_USAGE,
                                  this.exp.getGamma(),
                                  0.,
                                  ReconScenario.SENSOR_A_RANGE
                                )
                            ),
                          1,
                          true
                        )
                    ),
                  new KinematicsProto(
                      ReconScenario.GRID_CENTER,
                      new FuelProto(1., 1., ReconScenario.DRONE_FUEL_USAGE),
                      new MotionProto(
                          ReconScenario.DRONE_INITIAL_VELOCITY,
                          ReconScenario.DRONE_MAX_VELOCITY,
                          ReconScenario.DRONE_MAX_ACCELERATION
                        )
                    )
                ),
              ReconScenario.DRONE_COLOR,
              this.manager.getDroneCount(),
              true,
              true
            ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(ReconScenario.INTEL_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              ReconScenario.SENSOR_A_COMMS,
                              Set.of(ReconScenario.INTEL_SENSOR_A_TAG),
                              Set.of(ReconScenario.DRONE_SENSOR_A_TAG),
                              new SensorStats(
                                  ReconScenario.SENSOR_A_BATT_USAGE,
                                  this.exp.getGamma(),
                                  0.,
                                  ReconScenario.SENSOR_A_RANGE
                                )
                            ),
                          1,
                          true
                        )
                    ),
                  new KinematicsProto(
                      null,
                      null,
                      new MotionProto(
                          ReconScenario.INTEL_INITIAL_VELOCITY,
                          ReconScenario.INTEL_MAX_VELOCITY,
                          ReconScenario.INTEL_MAX_ACCELERATION
                        )
                    )
                ),
              ReconScenario.INTEL_COLOR,
              ((IntelManager) this.manager.getAssetManager()).getIntelCount(),
              true,
              true
            ),
          new RemoteConfig(
              new RemoteProto(
                  Set.of(ReconScenario.INTEL_POPUP_TAG),
                  List.of(
                      new SensorConfig(
                          new SensorProto(
                              ReconScenario.SENSOR_A_COMMS,
                              Set.of(ReconScenario.INTEL_SENSOR_A_TAG),
                              Set.of(ReconScenario.DRONE_SENSOR_A_TAG),
                              new SensorStats(
                                  ReconScenario.SENSOR_A_BATT_USAGE,
                                  this.exp.getGamma(),
                                  0.,
                                  ReconScenario.SENSOR_A_RANGE
                                )
                            ),
                          1,
                          true
                        )
                    ),
                  new KinematicsProto(
                      null,
                      null,
                      new MotionProto(
                          ReconScenario.INTEL_INITIAL_VELOCITY,
                          ReconScenario.INTEL_MAX_VELOCITY,
                          ReconScenario.INTEL_MAX_ACCELERATION
                        )
                    )
                ),
              ReconScenario.INTEL_COLOR,
              ((IntelManager) this.manager.getAssetManager()).getPopupCount(),
              true,
              true
            )

      );
  }

  protected Iterator<Long> loadSeeds(String seedFile) throws IOException {
    return Files.lines(Path.of(String.format("config/%s.txt", seedFile)))
      .mapToLong(Long::parseLong)
      .boxed()
      .collect(Collectors.toList())
      .iterator();
  }

  protected void reportScore() {
    this.reportScore(ReconScenario.MISSION_LENGTH);
  }

  protected void reportScore(Snapshot snap) {
    this.reportScore(snap.getTime());
  }

  protected void reportScore(double simTime) {
    this.logger.log(simTime, String.format("Score: %.4f", this.getScore()));
  }

  protected void reportTrial() {
    this.logger.log(-1.,
        String.format("Seed %d - ALPHA %.3f - BETA %.3f - GAMMA %.3f", this.exp.getSeed(), this.exp.getAlpha(),
            this.exp.getBeta(), this.exp.getGamma()));
  }

  public void addTask(Zone task) {
    if (this.hasTasks()) {
      this.tasks.get().addTask(task);
    }
  }

  public void close() {
    this.reportScore();
    this.manager.close();
    if (this.hasTasks()) {
      this.tasks.get().close();
    }
    if (this.hasNegotiations()) {
      this.negotiations.get().close();
    }
  }

  public double getAlpha() {
    return this.exp.getAlpha();
  }

  public int getAssetCount() {
    return this.manager.getAssetCount();
  }

  public int getBaseCount() {
    return this.manager.getBaseCount();
  }

  public double getBeta() {
    return this.exp.getBeta();
  }

  public int getCooldown(String droneID) {
    return this.manager.getCooldown(droneID);
  }

  public int getDroneCount() {
    return this.manager.getDroneCount();
  }

  public String getDroneTag() {
    return ReconScenario.DRONE_TAG;
  }

  public double getGamma() {
    return this.exp.getGamma();
  }

  public Optional<Grid> getGrid() {
    return Optional.of(this.grid);
  }

  public Vector getGridCenter() {
    return ReconScenario.GRID_CENTER;
  }

  public RemoteManager getManager() {
    return this.manager;
  }

  public int getMissionLength() {
    return ReconScenario.MISSION_LENGTH;
  }

  public Optional<NegotiationManager> getNegotiations() {
    return (this.negotiations != null) ? this.negotiations : Optional.empty();
  }

  public double getPhi() {
    return 1. - (1. - this.getBeta()) * (1. - this.getGamma());
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.manager.getRemoteConfigs();
  }

  public Random getRng() {
    return this.exp.getRng();
  }

  public String getScenarioID() {
    return this.scenarioID;
  }

  public double getScore() {
    return this.score;
  }

  public long getSeed() {
    return this.exp.getSeed();
  }

  public double getStepSize() {
    return ReconScenario.STEP_SIZE;
  }

  public int getTrials() {
    return this.exp.getTrials();
  }

  public Stream<Zone> getZones() {
    if (!this.hasGrid()) {
      return Stream.of();
    }
    return Arrays.stream(this.getGrid().get().getZones()).flatMap(row -> Arrays.stream(row));
  }

  public boolean hasGrid() {
    return true;
  }

  public boolean hasTasks() {
    return this.tasks != null && this.tasks.isPresent();
  }

  public boolean hasNegotiations() {
    return this.negotiations != null && this.negotiations.isPresent();
  }

  public void init() {
    this.manager.setRemoteConfigs(this.loadRemotes());
    this.manager.init();
    this.tasks = this.getTaskManager();
    this.negotiations = this.getNegotiations();
    if (this.hasTasks()) {
      this.tasks.get().init();
    }
    if (this.hasNegotiations()) {
      this.negotiations.get().init();
    }
    this.score = 0;
    this.reportTrial();
  }

  public boolean isAcceptable(Snapshot Snap, RemoteState state, String senderID, String receiverID,
      Proposal proposal) {
    return this.hasNegotiations();
  }

  public boolean isDone(String remoteID) {
    return this.manager.isDone(remoteID);
  }

  public boolean isOnCooldown(String remoteID) {
    return this.manager.isOnCooldown(remoteID);
  }

  public Optional<Contract> negotiate(Snapshot snap, RemoteState state, String senderID, String receiverID) {
    if (!this.hasNegotiations()) {
      return Optional.empty();
    }
    List<Proposal> proposals = this.negotiations.get().getProposals(senderID)
        .stream()
        .filter(proposal -> this.isAcceptable(snap, state, senderID, receiverID, proposal))
        .collect(Collectors.toList());
    if (proposals.isEmpty()) {
      return Optional.empty();
    }
    Collections.sort(proposals, new Comparator<Proposal>() {
      @Override
      public int compare(Proposal a, Proposal b) {
        return -Double.compare(rankProposal(a), rankProposal(b));
      }
    });
    return Optional.of(this.negotiations.get().acceptProposal(senderID, receiverID, proposals.get(0)));

  }

  public Optional<Zone> nextTask(Snapshot snap, RemoteState state) {
    if (!this.hasTasks()) {
      return Optional.empty();
    }
    return this.tasks.get().getNextTask(snap, state);
  }

  public double rankProposal(Proposal p) {
    return 1.;
  }

  public void report(double simTime, String fmt, Object... args) {
    if (args != null && args.length > 0) {
      this.logger.log(simTime, String.format(fmt, args));
    } else {
      this.logger.log(simTime, fmt);
    }
  }

  public void reset() {
    this.reportScore();
    this.exp.reset();
    this.manager.reset();
    this.manager.setRemoteConfigs(this.loadRemotes());
    if (this.hasTasks()) {
      this.tasks.get().reset();
    }
    if (this.hasNegotiations()) {
      this.negotiations.get().reset();
    }
    this.score = 0;
    this.reportTrial();
  }

  public boolean scoreContract(Snapshot snap, Contract contract) {
    return this.scoreContract(snap, contract, false);
  }

  public boolean scoreContract(Snapshot snap, Contract contract, boolean earlyCompletion) {
    if (!this.hasNegotiations()) {
      return false;
    }
    if (!contract.isOriginal()) {
      return true;
    }
    double roll = this.getRng().getRandomProbability();
    if (roll > contract.getProposal().getSuccessLikelihood()) {
      this.report(
          snap.getTime(),
          ":: %s :: %s :: Fails task",
          contract.getSenderID(),
          contract.getReceiverID()
        );
      return false;
    }
    this.score += contract.getProposal().getReward();
    this.report(
        snap.getTime(),
        ":: %s :: %s :: Succeeds task",
        contract.getSenderID(),
        contract.getReceiverID()
      );
    return true;
  }

  public void setDone(String remoteID, boolean droneNotIntel) {
    this.manager.setDone(remoteID, droneNotIntel);
  }

  public void setRemoteManager(RemoteManager manager) {
    this.manager = manager;
  }

  public boolean terminateContract(Contract contract) {
    if (!this.hasNegotiations()) {
      return false;
    }
    if (!contract.isOriginal()) {
      return true;
    }
    this.negotiations.get().terminateContract(contract.getSenderID(), contract.getReceiverID());
    return true;
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    if (this.hasTasks()) {
      this.tasks.get().update(snap);
    }
    if (this.hasNegotiations()) {
      this.negotiations.get().update(snap);
    }
    Collection<IntentionSet> intentions = this.manager.update(snap);
    this.reportScore(snap);
    return intentions;
  }
}
