package com.seat.rescuesim.simserver.scenario;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.scenario.ScenarioConfig;
import com.seat.rescuesim.common.scenario.ScenarioStatus;
import com.seat.rescuesim.common.scenario.ScenarioType;
import com.seat.rescuesim.common.scenario.Snapshot;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.Random;
import com.seat.rescuesim.simserver.core.SimException;
import com.seat.rescuesim.simserver.remote.MobileRemote;
import com.seat.rescuesim.simserver.remote.Remote;
import com.seat.rescuesim.simserver.remote.RemoteFactory;
import com.seat.rescuesim.simserver.remote.VictimRemote;

public class SimScenario {

    private HashMap<String, Remote> activeRemotes;
    private HashMap<String, Remote> allRemotes;
    private ScenarioConfig config;
    private HashMap<String, Remote> dynamicRemotes;
    private Random rng;
    private ScenarioStatus status;
    private double time;

    public SimScenario(ScenarioConfig config) {
        this(config, new Random(config.getSeed()));
    }

    public SimScenario(ScenarioConfig config, Random rng) {
        this.config = config;
        this.rng = rng;
        this.time = 0;
        this.status = ScenarioStatus.START;
        this.initRemotes();
    }

    private void assertFail(String msg) {
        Debugger.logger.err(msg);
        this.setStatus(ScenarioStatus.ERROR);
    }

    private void enforceBounds(MobileRemote remote, Vector prevLocation) {
        if (this.getMap().isInbounds(remote.getLocation())) {
            return;
        }
        Vector edgePoint = this.getMap().edgePointBetween(prevLocation, remote.getLocation());
        if (edgePoint == null) {
            Debugger.logger.err(String.format("Could not compute bounce location for %s on remote %s",
                remote.getLocation().toString(), remote.getLabel()));
            return;
        }
        Vector bounce = this.getMap().bounceLocation(prevLocation, remote.getLocation());
        if (bounce == null) {
            Debugger.logger.err(String.format("Could not compute bounce location for %s on remote %s",
                remote.getLocation().toString(), remote.getLabel()));
            return;
        }
        remote.setLocation(bounce);
        Vector bounceDirection = Vector.subtract(bounce, edgePoint).getUnitVector();
        remote.setVelocity(Vector.scale(bounceDirection, remote.getVelocity().getMagnitude()));
        remote.setAcceleration(Vector.scale(bounceDirection, remote.getAcceleration().getMagnitude()));
    }

