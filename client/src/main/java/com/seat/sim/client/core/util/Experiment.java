package com.seat.sim.client.core.util;

import java.util.Iterator;
import java.util.Optional;

import com.seat.sim.common.util.Random;
import com.seat.sim.common.util.Range;

public class Experiment {

  private double alpha;
  private Optional<Range> alphaRange;
  private double beta;
  private Optional<Range> betaRange;
  private double gamma;
  private Optional<Range> gammaRange;
  private Random rng;
  private long seed;
  private Iterator<Long> seeds;
  private int totalTrials;
  private int trials;
  private int trialsPer;

  public Experiment(Range alpha, Range beta, Range gamma, int trialsPer, int threadID, int threadCount, long seed,
      Iterator<Long> seeds) {
    this.alphaRange = (alpha != null) ? Optional.of(alpha) : Optional.empty();
    this.betaRange = (beta != null) ? Optional.of(beta) : Optional.empty();
    this.gammaRange = (gamma != null) ? Optional.of(gamma) : Optional.empty();
    this.trialsPer = trialsPer;
    this.seed = seed;
    this.seeds = seeds;
    this.init(threadID, threadCount);
  }

  private void init(int threadID, int threadCount) {
    this.alpha = (this.alphaRange.isPresent()) ? this.alphaRange.get().getStart() : 0.;
    this.beta = (this.betaRange.isPresent()) ? this.betaRange.get().getStart() : 0.;
    this.gamma = (this.gammaRange.isPresent()) ? this.gammaRange.get().getStart() : 0.;
    this.setTrial(threadID, threadCount);
    this.setSeed();
  }

  private void setSeed() {
    this.seed = (this.seeds.hasNext()) ? this.seeds.next() : this.rng.getRng().nextLong();
    this.rng = new Random(this.seed);
  }

  private void setTrial(int threadID, int threadCount) {
    this.totalTrials = trialsPer;
    if (this.alphaRange.isPresent()) {
      this.totalTrials *= this.alphaRange.get().points();
    }
    if (this.betaRange.isPresent()) {
      this.totalTrials *= this.betaRange.get().points();
    }
    if (this.gammaRange.isPresent()) {
      this.totalTrials *= this.gammaRange.get().points();
    }
    this.totalTrials /= threadCount;
    this.trials = 0;
    for (int t = 0; t < (threadID - 1) * this.totalTrials; t++) {
      this.reset();
    }
  }

  private void updateAlpha() {
    if (this.alphaRange.isEmpty()) {
      return;
    }
    this.alpha += this.alphaRange.get().getStep();
    if (this.alpha == this.alphaRange.get().getEnd()) {
      this.alpha = this.alphaRange.get().getStart();
    }
  }

  private void updateBeta() {
    if (this.betaRange.isEmpty()) {
      return;
    }
    this.beta += this.betaRange.get().getStep();
    if (this.beta == this.betaRange.get().getEnd()) {
      this.beta = this.betaRange.get().getStart();
      this.updateAlpha();
    }
  }

  private void updateGamma() {
    if (this.gammaRange.isEmpty()) {
      return;
    }
    this.gamma += this.gammaRange.get().getStep();
    if (this.gamma == this.gammaRange.get().getEnd()) {
      this.gamma = this.gammaRange.get().getStart();
      if (this.betaRange.isPresent()) {
        this.updateBeta();
      } else {
        this.updateAlpha();
      }
    }
  }

  public double getAlpha() {
    return this.alpha;
  }

  public double getBeta() {
    return this.beta;
  }

  public double getGamma() {
    return this.gamma;
  }

  public Random getRng() {
    return this.rng;
  }

  public long getSeed() {
    return this.seed;
  }

  public int getTrials() {
    return this.totalTrials;
  }

  public boolean hasNextTrial() {
    return this.trials < this.totalTrials;
  }

  public void reset() {
    this.trials++;
    if (this.trials % this.trialsPer == 0) {
      if (this.gammaRange.isPresent()) {
        this.updateGamma();
      } else if (this.betaRange.isPresent()) {
        this.updateBeta();
      } else if (this.alphaRange.isPresent()) {
        this.updateAlpha();
      }
    }
    this.setSeed();
  }
}
