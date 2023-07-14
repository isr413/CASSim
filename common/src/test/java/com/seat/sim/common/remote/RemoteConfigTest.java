package com.seat.sim.common.remote;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.seat.sim.common.gui.TeamColor;
import com.seat.sim.common.json.JsonArrayBuilder;
import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;

public class RemoteConfigTest {

  public static RemoteConfig MockRemoteConfig() {
    return RemoteConfigTest.MockRemoteConfig(false);
  }

  public static RemoteConfig MockRemoteConfig(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new RemoteConfig(RemoteProtoTest.MockRemoteProto(), TeamColor.BLUE, 2, true, true)
        : new RemoteConfig(RemoteProtoTest.MockRemoteProto(), TeamColor.BLUE, Set.of("r1", "r2"), true, true);
  }

  @Test
  public void remoteConfigToJson() {
    RemoteConfig config = RemoteConfigTest.MockRemoteConfig();
    JsonObject json = config.toJson().getJsonObject();
    assertThat(new RemoteProto(json.getJson(RemoteConfig.PROTO)), is(config.getProto()));
    assertThat(TeamColor.decodeType(json), equalTo(config.getTeam()));
    assertThat(
        json.getJsonArray(RemoteConfig.REMOTE_IDS).toList(String.class),
        containsInAnyOrder(config.getRemoteIDs().toArray())
      );
    assertThat(json.getBoolean(RemoteConfig.ACTIVE), is(config.isActive()));
    assertThat(json.getBoolean(RemoteConfig.DYNAMIC), is(config.isDynamic()));
  }

  @Test
  public void jsonToRemoteConfig() {
    RemoteConfig config = RemoteConfigTest.MockRemoteConfig();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(RemoteConfig.PROTO, config.getProto().toJson());
    json.put(RemoteConfig.TEAM, config.getTeam().getType());
    JsonArrayBuilder jsonRemotes = JsonBuilder.Array();
    config.getRemoteIDs().forEach(jsonRemotes::put);
    json.put(RemoteConfig.REMOTE_IDS, jsonRemotes.toJson());
    json.put(RemoteConfig.COUNT, config.getCount());
    json.put(RemoteConfig.ACTIVE, config.isActive());
    json.put(RemoteConfig.DYNAMIC, config.isDynamic());
    assertThat(new RemoteConfig(json.toJson()), is(config));
  }

  @Test
  public void remoteConfigDecode() {
    RemoteConfig config = RemoteConfigTest.MockRemoteConfig();
    assertThat(new RemoteConfig(config.toJson()), is(config));
  }

  @Test
  public void remoteConfigPartialDecode() {
    RemoteConfig config = RemoteConfigTest.MockRemoteConfig(true);
    JsonObject json = config.toJson().getJsonObject();
    assertThat(new RemoteProto(json.getJson(RemoteConfig.PROTO)), is(config.getProto()));
    assertThat(TeamColor.decodeType(json), equalTo(config.getTeam()));
    assertThat(json.getInt(RemoteConfig.COUNT), equalTo(config.getCount()));
    assertThat(json.getBoolean(RemoteConfig.ACTIVE), is(config.isActive()));
    assertThat(json.getBoolean(RemoteConfig.DYNAMIC), is(config.isDynamic()));
    assertThat(new RemoteConfig(json.toJson()), is(config));
  }

  @Test
  public void hasMatch() {
    RemoteConfig config = RemoteConfigTest.MockRemoteConfig();
    config
      .getProto()
      .getTags()
      .stream()
      .map(tag -> String.valueOf(tag))
      .forEach(tag -> {
          assertThat(config.hasRemoteWithTag(tag), is(true));
          assertThat(config.hasRemoteMatch(Set.of(tag)), is(true));
          assertThat(config.hasRemoteMatch(Set.of(tag, "nt1")), is(true));
        });
    assertThat(
        config
          .hasRemoteMatch(
            config
              .getProto()
              .getTags()
              .stream()
              .map(tag -> String.valueOf(tag))
              .collect(Collectors.toSet())
          ),
        is(true)
      );
    assertThat(config.hasRemoteMatch(Set.of("nt1", "nt2")), is(false));
    assertThat(config.hasRemoteMatch(Set.of()), is(false));
  }

  @Test
  public void hasRemote() {
    RemoteConfig config = RemoteConfigTest.MockRemoteConfig();
    assertThat(config.hasRemoteIDs(), is(true));
    config
      .getRemoteIDs()
      .stream()
      .map(remoteID -> String.valueOf(remoteID))
      .forEach(remoteID -> assertThat(config.hasRemoteWithID(remoteID), is(true)));
  }

  @Test
  public void hasTeam() {
    assertThat(RemoteConfigTest.MockRemoteConfig().hasTeam(), is(true));
    assertThat(
        new RemoteConfig(
            RemoteProtoTest.MockRemoteProto(),
            TeamColor.NONE,
            Set.of(),
            true,
            true
          )
          .hasTeam(),
        is(false)
      );
  }
}
