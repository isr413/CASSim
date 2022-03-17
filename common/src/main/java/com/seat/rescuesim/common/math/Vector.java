package com.seat.rescuesim.common.math;

import com.seat.rescuesim.common.json.*;

/** A vector triple of doubles for representing points and forces in 2D and 3D space. */
public class Vector extends JSONAble {
    /** A basis vectors for vectors in 3D space. */
    public static final Vector BASIS_VECTOR = new Vector(1, 1, 1);
    public static final Vector X_BASIS = new Vector(1, 0, 0);
    public static final Vector Y_BASIS = new Vector(0, 1, 0);
    public static final Vector Z_BASIS = new Vector(0, 0, 1);

    /** Returns the a new vector with the absolute value of the components of a. */
    public static Vector abs(Vector a) {
        return new Vector(Math.abs(a.x), Math.abs(a.y), Math.abs(a.z));
    }

    /** Returns a new vector that is the vector addition of a + b. */
    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /** Returns the angle between the provided vectors. */
    public static double angleBetween(Vector a, Vector b) {
        return Math.acos((a.x*b.x + a.y*b.y + a.z*b.z) / (a.getMagnitude() * b.getMagnitude()));
    }

    /** Returns the cross product of the provided vectors. */
    public static Vector cross(Vector a, Vector b) {
        return new Vector(
            a.y*b.z - a.z*b.y,
            a.z*b.x - a.x*b.z,
            a.x*b.y - a.y*b.x
        );
    }

    /** Returns the Euclidean distance between the two vectors. */
    public static double dist(Vector a, Vector b) {
        return Math.sqrt(Math.pow(b.x-a.x, 2) + Math.pow(b.y-a.y, 2) + Math.pow(b.z-a.z, 2));
    }

    /** Returns the dot product of the provided vectors. */
    public static double dot(Vector a, Vector b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }

    /** Returns the 2D vector with the provided magnitude at the provided angle. */
    public static Vector get2DVector(double magnitude, double angleXY) {
        return Vector.get3DVector(magnitude, Math.PI/2.0, angleXY);
    }

    /** Returns the 3D vector with the provided magnitude at the provided angles. */
    public static Vector get3DVector(double magnitude, double angleBetween, double angleXY) {
        return new Vector(
            Vector.getXComponent(magnitude, angleBetween, angleXY),
            Vector.getYComponent(magnitude, angleBetween, angleXY),
            Vector.getZComponent(magnitude, angleBetween)
        );
    }

    /** Computes X component of the magnitude given the provided angles (in radians). */
    public static double getXComponent(double magnitude, double angleXY) {
        return Vector.getXComponent(magnitude, Math.PI/2.0, angleXY);
    }

    /** Computes X component of the magnitude given the provided angles (in radians). */
    public static double getXComponent(double magnitude, double angleBetween, double angleXY) {
        return magnitude * Math.sin(angleBetween) * Math.cos(angleXY);
    }

    /** Computes X component of the magnitude given the provided angles (in radians). */
    public static Vector getXVector(double magnitude, double angleXY) {
        return Vector.getXVector(magnitude, Math.PI/2.0, angleXY);
    }

    /** Computes X component of the magnitude given the provided angles (in radians). */
    public static Vector getXVector(double magnitude, double angleBetween, double angleXY) {
        return new Vector(Vector.getXComponent(magnitude, angleBetween, angleXY), 0, 0);
    }

    /** Computes Y component of the magnitude given the provided angles (in radians). */
    public static double getYComponent(double magnitude, double angleXY) {
        return Vector.getYComponent(magnitude, Math.PI/2.0, angleXY);
    }

    /** Computes Y component of the magnitude given the provided angles (in radians). */
    public static double getYComponent(double magnitude, double angleBetween, double angleXY) {
        return magnitude * Math.sin(angleBetween) * Math.sin(angleXY);
    }

    /** Computes Y component of the magnitude given the provided angles (in radians). */
    public static Vector getYVector(double magnitude, double angleXY) {
        return Vector.getYVector(magnitude, Math.PI/2.0, angleXY);
    }

    /** Computes Y component of the magnitude given the provided angles (in radians). */
    public static Vector getYVector(double magnitude, double angleBetween, double angleXY) {
        return new Vector(0, Vector.getYComponent(magnitude, angleBetween, angleXY), 0);
    }

    /** Computes Z component of the magnitude given the provided angles (in radians). */
    public static double getZComponent(double magnitude, double angleBetween) {
        return magnitude * Math.cos(angleBetween);
    }

    /** Computes Z component of the magnitude given the provided angles (in radians). */
    public static Vector getZVector(double magnitude, double angleBetween) {
        return new Vector(0, 0, Vector.getZComponent(magnitude, angleBetween));
    }

