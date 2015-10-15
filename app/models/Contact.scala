package models

import play.api.libs.json._

case class Contact(
  id: Option[Long],
  fullName: String,
  number: String
)

object Contact {
  implicit val ljdkawjdlkaw = Json.format[Contact]
}