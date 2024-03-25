const std = @import("std");
const stdout = std.io.getStdOut().writer();
const RndGen = std.rand.DefaultPrng;
const Semaphore = std.Thread.Semaphore;

const DIAGONAL_WEIGHT = 0.075;
const ORTHOGONAL_WEIGHT = 0.175;
const EPS = 1e-8;

pub fn main() !void {
    var args = std.process.args();
    _ = args.skip(); // skip name of program
    const trials = try std.fmt.parseInt(usize, args.next().?, 10);
    const grid_size = try std.fmt.parseInt(usize, args.next().?, 10);
    const turns = try std.fmt.parseInt(usize, args.next().?, 10);
    const drone_count = try std.fmt.parseInt(usize, args.next().?, 10);
    const alpha = try std.fmt.parseFloat(f32, args.next().?);
    const beta = try std.fmt.parseFloat(f32, args.next().?);
    const gamma = try std.fmt.parseFloat(f32, args.next().?);
    const phi = 1.0 - (1.0 - beta) * (1.0 - gamma);

    const cpus = try std.Thread.getCpuCount();

    const thread_count = @min(cpus, grid_size);

    var m_lock: Semaphore = .{};
    var g_lock: Semaphore = .{};
    var w_lock: Semaphore = .{};

    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    const allocator = arena.allocator();

    var task_grid = try allocator.alloc(f32, grid_size * grid_size);
    var task_cache = try allocator.alloc(f32, grid_size * grid_size);
    var task_row_weights = try allocator.alloc(f32, grid_size);
    var drone_grid = try allocator.alloc(f32, grid_size * grid_size);
    var drone_cache = try allocator.alloc(f32, grid_size * grid_size);
    var drone_row_weights = try allocator.alloc(f32, grid_size);

    var prng = RndGen.init(blk: {
        var seed: u64 = undefined;
        try std.os.getrandom(std.mem.asBytes(&seed));
        break :blk seed;
    });
    const rng = prng.random();

    for (0..thread_count) |thread_id| {
        const rows_per_thread = (grid_size + thread_count - 1) / thread_count;
        var thread = try std.Thread.spawn(.{}, work, .{ thread_id, &m_lock, &g_lock, &w_lock, rows_per_thread, @as(usize, grid_size), task_grid[0..], task_cache[0..], drone_grid[0..], drone_cache[0..], trials, turns, alpha, task_row_weights[0..], drone_row_weights[0..] });
        thread.detach();
    }

    const init_weight: f32 = @floatFromInt(grid_size * grid_size);

    for (0..trials) |_| {
        for (0..thread_count) |_| m_lock.wait();

        drone_grid[grid_size / 2 * grid_size + grid_size / 2] = @floatFromInt(drone_count);

        for (0..turns) |_| {
            for (0..thread_count) |_| g_lock.post();
            for (0..thread_count) |_| m_lock.wait();
            for (0..thread_count) |_| w_lock.post();
            for (0..thread_count) |_| m_lock.wait();

            var total_drone_weight: f32 = 0.0;
            for (drone_row_weights) |w| total_drone_weight += w;

            for (0..drone_count) |_| {
                var roll = rng.float(f32);

                var i: usize = 0;
                while (i < grid_size - 1) : (i += 1) {
                    roll -= drone_row_weights[i] / total_drone_weight;
                    if (roll < EPS) break;
                }

                roll = rng.float(f32);

                var j: usize = 0;
                while (j < grid_size - 1) : (j += 1) {
                    roll -= drone_grid[i * grid_size + j] / drone_row_weights[i];
                    if (roll < EPS) break;
                }

                const k = i * grid_size + j;
                task_row_weights[i] -= task_grid[k];
                task_grid[k] *= (1.0 - phi);
                task_row_weights[i] += task_grid[k];
            }

            var total_task_weight: f32 = 0.0;
            for (task_row_weights) |w| total_task_weight += w;
            try stdout.print("{d:.4} ", .{(init_weight - total_task_weight) / init_weight});
        }
        try stdout.print("\n", .{});
    }
}

fn work(thread_id: usize, m_lock: *Semaphore, g_lock: *Semaphore, w_lock: *Semaphore, rows_per_thread: usize, grid_size: usize, task_grid: []f32, task_cache: []f32, drone_grid: []f32, drone_cache: []f32, trials: usize, turns: usize, alpha: f32, task_row_weights: []f32, drone_row_weights: []f32) void {
    const start = thread_id * rows_per_thread * grid_size;
    const end = @min((thread_id + 1) * rows_per_thread, grid_size) * grid_size;

    for (0..trials) |_| {
        for (start..end) |i| task_grid[i] = 1.0;
        for (start..end) |i| drone_grid[i] = 0.0;
        m_lock.post();

        for (0..turns) |_| {
            g_lock.wait();
            for (start..end) |i| {
                drone_cache[i] = move_weight(grid_size, drone_grid[0..], i);
                task_cache[i] = move_weight(grid_size, task_grid[0..], i) * alpha +
                    task_grid[i] * (1.0 - alpha);
            }
            m_lock.post();

            w_lock.wait();
            var i: usize = start;
            while (i < end) : (i += grid_size) {
                var task_row_weight: f32 = 0.0;
                var drone_row_weight: f32 = 0.0;

                for (0..grid_size) |j| {
                    const k = i + j;
                    task_grid[k] = task_cache[k];
                    drone_grid[k] = drone_cache[k];
                    task_row_weight += task_grid[k];
                    drone_row_weight += drone_grid[k];
                }

                task_row_weights[i / grid_size] = task_row_weight;
                drone_row_weights[i / grid_size] = drone_row_weight;
            }
            m_lock.post();
        }
    }
}

inline fn move_weight(grid_size: usize, grid: []f32, i: usize) f32 {
    const orthgonal_moves = grid[get_index(grid_size, i, 0, -1)] +
        grid[get_index(grid_size, i, 0, 1)] +
        grid[get_index(grid_size, i, -1, 0)] +
        grid[get_index(grid_size, i, 1, 0)];
    const diagonal_moves = grid[get_index(grid_size, i, -1, -1)] +
        grid[get_index(grid_size, i, -1, 1)] +
        grid[get_index(grid_size, i, 1, -1)] +
        grid[get_index(grid_size, i, 1, 1)];
    return ORTHOGONAL_WEIGHT * orthgonal_moves +
        DIAGONAL_WEIGHT * diagonal_moves;
}

inline fn get_index(grid_size: usize, i: usize, row_offset: isize, col_offset: isize) usize {
    var row = i / grid_size;
    if (row_offset < 0) {
        row = if (row == 0) grid_size - 1 else row - 1;
    } else if (row_offset > 0) {
        row = (row + 1) % grid_size;
    }

    var col = i % grid_size;
    if (col_offset < 0) {
        col = if (col == 0) grid_size - 1 else col - 1;
    } else if (col_offset > 0) {
        col = (col + 1) % grid_size;
    }

    return row * grid_size + col;
}
