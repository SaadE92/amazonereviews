package com.amanalyzer.repository

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Singleton objects. Initializes, spark configuration, context and session
  */
object AppContextInitializer{

  val sparkConf = new SparkConf().setAppName("amazone-reviews-analyzer").setMaster("local[6]").set("spark.driver.memory","6g").set("spark.executor.memory","6g").set("spark.driver.allowMultipleContexts", "true").set("spark.network.timeout","600")

  val sparkContext = new SparkContext(sparkConf)

  val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
}


/**
  methods to load Stream files, and method to load Raw data
  */
object ResourcesContextInitilatizer {

  val getFileStream = (fileName : String) =>  getClass.getResourceAsStream(fileName)

  val getFileLines = (fileName : String) => Source.fromInputStream(getFileStream(fileName)).getLines

  def getReviewsData:Dataset[Row] = AppContextInitializer.sparkSession.read.json("/Users/saaderrazi/Downloads/Movies_and_TV_5.json").cache()
}
