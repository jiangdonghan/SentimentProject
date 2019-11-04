package models
import org.apache.spark.internal.config
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{CountVectorizer, RegexTokenizer, StopWordsRemover}
import org.apache.spark.sql.functions.when
import org.apache.spark.sql.types._

/**
 * Using Spark Machine learning library TO train a sentiment analysis model
 */

object SparkMilibTrainSentiment {
// reference : https://databricks.com/wp-content/uploads/2015/10/STEP-3-Sentiment_Analysis.html
  val sc = SparkConfig.getSparkSession("SparkMilibTrainSentiment")
  import sc.implicits._

  //read train data which i downloaded from https://www.kaggle.com/marklvl/sentiment-labelled-sentences-data-set
  val readData = sc.read.option("delimiter", "\t").csv("trainingdataset/sentiment_labelled.txt")
    .toDF("text", "isPositive")
    .select($"text", $"isPositive".cast(DoubleType))
  // add label column
  val trainingData = readData.withColumn("label", when(readData("isPositive") === 1.0D, 1.0D).otherwise(0.0D))
  //read stop words
  // Read stop words data from given file
  val stopwords: Array[String] = sc.sparkContext.textFile("src/main/resources/data/stopwords.txt").flatMap(_.stripMargin.split("\\s+")).collect ++ Array("rt")
  // Create a tokenizer to split data
  val tokenizer = new RegexTokenizer()
    .setGaps(false)
    .setPattern("\\p{L}+")
    .setInputCol("text")
    .setOutputCol("words")

  // filter out stop words
  val filterer = new StopWordsRemover()
    .setStopWords(stopwords)
    .setCaseSensitive(false)
    .setInputCol("words")
    .setOutputCol("filtered")

  // vectorized data
  val countVectorizer = new CountVectorizer()
    .setInputCol("filtered")
    .setOutputCol("features")

  // create a logistic regression model
  val lr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.2)
    .setElasticNetParam(0.0)

  // create pipeline for tokenizer, filter stop words, vector, and LR

  val pipeline = new Pipeline().setStages(Array(tokenizer, filterer, countVectorizer, lr))

  lrModel.write.overwrite().save("TrainedModel")

  // fit the train data for model

  val lrModel = pipeline.fit(trainingData)

  def checkSentiment(pred: Double): String = if (pred == 1.0) "positive" else "negative"


  case class inputText(text: String)
  def getSentimentResult(text: String): String={
    sc.sqlContext.udf.register("checkSentiment", checkSentiment _)
    val textDataFrame = sc.sqlContext.createDataFrame(sc.sparkContext.makeRDD(Seq(inputText(text))))
    val result = lrModel.transform(textDataFrame).selectExpr("text", "checkSentiment(prediction) as sentiment")
    result.show( false)
    return result.toString()
  }
}
