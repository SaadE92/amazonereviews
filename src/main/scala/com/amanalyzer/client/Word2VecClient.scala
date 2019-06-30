package com.amanalyzer.client

import com.amanalyzer.classification.AmazonReviewsKMeansCompanion
import com.amanalyzer.preprocessings.{ReviewStopWordsRemoverCompanion, ReviewTokenizerCompanion, Word2VecModelCompanion}
import com.amanalyzer.repository.{AppContextInitializer, ResourcesContextInitilatizer}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.Row
import AppContextInitializer.sparkSession.implicits._
import org.apache.spark.rdd.RDD




object Word2VecClient {

  def main(args: Array[String]): Unit = {

    val vectorDim = 100

    val removedSWTokenizedData = ReviewStopWordsRemoverCompanion.reviewStopWordsRemover.remover.transform(ReviewTokenizerCompanion.reviewTokenizer.regexTokenizer.transform(ResourcesContextInitilatizer.getReviewsDataAsReviewsRDD))

    removedSWTokenizedData.rdd.saveAsTextFile("raw")

    def textVectorUdf (reviewText:Seq[String]) : Vector = Word2VecModelCompanion.word2VecModel.textVectors(reviewText,vectorDim)

    val vectorRepresentationsUDF = udf(textVectorUdf _)

    val enrichedDF = removedSWTokenizedData.withColumn("vectorRepresentation", vectorRepresentationsUDF('cleanedTokenizedReviewText))

    val vectorsRepresentation : RDD[Vector]= enrichedDF.select("vectorRepresentation").rdd.filter(xs => xs != null).map{
        case Row(xs: Vector) => if(xs != null) Vectors.dense(xs.toArray) else Vectors.zeros(100)
        case _ => Vectors.zeros(100)
    }

    vectorsRepresentation.saveAsTextFile("result")
    AmazonReviewsKMeansCompanion.amazonReviewsKMeans.clusterWordsVectors(vectorsRepresentation, Word2VecModelCompanion.word2VecModel.w2vModel)
  }
}
