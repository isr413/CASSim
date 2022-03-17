package com.seat.rescuesim.simserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.seat.rescuesim.common.ScenarioConfig;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.simserver.sim.SimException;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.util.ArgsParser;

public class App {

    private static final int DEFAULT_PORT = 8080;
    private static final String PORT_PARAM = "-p";

    private static ServerSocket getServerSocketBlocking(ArgsParser args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.hasParam(PORT_PARAM)) {
            port = args.getInt(PORT_PARAM);
        }
        ServerSocket server = new ServerSocket(port);
        Debugger.logger.info(String.format("Listening on port=%d", port));
        return server;
    }

    private static ScenarioConfig getScenarioConfigBlocking(BufferedReader in) throws IOException, SimException {
        Debugger.logger.info("Waiting for scenario config...");
        while (true) {
            if (in.ready()) {
                String scenarioEncoding = in.readLine();
                if (!JSONOption.isJSON(scenarioEncoding)) {
                    throw new SimException(scenarioEncoding);
                }
                ScenarioConfig scenarioConfig = new ScenarioConfig(scenarioEncoding);
                Debugger.logger.info(String.format("Received scenario <%s>", scenarioConfig.getScenarioID()));
                return scenarioConfig;
            }
        }
    }

    private static void runScenarioSync(ScenarioConfig config, BufferedReader in,
            PrintWriter out) throws IOException, SimException {
        Debugger.logger.info(String.format("Running scenario <%s> ...", config.getScenarioID()));
        SimScenario scenario = new SimScenario(config);
        double time = 0.0;
        while (time < config.getMissionLength()) {
            Debugger.logger.info(String.format("Updating scenario <%s> for time=%.2f", config.getScenarioID(), time));
            time += config.getStepSize();
            if (config.getMissionLength() < time) {
                scenario.update(time - config.getMissionLength());
            } else {
                scenario.update(config.getStepSize());
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket client = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            Debugger.logger.info("Sim server starting...");
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
        } catch (SimException e) {
            System.err.println(e);
            if (out != null) {
                out.println(e.toString());
            }
        }
    }

}
