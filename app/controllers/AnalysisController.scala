package controllers
import com.johnsnowlabs.nlp.annotators.sda.vivekn.ViveknSentimentApproach
import javax.inject.Inject
import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import com.johnsnowlabs.nlp.{Finisher, SparkNLP}
import models.Text
import play.api.data._
import play.api.i18n._
import play.api.mvc._
//import uk.ac.wlv.sentistrength.SentiStrength
class AnalysisController  @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc){
  import TextForm._
  private val inputs = scala.collection.mutable.ArrayBuffer(
    Text("Example Text: i love this world", "positive")
  )
  def sentimentAnalysisUsingSparkNLP(text: String): String = {
    val testData = text
    val pipeline = PretrainedPipeline("analyze_sentiment", lang = "en")
    val annotation = pipeline.annotate(testData)
    var result:String =annotation("sentiment").head
    return result
  }



//  def SentiStrengthgetScore(text: String): String = {
//    val sentiStrength = new SentiStrength
//    val ssthInitialisation = Array[String]("sentidata", "res/SentiStrength_Data/", "explain")
//    sentiStrength.initialise(ssthInitialisation)
//    val score = sentiStrength.computeSentimentScores(text)
//    val split = score.split("\\s+")
//    "positive: " + split(0) + ";" + "negative: " + split(1)
//  }
  private val postUrl = routes.AnalysisController.createResult()
  def listInputs = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.SentiAnalysis(inputs.toSeq, form, postUrl))
  }
  def createResult = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Data] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.SentiAnalysis(inputs.toSeq, formWithErrors, postUrl))
    }

    val successFunction = { data: Data =>
      // This is the good case, where the form was successfully parsed as a Data object.
      // getting error
      //val text = Text(name = sentimentAnalysisUsingSparkNLP(data.text1), price = data.text2)
      val text = Text(text1 = data.text1, text2 = sentimentAnalysisUsingSparkNLP(data.text2))
      inputs.append(text)
      Redirect(routes.AnalysisController.listInputs()).flashing("info" -> "Sentiment Analysis result displayed!")
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

}
