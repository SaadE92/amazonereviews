package com.amanalyzer.preprocessings

import org.apache.spark.ml.feature.{StopWordsRemover}

object ReviewStopWordsRemoverCompanion{
  val reviewStopWordsRemover : ReviewStopWordsRemover = new ReviewStopWordsRemover
}

class ReviewStopWordsRemover {

  val remover = new StopWordsRemover()
    .setInputCol("tokenizedText")
    .setOutputCol("cleanedTokenizedReviewText")

}
