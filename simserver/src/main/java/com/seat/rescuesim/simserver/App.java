package com.seat.rescuesim.simserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.seat.rescuesim.common.util.Debugger;

public class App {

    private static final int PORT = 8080;

    private static void waitForScenario(BufferedReader in) {
        Debugger.logger.info("Waiting for scenario config...");
        boolean waiting = true;
        String message = null;
        try {
            while (waiting) {
                if (in.ready()) {
                    message = in.readLine();
                    System.out.println(message);
                    waiting = false;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        try {
            Debugger.logger.info("Sim server starting...");
            ServerSocket server = new ServerSocket(PORT);
            Debugger.logger.info(String.format("Listening on port=%d", PORT));
            Socket client = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            waitForScenario(in);
            server.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
