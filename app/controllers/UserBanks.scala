package controllers

import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import javax.inject.Inject
import play.api.i18n.{ I18nSupport, MessagesApi }
import ejisan.play.libs.{ PageMetaSupport, PageMetaApi }
import com.ejisan.play.Forms._

import models.UserBank
import models.dao
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class UserBanks @Inject() (
  val messagesApi: MessagesApi,
  val pageMetaApi: PageMetaApi,
  val userBank: dao.UserBanks
) extends Controller with I18nSupport with PageMetaSupport {

  val bankForm = Form(
    mapping(
      "id" -> default(optional(longNumber), None),
      "id_user" -> default(shortNumber, 2.toShort),
      "bank_name" -> nonEmptyText,
      "branch" -> nonEmptyText,
      "acct_type" -> nonEmptyText,
      "acct_number" -> nonEmptyText,
      "acct_owner" -> nonEmptyText,
      "status" -> default(shortNumber, 0.toShort)
    )(UserBank.apply)(UserBank.unapply)
  )

  def index = Action { implicit request =>
    Ok(views.html.pages.user_banks(bankForm))
  }

  def addBank = Action.async { implicit request =>
    bankForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.pages.user_banks(formWithErrors)))
      },
      data => {
        val newBank = models.UserBank(data.id, data.id_user, data.bank_name, data.branch, data.acct_type, data.acct_number, data.acct_owner,data.status)
        userBank.insert(newBank).map( r =>
          Redirect(routes.UserBanks.index())
        )

      }
    )
  }

  def getUserBanks = Action.async { request =>
    userBank.findByIdUser(1).map { result =>
      Ok(Json.toJson(result))
    }
  }

  def getAllUserBanks = Action.async { request =>
    userBank.all.map { result =>
      Ok(Json.toJson(result))
    }
  }

  def update(id: Long) = Action.async { implicit request =>
    userBank.findById(id) flatMap {
      case Some(contact) =>
        Future.successful(Ok(Json.toJson(contact)))
      case None => Future.successful(NotFound("Not Found"))
    }
  }

  def updateBank= Action.async { implicit request =>
    bankForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.pages.user_banks(formWithErrors)))
      },
      data => userBank.update(data) map { result =>
          Redirect(routes.UserBanks.index())
      }
    )
  }

  def delete(id: Long) = Action.async { implicit request =>
    userBank.delete(id) map { result =>
      if (result) Ok(result.toString)
      else InternalServerError
    }
  }

}
