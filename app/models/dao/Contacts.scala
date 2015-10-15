package models.dao

import models.tables
import models.Contact

import scala.concurrent.Future
import javax.inject.{ Inject, Singleton }

import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.github.tototoshi.slick.PostgresJodaSupport._

@Singleton
final class Contacts @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider
) extends tables.Contacts {
  import slick.driver.PostgresDriver.api._
  /** Table query */
  object query extends TableQuery(new ContactsTable(_)) {
  /** Finding contact by contact's id query */
  def filterById(id: Long): Query[ContactsTable, Contact, Seq] = this.filter(_.id === id)

  }

  /** Insert new contact */
  def insert(contact: Contact): Future[Long] = db.run((query returning query.map(_.id)) += contact)
  /** Get all contacts */
  def all: Future[Seq[Contact]] = db.run(query.result)
  /** Find contact by contact's id */
  def findById(id: Long): Future[Option[Contact]] = db.run(query.filterById(id).result.headOption)
  /** Delete contact*/
  def delete(id: Long): Future[Boolean] = db.run {
    query.filter(_.id === id).delete.map(_ > 0)
  }
  /** Update contact*/
  def update(contact: Contact): Future[Boolean] = db.run {
    query.filter(_.id === contact.id).update(contact).map(_ > 0)
  }

}
