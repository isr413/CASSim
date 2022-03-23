package com.seat.rescuesim.simserver.sim;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.seat.rescuesim.common.ScenarioConfig;
import com.seat.rescuesim.common.SnapStatus;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.simserver.sim.remote.KineticSimRemote;
import com.seat.rescuesim.simserver.sim.remote.SimRemote;
import com.seat.rescuesim.simserver.sim.remote.SimRemoteFactory;
import com.seat.rescuesim.simserver.sim.remote.SimVictim;
import com.seat.rescuesim.simserver.util.Random;

public class SimScenario {

    private HashMap<String, SimRemote> activeRemotes;
    private HashMap<String, SimRemote> allRemotes;
    private ScenarioConfig config;
    private HashMap<String, SimRemote> dynamicRemotes;
    private Random rng;
    private SnapStatus status;
    private double time;

    public SimScenario(ScenarioConfig config) {
        this(config, new Random(config.getSeed()));
    }

    public SimScenario(ScenarioConfig config, Random rng) {
        this.config = config;
        this.rng = rng;
        this.time = 0;
        this.status = SnapStatus.START;
        this.allRemotes = new HashMap<>();
        this.activeRemotes = new HashMap<>();
        this.dynamicRemotes = new HashMap<>();
        this.initRemotes();
    }

    private void enforceBounds(KineticSimRemote remote, Vector prevLocation) {
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
        int numRemotes = 0;
        for (RemoteConfig remoteConfig : this.config.getRemotes()) {
            RemoteSpec remoteSpec = remoteConfig.getSpec();
            System.out.println(remoteSpec.toString());
            Iterator<String> remoteIDs = remoteConfig.getRemoteIDs().iterator();
            for (int i = 0; i < remoteConfig.getCount(); i++) {
                String label = (i < remoteConfig.getRemoteIDs().size()) ?
                    remoteIDs.next() :
                    String.format("r<%d>", numRemotes);
                SimRemote remote = SimRemoteFactory.getSimRemote(remoteSpec, label);
                numRemotes++;
                this.allRemotes.put(label, remote);
                if (remote.isActive()) {
                    this.activeRemotes.put(label, remote);
                }
                if (remoteConfig.isDynamic()) {
                    this.dynamicRemotes.put(label, remote);
                }
                if (!remoteSpec.hasLocation()) {
                    remote.setLocation(this.rng.getRandomLocation2D(this.getMapWidth(), this.getMapHeight()));
                }
                if (remote.isActive() && remoteConfig.isPassive() && remote.isKinetic()) {
                    if (remoteConfig.getRemoteType().equals(RemoteType.VICTIM)) {
                        ((SimVictim) remote).setVelocity(this.rng.getRandomSpeed2D(
                            ((VictimSpec) remoteSpec).getSpeedMean(),
                            ((VictimSpec) remoteSpec).getSpeedStddev()
                        ));
                    } else {
                        ((KineticSimRemote) remote).setVelocity(this.rng.getRandomSpeed2D(
                            ((KineticSimRemote) remote).getMaxVelocity()
                        ));
                    }
                }
            }
        }
    }

    public HashSet<String> getActiveRemoteIDs() {
        return new HashSet<String>(this.activeRemotes.keySet());
    }

    public ArrayList<SimRemote> getActiveRemotes() {
        return new ArrayList<SimRemote>(this.activeRemotes.values());
    }

