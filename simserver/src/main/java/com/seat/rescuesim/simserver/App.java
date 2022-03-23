package com.seat.rescuesim.simserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.seat.rescuesim.common.ScenarioConfig;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.simserver.sim.SimException;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.util.ArgsParser;

public class App {

    private static final int DEFAULT_PORT = 8080;
    private static final String PORT_PARAM = "-p";

    private static String getInputBlocking(BufferedReader in) throws IOException, JSONException {
        while (true) {
            if (in.ready()) {
                String encoding = in.readLine();
                if (!JSONOption.isJSON(encoding)) {
                    throw new JSONException(String.format("Cannot decode %s", encoding));
                }
                return encoding;
            }
        }
    }

    private static ServerSocket getServerSocketBlocking(ArgsParser args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.hasParam(PORT_PARAM)) {
            port = args.getInt(PORT_PARAM);
        }
        ServerSocket server = new ServerSocket(port);
        Debugger.logger.info(String.format("Listening on port=%d ...", port));
        return server;
    }

    private static ScenarioConfig getScenarioConfigBlocking(BufferedReader in) throws IOException, JSONException {
        Debugger.logger.info("Waiting for scenario config ...");
        String encoding = getInputBlocking(in);
        ScenarioConfig scenarioConfig = new ScenarioConfig(encoding);
        Debugger.logger.state(String.format("Received scenario <%s>", scenarioConfig.getScenarioID()));
        return scenarioConfig;
    }

    private static HashMap<String, RemoteController> getRemoteIntentionsBlocking(SimScenario scenario,
            BufferedReader in) throws IOException, JSONException {
        if (!scenario.hasRemotes()) {
            Debugger.logger.info(String.format("No remotes for <%s>", scenario.getScenarioID()));
            return new HashMap<>();
        }
        Debugger.logger.info(String.format("Waiting for intentions for remotes %s ...",
            scenario.getActiveDynamicRemoteIDs().toString()));
        String encoding = getInputBlocking(in);
        HashMap<String, RemoteController> controllers = new HashMap<>();
        JSONArray json = JSONOption.String(encoding).someArray();
        for (int i = 0; i < json.length(); i++) {
            RemoteController controller = new RemoteController(json.getJSONObject(i));
            if (controller.hasIntentions()) {
                controllers.put(controller.getRemoteID(), controller);
            }
        }
        Debugger.logger.state(String.format("Received intentions for remotes %s ...", controllers.keySet().toString()));
        return controllers;
    }

    private static void runScenarioSync(ScenarioConfig config, BufferedReader in, PrintWriter out) {
        Debugger.logger.info(String.format("Running scenario <%s> ...", config.getScenarioID()));
        SimScenario scenario = new SimScenario(config);
        Snapshot snap = scenario.getSnapshot();
        Debugger.logger.info(String.format("Sending initial snap <%s> ...", snap.getHash()));
        out.println(snap.encode());
        double time = 0.0;
        while (time < scenario.getMissionLength()) {
            try {
                HashMap<String, RemoteController> controllers = getRemoteIntentionsBlocking(scenario, in);
                Debugger.logger.info(String.format("Updating scenario <%s> for time=%.2f ...", scenario.getScenarioID(),
                    time + scenario.getStepSize()));
                if (scenario.getMissionLength() < time + scenario.getStepSize()) {
                    scenario.update(controllers, scenario.getMissionLength() - time);
                    time = scenario.getMissionLength();
                } else {
                    scenario.update(controllers, scenario.getStepSize());
                    time += scenario.getStepSize();
                }
                Debugger.logger.info(String.format("Sending snap <%s> ...", snap.getHash()));
                snap = scenario.getSnapshot();
                out.println(snap.encode());
                if (scenario.isDone()) {
                    Debugger.logger.state(String.format("Scenario <%s> is done", scenario.getScenarioID()));
                    break;
                }
            } catch (IOException e) {
                if (out != null) {
                    out.println(e.toString());
                }
                e.printStackTrace();
            } catch (JSONException e) {
                if (out != null) {
                    out.println(e.toString());
                }
                e.printStackTrace();
            } catch (SimException e) {
                if (out != null) {
                    out.println(e.toString());
                }
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket client = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            Debugger.logger.info("Sim server starting ...");
            ArgsParser argsParser = new ArgsParser(args);
            server = getServerSocketBlocking(argsParser);
            client = server.accept();
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            ScenarioConfig scenarioConfig = getScenarioConfigBlocking(in);
            runScenarioSync(scenarioConfig, in, out);
            server.close();
        } catch (IOException e) {
            System.err.println(e);
            if (out != null) {
                out.println(e.toString());
            }
        } catch (JSONException e) {
            System.err.println(e);
            if (out != null) {
                out.println(e.toString());
            }
        }
    }

}
