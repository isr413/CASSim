package com.seat.sim.common.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.core.CommonException;

public class ArgsParser {

  private HashMap<String, String> parsedArgs;

  public ArgsParser() {
    this.parsedArgs = new HashMap<>();
  }

  public ArgsParser(String[] args) {
    this.parsedArgs = this.parse(args);
  }

  private HashMap<String, String> parse(String[] args) throws CommonException {
    HashMap<String, String> parsedArgs = new HashMap<>();
    for (int i = 0; i < args.length; i++) {
      if (args[i].length() >= 3 && args[i].charAt(0) == '-' && args[i].charAt(1) == '-') {
        parsedArgs.put(args[i], args[i].substring(2));
      } else if (args[i].length() >= 2 && args[i].charAt(0) == '-') {
        if (i+1 >= args.length) {
          throw new CommonException(String.format("No input arg for parameter %s", args[i]));
        }
        parsedArgs.put(args[i], args[i+1]);
        i++;
      } else {
        throw new CommonException(String.format("Unrecognized arg %s", args[i]));
      }
    }
    return parsedArgs;
  }

  public Set<String> getArgs() {
    return new HashSet<String>(this.parsedArgs.values());
  }

  public double getDouble(String param) throws NumberFormatException {
    if (!this.hasParam(param)) {
      throw new CommonException(String.format("No param matching %s", param));
    }
    try {
      return Double.parseDouble(this.parsedArgs.get(param));
    } catch (NumberFormatException e) {
      throw new CommonException(e.getMessage());
    }
  }

  public int getInt(String param) throws CommonException {
    if (!this.hasParam(param)) {
      throw new CommonException(String.format("No param matching %s", param));
    }
    try {
      return Integer.parseInt(this.parsedArgs.get(param));
    } catch (NumberFormatException e) {
      throw new CommonException(e.getMessage());
    }
  }

  public long getLong(String param) throws NumberFormatException {
    if (!this.hasParam(param)) {
      throw new CommonException(String.format("No param matching %s", param));
    }
    try {
      return Long.parseLong(this.parsedArgs.get(param));
    } catch (NumberFormatException e) {
      throw new CommonException(e.getMessage());
    }
  }

  public Set<String> getParams() {
    return new HashSet<String>(this.parsedArgs.keySet());
  }

  public String getString(String param) {
    if (!this.hasParam(param)) {
      throw new CommonException(String.format("No param matching %s", param));
    }
    return this.parsedArgs.get(param);
  }

  public boolean hasParam(String param) {
    return this.parsedArgs.containsKey(param);
  }

  public boolean hasParams() {
    return !this.parsedArgs.isEmpty();
  }
}
