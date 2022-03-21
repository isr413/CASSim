package com.seat.rescuesim.simserver.sim.remote;

import com.seat.rescuesim.common.base.BaseSpec;
import com.seat.rescuesim.common.drone.DroneSpec;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.victim.VictimSpec;

public class SimRemoteFactory {

    public static SimRemote getSimRemote(RemoteSpec spec, String label) {
        if (spec.getSpecType().equals(RemoteType.BASE)) {
            return new SimBase((BaseSpec) spec, label);
        } else if (spec.getSpecType().equals(RemoteType.DRONE)) {
            return new SimDrone((DroneSpec) spec, label);
        } else if (spec.getSpecType().equals(RemoteType.VICTIM)) {
            return new SimVictim((VictimSpec) spec, label);
        } else {
            return null;
        }
    }

}
