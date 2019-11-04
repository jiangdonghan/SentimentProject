package models

import com.johnsnowlabs.nlp.annotator._
import com.johnsnowlabs.nlp.base._
import com.johnsnowlabs.util.Benchmark
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.SparkSession
import com.johnsnowlabs.nlp.DocumentAssembler
import com.johnsnowlabs.nlp.annotators.sda.vivekn.ViveknSentimentApproach
import models.TrainSentiment.{document, finisher, normalizer, spark, token, vivekn}
import com.johnsnowlabs.nlp.annotator._
import com.johnsnowlabs.nlp.annotators.spell.norvig.NorvigSweetingModel
import com.johnsnowlabs.nlp.base._
import com.johnsnowlabs.nlp.util.io.ResourceHelper
import com.johnsnowlabs.util.Benchmark
import org.apache.spark.sql.functions.rand
class ViveknSentiment extends App{
  val spark: SparkSession = SparkSession
    .builder()
    .appName("test")
    .master("local[*]")
    .config("spark.driver.memory", "4G")
    .config("spark.kryoserializer.buffer.max","200M")
    .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._
  def getSentiment(text: String): String = {
    val text = ("I really liked this movie!", "positive")
    val trainingData = Seq(text).toDS.toDF("text","sentiment")
    val documentAssembler = new DocumentAssembler()
      .setInputCol("text")
      .setOutputCol("document")

    val tokenizer = new Tokenizer()
      .setInputCols(Array("document"))
      .setOutputCol("token")

    val pipeline = new RecursivePipeline()
      .setStages(Array(
        documentAssembler,
        tokenizer
      ))

    val readyData = pipeline.fit(trainingData).transform(trainingData)

    val sentimentDetector = new ViveknSentimentApproach()
      .setInputCols("token", "document")
      .setOutputCol("sentiment")
      .setCorpusPrune(0)
      .setSentimentCol("sentimstatnt")
    val sentimentData = sentimentDetector.fit(readyData).transform(readyData)
    sentimentData.show(truncate = false)

    return sentimentData.toString()
  }


}

