package com.seat.sim.simserver.core;

import java.util.HashMap;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.net.JSONSocket;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.simserver.scenario.SimScenario;

public class AppServer {

    private JSONSocket socket;

    public AppServer() throws CommonException {
        this.socket = JSONSocket.Server();
    }

    public AppServer(int port) throws CommonException {
        this.socket = JSONSocket.Server(port);
    }

    public void run() throws CommonException, ServerException, SimException {
        Debugger.logger.info("Running server ...");

        Debugger.logger.info("Waiting for scenario config ...");
        ScenarioConfig config = this.socket.getScenarioConfigBlocking();
        Debugger.logger.state(String.format("Received scenario config <%s>", config.getScenarioID()));

        Debugger.logger.info(config.toString());

        SimScenario scenario = new SimScenario(config);
        Debugger.logger.info(String.format("Running scenario <%s> ...", scenario.getScenarioID()));

        Snapshot snap = scenario.getSnapshot();
        Debugger.logger.info(String.format("Sending initial snap <%s> ...", snap.getHash()));
        this.socket.sendSnapshot(snap);
        Debugger.logger.state(String.format("Snap <%s> sent", snap.getHash()));

        while (true) {
            double time = scenario.getTime() + scenario.getStepSize();

            Debugger.logger.info("Waiting for intention(s) ...");
            HashMap<String, IntentionSet> intentions = this.socket.getIntentionsBlocking();
            Debugger.logger.state("Received intention(s)");

            Debugger.logger.info(String.format("Updating scenario <%s> to time=%.2f ...", scenario.getScenarioID(),
                time));
            if (scenario.getMissionLength() < time) {
                scenario.update(intentions, scenario.getMissionLength() + scenario.getStepSize() - time);
            } else {
                scenario.update(intentions, scenario.getStepSize());
            }

            snap = scenario.getSnapshot();
            Debugger.logger.info(String.format("Sending snap <%s> ...", snap.getHash()));
            this.socket.sendSnapshot(snap);
            Debugger.logger.state(String.format("Snap <%s> sent", snap.getHash()));

            if (scenario.isDone()) {
                break;
            }
        }

        Debugger.logger.state(String.format("Scenario <%s> is done", scenario.getScenarioID()));
    }

}
