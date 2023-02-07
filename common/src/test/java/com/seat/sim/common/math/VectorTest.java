package com.seat.sim.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.seat.sim.common.json.*;
import org.junit.Test;


public class VectorTest {

  private static void assertVectorEquals(Vector a, Vector b) {
    assertEquals(a.getX(), b.getX(), 0.001);
    assertEquals(a.getY(), b.getY(), 0.001);
    assertEquals(a.getZ(), b.getZ(), 0.001);
  }

  @Test
  public void absShouldProduceNewVectorWithAbsoluteValueOfComponents() {
    Vector ints = new Vector(-1, -2, 3);
    VectorTest.assertVectorEquals(new Vector(1, 2, 3), Vector.abs(ints));
    assertNotSame(ints, Vector.abs(ints));
    Vector doubles = new Vector(-1.1, 2.2, -3.3);
    VectorTest.assertVectorEquals(new Vector(1.1, 2.2, 3.3), Vector.abs(doubles));
    assertNotSame(doubles, Vector.abs(doubles));
    Vector mix = new Vector(1, 2.345, -6);
    VectorTest.assertVectorEquals(new Vector(1, 2.345, 6), Vector.abs(mix));
    assertNotSame(mix, Vector.abs(mix));
    Vector positives = new Vector(1, 2, 3);
    VectorTest.assertVectorEquals(positives, Vector.abs(positives));
    assertNotSame(positives, Vector.abs(positives));
    Vector zeros = new Vector(0, 0, 0);
    VectorTest.assertVectorEquals(zeros, Vector.abs(zeros));
    assertNotSame(zeros, Vector.abs(zeros));
  }

  @Test
  public void addShouldProduceNewVectorWithAddedComponents() {
    Vector positives = new Vector(1, 2, 3);
    Vector xneg = new Vector(-4, 5, 6);
    Vector yneg = new Vector(7, -8, 9);
    Vector zneg = new Vector(10, 11, -12);
    VectorTest.assertVectorEquals(new Vector(2, 4, 6), Vector.add(positives, positives));
    assertNotSame(positives, Vector.add(positives, positives));
    VectorTest.assertVectorEquals(new Vector(-8, 10, 12), Vector.add(xneg, xneg));
    assertNotSame(xneg, Vector.add(xneg, xneg));
    VectorTest.assertVectorEquals(new Vector(-3, 7, 9), Vector.add(positives, xneg));
    assertNotSame(positives, Vector.add(positives, xneg));
    assertNotSame(xneg, Vector.add(positives, xneg));
    VectorTest.assertVectorEquals(new Vector(8, -6, 12), Vector.add(positives, yneg));
    assertNotSame(positives, Vector.add(positives, yneg));
    assertNotSame(yneg, Vector.add(positives, yneg));
    VectorTest.assertVectorEquals(new Vector(11, 13, -9), Vector.add(positives, zneg));
    assertNotSame(positives, Vector.add(positives, zneg));
    assertNotSame(zneg, Vector.add(positives, zneg));
  }

  @Test
  public void decodeShouldProduceEquivalentVector() {
    JsonArrayBuilder json = JsonBuilder.Array();
    json.put(0.0);
    json.put(1.0);
    json.put(2.0);
    VectorTest.assertVectorEquals(new Vector(0.0, 1.0, 2.0), new Vector(json.toJson()));
    VectorTest.assertVectorEquals(new Vector(0.0, 1.0, 2.0), new Vector(Json.of(json.toString())));
    json = JsonBuilder.Array();
    json.put(-1.0);
    json.put(-2.0);
    json.put(-3.0);
    VectorTest.assertVectorEquals(new Vector(-1.0, -2.0, -3.0), new Vector(json.toJson()));
    VectorTest.assertVectorEquals(new Vector(-1.0, -2.0, -3.0), new Vector(Json.of(json.toString())));
  }

  @Test
  public void defaultConstructorShouldReturnZeroVector() {
    VectorTest.assertVectorEquals(new Vector(0, 0, 0), new Vector());
  }