    private void initRemotes() {
        this.allRemotes = new HashMap<>();
        this.activeRemotes = new HashMap<>();
        this.dynamicRemotes = new HashMap<>();
        int remoteCount = 0;
        for (RemoteConfig remoteConfig : this.config.getRemotes()) {
            RemoteProto remoteProto = remoteConfig.getProto();
            Iterator<String> remoteIDs = remoteConfig.getRemoteIDs().iterator();
            for (int i = 0; i < remoteConfig.getCount(); i++) {
                String remoteID = (i < remoteConfig.getRemoteIDs().size()) ?
                    remoteIDs.next() :
                    String.format("%s:(%d)", remoteProto.getLabel(), remoteCount);
                Remote remote = RemoteFactory.getRemote(remoteProto, remoteID, remoteConfig.isActive());
                remoteCount++;
                this.allRemotes.put(remoteID, remote);
                if (remoteConfig.isActive()) {
                    this.activeRemotes.put(remoteID, remote);
                }
                if (remoteConfig.isDynamic()) {
                    this.dynamicRemotes.put(remoteID, remote);
                }
                if (!remote.hasLocation()) {
                    remote.setLocation(this.rng.getRandomLocation2D(this.getMapWidth(), this.getMapHeight()));
                }
                if (remoteConfig.isPassive() && remote.isActive() && remote.isMobile()) {
                    if (remoteConfig.getRemoteType().equals(RemoteType.VICTIM)) {
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
                            // TODO: scale initial velocity
                            mobileRemote.setVelocity(this.rng.getRandomDirection2D());
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

    public ArrayList<Remote> getActiveDynamicRemotes() {
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

    public Map getMap() {
        return this.config.getMap();
    }

    public int getMapHeight() {
        return this.getMap().getHeight();
    }

    public int getMapWidth() {
        return this.getMap().getWidth();
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

    public ScenarioType getScenarioType() {
        return this.config.getScenarioType();
    }

    public Snapshot getSnapshot() {
        return new Snapshot(
            LocalTime.now().toString(),
            this.getScenarioType(),
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

    private HashMap<String, RemoteState> getState() {
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

    private void updateDynamicRemotes(HashMap<String, IntentionSet> intentions, double stepSize)
            throws SimException {
        Debugger.logger.info(String.format("Updating remotes %s ...", this.getDynamicRemoteIDs().toString()));
        for (Remote remote : this.getDynamicRemotes()) {
            // TODO: add map effects
            // TODO: sensor properties (accuracy, delay, interference, etc.)
            if (intentions != null && intentions.containsKey(remote.getRemoteID())) {
                remote.update(this, intentions.get(remote.getRemoteID()), stepSize);
            } else {
                remote.update(this, stepSize);
            }
            if (this.getMap().isOutOfBounds(remote.getLocation())) {
                this.assertFail(String.format("Remote %s out of bounds at %s", remote.getLabel(),
                    remote.getLocation().toString()));
            }
        }
    }

    private void updatePassiveRemotes(HashMap<String, IntentionSet> intentions, double stepSize)
            throws SimException {
        Debugger.logger.info(String.format("Updating remotes %s ...", this.getPassiveRemoteIDs().toString()));
        for (Remote remote : this.getPassiveRemotes()) {
            if (intentions.containsKey(remote.getRemoteID())) {
                remote.update(this, intentions.get(remote.getRemoteID()), stepSize);
            } else {
                RemoteController controller = new RemoteController(remote.getRemoteID());
                if (this.justStarted() && remote.isActive() && remote.hasInactiveSensors()) {
                    controller.activateAllSensors();
                }
                if ((remote.isInactive() || remote.isDone()) && remote.hasActiveSensors()) {
                    controller.deactivateAllSensors();
                }
                Vector location = remote.getLocation();
                if (this.getMap().isInbounds(location)) {
                    if (remote.isActive() && remote.isMobile()) {
                        // TODO: better random walk
                        /**controller.addIntention(
                            Intent.Goto(
                                Vector.scale(
                                    this.rng.getRandomDirection2D(),
                                    ((KineticSimRemote) remote).getVelocity().getMagnitude()
                                )
                            )
                        );*/
                    }
                    // TODO: add map effects
                    // TODO: sensor properties (accuracy, delay, interference, etc.)
                    remote.update(this, controller.getIntentions(), stepSize);
                    if (remote.isMobile() && this.getMap().isOutOfBounds(remote.getLocation())) {
                        this.enforceBounds((MobileRemote) remote, location);
                    }
                } else {
                    this.assertFail(String.format("Remote %s out of bounds at %s", remote.getLabel(),
                        remote.getLocation().toString()));
                    continue;
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

    public void update(HashMap<String, IntentionSet> intentions, double stepSize) throws SimException {
        this.time += stepSize;
        if (this.isDone()) {
            return;
        }
        if (this.hasError()) {
            this.setStatus(ScenarioStatus.IN_PROGRESS);
        }
        this.updateDynamicRemotes(intentions, stepSize);
        this.updatePassiveRemotes(intentions, stepSize);
        for (Remote remote : this.getRemotes()) {
            if ((remote.isInactive() || remote.isDone()) && this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.remove(remote.getRemoteID());
            } else if (remote.isActive() && !this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.put(remote.getRemoteID(), remote);
            }
        }
        if (this.justStarted()) {
            this.setStatus(ScenarioStatus.IN_PROGRESS);
        }
        if (this.getMissionLength() <= this.time) {
            this.setStatus(ScenarioStatus.DONE);
        }
    }

}
