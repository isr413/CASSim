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

    public static JSONSocket Client() throws CoreException {
        return JSONSocket.Client(JSONSocket.DEFAULT_ADDRESS, JSONSocket.DEFAULT_PORT);
    }

    public static JSONSocket Client(int port) throws CoreException {
        return JSONSocket.Client(JSONSocket.DEFAULT_ADDRESS, port);
    }

    public static JSONSocket Client(InetAddress address, int port) throws CoreException {
        try {
            return new JSONSocket(new Socket(address, port));
        } catch(IOException e) {
            throw new CoreException(e.toString());
        }
    }

    public static JSONSocket Client(String address, int port) throws CoreException {
        try {
            return new JSONSocket(new Socket(address, port));
        } catch(IOException e) {
            throw new CoreException(e.toString());
        }
    }

    public static JSONSocket Server() throws CoreException {
        return JSONSocket.Server(JSONSocket.DEFAULT_PORT);
    }

    public static JSONSocket Server(int port) throws CoreException {
        try {
            return new JSONSocket(new ServerSocket(port));
        } catch(IOException e) {
            throw new CoreException(e.toString());
        }
    }

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    private JSONSocket(ServerSocket socket) throws CoreException {
        try {
            this.serverSocket = socket;
            this.clientSocket = this.serverSocket.accept();
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new CoreException(e.toString());
        }
    }

    private JSONSocket(Socket socket) throws CoreException {
        try {
            this.serverSocket = null;
            this.clientSocket = socket;
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new CoreException(e.toString());
        }
    }

    public void close() throws CoreException {
        try {
            this.out.close();
            this.in.close();
            this.clientSocket.close();
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            throw new CoreException(e.toString());
        }
    }

    public String getAddress() {
        return (this.serverSocket != null) ?
            this.serverSocket.getInetAddress().toString() :
            this.clientSocket.getInetAddress().toString();
    }

    public JSONOption getInputBlocking() throws CoreException {
        try {
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
        } catch(IOException e) {
            throw new CoreException(e.toString());
        }
    }

    public HashMap<String, IntentionSet> getIntentionsBlocking() throws CoreException {
        try {
            JSONArray json = this.getInputBlocking().someArray();
            HashMap<String, IntentionSet> controllers = new HashMap<>();
            for (int i = 0; i < json.length(); i++) {
                IntentionSet controller = new IntentionSet(json.getJSONOption(i));
                if (controller.hasIntentions()) {
                    controllers.put(controller.getRemoteID(), controller);
                }
            }
            return controllers;
        } catch (JSONException e) {
            throw new CoreException(e.toString());
        }
    }

    public int getPort() {
        return this.clientSocket.getPort();
    }

    public ScenarioConfig getScenarioBlocking() throws CoreException {
        return new ScenarioConfig(this.getInputBlocking());
    }

    public Snapshot getSnapshotBlocking() throws CoreException {
        return new Snapshot(this.getInputBlocking());
    }

    public void send(String encoding) {
        this.out.println(encoding);
    }

    public void send(JSONOption option) throws CoreException {
        try {
            this.out.println(option.toString());
        } catch (JSONException e) {
            throw new CoreException(e.toString());
        }
    }

    public void sendIntentions(ArrayList<IntentionSet> controllers) throws CoreException {
        try {
            JSONArrayBuilder json = JSONBuilder.Array();
            for (IntentionSet controller : controllers) {
                json.put(controller.toJSON());
            }
            this.send(json.toJSON().toString());
        } catch (JSONException e) {
            throw new CoreException(e.toString());
        }
    }

    public void sendScenario(ScenarioConfig scenario) throws CoreException {
        try {
            this.send(scenario.encode());
        } catch (JSONException e) {
            throw new CoreException(e.toString());
        }
    }

    public void sendSnapshot(Snapshot snap) throws CoreException {
        try {
            this.send(snap.encode());
        } catch (JSONException e) {
            throw new CoreException(e.toString());
        }
    }

}