  @Test
  public void gettersShouldFetchComponents() {
    Vector vec = new Vector(1, 2, 3);
    assertEquals(1.0, vec.getX(), 0);
    assertEquals(2.0, vec.getY(), 0);
    assertEquals(3.0, vec.getZ(), 0);
    VectorTest.assertVectorEquals(new Vector(1, 2, 0), vec.getProjectionXY());
    VectorTest.assertVectorEquals(new Vector(1, 0, 3), vec.getProjectionXZ());
    VectorTest.assertVectorEquals(new Vector(0, 2, 3), vec.getProjectionYZ());
    assertEquals(Math.PI/4.0, new Vector(1, 1, 1).getAngleXY(), 0.001);
    assertEquals(Math.PI/4.0, new Vector(1, 1, 1).getAngleXZ(), 0.001);
    assertEquals(Math.PI/4.0, new Vector(1, 1, 1).getAngleYZ(), 0.001);
  }

  @Test
  public void getMagnitudeShouldReturnVectorLength() {
    assertEquals(0, new Vector(0, 0, 0).getMagnitude(), 0);
    assertEquals(5, new Vector(5, 0, 0).getMagnitude(), 0);
    assertEquals(5, new Vector(3, 4, 0).getMagnitude(), 0);
    assertEquals(6, new Vector(2, 4, 4).getMagnitude(), 0);
  }

  @Test
  public void getUnitVectorShouldReturnNewUnitVector() {
    Vector oned = new Vector(5, 0, 0);
    Vector twod = new Vector(3, 4, 0);
    Vector positives = new Vector(2, 4, 4);
    Vector negatives = new Vector(-2, -4, -4);
    VectorTest.assertVectorEquals(new Vector(1, 0, 0), oned.getUnitVector());
    assertNotSame(oned, oned.getUnitVector());
    VectorTest.assertVectorEquals(new Vector(3.0/5.0, 4.0/5.0, 0), twod.getUnitVector());
    assertNotSame(twod, twod.getUnitVector());
    VectorTest.assertVectorEquals(new Vector(2.0/6.0, 4.0/6.0, 4.0/6.0), positives.getUnitVector());
    assertNotSame(positives, positives.getUnitVector());
    VectorTest.assertVectorEquals(new Vector(-2.0/6.0, -4.0/6.0, -4.0/6.0), negatives.getUnitVector());
    assertNotSame(negatives, negatives.getUnitVector());
  }

  @Test
  public void encodeShouldProduceDecodableString() {
    Vector positives = new Vector(1, 2, 3);
    Vector negatives = new Vector(-1, -2, -3);
    Vector zeros = new Vector(0, 0, 0);
    VectorTest.assertVectorEquals(positives, new Vector(Json.of(positives.encode())));
    VectorTest.assertVectorEquals(negatives, new Vector(Json.of(negatives.encode())));
    VectorTest.assertVectorEquals(zeros, new Vector(Json.of(zeros.encode())));
  }

  @Test
  public void equalsShouldReturnTrueOnlyForMatchingVectors() {
    Vector positives = new Vector(1, 2, 3);
    Vector negatives = new Vector(-1, -2, -3);
    assertTrue(positives.equals(new Vector(1, 2, 3)));
    assertTrue(positives.equals(positives.encode()));
    assertTrue(negatives.equals(new Vector(-1, -2, -3)));
    assertTrue(negatives.equals(negatives.encode()));
    assertFalse(positives.equals(negatives));
    assertFalse(positives.equals(negatives.encode()));
  }

  @Test
  public void oneDConstructorShouldReturn1DVector() {
    VectorTest.assertVectorEquals(new Vector(1, 0, 0), new Vector(1));
  }

  @Test
  public void twoDConstructorShouldReturn2DVector() {
    VectorTest.assertVectorEquals(new Vector(1, 2, 0), new Vector(1, 2));
  }

  @Test
  public void toJsonShouldProduceDecodableJson() {
    Vector positives = new Vector(1, 2, 3);
    Vector negatives = new Vector(-1, -2, -3);
    Vector zeros = new Vector(0, 0, 0);
    VectorTest.assertVectorEquals(positives, new Vector(positives.toJson()));
    VectorTest.assertVectorEquals(negatives, new Vector(negatives.toJson()));
    VectorTest.assertVectorEquals(zeros, new Vector(zeros.toJson()));
  }

