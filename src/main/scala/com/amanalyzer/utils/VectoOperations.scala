package com.amanalyzer.utils

import org.apache.spark.mllib.linalg.{Vector, Vectors}

/**
  * Vecto operations companion objects
  * static methods
  */
object VectoOperations {
  import breeze.linalg.{Vector => BV}
  import org.apache.spark.mllib.linalg.{Vector => SparkVector}
  /*
    Make breeze compliant vector
   */
  def toBreeze(v:Vector) = BV(v.toArray)
  /*
    Reverse (from breeze format to normal vector format
   */
  def fromBreeze(bv:BV[Double]) = Vectors.dense(bv.toArray)
  /*
    Adding to vectors
   */
  def add(v1:SparkVector, v2:SparkVector) = fromBreeze(toBreeze(v1) + toBreeze(v2))
  /*
    Scalar multiplication
   */
  def scalarMultiply(a:Double, v:SparkVector) = fromBreeze(a * toBreeze(v))

}
