package models

import play.api.libs.json._

case class UserBank(
  id: Option[Long],
  id_user: Short,
  bank_name:String,
  branch:String,
  acct_type:String,
  acct_number:String,
  acct_owner:String,
  status:Short
)

object UserBank {
  implicit val ljdkawjdlkaw = Json.format[UserBank]
}