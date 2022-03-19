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
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.victim.VictimConfig;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.simserver.util.Random;

public class SimScenario {

    private HashSet<String> activeRemotes;
    private HashSet<String> allRemotes;
    private ScenarioConfig config;
    private HashMap<String, SimVictim> dynamicVictims;
    private HashMap<String, SimVictim> passiveVictims;
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
        this.allRemotes = new HashSet<>();
        this.activeRemotes = new HashSet<>();
        this.dynamicVictims = new HashMap<>();
        this.passiveVictims = new HashMap<>();
        this.initVictims();
    }

    private boolean boundsCheck(Vector nextLocation) {
        return 0 <= nextLocation.getX() && nextLocation.getX() <= this.getMapWidth() && 0 <= nextLocation.getY() &&
            nextLocation.getY() <= this.getMapHeight();
    }

    public void enforceBounds(KineticSimRemote remote, Vector prevLocation) {
        int impactType = this.getImpactType(prevLocation, remote.getLocation());
        Vector impactPoint = this.getImpactPoint(prevLocation, remote.getLocation(), impactType);
        if (impactPoint == null) {
            Debugger.logger.err(String.format("Could not compute bounce location for %s on victim %s",
                remote.getLocation().toString(), remote.getLabel()));
            return;
        }
        Vector bounce = this.getBounceLocation(impactPoint, remote.getLocation(), impactType);
        if (bounce == null) {
            Debugger.logger.err(String.format("Could not compute bounce location for %s on victim %s",
                remote.getLocation().toString(), remote.getLabel()));
            return;
        }
        remote.setLocation(bounce);
        Vector newVelocity = Vector.scale(Vector.subtract(bounce, impactPoint).getUnitVector(),
            remote.getVelocity().getMagnitude());
        remote.setVelocity(newVelocity);
        Vector newAcceleration = Vector.scale(Vector.subtract(bounce, impactPoint).getUnitVector(),
            remote.getAcceleration().getMagnitude());
        remote.setAcceleration(newAcceleration);
    }

    private Vector getBounceLocation(Vector impactPoint, Vector nextLocation, int impactType) {
        double deltaX = nextLocation.getX() - impactPoint.getX();
        double deltaY = nextLocation.getY() - impactPoint.getY();
        switch (impactType) {
            case 0:
            case 2:
            case 4:
            case 6:
                return new Vector(impactPoint.getX() - deltaX, impactPoint.getY() - deltaY);
            case 1:
            case 5:
                return new Vector(impactPoint.getX() + deltaX, impactPoint.getY() - deltaY);
            case 3:
            case 7:
                return new Vector(impactPoint.getX() - deltaX, impactPoint.getY() + deltaY);
            default:
                return null;
        }
    }

    private Vector getImpactPoint(Vector location, Vector nextLocation, int impactType) {
        double slope = Vector.slope(location, nextLocation);
        switch (impactType) {
            case 0:
                return new Vector(0, 0);
            case 2:
                return new Vector(this.getMapWidth(), 0);
            case 1:
                double deltaX = (Double.isFinite(slope) && slope != 0) ? (0 - location.getY()) / slope : 0;
                return new Vector(location.getX() + deltaX, 0);
            case 4:
                return new Vector(this.getMapWidth(), this.getMapHeight());
            case 3:
                double deltaY = (Double.isFinite(slope)) ? (this.getMapWidth() - location.getX()) * slope : 0;
                return new Vector(this.getMapWidth(), location.getY() + deltaY);
            case 6:
                return new Vector(0, this.getMapHeight());
            case 5:
                deltaX = (Double.isFinite(slope) && slope != 0) ? (this.getMapHeight()-location.getY()) / slope : 0;
                return new Vector(location.getX() + deltaX, this.getMapHeight());
            case 7:
                deltaY = (Double.isFinite(slope)) ? (0 - location.getX()) * slope : 0;
                return new Vector(0, location.getY() + deltaY);
            default:
                return null;
        }
    }

    public int getImpactType(Vector location, Vector nextLocation) {
        if (nextLocation.getX() < 0 && nextLocation.getY() < 0) {
            return 0;
        } else if (this.getMapWidth() < nextLocation.getX() && nextLocation.getY() < 0) {
            return 2;
        } else if (nextLocation.getY() < 0) {
            return 1;
        } else if (this.getMapWidth() < nextLocation.getX() && this.getMapHeight() < nextLocation.getY()) {
            return 4;
        } else if (this.getMapWidth() < nextLocation.getX()) {
            return 3;
        } else if (nextLocation.getX() < 0 && this.getMapHeight() < nextLocation.getY()) {
            return 6;
        } else if (this.getMapHeight() < nextLocation.getY()) {
            return 5;
        } else {
            return 7;
        }
    }

    private void initVictims() {
        for (VictimConfig victimConfig : this.config.getVictimConfig()) {
            VictimSpec victimSpec = victimConfig.getSpec();
            Iterator<String> remoteIDs = victimConfig.getRemoteIDs().iterator();
            for (int i = 0; i < victimConfig.getCount(); i++) {
                String label = (i < victimConfig.getRemoteIDs().size()) ? remoteIDs.next() : String.format("v<%d>", i);
                this.allRemotes.add(label);
                if (victimConfig.isDynamic()) {
                    this.activeRemotes.add(label);
                    this.dynamicVictims.put(label, new SimVictim(label, victimSpec,
                        this.rng.getRandomLocation2D(this.getMapWidth(), this.getMapHeight()),
                        this.rng.getRandomSpeed2D(victimSpec.getSpeedMean(), victimSpec.getSpeedStddev())));
                } else {
                    this.passiveVictims.put(label, new SimVictim(label, victimSpec,
                        this.rng.getRandomLocation2D(this.getMapWidth(), this.getMapHeight()),
                        this.rng.getRandomSpeed2D(victimSpec.getSpeedMean(), victimSpec.getSpeedStddev())));
                }
            }
        }
    }

    public HashSet<String> getActiveRemotes() {
        return this.activeRemotes;
    }

    public Map getMap() {
        return this.config.getMap();
    }

    public int getMapHeight() {
        return this.getMap().getHeight() * this.getMap().getZoneSize();
    }

    public int getMapWidth() {
        return this.getMap().getWidth() * this.getMap().getZoneSize();
    }

    public int getMissionLength() {
        return this.config.getMissionLength();
    }

    public HashSet<String> getRemotes() {
        return this.allRemotes;
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
            this.allRemotes,
            this.activeRemotes,
            this.getState()
        );
    }

    private HashMap<String, RemoteState> getState() {
        HashMap<String, RemoteState> state = new HashMap<>();
        for (String remoteID : this.passiveVictims.keySet()) {
            state.put(remoteID, this.passiveVictims.get(remoteID).getState());
        }
        for (String remoteID : this.dynamicVictims.keySet()) {
            state.put(remoteID, this.dynamicVictims.get(remoteID).getState());
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
        return this.activeRemotes.contains(remoteID);
    }

    public boolean hasError() {
        return this.status.equals(SnapStatus.ERROR);
    }

    public boolean hasRemotes() {
        return !this.allRemotes.isEmpty();
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.allRemotes.contains(remoteID);
    }

    public boolean isDone() {
        return this.status.equals(SnapStatus.DONE);
    }

    public boolean isInProgress() {
        return this.status.equals(SnapStatus.START) || this.status.equals(SnapStatus.IN_PROGRESS);
    }

    private void updateDynamicRemotes(ArrayList<SimRemote> remotes, HashMap<String, ArrayList<Intention>> intentions,
            double stepSize) throws SimException {
        Debugger.logger.info(String.format("Updating dynamic remotes %s", remotes.toString()));
        if (intentions == null || intentions.isEmpty()) {
            for (SimRemote remote : remotes) {
                remote.update(stepSize);
            }
        } else {
            for (SimRemote remote : remotes) {
                if (intentions.containsKey(remote.getRemoteID())) {
                    remote.update(intentions.get(remote.getRemoteID()), stepSize);
                } else {
                    remote.update(stepSize);
                }
            }
        }
    }

    private void updatePassiveRemotes(ArrayList<SimRemote> remotes, HashMap<String, ArrayList<Intention>> intentions,
            double stepSize) throws SimException {
        Debugger.logger.info(String.format("Updating passive remotes %s", remotes.toString()));
        for (SimRemote remote : remotes) {
            Vector location = remote.getLocation();
            if (!this.boundsCheck(location)) {
                Debugger.logger.err(String.format("Remote %s out of bounds at %s", remote.getLabel(),
                    location.toString()));
                continue;
            }
            ArrayList<Intention> remoteIntentions = new ArrayList<>();
            remoteIntentions.add(Intent.None());
            // TODO: add random movement
            // TODO: add map effects
            remote.update(remoteIntentions, stepSize);
            if (remote.isKinetic() && !this.boundsCheck(remote.getLocation())) {
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

    public void update(HashMap<String, ArrayList<Intention>> intentions, double stepSize) throws SimException {
        this.time += stepSize;
        this.updateDynamicRemotes(new ArrayList<SimRemote>(this.dynamicVictims.values()), intentions, stepSize);
        this.updatePassiveRemotes(new ArrayList<SimRemote>(this.passiveVictims.values()), intentions, stepSize);
    }

}
