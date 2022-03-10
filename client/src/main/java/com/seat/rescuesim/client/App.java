package com.seat.rescuesim.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.seat.rescuesim.client.sandbox.ACSOS;

public class App {

    private static final InetAddress ADDRESS = InetAddress.getLoopbackAddress();
    private static final int PORT = 8080;

    private static Application GetApplication(String[] args) {
        return new ACSOS(args);
    }

    public static void main(String[] args) {
        Application app = GetApplication(args);
        System.out.println(app.getScenario().encode());
        try {
            Socket socket = new Socket(ADDRESS, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(app.getScenario().encode());
            socket.close();
        } catch(IOException e) {
            System.err.println(e);
        }
    }

}
