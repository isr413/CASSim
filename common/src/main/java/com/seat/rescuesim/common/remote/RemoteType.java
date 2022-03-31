package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable enumeration to denote types of Remotes. */
public enum RemoteType implements SerializableEnum {
    NONE(0, RemoteType.NONE_REMOTE),
    BASE(1, RemoteType.STATIC_REMOTE),
    DRONE(2, RemoteType.KINETIC_REMOTE),
    VICTIM(3, RemoteType.KINETIC_REMOTE);

    public static final int NONE_REMOTE = 0;
    public static final int STATIC_REMOTE = 1;
    public static final int KINETIC_REMOTE = 1 << 1;

    private int categories;
    private int type;

    private RemoteType(int type, int categories) {
        this.type = type;
        this.categories = categories;
    }

    public boolean equals(RemoteType type) {
        return this.type == type.type;
    }

    public int getCategories() {
        return this.categories;
    }

    public int getType() {
        return this.type;
    }

    public boolean hasCategory(int categoryFlags) {
        return (this.categories & categoryFlags) > 0;
    }

    public String toString() {
        return this.getLabel();
    }

}
