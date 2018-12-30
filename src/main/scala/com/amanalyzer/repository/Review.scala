package com.amanalyzer.repository

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
case class Review(@JsonProperty("reviewerID") val reviewerID: String,
                  val asin: String,
                  val reviewText: String,
                  val overall: Double
                 ){

  def getAsin = asin

  def getReviewText = reviewText

  def getOverall = overall

}
