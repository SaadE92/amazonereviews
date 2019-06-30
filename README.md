# amazonereviews
scala/spark amazone reviews analyzer

## First step

Save the vectorized version of reviews field in the json file under data/, to do so:

```
   spark-shell
   val raw = spark.read.json("${path_to_json_reviews_data}/Movies_and_TV_5.json").cache()
   val reviewsText = raw.select("reviewText").map(line => line.mkString.split(" ").toSeq).toJavaRDD
   import org.apache.spark.mllib.feature.Word2VecModel
   import org.apache.spark.mllib.linalg.{Vector, Vectors}
   import org.apache.spark.mllib.feature.Word2Vec
   val word2vec = new Word2Vec()
   val model = word2vec.fit(reviewsText)
   model.save(AppContextInitializer.sparkContext, "w2vModel")
```

## Second step

  - Replace ${path_to_json_reviews_data} in com.amanalyzer.repository.AppContextInitializer$ line 31 by the path to your data folder
  
  - Replace ${path_to_your_generated_w2vModel} in com.amanalyzer.preprocessings.W2VecModel#w2vModel line 38.
  launch
  
  ```
  sbt assembly
  ```
  
## Third step

   - Launch the following command :
   ```
   spark-submit --master local[8] --driver-memory 6g --executor-memory 6g --class com.amanalyzer.client.Word2VecClient /Users/saaderrazi/amazone-reviews-analyzer/target/scala-2.11/amazone-reviews-analyzer-assembly-0.1.jar
   ```

### TODO
   - classification yields a wrong result as of now because the model generated is per word => need to generate a per sentence model.
