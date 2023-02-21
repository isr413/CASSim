import pandas as pd
import sys
import time


def add_data(df, scenario, trial, seed, alpha, beta, gamma, drones, victims):
    data = pd.DataFrame({
            "Scenario": scenario,
            "Trial": trial,
            "Seed": seed,
            "Alpha": alpha,
            "Beta": beta,
            "Gamma": gamma,
            "Drones": drones,
            "Victims": victims
        }, index=[0])
    if df.empty:
        return data
    df = pd.concat([df, data], ignore_index=True)
    df.reset_index()
    return df


def parse_file(filename, trial_offset=0):
    df = pd.DataFrame(columns=["Scenario", "Trial", "Seed", "Alpha", "Beta",
                               "Gamma", "Drones", "Victims"])
    scenario = filename.strip().split("/")[-1].split("_")[0]
    with open(filename) as f:
        trial = 0
        seed = 0
        alpha = 0
        beta = 0
        gamma = 0
        drones = 0
        victims = 0
        for line in f:
            line = line.strip()
            split = line.split(" ")
            if "Seed" in split:
                if trial > 0:
                    df = add_data(df, scenario, trial_offset + trial, seed,
                                  alpha, beta, gamma, drones, victims)
                trial += 1
                seed = int(split[3])
                if "GAMMA" in split:
                    alpha = float(split[6])
                    beta = float(split[9])
                    gamma = float(split[12])
                elif "BETA" in split:
                    alpha = float(split[6])
                    beta = float(split[9])
                    gamma = 0
                elif "ALPHA" in split:
                    alpha = float(split[6])
                    beta = 0
                    gamma = 0
                else:
                    alpha = 0
                    beta = 0
                    gamma = 0
                drones = 0
                victims = 0
                continue
            if "Drone done" in line:
                drones += 1
            if "Rescued victim" in line:
                victims += 1
        df = add_data(df, scenario, trial_offset + trial, seed, alpha, beta,
                      gamma, drones, victims)
    return (df, trial_offset + trial)


def parse_files(targets):
    df = pd.DataFrame()
    trial_offset = 0
    for filename in targets:
        if df.empty:
            df, trial_offset = parse_file(filename, trial_offset)
        else:
            data, trial_offset = parse_file(filename, trial_offset)
            df = pd.concat([df, data], ignore_index=True)
            df.reset_index()
    return df


def save_data(df):
    df.to_csv(f'logs/parser_{time.strftime("%Y%m%d%H%M%S")}.csv')


if __name__ == "__main__":
    df = parse_files([filename for filename in sys.argv[1:]])
    save_data(df)
