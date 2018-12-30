package com.amanalyzer.preprocessings

import com.amanalyzer.repository.{AppContextInitializer, ResourcesContextInitilatizer, Review}
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import AppContextInitializer.sparkSession.implicits._
import com.amanalyzer.utils.VectoOperations
import scala.math

/**
  * Word2Vec Companion object that returns a Singleton instance of Word2VecModel
  */
object Word2VecModelCompanion{

  val word2VecModel : W2VecModel = new W2VecModel


}

/**
  * Word2VecModel functions to Save the model
  *                            Read and load the model
  *                            Get vector representation of words
  *                            Broadcast vectors to the workers
  */
class W2VecModel extends Serializable {

  def saveWord2Vec()= {
    val persistedRawData = ResourcesContextInitilatizer.getReviewsDataAsReviewsRDD
    val pairPersistedRawData = persistedRawData.map(review => (review.asin, review)).persist()
    val rawData = pairPersistedRawData.reduceByKey(getMaxByOverall)
    val resultDF = rawData.join(pairPersistedRawData)
    import org.apache.spark.mllib.feature.{Word2Vec}
    val word2vec = new Word2Vec()
    val reviewsText = pairPersistedRawData.mapValues(review => review.reviewText.split(" ").toSeq).values
    val model = word2vec.fit(reviewsText)
    model.save(AppContextInitializer.sparkContext, ".")
  }

  private def getMaxByOverall = (rev1 : Review, rev2 : Review) => {
    if(rev1.overall > rev2.overall) rev1 else rev2
  }

  // Reads Word2VecModel
  val w2vModel = Word2VecModel.load(AppContextInitializer.sparkContext, "w2vModel")

  // Gets serializable Map[String, Array[Float]]
  val vectors = w2vModel.getVectors.mapValues(vv => Vectors.dense(vv.map(_.toDouble))).map(identity)

  //Broadcasts vectors through executors nodes
  val bVectors = AppContextInitializer.sparkContext.broadcast(vectors)

  /**
    * Computes text vectors, based on previously computed word representations
    * of words contained in the reviews texts.
    * @return Vector representation for review text
    */
  def textVectors (reviewText:Seq[String], vectorDim:Int) : Vector  = {
    val zeros = Vectors.zeros(vectorDim)
    var vSum = zeros
    if(reviewText.length > 1) {
      var vNb = 0
      reviewText.map(word => {
          vSum = VectoOperations.add(bVectors.value.get(word.toString).getOrElse(zeros), vSum)
          vNb += 1
        if (vNb != 0) {
          vSum = VectoOperations.scalarMultiply(1.0 / vNb, vSum)
        }
      })
    }
    if(Vectors.norm(vSum, 1.0) > 0.0) {
      vSum
    }
    else {
      null
    }
  }

}
