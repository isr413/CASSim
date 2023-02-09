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
import java.util.Map;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.*;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.Snapshot;

public class JsonSocket {

  protected static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
  protected static final int DEFAULT_PORT = 8080;

  public static JsonSocket Client() throws CommonException {
    return JsonSocket.Client(JsonSocket.DEFAULT_ADDRESS, JsonSocket.DEFAULT_PORT);
  }

  public static JsonSocket Client(int port) throws CommonException {
    return JsonSocket.Client(JsonSocket.DEFAULT_ADDRESS, port);
  }

  public static JsonSocket Client(InetAddress address) throws CommonException {
    try {
      return new JsonSocket(new Socket(address, JsonSocket.DEFAULT_PORT));
    } catch(IOException e) {
      throw new CommonException(e.toString());
    }
  }

  public static JsonSocket Client(String address) throws CommonException {
    try {
      return new JsonSocket(new Socket(address, JsonSocket.DEFAULT_PORT));
    } catch(IOException e) {
      throw new CommonException(e.toString());
    }
  }

  public static JsonSocket Client(InetAddress address, int port) throws CommonException {
    try {
      return new JsonSocket(new Socket(address, port));
    } catch(IOException e) {
      throw new CommonException(e.toString());
    }
  }

  public static JsonSocket Client(String address, int port) throws CommonException {
    try {
      return new JsonSocket(new Socket(address, port));
    } catch(IOException e) {
      throw new CommonException(e.toString());
    }
  }

  public static JsonSocket Server() throws CommonException {
    return JsonSocket.Server(JsonSocket.DEFAULT_PORT);
  }

  public static JsonSocket Server(int port) throws CommonException {
    try {
      return new JsonSocket(new ServerSocket(port));
    } catch(IOException e) {
      throw new CommonException(e.toString());
    }
  }

  private ServerSocket serverSocket;
  private Socket clientSocket;
  private BufferedReader in;
  private PrintWriter out;

  private JsonSocket(ServerSocket socket) throws CommonException {
    try {
      this.serverSocket = socket;
      this.clientSocket = this.serverSocket.accept();
      this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
      this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
    } catch (IOException e) {
      throw new CommonException(e.toString());
    }
  }

  private JsonSocket(Socket socket) throws CommonException {
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

  public Json getInputBlocking() throws CommonException {
    try {
      while (true) {
        if (this.in.ready()) {
          String encoding = in.readLine();
          Json json = Json.of(encoding);
          return json;
        }
      }
    } catch(IOException e) {
      throw new CommonException(e.toString());
    }
  }

  public Map<String, IntentionSet> getIntentionsBlocking() throws CommonException {
    try {
      JsonArray json = this.getInputBlocking().getJsonArray();
      HashMap<String, IntentionSet> controllers = new HashMap<>();
      for (int i = 0; i < json.length(); i++) {
        IntentionSet controller = new IntentionSet(json.getJson(i));
        if (controller.hasIntentions()) {
          controllers.put(controller.getRemoteID(), controller);
        }
      }
      return controllers;
    } catch (JsonException e) {
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

  public void send(Json json) throws CommonException {
    try {
      this.out.println(json.toString());
    } catch (JsonException e) {
      throw new CommonException(e.toString());
    }
  }

  public void sendIntentions(Collection<IntentionSet> intentions) throws CommonException {
    try {
      JsonArrayBuilder json = JsonBuilder.Array();
      for (IntentionSet intention : intentions) {
        json.put(intention.toJson());
      }
      this.send(json.toJson().toString());
    } catch (JsonException e) {
      throw new CommonException(e.toString());
    }
  }

  public void sendScenarioConfig(ScenarioConfig scenario) throws CommonException {
    try {
      this.send(scenario.encode());
    } catch (JsonException e) {
      throw new CommonException(e.toString());
    }
  }

  public void sendSnapshot(Snapshot snap) throws CommonException {
    try {
      this.send(snap.encode());
    } catch (JsonException e) {
      throw new CommonException(e.toString());
    }
  }
}
