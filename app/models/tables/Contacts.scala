package models.tables

import models.dao
import models.Contact

import com.github.tototoshi.slick.PostgresJodaSupport._
import org.joda.time.LocalDate
import play.api.db.slick.HasDatabaseConfigProvider
import slick.profile.RelationalProfile

private[models] trait Contacts extends HasDatabaseConfigProvider[RelationalProfile] {
  import slick.driver.PostgresDriver.api._

  /** Table definition */
  protected[this] class ContactsTable(tag: Tag) extends Table[Contact](tag, "CONTACTS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def fullName = column[String]("FULLNAME", O.Length(255, true))
    def number = column[String]("NUMBER", O.Length(255, true))
    def * = (id.?,
      fullName,
      number
    ) <> ((Contact.apply _).tupled, Contact.unapply _)
  }
}