  @Test
  public void powShouldProduceNewVectorWithExponentiatedComponents() {
    Vector positives = new Vector(2, 3, 4);
    Vector negatives = new Vector(-2, -3, -4);
    Vector ones = new Vector(1, 1, 1);
    Vector zeros = new Vector(0, 0, 0);
    VectorTest.assertVectorEquals(ones, Vector.pow(positives, 0));
    assertNotSame(positives, Vector.pow(positives, 0));
    VectorTest.assertVectorEquals(positives, Vector.pow(positives, 1));
    assertNotSame(positives, Vector.pow(positives, 1));
    VectorTest.assertVectorEquals(new Vector(4, 9, 16), Vector.pow(positives, 2));
    assertNotSame(positives, Vector.pow(positives, 2));
    VectorTest.assertVectorEquals(ones, Vector.pow(negatives, 0));
    assertNotSame(negatives, Vector.pow(negatives, 0));
    VectorTest.assertVectorEquals(negatives, Vector.pow(negatives, 1));
    assertNotSame(negatives, Vector.pow(negatives, 1));
    VectorTest.assertVectorEquals(new Vector(4, 9, 16), Vector.pow(negatives, 2));
    assertNotSame(negatives, Vector.pow(negatives, 2));
    VectorTest.assertVectorEquals(ones, Vector.pow(ones, 0));
    assertNotSame(ones, Vector.pow(ones, 0));
    VectorTest.assertVectorEquals(ones, Vector.pow(ones, 1));
    assertNotSame(ones, Vector.pow(ones, 1));
    VectorTest.assertVectorEquals(ones, Vector.pow(ones, 2));
    assertNotSame(ones, Vector.pow(ones, 2));
    VectorTest.assertVectorEquals(ones, Vector.pow(zeros, 0));
    assertNotSame(zeros, Vector.pow(zeros, 0));
    VectorTest.assertVectorEquals(zeros, Vector.pow(zeros, 1));
    assertNotSame(zeros, Vector.pow(zeros, 1));
    VectorTest.assertVectorEquals(zeros, Vector.pow(zeros, 2));
    assertNotSame(zeros, Vector.pow(zeros, 2));
  }

  @Test
  public void scaleShouldProduceNewVectorWithScaledComponents() {
    Vector positives = new Vector(1, 2, 3);
    Vector negatives = new Vector(-4, -5, -6);
    Vector zeros = new Vector(0, 0, 0);
    VectorTest.assertVectorEquals(new Vector(2, 4, 6), Vector.scale(positives, 2));
    assertNotSame(positives, Vector.scale(positives, 2));
    VectorTest.assertVectorEquals(new Vector(-2, -4, -6), Vector.scale(positives, -2));
    assertNotSame(positives, Vector.scale(positives, -2));
    VectorTest.assertVectorEquals(new Vector(0.5, 1, 1.5), Vector.scale(positives, 0.5));
    assertNotSame(positives, Vector.scale(positives, 0.5));
    VectorTest.assertVectorEquals(positives, Vector.scale(positives, 1));
    assertNotSame(positives, Vector.scale(positives, 1));
    VectorTest.assertVectorEquals(new Vector(-8, -10, -12), Vector.scale(negatives, 2));
    assertNotSame(negatives, Vector.scale(negatives, 2));
    VectorTest.assertVectorEquals(new Vector(8, 10, 12), Vector.scale(negatives, -2));
    assertNotSame(negatives, Vector.scale(negatives, -2));
    VectorTest.assertVectorEquals(new Vector(-2, -2.5, -3), Vector.scale(negatives, 0.5));
    assertNotSame(negatives, Vector.scale(negatives, 0.5));
    VectorTest.assertVectorEquals(negatives, Vector.scale(negatives, 1));
    assertNotSame(negatives, Vector.scale(negatives, 1));
    VectorTest.assertVectorEquals(zeros, Vector.scale(zeros, 2));
    assertNotSame(zeros, Vector.scale(zeros, 2));
    VectorTest.assertVectorEquals(zeros, Vector.scale(zeros, -2));
    assertNotSame(zeros, Vector.scale(zeros, -2));
    VectorTest.assertVectorEquals(zeros, Vector.scale(zeros, 0.5));
    assertNotSame(zeros, Vector.scale(zeros, 0.5));
    VectorTest.assertVectorEquals(zeros, Vector.scale(zeros, 1));
    assertNotSame(zeros, Vector.scale(zeros, 1));
  }

