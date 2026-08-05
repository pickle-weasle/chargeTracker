package models

import anorm._
import play.api.libs.json.{Format, Json}
import anorm.SqlParser._

case class Station(
    id: String,
    name: String,
    address: String,
    chargingSpeed: Double
)

object Station {
  implicit val format: Format[Station] = Json.format[Station]

  val parser: RowParser[Station] = {
    get[String]("id") ~
      get[String]("name") ~
      get[String]("address") ~
      get[Double]("charging_speed") map {
      case id ~ name ~ address ~ chargingSpeed => Station(id, name, address, chargingSpeed)
    }
  }
}