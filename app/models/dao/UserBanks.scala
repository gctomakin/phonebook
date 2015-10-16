package models.dao

import models.tables
import models.UserBank

import scala.concurrent.Future
import javax.inject.{ Inject, Singleton }

import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.github.tototoshi.slick.PostgresJodaSupport._

@Singleton
final class UserBanks @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider
) extends tables.UserBanks {
  import slick.driver.PostgresDriver.api._
  /** Table query */
  object query extends TableQuery(new UserBanksTable(_)) {
  /** Finding user bank by user bank's id query */
  def filterById(id: Long): Query[UserBanksTable, UserBank, Seq] = this.filter(_.id === id)
  def filterByIdUser(id_user: Short): Query[UserBanksTable, UserBank, Seq] = this.filter(_.id_user === id_user)

  }

  /** Insert new bank */
  def insert(bank: UserBank): Future[Long] = db.run((query returning query.map(_.id)) += bank)
  /** Get all user banks */
  def findByIdUser(id_user: Short): Future[Seq[UserBank]] = db.run(query.filterByIdUser(id_user).result)
  /** Get all banks */
  def all: Future[Seq[UserBank]] = db.run(query.result)
  /** Delete bank*/
  def delete(id: Long): Future[Boolean] = db.run {
    query.filter(_.id === id).delete.map(_ > 0)
  }
  /** Update bank*/
  def update(bank: UserBank): Future[Boolean] = db.run {
    query.filter(_.id === bank.id).update(bank).map(_ > 0)
  }
  /** Find bank by bank's id */
  def findById(id: Long): Future[Option[UserBank]] = db.run(query.filterById(id).result.headOption)



}
