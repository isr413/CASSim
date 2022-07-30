package com.seat.sim.server.scenario;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteController;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.ScenarioStatus;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.common.util.Random;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.remote.MobileRemote;
import com.seat.sim.server.remote.Remote;

public class Scenario {

    private Map<String, Remote> activeRemotes;
    private Map<String, Remote> allRemotes;
    private ScenarioConfig config;
    private Map<String, Remote> dynamicRemotes;
    private Random rng;
    private ScenarioStatus status;
    private double time;

    public Scenario(ScenarioConfig config) {
        this(config, new Random(config.getSeed()));
    }

    public Scenario(ScenarioConfig config, Random rng) {
        this.config = config;
        this.rng = rng;
        this.time = 0;
        this.status = ScenarioStatus.START;
        this.init();
    }

    private void assertFail(String msg) {
        Debugger.logger.err(msg);
        this.setStatus(ScenarioStatus.ERROR);
    }

    private void enforceBounds(MobileRemote remote, Vector prevLocation) {
        if (this.getGrid().isInbounds(remote.getLocation())) return;
        Vector boundsCollision = this.getGrid().getBoundsCollisionLocation(prevLocation, remote.getLocation());
        if (boundsCollision == null) {
            Debugger.logger.err(String.format("Could not compute bounce location for %s on remote %s",
                remote.getLocation().toString(), remote.getLabel()));
            return;
        }
        Vector nextLocation = this.getGrid().getLocationAfterBounce(prevLocation, remote.getLocation());
        if (nextLocation == null) {
            Debugger.logger.err(String.format("Could not compute bounce location for %s on remote %s",
                remote.getLocation().toString(), remote.getLabel()));
            return;
        }
        remote.setLocation(nextLocation);
        Vector bounceDirection = Vector.subtract(nextLocation, boundsCollision).getUnitVector();
        remote.setVelocity(Vector.scale(bounceDirection, remote.getVelocity().getMagnitude()));
        remote.setAcceleration(Vector.scale(bounceDirection, remote.getAcceleration().getMagnitude()));
    }

    private void init() {
        this.allRemotes = new HashMap<>();
        this.activeRemotes = new HashMap<>();
        this.dynamicRemotes = new HashMap<>();
        int remoteCount = 0;
        for (RemoteConfig remoteConfig : this.config.getRemoteConfigs()) {
            RemoteProto remoteProto = remoteConfig.getProto();
            Iterator<String> remoteIDs = remoteConfig.getRemoteIDs().iterator();
            for (int i = 0; i < remoteConfig.getCount(); i++) {
                String remoteID = (i < remoteConfig.getRemoteIDs().size()) ?
                    remoteIDs.next() :
                    String.format("%s:(%d)", remoteProto.getLabel(), remoteCount);
                Remote remote;
                if (remoteProto.isMobile()) {
                    remote = new MobileRemote(remoteProto, remoteID, remoteConfig.getTeam(), remoteConfig.isActive());
                } else {
                    remote = new Remote(remoteProto, remoteID, remoteConfig.getTeam(), remoteConfig.isActive());
                }
                remoteCount++;
                this.allRemotes.put(remoteID, remote);
                if (remoteConfig.isActive()) {
                    this.activeRemotes.put(remoteID, remote);
                }
                if (remoteConfig.isDynamic()) {
                    this.dynamicRemotes.put(remoteID, remote);
                }
                if (!remote.hasLocation()) {
                    remote.setLocation(this.rng.getRandomLocation2D(this.getGridWidth(), this.getGridHeight()));
                }
                if (!remoteConfig.isDynamic() && remote.isActive() && remote.isMobile()) {
                    MobileRemote mobileRemote = (MobileRemote) remote;
                    if (mobileRemote.hasMaxVelocity()) {
                        mobileRemote.setVelocity(this.rng.getRandomSpeed2D(mobileRemote.getMaxVelocity()));
                    } else {
                        mobileRemote.setVelocity(Vector.scale(this.rng.getRandomDirection2D(), this.getZoneSize()));
                    }
                }
            }
        }
    }

