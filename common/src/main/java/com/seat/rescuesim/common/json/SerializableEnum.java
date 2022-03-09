package com.seat.rescuesim.common.json;

/** An interface to support serializable enums. */
public interface SerializableEnum {

    /** Must be implemented to return the enum's type (enum constant). */
    int getType();

    /** Returns the String encoding of the enum. */
    default String encode() {
        return String.valueOf(this.getType());
    }

    /** Returns true if the encoded SerializableEnum has the same enum constant. */
    default boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

    /** Returns the String representation of the enum. */
    default String getLabel() {
        return String.format("<%d>", this.getType());
    }

}
