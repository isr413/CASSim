package com.seat.rescuesim.common.util;

public enum FieldType {
    NONE(0),
    PUSH(1),
    PULL(2);

    private int type;

    private FieldType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return String.format("<%d>", this.type);
    }

    public int getType() {
        return this.type;
    }

    public String encode() {
        return String.valueOf(this.type);
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(FieldType type) {
        return this.type == type.type;
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
