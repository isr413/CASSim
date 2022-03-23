package com.seat.rescuesim.simserver.sim.remote;

import com.seat.rescuesim.common.base.BaseSpec;
import com.seat.rescuesim.common.drone.DroneSpec;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.simserver.sim.util.SimException;

public class SimRemoteFactory {

    public static SimRemote getSimRemote(RemoteSpec spec, String label) throws SimException {
        if (spec.getRemoteType().equals(RemoteType.BASE)) {
            return new SimBase((BaseSpec) spec, label);
        } else if (spec.getRemoteType().equals(RemoteType.DRONE)) {
            return new SimDrone((DroneSpec) spec, label);
        } else if (spec.getRemoteType().equals(RemoteType.VICTIM)) {
            return new SimVictim((VictimSpec) spec, label);
        }
        throw new SimException(String.format("Unrecognized spec %s", spec.toString()));
    }

}
