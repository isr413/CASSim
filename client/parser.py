import pandas as pd
import sys
import time


def add_data(df, scenario, trial, seed, alpha, beta, gamma, drones, victims,
             turn, successes, fails, score, heat):
    data = pd.DataFrame({
            "Scenario": scenario,
            "Trial": trial,
            "Seed": seed,
            "Alpha": alpha,
            "Beta": beta,
            "Gamma": gamma,
            "Drones": drones,
            "Victims": len(victims),
            "Turn": turn,
            "Successes": successes,
            "Fails": fails,
            "Score": score,
            "Heat": heat
        }, index=[0])

    if df.empty:
        return data

    df = pd.concat([df, data], ignore_index=True)
    df.reset_index()

    return df


def parse_file(filename, trial_offset=0):
    df = pd.DataFrame(columns=["Scenario", "Trial", "Seed", "Alpha", "Beta",
                               "Gamma", "Drones", "Victims", "Turn",
                               "Successes", "Fails", "Score", "Heat"])
    scenario = filename.strip().split("/")[-1].split("_")[0]

    with open(filename) as f:
        trial = seed = alpha = beta = gamma = drones = 0
        turn = successes = fails = score = heat = 0
        victims = set()

        for line in f:
            line = line.strip()
            split = line.split(" ")

            if "Seed" in split:
                if trial > 0:
                    df = add_data(df, scenario, trial_offset + trial, seed,
                                  alpha, beta, gamma, drones, victims, turn,
                                  successes, fails, score, heat)

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

                drones = turn = 0
                successes = fails = score = heat = 0
                victims = set()
                continue

            curr_turn = float(split[1].strip()[:-1])
            if curr_turn > turn:
                df = add_data(df, scenario, trial_offset + trial, seed,
                              alpha, beta, gamma, drones, victims, turn,
                              successes, fails, score, heat)
                turn = curr_turn

            if "Drone done" in line:
                drones += 1

            if "Detected victim" in line:
                victims.add(line.split("::")[-2].strip())

            if "Fails task" in line:
                fails += 1

            if "Succeeds task" in line:
                successes += 1

            if "Score" in line:
                score = float(line.split(":")[-1].strip())

            if "Heat" in line:
                heat = float(line.split(":")[-1].strip())

        df = add_data(df, scenario, trial_offset + trial, seed, alpha, beta,
                      gamma, drones, victims, turn, successes, fails, score, heat)

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


def save_data(df, filename=None):
    if not filename:
        filename = f'parser_{time.strftime("%Y%m%d%H%M%S")}'

    df.to_csv(f'logs/{filename}.csv', index=False)


if __name__ == "__main__":
    target = sys.argv[1]

    if ' ' in target:
        df = parse_files(target.split(' '))
        save_data(df)
    else:
        df = parse_files([target.replace('_1_', f'_{i + 1}_')
                         for i in range(4)])
        filename = f"parser_{target.split('/')[1].split('_1_')[0]}"
        save_data(df, filename=filename)
