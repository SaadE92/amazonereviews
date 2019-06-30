package com.amanalyzer.classification

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD


object AmazonReviewsKMeansCompanion {
  val amazonReviewsKMeans : AmazonReviewsKMeans = new AmazonReviewsKMeans(5,100)
}

/**
  * Amazon Reviews Class that clusters reveiws text
  * @param nbClusters
  * @param nbIterations
  */
class AmazonReviewsKMeans(val nbClusters: Int, val nbIterations: Int) {

  def clusterWordsVectors(sent2vec:RDD[Vector], w2vModel:Word2VecModel) = {
    val clustering = KMeans.train(sent2vec, nbClusters, nbIterations)

    clustering.clusterCenters.foreach(clusterCenter => {
      w2vModel.findSynonyms(clusterCenter, 5).foreach(synonym => print(" %s (%5.3f),"
        .format(synonym._1, synonym._2)))
      println()
    })
  }
}