    public SimRemote getActiveRemoteWithID(String remoteID) throws SimException {
        if (!this.hasActiveRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no active remote %s", this.getScenarioID(),
                remoteID));
        }
        return this.activeRemotes.get(remoteID);
    }

    public HashSet<String> getActiveDynamicRemoteIDs() {
        HashSet<String> activeDynamicRemoteIDs = new HashSet<>();
        for (String remoteID : this.activeRemotes.keySet()) {
            if (this.hasDynamicRemoteWithID(remoteID)) {
                activeDynamicRemoteIDs.add(remoteID);
            }
        }
        return activeDynamicRemoteIDs;
    }

    public ArrayList<SimRemote> getActiveDynamicRemotes() {
        ArrayList<SimRemote> activeDynamicRemotes = new ArrayList<>();
        for (String remoteID : this.activeRemotes.keySet()) {
            if (this.hasDynamicRemoteWithID(remoteID)) {
                activeDynamicRemotes.add(this.getDynamicRemoteWithID(remoteID));
            }
        }
        return activeDynamicRemotes;
    }

    public SimRemote getActiveDynamicRemoteWithID(String remoteID) throws SimException {
        if (!this.hasActiveDynamicRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no active, dynamic remote %s", this.getScenarioID(),
                remoteID));
        }
        return this.activeRemotes.get(remoteID);
    }

    public HashSet<String> getDynamicRemoteIDs() {
        return new HashSet<String>(this.dynamicRemotes.keySet());
    }

    public ArrayList<SimRemote> getDynamicRemotes() {
        return new ArrayList<SimRemote>(this.dynamicRemotes.values());
    }

    public SimRemote getDynamicRemoteWithID(String remoteID) throws SimException {
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

    public HashSet<String> getRemoteIDs() {
        return new HashSet<String>(this.allRemotes.keySet());
    }

    public ArrayList<SimRemote> getRemotes() {
        return new ArrayList<SimRemote>(this.allRemotes.values());
    }

    public SimRemote getRemoteWithID(String remoteID) throws SimException {
        if (!this.hasRemoteWithID(remoteID)) {
            throw new SimException(String.format("Scenario <%s> has no remote %s", this.getScenarioID(), remoteID));
        }
        return this.allRemotes.get(remoteID);
    }

    public String getScenarioID() {
        return this.config.getScenarioID();
    }

    public SnapStatus getSnapStatus() {
        return this.status;
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

    private HashMap<String, RemoteState> getState() {
        HashMap<String, RemoteState> state = new HashMap<>();
        for (String remoteID : this.allRemotes.keySet()) {
            state.put(remoteID, this.allRemotes.get(remoteID).getState());
        }
        return state;
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
        return this.status.equals(SnapStatus.ERROR);
    }

    public boolean hasRemotes() {
        return !this.allRemotes.isEmpty();
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.allRemotes.containsKey(remoteID);
    }

    public boolean isDone() {
        return this.status.equals(SnapStatus.DONE);
    }

    public boolean isInProgress() {
        return this.status.equals(SnapStatus.START) || this.status.equals(SnapStatus.IN_PROGRESS);
    }

    private void updateDynamicRemotes(HashMap<String, RemoteController> intentions, double stepSize)
            throws SimException {
        Debugger.logger.info(String.format("Updating remotes %s ...", this.getDynamicRemoteIDs().toString()));
        for (SimRemote remote : this.dynamicRemotes.values()) {
            // TODO: add map effects
            // TODO: sensor properties (accuracy, delay, interference, etc.)
            if (intentions != null && intentions.containsKey(remote.getRemoteID())) {
                remote.update(this, intentions.get(remote.getRemoteID()), stepSize);
            } else {
                remote.update(this, stepSize);
            }
            if (this.getMap().isOutOfBounds(remote.getLocation())) {
                Debugger.logger.err(String.format("Remote %s out of bounds at %s", remote.getLabel(),
                    remote.getLocation().toString()));
                this.status = SnapStatus.ERROR;
            }
            if (remote.isInactive() && this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.remove(remote.getRemoteID());
            } else if (remote.isActive() && !this.hasActiveRemoteWithID(remote.getRemoteID())) {
                this.activeRemotes.put(remote.getRemoteID(), remote);
            }
        }
    }

    private void updatePassiveRemotes(HashMap<String, RemoteController> intentions, double stepSize)
            throws SimException {
        Debugger.logger.info("Updating passive remotes ...");
        for (SimRemote remote : this.activeRemotes.values()) {
            if (this.hasDynamicRemoteWithID(remote.getRemoteID())) {
                continue;
            }
            if (intentions.containsKey(remote.getRemoteID())) {
                remote.update(this, intentions.get(remote.getRemoteID()), stepSize);
                continue;
            }
            RemoteController controller = new RemoteController(remote.getRemoteType(), remote.getRemoteID());
            if (this.status.equals(SnapStatus.START) && remote.isActive() && remote.hasInactiveSensors()) {
                controller.addIntention(Intent.Activate(remote.getInactiveSensorIDs()));
            }
            if (remote.isInactive() && remote.hasActiveSensors()) {
                controller.addIntention(Intent.Deactivate(remote.getActiveSensorIDs()));
            }
            Vector location = remote.getLocation();
            if (this.getMap().isInbounds(location)) {
                if (remote.isActive() && remote.isKinetic()) {
                    controller.addIntention(
                        Intent.Goto(
                            Vector.scale(
                                this.rng.getRandomDirection2D(),
                                ((KineticSimRemote) remote).getVelocity().getMagnitude()
                            )
                        )
                    );
                }
            } else {
                Debugger.logger.err(String.format("Remote %s out of bounds at %s", remote.getLabel(),
                    location.toString()));
                remote.update(this, controller, stepSize);
                this.status = SnapStatus.ERROR;
                continue;
            }
            // TODO: add map effects
            remote.update(this, controller, stepSize);
            if (remote.isKinetic() && this.getMap().isOutOfBounds(remote.getLocation())) {
                this.enforceBounds((KineticSimRemote) remote, location);
            }
        }
    }

    public void update() throws SimException {
        this.update(null, this.config.getStepSize());
    }

    public void update(double stepSize) throws SimException {
        this.update(null, stepSize);
    }

    public void update(HashMap<String, RemoteController> intentions, double stepSize) throws SimException {
        this.time += stepSize;
        if (this.status.equals(SnapStatus.DONE)) {
            return;
        }
        if (this.status.equals(SnapStatus.ERROR)) {
            this.status = SnapStatus.IN_PROGRESS;
        }
        this.updateDynamicRemotes(intentions, stepSize);
        this.updatePassiveRemotes(intentions, stepSize);
        if (this.status.equals(SnapStatus.START)) {
            this.status = SnapStatus.IN_PROGRESS;
        }
        if (this.getMissionLength() <= this.time) {
            this.status = SnapStatus.DONE;
        }
    }

}
