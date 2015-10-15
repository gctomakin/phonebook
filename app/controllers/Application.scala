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

import models.Contact
import models.dao
import play.api.libs.concurrent.Execution.Implicits.defaultContext
// import libs.Security

class Application @Inject() (
  val messagesApi: MessagesApi,
  val pageMetaApi: PageMetaApi,
  val contacts: dao.Contacts
) extends Controller with I18nSupport with PageMetaSupport {

  val contactForm = Form(
    mapping(
      "id" -> default(optional(longNumber), None),
      "fullName" -> nonEmptyText,
      "number" -> nonEmptyText
    )(Contact.apply)(Contact.unapply)
  )

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def add = Action { implicit request =>
    Ok(views.html.pages.add(contactForm))
  }

  def display = Action.async { implicit request =>
    Future.successful(Ok(views.html.pages.display()))
  }

  def getContacts = Action.async { request =>
    contacts.all.map { result =>
      Ok(Json.toJson(result))
    }
  }

  def addContact = Action.async { implicit request =>
    contactForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.pages.add(formWithErrors)))
      },
      data => {
        val newContact = models.Contact(data.id, data.fullName, data.number)
        contacts.insert(newContact)
        Future.successful(Redirect(routes.Application.display()))
      }
    )
  }

  def update(id: Long) = Action.async { implicit request =>
    contacts.findById(id) flatMap {
      case Some(contact) =>
        Future.successful(Ok(Json.toJson(contact)))
      case None => Future.successful(NotFound("Not Found"))
    }
  }

  def updateContact = Action.async { implicit request =>
    contactForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.pages.add(formWithErrors)))
      },
      data => contacts.update(data) map { result =>
        if (result) Redirect(routes.Application.display())
        else InternalServerError
      }
    )
  }

  def delete(id: Long) = Action.async { implicit request =>
    contacts.delete(id) map { result =>
      if (result) Ok(result.toString)
      else InternalServerError
    }
  }

}