  @Test
  public void mulShouldProduceNewVectorWithScaledComponents() {
    Vector positives = new Vector(1, 2, 3);
    Vector negatives = new Vector(-4, -5, -6);
    Vector zeros = new Vector(0, 0, 0);
    Vector scalePositive = new Vector(3, 2, 1);
    Vector scaleNegative = new Vector(-3, -2, -1);
    Vector scaleOne = new Vector(1, 1, 1);
    Vector scaleZero = new Vector(0, 0, 0);
    VectorTest.assertVectorEquals(new Vector(3, 4, 3), Vector.mul(positives, scalePositive));
    assertNotSame(positives, Vector.mul(positives, scalePositive));
    VectorTest.assertVectorEquals(new Vector(-3, -4, -3), Vector.mul(positives, scaleNegative));
    assertNotSame(positives, Vector.mul(positives, scaleNegative));
    VectorTest.assertVectorEquals(zeros, Vector.mul(positives, scaleZero));
    assertNotSame(positives, Vector.mul(positives, scaleZero));
    VectorTest.assertVectorEquals(positives, Vector.mul(positives, scaleOne));
    assertNotSame(positives, Vector.mul(positives, scaleOne));
    VectorTest.assertVectorEquals(new Vector(-12, -10, -6), Vector.mul(negatives, scalePositive));
    assertNotSame(negatives, Vector.mul(negatives, scalePositive));
    VectorTest.assertVectorEquals(new Vector(12, 10, 6), Vector.mul(negatives, scaleNegative));
    assertNotSame(negatives, Vector.mul(negatives, scaleNegative));
    VectorTest.assertVectorEquals(zeros, Vector.mul(negatives, scaleZero));
    assertNotSame(negatives, Vector.mul(negatives, scaleZero));
    VectorTest.assertVectorEquals(negatives, Vector.mul(negatives, scaleOne));
    assertNotSame(negatives, Vector.mul(negatives, scaleOne));
    VectorTest.assertVectorEquals(zeros, Vector.mul(zeros, scalePositive));
    assertNotSame(zeros, Vector.mul(zeros, scalePositive));
    VectorTest.assertVectorEquals(zeros, Vector.mul(zeros, scaleNegative));
    assertNotSame(zeros, Vector.mul(zeros, scaleNegative));
    VectorTest.assertVectorEquals(zeros, Vector.mul(zeros, scaleZero));
    assertNotSame(zeros, Vector.mul(zeros, scaleZero));
    VectorTest.assertVectorEquals(zeros, Vector.mul(zeros, scaleOne));
    assertNotSame(zeros, Vector.mul(zeros, scaleOne));
  }

  @Test
  public void sqrtShouldProduceNewVectorWithSquareRootOfComponents() {
    Vector positives = new Vector(4, 9, 16);
    Vector ones = new Vector(1, 1, 1);
    Vector zeros = new Vector(0, 0, 0);
    VectorTest.assertVectorEquals(new Vector(2, 3, 4), Vector.sqrt(positives));
    assertNotSame(positives, Vector.sqrt(positives));
    VectorTest.assertVectorEquals(ones, Vector.sqrt(ones));
    assertNotSame(ones, Vector.sqrt(ones));
    VectorTest.assertVectorEquals(zeros, Vector.sqrt(zeros));
    assertNotSame(zeros, Vector.sqrt(zeros));
  }