    /** Returns a new vector that has the components of a to the power of exp. */
    public static Vector pow(Vector a, double exp) {
        return new Vector(Math.pow(a.x, exp), Math.pow(a.y, exp), Math.pow(a.z, exp));
    }

    /** Returns a new vector that has the components of a scaled by scalar. */
    public static Vector scale(Vector a, double scalar) {
        return new Vector(a.x * scalar, a.y * scalar, a.z * scalar);
    }

    /** Returns a new vector that has the components of a scaled by the components of b. */
    public static Vector scaleNonUniform(Vector a, Vector b) {
        return new Vector(a.x*b.x, a.y*b.y, a.z*b.z);
    }

    /** Returns the slope between the vectors. */
    public static double slope(Vector a, Vector b) {
        if (b.x == a.x) {
            return (b.y >= a.y) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return (b.y - a.y) / (b.x - a.x);
    }

    /** Returns a new vector that has the square root of the components of a. */
    public static Vector sqrt(Vector a) {
        return new Vector(Math.sqrt(a.x), Math.sqrt(a.y), Math.sqrt(a.z));
    }

    /** Returns a new vector that is the vector subtraction of a - b. */
    public static Vector subtract(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    private double x; // X-axis (+ goes east; - goes west)
    private double y; // Y-axis (+ goes south; - goes north)
    private double z; // Z-axis (+ increases elevation; - decreases elevation)

    /** JSONAble constructor for JSONArray interface type. */
    public Vector(JSONArray json) {
        super(json);
    }

    /** JSONAble constructor for JSONOption. */
    public Vector(JSONOption option) {
        super(option);
    }

    /** JSONAble constructor for String encoding. */
    public Vector(String encoding) {
        super(encoding);
    }

    /** Constructs a zero vector. */
    public Vector() {
        this(0, 0, 0);
    }

    /** Constructs a 1D vector with the provided component and zero Y and Z components. */
    public Vector(double x) {
        this(x, 0, 0);
    }

    /** Constructs a 2D vector with the provided components and a zero Z component. */
    public Vector(double x, double y) {
        this(x, y, 0);
    }

    /** Constructs a 3D vector with the provided components. */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Decodes the JSONArray. */
    @Override
    protected void decode(JSONArray json) {
        this.x = json.getDouble(0);
        this.y = json.getDouble(1);
        this.z = json.getDouble(2);
    }

    /** Returns the angle between the Z-basis and this vector. */
    public double getAngleBetweenZ() {
        return Vector.angleBetween(Vector.Z_BASIS, this);
    }

    /** Returns the angle between the vector and the XY plane where X is the adjacent axis. */
    public double getAngleXY() {
        return Vector.angleBetween(Vector.X_BASIS, this.getProjectionXY());
    }

    /** Returns the angle between the vector and the XZ plane where X is the adjacent axis. */
    public double getAngleXZ() {
        return Vector.angleBetween(Vector.X_BASIS, this.getProjectionXZ());
    }

    /** Returns the angle between the vector and the YZ plane where Y is the adjacent axis. */
    public double getAngleYZ() {
        return Vector.angleBetween(Vector.Y_BASIS, this.getProjectionYZ());
    }

    /** Returns a new vector of this vector projected onto the XY plane. */
    public Vector getProjectionXY() {
        return new Vector(this.x, this.y, 0);
    }

    /** Returns a new vector of this vector projected onto the XZ plane. */
    public Vector getProjectionXZ() {
        return new Vector(this.x, 0, this.z);
    }

    /** Returns a new vector of this vector projected onto the YZ plane. */
    public Vector getProjectionYZ() {
        return new Vector(0, this.y, this.z);
    }

    /** Returns the vector's X component. */
    public double getX() {
        return this.x;
    }

    /** Returns the vector's Y component. */
    public double getY() {
        return this.y;
    }

    /** Returns the vector's Z component. */
    public double getZ() {
        return this.z;
    }

    /** Returns the vector's length. */
    public double getMagnitude() {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }

    /** Returns the vector scaled down by its length. */
    public Vector getUnitVector() {
        double magnitude = this.getMagnitude();
        return new Vector(this.x / magnitude, this.y / magnitude, this.z / magnitude);
    }

    /** Returns a decodable JSON representation of this vector. */
    public JSONOption toJSON() {
        JSONArrayBuilder json = JSONBuilder.Array();
        json.put(this.x);
        json.put(this.y);
        json.put(this.z);
        return json.toJSON();
    }

    /** Returns true if both vectors have equal components. */
    public boolean equals(Vector vec) {
        return this.x == vec.x && this.y == vec.y && this.z == vec.z;
    }

}
