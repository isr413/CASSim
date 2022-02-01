package com.seat.rescuesim;

public class Field extends Tuple<Vector, Vector> {

    public Field(Vector direction, Vector acceleration) {
        super(direction, acceleration);
    }

    public Vector getDirection() {
        return this.first;
    }

    public Vector getAcceleration() {
        return this.second;
    }

}
