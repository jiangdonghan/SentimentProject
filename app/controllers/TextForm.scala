package controllers

object TextForm {
  import play.api.data.Forms._
  import play.api.data.Form

  /**
   * A form processing DTO that maps to the form below.
   *
   * Using a class specifically for form binding reduces the chances
   * of a parameter tampering attack and makes code clearer.
   */
  case class Data(text1: String, text2: String)

  /**
   * It specifies the form fields and their types,
   * as well as how to convert from a Data to form data and vice versa.
   */
  val form = Form(
    mapping(
      "text1" -> nonEmptyText,
      "text2" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
}
