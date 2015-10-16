package models.tables

import models.dao
import models.UserBank

import com.github.tototoshi.slick.PostgresJodaSupport._
import org.joda.time.LocalDate
import play.api.db.slick.HasDatabaseConfigProvider
import slick.profile.RelationalProfile

private[models] trait UserBanks extends HasDatabaseConfigProvider[RelationalProfile] {
  import slick.driver.PostgresDriver.api._

  /** Table definition */
  protected[this] class UserBanksTable(tag: Tag) extends Table[UserBank](tag, "USER_BANKS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def id_user = column[Short]("ID_USER")
    def bank_name = column[String]("BANK_NAME", O.Length(255,true))
    def branch = column[String]("BRANCH", O.Length(255,true))
    def acct_type = column[String]("ACCT_TYPE", O.Length(255,true))
    def acct_number = column[String]("ACCT_NUMBER", O.Length(255,true))
    def acct_owner = column[String]("ACCT_OWNER", O.Length(255,true))
    def status = column[Short]("STATUS")
    def * = (id.?,
      id_user,
      bank_name,
      branch,
      acct_type,
      acct_number,
      acct_owner,
      status
    ) <> ((UserBank.apply _).tupled, UserBank.unapply _)
  }
}
