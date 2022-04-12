package com.seat.sim.server.scenario;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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
import com.seat.sim.server.remote.RemoteFactory;
import com.seat.sim.server.remote.VictimRemote;

public class Scenario {

    private HashMap<String, Remote> activeRemotes;
    private HashMap<String, Remote> allRemotes;
    private ScenarioConfig config;
    private HashMap<String, Remote> dynamicRemotes;
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
        if (this.getGrid().isInbounds(remote.getLocation())) {
            return;
        }
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
                Remote remote = RemoteFactory.getRemote(
                    remoteProto,
                    remoteID,
                    remoteConfig.getTeam(),
                    remoteConfig.isActive()
                );
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
                if (remoteConfig.isPassive() && remote.isActive() && remote.isMobile()) {
                    if (VictimRemote.class.isAssignableFrom(remote.getClass())) {
                        VictimRemote victim = (VictimRemote) remote;
                        victim.setVelocity(this.rng.getRandomSpeed2D(
                            victim.getProto().getSpeedMean(),
                            victim.getProto().getSpeedStdDev()
                        ));
                    } else {
                        MobileRemote mobileRemote = (MobileRemote) remote;
                        if (mobileRemote.getProto().hasMaxVelocity()) {
                            mobileRemote.setVelocity(this.rng.getRandomSpeed2D(
                                mobileRemote.getProto().getMaxVelocity()
                            ));
                        } else {
                            mobileRemote.setVelocity(Vector.scale(this.rng.getRandomDirection2D(), this.getZoneSize()));
                        }
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
        HashSet<String> activeDynamicRemoteIDs = new HashSet<>();
        for (String remoteID : this.activeRemotes.keySet()) {
            if (this.hasDynamicRemoteWithID(remoteID)) {
                activeDynamicRemoteIDs.add(remoteID);
            }
        }
        return activeDynamicRemoteIDs;
    }

    public Collection<Remote> getActiveDynamicRemotes() {
        ArrayList<Remote> activeDynamicRemotes = new ArrayList<>();
        for (String remoteID : this.activeRemotes.keySet()) {
            if (this.hasDynamicRemoteWithID(remoteID)) {
                activeDynamicRemotes.add(this.getDynamicRemoteWithID(remoteID));
            }
        }
        return activeDynamicRemotes;
    }

    public Remote getActiveDynamicRemoteWithID(String remoteID) throws SimException {
        if (!this.hasActiveDynamicRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no active, dynamic remote %s", this.getScenarioID(),
                remoteID));
        }
        return this.activeRemotes.get(remoteID);
    }

    public Collection<String> getDynamicRemoteIDs() {
        return this.dynamicRemotes.keySet();
    }

    public Collection<Remote> getDynamicRemotes() {
        return this.dynamicRemotes.values();
    }

    public Remote getDynamicRemoteWithID(String remoteID) throws SimException {
        if (!this.hasDynamicRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no dynamic remote %s", this.getScenarioID(),
                remoteID));
        }
        return this.dynamicRemotes.get(remoteID);
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
        HashSet<String> passiveRemoteIDs = new HashSet<>();
        for (String remoteID : this.allRemotes.keySet()) {
            if (this.hasDynamicRemoteWithID(remoteID)) {
                continue;
            }
            passiveRemoteIDs.add(remoteID);
        }
        return passiveRemoteIDs;
    }

    public Collection<Remote> getPassiveRemotes() {
        ArrayList<Remote> passiveRemotes = new ArrayList<>();
        for (String remoteID : this.getPassiveRemoteIDs()) {
            passiveRemotes.add(this.getPassiveRemoteWithID(remoteID));
        }
        return passiveRemotes;
    }

    public Remote getPassiveRemoteWithID(String remoteID) throws SimException {
        if (!this.hasPassiveRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no passive remote %s", this.getScenarioID(),
                remoteID));
        }
        return this.allRemotes.get(remoteID);
    }

    public Collection<String> getRemoteIDs() {
        return this.allRemotes.keySet();
    }

    public Collection<Remote> getRemotes() {
        return this.allRemotes.values();
    }

    public Remote getRemoteWithID(String remoteID) throws SimException {
        if (!this.hasRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no remote %s", this.getScenarioID(), remoteID));
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
            this.getRemoteIDs(),
            this.getActiveRemoteIDs(),
            this.getDynamicRemoteIDs(),
            this.getState()
        );
    }

    public Map<String, RemoteState> getState() {
        HashMap<String, RemoteState> state = new HashMap<>();
        for (String remoteID : this.allRemotes.keySet()) {
            state.put(remoteID, this.allRemotes.get(remoteID).getState());
        }
        return state;
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
        for (String remoteID : this.activeRemotes.keySet()) {
            if (this.hasDynamicRemoteWithID(remoteID)) {
                return true;
            }
        }
        return false;
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
            throw new SimException(String.format("Scenario %s cannot set time to %f", this.getScenarioID(), time));
        }
        this.time = time;
    }

    private void updateRemotes(Map<String, IntentionSet> intentions, double stepSize) throws SimException {
        for (Remote remote : this.getRemotes()) {
            if (remote.isDisabled()) {
                continue;
            }
            if (this.hasDynamicRemoteWithID(remote.getRemoteID())) {
                if (intentions != null && intentions.containsKey(remote.getRemoteID())) {
                    this.updateDynamicRemote(remote, intentions.get(remote.getRemoteID()), stepSize);
                } else {
                    this.updateDynamicRemote(remote, null, stepSize);
                }
            } else {
                if (intentions != null && intentions.containsKey(remote.getRemoteID())) {
                    this.updatePassiveRemote(remote, intentions.get(remote.getRemoteID()), stepSize);
                } else {
                    this.updatePassiveRemote(remote, null, stepSize);
                }
            }
            if (this.getGrid().isOutOfBounds(remote.getLocation())) {
                this.assertFail(String.format("Remote %s out of bounds at %s", remote.getLabel(),
                    remote.getLocation().toString()));
            }
            if ((remote.isInactive() || remote.isDone()) && this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.remove(remote.getRemoteID());
            } else if (remote.isActive() && !this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.put(remote.getRemoteID(), remote);
            }
        }
    }

    private void updateDynamicRemote(Remote remote, IntentionSet intentions, double stepSize) throws SimException {
        if (intentions != null) {
            remote.update(this, intentions, stepSize);
        } else {
            remote.update(this, stepSize);
        }
    }

    private void updatePassiveRemote(Remote remote, IntentionSet intentions, double stepSize) throws SimException {
        if (intentions != null) {
            remote.update(this, intentions, stepSize);
        } else {
            RemoteController controller = new RemoteController(remote.getRemoteID());
            if (this.justStarted() && remote.isActive() && remote.hasInactiveSensors()) {
                controller.activateAllSensors();
            }
            if ((remote.isInactive() || remote.isDone()) && remote.hasActiveSensors()) {
                controller.deactivateAllSensors();
            }
            Vector location = remote.getLocation();
            if (this.getGrid().isInbounds(location)) {
                remote.update(this, controller.getIntentions(), stepSize);
                if (remote.isMobile() && this.getGrid().isOutOfBounds(remote.getLocation())) {
                    this.enforceBounds((MobileRemote) remote, location);
                }
            }
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
        if (this.isDone()) {
            return;
        }
        if (this.hasError()) {
            this.setStatus(ScenarioStatus.IN_PROGRESS);
        }
        Debugger.logger.info("Updating remotes ...");
        this.updateRemotes(intentions, stepSize);
        Debugger.logger.state("Updated remotes");
        if (this.justStarted()) {
            this.setStatus(ScenarioStatus.IN_PROGRESS);
        }
        if (this.getMissionLength() <= this.time) {
            this.setStatus(ScenarioStatus.DONE);
        }
    }

}
