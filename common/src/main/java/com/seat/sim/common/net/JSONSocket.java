package com.seat.sim.common.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.Snapshot;

public class JSONSocket {

    protected static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
    protected static final int DEFAULT_PORT = 8080;

    public static JSONSocket Client() throws CommonException {
        return JSONSocket.Client(JSONSocket.DEFAULT_ADDRESS, JSONSocket.DEFAULT_PORT);
    }

    public static JSONSocket Client(int port) throws CommonException {
        return JSONSocket.Client(JSONSocket.DEFAULT_ADDRESS, port);
    }

    public static JSONSocket Client(InetAddress address) throws CommonException {
        try {
            return new JSONSocket(new Socket(address, JSONSocket.DEFAULT_PORT));
        } catch(IOException e) {
            throw new CommonException(e.toString());
        }
    }

    public static JSONSocket Client(String address) throws CommonException {
        try {
            return new JSONSocket(new Socket(address, JSONSocket.DEFAULT_PORT));
        } catch(IOException e) {
            throw new CommonException(e.toString());
        }
    }

    public static JSONSocket Client(InetAddress address, int port) throws CommonException {
        try {
            return new JSONSocket(new Socket(address, port));
        } catch(IOException e) {
            throw new CommonException(e.toString());
        }
    }

    public static JSONSocket Client(String address, int port) throws CommonException {
        try {
            return new JSONSocket(new Socket(address, port));
        } catch(IOException e) {
            throw new CommonException(e.toString());
        }
    }

    public static JSONSocket Server() throws CommonException {
        return JSONSocket.Server(JSONSocket.DEFAULT_PORT);
    }

    public static JSONSocket Server(int port) throws CommonException {
        try {
            return new JSONSocket(new ServerSocket(port));
        } catch(IOException e) {
            throw new CommonException(e.toString());
        }
    }

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    private JSONSocket(ServerSocket socket) throws CommonException {
        try {
            this.serverSocket = socket;
            this.clientSocket = this.serverSocket.accept();
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new CommonException(e.toString());
        }
    }

    private JSONSocket(Socket socket) throws CommonException {
        try {
            this.serverSocket = null;
            this.clientSocket = socket;
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new CommonException(e.toString());
        }
    }

    public void close() throws CommonException {
        try {
            this.out.close();
            this.in.close();
            this.clientSocket.close();
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            throw new CommonException(e.toString());
        }
    }

    public String getAddress() {
        return (this.serverSocket != null) ?
            this.serverSocket.getInetAddress().toString() :
            this.clientSocket.getInetAddress().toString();
    }

    public JSONOption getInputBlocking() throws CommonException {
        try {
            while (true) {
                if (this.in.ready()) {
                    String encoding = in.readLine();
                    JSONOption option = JSONOption.String(encoding);
                    if (option.isNone()) {
                        throw new CommonException(encoding);
                    }
                    return option;
                }
            }
        } catch(IOException e) {
            throw new CommonException(e.toString());
        }
    }

    public HashMap<String, IntentionSet> getIntentionsBlocking() throws CommonException {
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
            throw new CommonException(e.toString());
        }
    }

    public int getPort() {
        return this.clientSocket.getPort();
    }

    public ScenarioConfig getScenarioConfigBlocking() throws CommonException {
        return new ScenarioConfig(this.getInputBlocking());
    }

    public Snapshot getSnapshotBlocking() throws CommonException {
        return new Snapshot(this.getInputBlocking());
    }

    public void send(String encoding) {
        this.out.println(encoding);
    }

    public void send(JSONOption option) throws CommonException {
        try {
            this.out.println(option.toString());
        } catch (JSONException e) {
            throw new CommonException(e.toString());
        }
    }

    public void sendIntentions(Collection<IntentionSet> intentions) throws CommonException {
        try {
            JSONArrayBuilder json = JSONBuilder.Array();
            for (IntentionSet intention : intentions) {
                json.put(intention.toJSON());
            }
            this.send(json.toJSON().toString());
        } catch (JSONException e) {
            throw new CommonException(e.toString());
        }
    }

    public void sendScenarioConfig(ScenarioConfig scenario) throws CommonException {
        try {
            this.send(scenario.encode());
        } catch (JSONException e) {
            throw new CommonException(e.toString());
        }
    }

    public void sendSnapshot(Snapshot snap) throws CommonException {
        try {
            this.send(snap.encode());
        } catch (JSONException e) {
            throw new CommonException(e.toString());
        }
    }

}