  @Test
  public void subShouldProduceNewVectorWithsubedComponents() {
    Vector positives = new Vector(1, 2, 3);
    Vector xneg = new Vector(-4, 5, 6);
    Vector yneg = new Vector(7, -8, 9);
    Vector zneg = new Vector(10, 11, -12);
    VectorTest.assertVectorEquals(new Vector(0, 0, 0), Vector.sub(positives, positives));
    assertNotSame(positives, Vector.sub(positives, positives));
    VectorTest.assertVectorEquals(new Vector(0, 0, 0), Vector.sub(xneg, xneg));
    assertNotSame(xneg, Vector.sub(xneg, xneg));
    VectorTest.assertVectorEquals(new Vector(5, -3, -3), Vector.sub(positives, xneg));
    assertNotSame(positives, Vector.sub(positives, xneg));
    assertNotSame(xneg, Vector.sub(positives, xneg));
    VectorTest.assertVectorEquals(new Vector(-6, 10, -6), Vector.sub(positives, yneg));
    assertNotSame(positives, Vector.sub(positives, yneg));
    assertNotSame(yneg, Vector.sub(positives, yneg));
    VectorTest.assertVectorEquals(new Vector(-9, -9, 15), Vector.sub(positives, zneg));
    assertNotSame(positives, Vector.sub(positives, zneg));
    assertNotSame(zneg, Vector.sub(positives, zneg));
  }

  @Test
  public void vector2DMathShouldProduceCorrectVectors() {
    Vector a = new Vector(2, 3);
    Vector b = new Vector(5, 6);
    assertEquals(Vector.dot(a, b), a.getMagnitude() * b.getMagnitude() * Math.cos(Vector.angleBetween(a, b)), 0);
    VectorTest.assertVectorEquals(Vector.cross(a, b), Vector.cross(Vector.sub(a, b), a));
    VectorTest.assertVectorEquals(Vector.cross(a, b), Vector.cross(Vector.sub(a, b), b));
    assertEquals(a.getX(), Vector.getXComponent(a.getMagnitude(), a.getAngleXY()), 0.001);
    assertEquals(a.getY(), Vector.getYComponent(a.getMagnitude(), a.getAngleXY()), 0.001);
    assertEquals(a.getZ(), Vector.getZComponent(a.getMagnitude(), a.getAngleBetweenZ()), 0.001);
    VectorTest.assertVectorEquals(new Vector(2, 0, 0), Vector.getXVector(a.getMagnitude(), a.getAngleXY()));
    VectorTest.assertVectorEquals(new Vector(0, 3, 0), Vector.getYVector(a.getMagnitude(), a.getAngleXY()));
    VectorTest.assertVectorEquals(a, Vector.get2DVector(a.getMagnitude(), a.getAngleXY()));
  }

  @Test
  public void vector3DMathShouldProduceCorrectVectors() {
    Vector a = new Vector(2, 3, 4);
    Vector b = new Vector(5, 6, 7);
    assertEquals(Vector.dot(a, b), a.getMagnitude() * b.getMagnitude() * Math.cos(Vector.angleBetween(a, b)), 0);
    VectorTest.assertVectorEquals(Vector.cross(a, b), Vector.cross(Vector.sub(a, b), a));
    VectorTest.assertVectorEquals(Vector.cross(a, b), Vector.cross(Vector.sub(a, b), b));
    assertEquals(a.getX(), Vector.getXComponent(a.getMagnitude(), a.getAngleBetweenZ(), a.getAngleXY()), 0.001);
    assertEquals(a.getY(), Vector.getYComponent(a.getMagnitude(), a.getAngleBetweenZ(), a.getAngleXY()), 0.001);
    assertEquals(a.getZ(), Vector.getZComponent(a.getMagnitude(), a.getAngleBetweenZ()), 0.001);
    VectorTest.assertVectorEquals(new Vector(2, 0, 0),
      Vector.getXVector(a.getMagnitude(), a.getAngleBetweenZ(), a.getAngleXY()));
    VectorTest.assertVectorEquals(new Vector(0, 3, 0),
      Vector.getYVector(a.getMagnitude(), a.getAngleBetweenZ(), a.getAngleXY()));
    VectorTest.assertVectorEquals(new Vector(0, 0, 4),
      Vector.getZVector(a.getMagnitude(), a.getAngleBetweenZ()));
    VectorTest.assertVectorEquals(a, Vector.get3DVector(a.getMagnitude(), a.getAngleBetweenZ(), a.getAngleXY()));
  }
}
