package com.seat.sim.client.core;

import java.util.Collection;

import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public interface AssetManager {

  public void close();

  public int getAssetCount();

  public boolean hasAsset(String assetID);

  public void init();

  public boolean isDone(String assetID);

  public void reset();

  public void setDone(String assetID);

  public Collection<IntentionSet> update(Snapshot snap);
}
