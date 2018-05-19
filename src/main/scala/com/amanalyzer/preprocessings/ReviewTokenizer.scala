package com.amanalyzer.preprocessings

import org.apache.spark.ml.feature.RegexTokenizer

object ReviewTokenizerCompanion{
  val reviewTokenizer : ReviewTokenizer = new ReviewTokenizer
}

class ReviewTokenizer {

  val regexTokenizer = new RegexTokenizer()
    .setInputCol("reviewText")
    .setOutputCol("tokenizedText")
    .setPattern("\\W")

}
