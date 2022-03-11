package com.seat.rescuesim.simserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.seat.rescuesim.common.Scenario;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.simserver.sim.SimScenario;

public class App {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            Debugger.logger.info("Sim server starting...");
            ServerSocket server = new ServerSocket(PORT);
            Debugger.logger.info(String.format("Listening on port=%d", PORT));
            Socket client = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            Scenario scenarioConfig = waitForScenarioConfig(in);
            Debugger.logger.info(String.format("Received scenario <%s>", scenarioConfig.getScenarioID()));
            System.out.println(scenarioConfig.toJSON().toString(4));
            runScenario(new SimScenario(scenarioConfig), in, out);
            server.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static Scenario waitForScenarioConfig(BufferedReader in) throws IOException {
        Debugger.logger.info("Waiting for scenario config...");
        while (true) {
            if (in.ready()) {
                String message = in.readLine();
                return new Scenario(message);
            }
        }
    }

    private static void runScenario(SimScenario scenario, BufferedReader in, PrintWriter out) throws IOException {
        Debugger.logger.info(String.format("Running scenario <%s> ...", scenario.getScenarioID()));
    }

}
