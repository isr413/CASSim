package com.seat.rescuesim.common.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.scenario.ScenarioConfig;
import com.seat.rescuesim.common.scenario.Snapshot;
import com.seat.rescuesim.common.util.CoreException;

public class JSONSocket {

    protected static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
    protected static final int DEFAULT_PORT = 8080;

    public static JSONSocket Client() throws IOException {
        return JSONSocket.Client(JSONSocket.DEFAULT_ADDRESS, JSONSocket.DEFAULT_PORT);
    }

    public static JSONSocket Client(int port) throws IOException {
        return JSONSocket.Client(JSONSocket.DEFAULT_ADDRESS, port);
    }

    public static JSONSocket Client(InetAddress address, int port) throws IOException {
        return new JSONSocket(new Socket(address, port));
    }

    public static JSONSocket Client(String address, int port) throws IOException {
        return new JSONSocket(new Socket(address, port));
    }

    public static JSONSocket Server() throws IOException {
        return JSONSocket.Server(JSONSocket.DEFAULT_PORT);
    }

    public static JSONSocket Server(int port) throws IOException {
        return new JSONSocket(new ServerSocket(port));
    }

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    private JSONSocket(ServerSocket socket) throws IOException {
        this.serverSocket = socket;
        this.clientSocket = this.serverSocket.accept();
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
    }

    private JSONSocket(Socket socket) throws IOException {
        this.serverSocket = null;
        this.clientSocket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
    }

    public void close() throws IOException {
        this.out.close();
        this.in.close();
        this.clientSocket.close();
        if (this.serverSocket != null) {
            this.serverSocket.close();
        }
    }

    public String getAddress() {
        return (this.serverSocket != null) ?
            this.serverSocket.getInetAddress().toString() :
            this.clientSocket.getInetAddress().toString();
    }

    public JSONOption getInputBlocking() throws CoreException, IOException {
        while (true) {
            if (this.in.ready()) {
                String encoding = in.readLine();
                JSONOption option = JSONOption.String(encoding);
                if (option.isNone()) {
                    throw new CoreException(encoding);
                }
                return option;
            }
        }
    }

    public HashMap<String, IntentionSet> getIntentionsBlocking() throws CoreException, IOException {
        JSONArray json;
        try {
            json = this.getInputBlocking().someArray();
        } catch (JSONException e) {
            throw new CoreException(e.toString());
        }
        HashMap<String, IntentionSet> controllers = new HashMap<>();
        for (int i = 0; i < json.length(); i++) {
            IntentionSet controller = new IntentionSet(json.getJSONOption(i));
            if (controller.hasIntentions()) {
                controllers.put(controller.getRemoteID(), controller);
            }
        }
        return controllers;
    }

    public int getPort() {
        return this.clientSocket.getPort();
    }

    public ScenarioConfig getScenarioBlocking() throws CoreException, IOException {
        return new ScenarioConfig(this.getInputBlocking());
    }

    public Snapshot getSnapshotBlocking() throws CoreException, IOException {
        return new Snapshot(this.getInputBlocking());
    }

    public void send(String encoding) {
        this.out.println(encoding);
    }

    public void send(JSONOption option) {
        this.out.println(option.toString());
    }

    public void sendIntentions(ArrayList<IntentionSet> controllers) {
        JSONArrayBuilder json = JSONBuilder.Array();
        for (IntentionSet controller : controllers) {
            json.put(controller.toJSON());
        }
        this.send(json.toJSON().toString());
    }

    public void sendScenario(ScenarioConfig scenario) {
        this.send(scenario.encode());
    }

    public void sendSnapshot(ScenarioConfig scenario) {
        this.send(scenario.encode());
    }

}