    public Collection<String> getActiveRemoteIDs() {
        return this.activeRemotes.keySet();
    }

    public Collection<Remote> getActiveRemotes() {
        return this.activeRemotes.values();
    }

    public Remote getActiveRemoteWithID(String remoteID) throws SimException {
        if (!this.hasActiveRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no active remote %s", this.getScenarioID(),
                remoteID));
        }
        return this.activeRemotes.get(remoteID);
    }

    public Collection<String> getActiveDynamicRemoteIDs() {
        return this.getActiveRemoteIDs().stream()
            .filter(remoteID -> this.hasDynamicRemoteWithID(remoteID))
            .collect(Collectors.toList());
    }

    public Collection<Remote> getActiveDynamicRemotes() {
        return this.getActiveDynamicRemoteIDs().stream()
            .map(remoteID -> this.getRemoteWithID(remoteID))
            .collect(Collectors.toList());
    }

    public Collection<String> getDynamicRemoteIDs() {
        return this.dynamicRemotes.keySet();
    }

    public Collection<Remote> getDynamicRemotes() {
        return this.dynamicRemotes.values();
    }

    public Grid getGrid() {
        return this.config.getGrid();
    }

    public int getGridHeight() {
        return this.getGrid().getHeight();
    }

    public int getGridWidth() {
        return this.getGrid().getWidth();
    }

    public int getMissionLength() {
        return this.config.getMissionLength();
    }

    public Collection<String> getPassiveRemoteIDs() {
        return this.getRemoteIDs().stream()
            .filter(remoteID -> !this.hasDynamicRemoteWithID(remoteID))
            .collect(Collectors.toList());
    }

    public Collection<Remote> getPassiveRemotes() {
        return this.getPassiveRemoteIDs().stream()
            .map(remoteID -> this.getRemoteWithID(remoteID))
            .collect(Collectors.toList());
    }

    public Collection<String> getRemoteIDs() {
        return this.allRemotes.keySet();
    }

    public Collection<Remote> getRemotes() {
        return this.allRemotes.values();
    }

    public Remote getRemoteWithID(String remoteID) throws SimException {
        if (!this.hasRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no remote %s", this.getScenarioID(),
                remoteID));
        }
        return this.allRemotes.get(remoteID);
    }

    public String getScenarioID() {
        return this.config.getScenarioID();
    }

    public String getScenarioType() {
        return this.config.getScenarioType();
    }

    public Snapshot getSnapshot() {
        return new Snapshot(
            LocalTime.now().toString(),
            this.getScenarioID(),
            this.status,
            this.time,
            this.getStepSize(),
            this.getActiveRemoteIDs(),
            this.getDynamicRemoteIDs(),
            this.getState()
        );
    }

    public Map<String, RemoteState> getState() {
        return this.getRemotes().stream().collect(Collectors.toMap(Remote::getRemoteID, Remote::getRemoteState));
    }

    public ScenarioStatus getStatus() {
        return this.status;
    }

    public double getStepSize() {
        return this.config.getStepSize();
    }

    public double getTime() {
        return this.time;
    }

    public int getZoneSize() {
        return this.getGrid().getZoneSize();
    }

    public boolean hasActiveRemotes() {
        return !this.activeRemotes.isEmpty();
    }

    public boolean hasActiveRemoteWithID(String remoteID) {
        return this.activeRemotes.containsKey(remoteID);
    }

    public boolean hasActiveDynamicRemotes() {
        return !this.getActiveDynamicRemoteIDs().isEmpty();
    }

    public boolean hasActiveDynamicRemoteWithID(String remoteID) {
        return this.hasActiveRemoteWithID(remoteID) && this.hasDynamicRemoteWithID(remoteID);
    }

    public boolean hasDynamicRemotes() {
        return !this.dynamicRemotes.isEmpty();
    }

    public boolean hasDynamicRemoteWithID(String remoteID) {
        return this.dynamicRemotes.containsKey(remoteID);
    }

    public boolean hasError() {
        return this.status.equals(ScenarioStatus.ERROR);
    }

    public boolean hasPassiveRemotes() {
        return this.dynamicRemotes.size() < this.allRemotes.size();
    }

    public boolean hasPassiveRemoteWithID(String remoteID) {
        return this.hasRemoteWithID(remoteID) && !this.hasDynamicRemoteWithID(remoteID);
    }

    public boolean hasRemotes() {
        return !this.allRemotes.isEmpty();
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.allRemotes.containsKey(remoteID);
    }

    public boolean isDone() {
        return this.status.equals(ScenarioStatus.DONE);
    }

    public boolean isInProgress() {
        return this.justStarted() || this.status.equals(ScenarioStatus.IN_PROGRESS);
    }

    public boolean justStarted() {
        return this.status.equals(ScenarioStatus.START);
    }

    public void setStatus(ScenarioStatus status) {
        this.status = status;
    }

    public void setTime(double time) throws SimException {
        if (time < 0 || this.getMissionLength() < time) {
            throw new SimException(String.format("Scenario %s cannot set time to %f", this.getScenarioID(),
                time));
        }
        this.time = time;
    }

    private void updateRemotes(Map<String, IntentionSet> intentions, double stepSize) throws SimException {
        for (Remote remote : this.getRemotes()) {
            if (!remote.isEnabled()) continue;
            Debugger.logger.info(String.format("Updating remote %s ...", remote.getRemoteID()));
            Vector prevLocation = null;
            if (this.getGrid().isInbounds(remote.getLocation())) {
                prevLocation = remote.getLocation();
            }
            if (intentions != null && intentions.containsKey(remote.getRemoteID())) {
                remote.update(this, intentions.get(remote.getRemoteID()), stepSize);
            } else if (this.hasDynamicRemoteWithID(remote.getRemoteID())) {
                remote.update(this, stepSize);
            } else {
                this.updatePassiveRemote(remote, stepSize);
            }
            if (this.getGrid().isOutOfBounds(remote.getLocation())) {
                if (remote.isMobile() && prevLocation != null) {
                    this.enforceBounds((MobileRemote) remote, prevLocation);
                } else {
                    this.assertFail(String.format("Remote %s out of bounds at %s", remote.getLabel(),
                        remote.getLocation().toString()));
                }
            }
            if ((!remote.isActive() || remote.isDone()) && this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.remove(remote.getRemoteID());
            } else if (remote.isActive() && !this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.put(remote.getRemoteID(), remote);
            }
        }
    }

    private void updatePassiveRemote(Remote remote, double stepSize) throws SimException {
        RemoteController controller = new RemoteController(remote.getRemoteID());
        if (this.justStarted() && remote.isActive() && remote.hasInactiveSensors()) {
            controller.activateAllSensors();
        }
        if ((!remote.isActive() || remote.isDone()) && remote.hasActiveSensors()) {
            controller.deactivateAllSensors();
        }
        if (this.getGrid().isInbounds(remote.getLocation())) {
            remote.update(this, controller.getIntentions(), stepSize);
        }
    }

    public void update() throws SimException {
        this.update(null, this.config.getStepSize());
    }

    public void update(double stepSize) throws SimException {
        this.update(null, stepSize);
    }

    public void update(Map<String, IntentionSet> intentions, double stepSize) throws SimException {
        this.time += stepSize;
        if (this.isDone()) return;
        if (this.hasError()) {
            this.setStatus(ScenarioStatus.IN_PROGRESS);
        }
        Debugger.logger.info("Updating remotes ...");
        this.updateRemotes(intentions, stepSize);
        if (this.justStarted()) {
            this.setStatus(ScenarioStatus.IN_PROGRESS);
        }
        if (this.getMissionLength() <= this.time) {
            this.setStatus(ScenarioStatus.DONE);
        }
    }

}
