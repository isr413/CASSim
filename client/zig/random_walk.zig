const std = @import("std");
const stdout = std.io.getStdOut().writer();
const RndGen = std.rand.DefaultPrng;

const DIAGONAL_WEIGHT = 0.075;
const ORTHOGONAL_WEIGHT = 0.175;
const EPS = 1e-8;

const Coord = struct {
    x: u16,
    y: u16,
};

pub fn main() !void {
    var args = std.process.args();
    _ = args.skip(); // skip name of program
    const trials = try std.fmt.parseInt(u8, args.next().?, 10);
    const grid_size = try std.fmt.parseInt(u16, args.next().?, 10);
    const turns = try std.fmt.parseInt(u16, args.next().?, 10);
    const drone_count = try std.fmt.parseInt(u16, args.next().?, 10);
    const task_count = try std.fmt.parseInt(u16, args.next().?, 10);
    const alpha = try std.fmt.parseFloat(f32, args.next().?);
    _ = alpha;
    const beta = try std.fmt.parseFloat(f32, args.next().?);
    const gamma = try std.fmt.parseFloat(f32, args.next().?);
    const phi = 1.0 - (1.0 - beta) * (1.0 - gamma);

    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    const allocator = arena.allocator();

    var drones = try allocator.alloc(Coord, drone_count);
    var tasks = try allocator.alloc(Coord, task_count);
    var drone_map = try allocator.alloc(u16, grid_size * grid_size);

    var prng = RndGen.init(blk: {
        var seed: u64 = undefined;
        try std.os.getrandom(std.mem.asBytes(&seed));
        break :blk seed;
    });
    const rng = prng.random();

    const mid_coord: Coord = .{ .x = grid_size / 2, .y = grid_size / 2 };

    for (0..trials) |_| {
        for (0..drone_count) |i| drones[i] = mid_coord;
        for (0..(grid_size * grid_size)) |i| drone_map[i] = 0;
        drone_map[get_index_of(grid_size, &mid_coord)] = drone_count;

        for (0..task_count) |j| {
            tasks[j] = .{ .x = rng.uintLessThan(usize, grid_size), .y = rng.uintLessThan(usize, grid_size) };
        }

        const n_drones = drone_count;
        var m_tasks = task_count;

        for (0..turns) |_| {
            for (0..n_drones) |i| {
                drone_map[get_index_of(grid_size, &drones[i])] -= 1;
                drones[i] = get_random_neighbor(grid_size, &drones[i]);
                drone_map[get_index_of(grid_size, &drones[i])] += 1;
            }

            for (0..m_tasks) |j| {
                tasks[j] = get_random_neighbor(grid_size, &tasks[j]);
            }

            var j: u16 = 0;
            while (j < m_tasks) : (j += 1) {
                if (drone_map[get_index_of(grid_size, &tasks[j])] == 0) continue;
                const roll = rng.float(f32);
                if (roll >= phi) continue;
                tasks[j] = tasks[m_tasks - 1];
                m_tasks -= 1;
            }

            const tasks_discovered = @as(f32, @floatFromInt(task_count - m_tasks));
            const init_tasks = @as(f32, @floatFromInt(task_count));

            try stdout.print("{d:.4} ", .{tasks_discovered / init_tasks});
        }
        try stdout.print("\n", .{});
    }
}

fn get_random_neighbor(grid_size: u16, coord: *Coord, rng: *std.Random) Coord {
    const roll = if ((coord.x == 0 or coord.x == grid_size - 1) and (coord.y == 0 or coord.y == grid_size - 1)) {
        rng.float(32) * std.math.pi / 2.0;
    } else if (coord.x == 0 or coord.x == grid_size - 1 or coord.y == 0 or coord.y == grid_size - 1) {
        rng.float(32) * std.math.pi;
    } else {
        rng.float(32) * 2.0 * std.math.pi;
    };

    const vx = @as(f32, @floatFromInt(coord.x)) + 0.5 + std.math.cos(roll) * std.math.sqrt(2);
    const vy = @as(f32, @floatFromInt(coord.y)) + 0.5 + std.math.sin(roll) * std.math.sqrt(2);

    const x = if (coord.x == 0) {
        coord.x + @as(u16, @intFromFloat(std.math.floor(vx + EPS)));
    } else if (coord.x == grid_size - 1) {
        coord.x - @as(u16, @intFromFloat(std.math.floor(vx + EPS)));
    } else {
        const x_i32 = @as(i32, @intCast(coord.x));
        @as(u16, @intCast(x_i32 + @as(i32, @intFromFloat(std.math.floor(vx + EPS)))));
    };

    const y = if (coord.y == 0) {
        coord.y + @as(u16, @intFromFloat(std.math.floor(vy + EPS)));
    } else if (coord.x == grid_size - 1) {
        coord.y - @as(u16, @intFromFloat(std.math.floor(vy + EPS)));
    } else {
        const y_i32 = @as(i32, @intCast(coord.y));
        @as(u16, @intCast(y_i32 + @as(i32, @intFromFloat(std.math.floor(vy + EPS)))));
    };

    return .{ .x = x, .y = y };
}

inline fn get_index_of(grid_size: u16, coord: *Coord) u16 {
    return coord.y * grid_size + coord.x;
}
